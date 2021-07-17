package de.htwberlin.kba.gr7.vocabduel.user_administration.rest;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.AuthService;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.*;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.LoggedInUser;
import de.htwberlin.kba.gr7.vocabduel.user_administration.rest.model.RegistrationData;
import de.htwberlin.kba.gr7.vocabduel.user_administration.rest.model.SignInData;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;
import javax.naming.InvalidNameException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Controller
@Path("/auth")
public class AuthServiceRestAdapter {

    private final AuthService AUTH_SERVICE;

    @Inject
    public AuthServiceRestAdapter(AuthService authService) {
        AUTH_SERVICE = authService;
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response registerUser(RegistrationData data) {
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
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response loginUser(SignInData data) {
        final LoggedInUser user = AUTH_SERVICE.loginUser(data.getEmail(), data.getPassword());
        if (user != null) {
            System.out.println("A user logged in: " + user);
            return Response.status(Response.Status.OK).entity(user).build();
        } else {
            System.out.println("A user failed to log in (email: " + data.getEmail() + ")");
            return Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity("Invalid login, please try again.")
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .build();
        }
    }
}