package de.htwberlin.kba.gr7.vocabduel.user_administration.rest;

import de.htwberlin.kba.gr7.vocabduel.shared_logic.rest.AuthInterceptor;
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

    // TODO Integrate functions listed above

    @PUT
    @Path("/update-account")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response updateUser(@HeaderParam(AuthInterceptor.USER_HEADER) final String userId, final User data) {
        if (data.getId() == null) {
            System.out.println("User update failed due to missing ID");
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("No user id given!").build();
        } else if (Long.parseLong(userId) != data.getId()) {
            System.out.println("User update failed due to no access (" + userId + " tried to access " + data.getId() + ")");
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("You are not allowed alter to this user's data!").build();
        }

        try {
            USER_SERVICE.updateUser(data);
        } catch (InvalidOrRegisteredMailException | AlreadyRegisteredUsernameException | IncompleteUserDataException | InvalidNameException e) {
            e.printStackTrace();
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        } catch (InvalidUserException e) {
            e.printStackTrace();
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }

        System.out.println("Successfully updated user data: " + data);
        return Response.ok(data).build();
    }

    @DELETE
    @Path("/delete-account")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteUser(@HeaderParam(AuthInterceptor.USER_HEADER) final String userId) {
        final User user = USER_SERVICE.getUserDataById(Long.parseLong(userId));
        USER_SERVICE.deleteUser(user);
        System.out.println("Successfully deleted user: " + user.toString());
        return Response.noContent().build();
    }
}
