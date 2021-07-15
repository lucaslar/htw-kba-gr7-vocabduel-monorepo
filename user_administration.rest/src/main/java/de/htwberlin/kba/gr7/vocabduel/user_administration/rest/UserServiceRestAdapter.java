package de.htwberlin.kba.gr7.vocabduel.user_administration.rest;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Controller
@Path("/user")
public class UserServiceRestAdapter {

    @Autowired
    private UserService userService;

    @GET
    @Path("/hello")
    public String hello() {
        System.out.println("There's something happening here..." + (userService == null));
        return "Hello REST world!";
    }
}
