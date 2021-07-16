package de.htwberlin.kba.gr7.vocabduel.user_administration.rest;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.AuthService;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Controller
@Path("/auth")
public class AuthServiceRestAdapter {

    private final AuthService AUTH_SERVICE;

    @Inject
    public AuthServiceRestAdapter(AuthService authService) {
        AUTH_SERVICE = authService;
    }

    @GET
    @Path("/hello")
    public String hello() {
        System.out.println("There's something happening here...");
        System.out.println("Auth Service is" + (AUTH_SERVICE == null ? "n't" : "") + " initialized");
        return "Hello REST world!";
    }
}
