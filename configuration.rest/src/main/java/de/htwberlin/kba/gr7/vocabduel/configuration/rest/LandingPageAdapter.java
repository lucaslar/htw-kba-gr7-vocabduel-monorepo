package de.htwberlin.kba.gr7.vocabduel.configuration.rest;

import org.springframework.stereotype.Controller;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Controller
@Path("/")
public class LandingPageAdapter {
    @GET
    @PermitAll
    @Path("/")
    public String landingPage() {
        return "<p>The Vocabduel REST configuration + application has been initialized successfully! You can access:</p><ul><li>API: <a href=\"/api\">/api/</a>[some/endpoint] (see Postman collection or technical documentation)</li><li>Web frontend: <a href=\"/app\"/>/app/</a></li></ul>";
    }
}
