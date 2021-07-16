package de.htwberlin.kba.gr7.vocabduel.user_administration.rest;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.UserService;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Controller
@Path("/user")
public class UserServiceRestAdapter {

    private final UserService USER_SERVICE;

    @Inject
    public UserServiceRestAdapter(UserService userService) {
        USER_SERVICE = userService;
    }

    @GET
    @Path("/hello")
    public String hello() {
        System.out.println("There's something happening here...");
        System.out.println("User Service is" + (USER_SERVICE == null ? "n't" : "") + " initialized");
        return "Hello REST world!";
    }
}
