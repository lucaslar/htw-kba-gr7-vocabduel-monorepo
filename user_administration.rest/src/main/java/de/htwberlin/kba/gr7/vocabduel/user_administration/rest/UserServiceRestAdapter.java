package de.htwberlin.kba.gr7.vocabduel.user_administration.rest;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.UserService;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.*;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;
import javax.naming.InvalidNameException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Controller
@Path("/user")
public class UserServiceRestAdapter {

    private final UserService USER_SERVICE;

    @Inject
    public UserServiceRestAdapter(UserService userService) {
        USER_SERVICE = userService;
    }

    @GET
    @Path("/find")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response findUsersByUsername(final String username){
        final List<User> users = USER_SERVICE.findUsersByUsername(username);
        if (users == null || users.isEmpty()) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("No User found. Please try another username.")
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .build();
        } else return Response.ok(users).build();
    }

    @GET
    @Path("/get")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response getUserDataByData(final String data){
        User user = USER_SERVICE.getUserDataByEmail(data);
        if(user == null){
            user = USER_SERVICE.getUserDataByUsername(data);
            if(user == null){
                user = USER_SERVICE.getUserDataById(Long.parseLong(data));
                if (user == null){
                    return Response
                            .status(Response.Status.BAD_REQUEST)
                            .entity("No User found.")
                            .type(MediaType.TEXT_PLAIN_TYPE)
                            .build();
                }
            }
        }
        return Response.ok(user).build();
    }

    @POST
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response updateUser(final User data){
        try {
            USER_SERVICE.updateUser(data);
        } catch (InvalidOrRegisteredMailException |
                AlreadyRegisteredUsernameException |
                IncompleteUserDataException | InvalidNameException |
                InvalidUserException e) {
            e.printStackTrace();
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .build();
        }
        // TODO: Implement proper error handling

        System.out.println("Successfully updated user: " + data.toString());
        return Response.ok(data).build();
    }

    @POST
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response deleteUser(final User user){
        USER_SERVICE.deleteUser(user);
        System.out.println("Successfully deleted user: " + user.toString());
        return Response.ok().build();
    }

    // TODO Remove example for auth-guarded route
    @GET
    @Path("/guarded")
    public Response guardedTest () {
        return Response.status(javax.ws.rs.core.Response.Status.OK).build();
    }
}
