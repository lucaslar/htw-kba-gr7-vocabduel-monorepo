package de.htwberlin.kba.gr7.vocabduel.user_administration.rest;

import de.htwberlin.kba.gr7.vocabduel.shared_logic.rest.AuthInterceptor;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.UserService;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.*;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import org.springframework.stereotype.Controller;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.naming.InvalidNameException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Path("/user")
public class UserServiceRestAdapter {

    private final UserService USER_SERVICE;

    @Inject
    public UserServiceRestAdapter(final UserService userService) {
        USER_SERVICE = userService;
    }

    @GET
    @Path("/find")
    @PermitAll
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response findUsersByUsername(@QueryParam("searchStr") final String searchStr) {
        // Query param since the user could enter slashes in the client
        if (searchStr == null || searchStr.isEmpty()) {
            System.out.println("Aborted search for user data due to null/empty string");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("Required query param \"searchStr\" must not be null/empty").build();
        }

        final List<User> users;
        try {
            users = USER_SERVICE.findUsersByUsername(searchStr);
            System.out.println("Incoming search for users with username: \"" + searchStr + "\" => " + users.size() + " result(s)");
            return Response.ok(users).build();
        } catch (UserOptimisticLockException e) {
            e.printStackTrace();
            return Response.status(Response.Status.PRECONDITION_FAILED).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        } catch (RuntimeException e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/get")
    @PermitAll
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response getUserData(@QueryParam("id") final String id, @QueryParam("username") final String username, @QueryParam("email") final String email) {
        final List<String> notNullParams = Arrays.stream(new String[]{id, username, email}).filter(p -> p != null && !p.isEmpty()).collect(Collectors.toList());
        if (notNullParams.size() != 1) {
            final String message = notNullParams.size() == 0
                    ? "No identifiers provided! Please add one of [id, email, username] incl. value as query param."
                    : ("Too many identifiers provided! Only one must be given but found were " + notNullParams.size());
            System.out.println("Failed to get user data; reason: " + message);
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(message).build();
        }

        try {
            User user;
            if (email != null) user = USER_SERVICE.getUserDataByEmail(email);
            else if (username != null) user = USER_SERVICE.getUserDataByUsername(username);
            else {
                try {
                    user = USER_SERVICE.getUserDataById(Long.parseLong(id));
                } catch (NumberFormatException e) {
                    System.out.println("Failed to get user data due to given ID not being a number");
                    return Response.status(Response.Status.BAD_REQUEST).entity("Provided ID is not a number!").type(MediaType.TEXT_PLAIN).build();
                }
            }

            if (user == null) {
                System.out.println("No matching user found for the given params: id=" + id + " , email=" + email + " , username" + username);
                return Response.status(Response.Status.NOT_FOUND).entity("No matching user found").type(MediaType.TEXT_PLAIN).build();
            }

            System.out.println("A user has been found successfully: " + user);
            return Response.ok(user).build();
        } catch (UserOptimisticLockException e) {
            e.printStackTrace();
            return Response.status(Response.Status.PRECONDITION_FAILED).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        } catch (RuntimeException e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("/update-account")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response updateUser(@HeaderParam(AuthInterceptor.USER_HEADER) final Long userId, final User data) {
        if (data.getId() == null) {
            System.out.println("User update failed due to missing ID");
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("No user id given!").build();
        } else if (!userId.equals(data.getId())) {
            System.out.println("User update failed due to no access (" + userId + " tried to access " + data.getId() + ")");
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("You are not allowed to alter this user's data!").build();
        }

        try {
            USER_SERVICE.updateUser(data);
        } catch (InvalidOrRegisteredMailException | AlreadyRegisteredUsernameException | IncompleteUserDataException | InvalidNameException e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).type(MediaType.TEXT_PLAIN).build();
        } catch (InvalidUserException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).type(MediaType.TEXT_PLAIN).build();
        } catch (UserOptimisticLockException e) {
            e.printStackTrace();
            return Response.status(Response.Status.PRECONDITION_FAILED).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        } catch (RuntimeException e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        }

        System.out.println("Successfully updated user data: " + data);
        return Response.ok(data).build();
    }
}
