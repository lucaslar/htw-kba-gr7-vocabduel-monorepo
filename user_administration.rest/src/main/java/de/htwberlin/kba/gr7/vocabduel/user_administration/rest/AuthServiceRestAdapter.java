package de.htwberlin.kba.gr7.vocabduel.user_administration.rest;

import de.htwberlin.kba.gr7.vocabduel.auth_interceptor.rest.AuthInterceptor;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.AuthService;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.UserService;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.*;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.AuthTokens;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.LoggedInUser;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.user_administration.rest.model.MissingData;
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
import java.util.ArrayList;

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
        LoggedInUser user;
        try {
            user = AUTH_SERVICE.registerUser(
                    data.getUsername(),
                    data.getEmail(),
                    data.getFirstName(),
                    data.getLastName(),
                    data.getPassword(),
                    data.getConfirm()
            );
        } catch (PasswordsDoNotMatchException | PwTooWeakException | InvalidOrRegisteredMailException | AlreadyRegisteredUsernameException | IncompleteUserDataException | InvalidNameException e) {
            e.printStackTrace();
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .build();
        }
        // TODO: Implement proper error handling
//        } catch (PasswordsDoNotMatchException e) {
//            e.printStackTrace();
//        } catch (PwTooWeakException e) {
//            e.printStackTrace();
//        } catch (InvalidOrRegisteredMailException e) {
//            e.printStackTrace();
//        } catch (AlreadyRegisteredUsernameException e) {
//            e.printStackTrace();
//        } catch (IncompleteUserDataException e) {
//            e.printStackTrace();
//        } catch (InvalidNameException e) {
//            e.printStackTrace();
//        }

        System.out.println("Successfully registered user: " + user.toString());
        return Response.status(Response.Status.CREATED).entity(user).build();
    }

    @POST
    @Path("/login")
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response loginUser(final SignInData data) {
        if (data.getEmail() == null || data.getPassword() == null) {
            final ArrayList<String> missing = new ArrayList<>();
            if (data.getEmail() == null) missing.add("email");
            if (data.getPassword() == null) missing.add("password");
            final MissingData missingInfo = new MissingData();
            missingInfo.setMessage("Missing login data");
            missingInfo.setMissingParams(missing);
            System.out.println("A user tried to log in with incomplete data");
            return Response.status(Response.Status.BAD_REQUEST).entity(missingInfo).build();
        }

        final LoggedInUser user = AUTH_SERVICE.loginUser(data.getEmail(), data.getPassword());
        if (user != null) {
            System.out.println("A user logged in: " + user);
            return Response.ok(user).build();
        }

        System.out.println("A user failed to log in (email: " + data.getEmail() + ")");
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity("Invalid login, please try again.")
                .type(MediaType.TEXT_PLAIN_TYPE)
                .build();
    }

    @GET
    @PermitAll
    @Path("/current-user")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response currentUser(@HeaderParam(HttpHeaders.AUTHORIZATION) final String token) {
        if (token == null || token.isEmpty()) {
            return AuthInterceptor.unauthorizedResponse("Access Token must not be null or empty!", false);
        }

        final User user = AUTH_SERVICE.fetchUser(token.replaceFirst("Bearer ", ""));
        if (user == null) {
            return AuthInterceptor.unauthorizedResponse("User could not be fetched. Is your token valid?");
        } else return Response.ok(user).build();
    }

    @POST
    @PermitAll
    @Path("/refresh-token")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response refreshAuthTokens(final String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .entity("Refresh Token must not be null or empty!")
                    .build();
        }

        final AuthTokens tokens = AUTH_SERVICE.refreshAuthTokens(refreshToken);
        if (tokens != null) return Response.status(Response.Status.OK).entity(tokens).build();
        else return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN_TYPE).build();
    }

    @POST
    @Path("/update-password")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.TEXT_PLAIN})
    public Response updatePassword(@HeaderParam(AuthInterceptor.USER_HEADER) final String userId, final PasswordData data) {
        final User user = USER_SERVICE.getUserDataById(Long.parseLong(userId));
        try {
            AUTH_SERVICE.updateUserPassword(user, data.getCurrentPassword(), data.getNewPassword(), data.getConfirm());
        } catch (InvalidFirstPwdException | PasswordsDoNotMatchException | PwTooWeakException e) {
            e.printStackTrace();
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        } catch (InvalidUserException e) {
            e.printStackTrace();
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        }
        System.out.println("The following user changed her/his password successfully: " + user);
        return Response.ok().build();
    }

    // TODO Remove example for auth-guarded route
    @GET
    @Path("/guarded")
    public Response guardedTest() {
        return Response.ok().build();
    }
}