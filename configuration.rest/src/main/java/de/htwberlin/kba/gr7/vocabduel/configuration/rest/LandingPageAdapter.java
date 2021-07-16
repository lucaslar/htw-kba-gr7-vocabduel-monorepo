package de.htwberlin.kba.gr7.vocabduel.configuration.rest;

import org.springframework.stereotype.Controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Controller
@Path("/")
public class LandingPageAdapter {
    @GET
    @Path("/")
    public String landingPage() {
        // TODO: Return html incl. links?
        return "The REST API has been initialized successfully!";
    }
}
