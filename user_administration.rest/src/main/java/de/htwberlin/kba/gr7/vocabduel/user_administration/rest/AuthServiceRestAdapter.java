package de.htwberlin.kba.gr7.vocabduel.user_administration.rest;

import de.htwberlin.kba.gr7.vocabduel.shared_logic.rest.AuthInterceptor;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.AuthService;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.UserService;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.*;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.AuthTokens;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.LoggedInUser;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.shared_logic.rest.model.MissingData;
import de.htwberlin.kba.gr7.vocabduel.user_administration.rest.model.PasswordData;
import de.htwberlin.kba.gr7.vocabduel.user_administration.rest.model.RegistrationData;
import de.htwberlin.kba.gr7.vocabduel.user_administration.rest.model.SignInData;
import org.springframework.stereotype.Controller;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.naming.InvalidNameException;
import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Controller
@Path("/auth")
public class AuthServiceRestAdapter {
    private final UserService USER_SERVICE;
    private final AuthService AUTH_SERVICE;

    @Inject
    public AuthServiceRestAdapter(final UserService userService, final AuthService authService) {
        USER_SERVICE = userService;
        AUTH_SERVICE = authService;
    }

    @POST
    @Path("/register")
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response registerUser(final RegistrationData data) {
        final Response missingDataResponse = MissingData.createMissingDataResponse(data, "register");
        if (missingDataResponse != null) return missingDataResponse;

        try {
            final LoggedInUser user = AUTH_SERVICE.registerUser(
                    data.getUsername(),
                    data.getEmail(),
                    data.getFirstName(),
                    data.getLastName(),
                    data.getPassword(),
                    data.getConfirm()
            );

            System.out.println("Successfully registered user: " + user.toString());
            return Response.status(Response.Status.CREATED).entity(user).build();
        } catch (PasswordsDoNotMatchException | PwTooWeakException | InvalidOrRegisteredMailException | AlreadyRegisteredUsernameException | IncompleteUserDataException | InvalidNameException e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).type(MediaType.TEXT_PLAIN).build();
        } catch (UserOptimisticLockException e) {
            e.printStackTrace();
            return Response.status(Response.Status.PRECONDITION_FAILED).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        } catch (RuntimeException e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/login")
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response loginUser(final SignInData data) {
        final Response missingDataResponse = MissingData.createMissingDataResponse(data, "login");
        if (missingDataResponse != null) return missingDataResponse;

        try {
            final LoggedInUser user = AUTH_SERVICE.loginUser(data.getEmail(), data.getPassword());

            if (user != null) {
                System.out.println("A user logged in: " + user);
                return Response.ok(user).build();
            } else {
                System.out.println("A user failed to log in (email: " + data.getEmail() + ")");
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid login, please try again.").type(MediaType.TEXT_PLAIN).build();
            }
        } catch (UserOptimisticLockException e) {
            e.printStackTrace();
            return Response.status(Response.Status.PRECONDITION_FAILED).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        } catch (RuntimeException e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/current-user")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response currentUser(@HeaderParam(HttpHeaders.AUTHORIZATION) final String token) {
        try {
            return Response.ok(AUTH_SERVICE.fetchUser(token.replaceFirst("Bearer ", ""))).build();
        } catch (UserOptimisticLockException e) {
            e.printStackTrace();
            return Response.status(Response.Status.PRECONDITION_FAILED).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        } catch (RuntimeException e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        }
    }

    @POST
    @PermitAll
    @Path("/refresh-token")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response refreshAuthTokens(final String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Refresh Token must not be null or empty!").build();
        }

        try {
            final AuthTokens tokens = AUTH_SERVICE.refreshAuthTokens(refreshToken);
            if (tokens != null) return Response.ok(tokens).build();
            else return Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Could not refresh. Your given Refresh Token does not seem to be valid")
                    .type(MediaType.TEXT_PLAIN).build();
        } catch (UserOptimisticLockException e) {
            e.printStackTrace();
            return Response.status(Response.Status.PRECONDITION_FAILED).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        } catch (RuntimeException e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("/update-password")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.TEXT_PLAIN})
    public Response updatePassword(@HeaderParam(AuthInterceptor.USER_HEADER) final Long userId, final PasswordData data) {
        final Response missingDataResponse = MissingData.createMissingDataResponse(data, "update-password");
        if (missingDataResponse != null) return missingDataResponse;

        try {
            final User user = USER_SERVICE.getUserDataById(userId);
            AUTH_SERVICE.updateUserPassword(user, data.getCurrentPassword(), data.getNewPassword(), data.getConfirm());
            System.out.println("The following user changed her/his password successfully: " + user);
            return Response.noContent().build();
        } catch (InvalidFirstPwdException | PasswordsDoNotMatchException | PwTooWeakException e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (InvalidUserException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (UserOptimisticLockException e) {
            e.printStackTrace();
            return Response.status(Response.Status.PRECONDITION_FAILED).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        } catch (RuntimeException e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        }
    }
}