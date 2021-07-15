package de.htwberlin.kba.gr7.vocabduel.user_administration.rest;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Controller
@Path("/auth")
public class AuthServiceRestAdapter {

    @Autowired
    private AuthService authService;

    @GET
    @Path("/hello")
    public String readCategory() {
        System.out.println("There's something happening here..." + (authService == null));
        return "Hello REST world!";
    }
}
