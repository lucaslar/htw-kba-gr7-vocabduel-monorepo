package de.htwberlin.kba.gr7.vocabduel.game_administration.rest;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Controller
@Path("/score")
public class ScoreServiceRestAdapter {

    @Autowired
    private ScoreService scoreService;

    @GET
    @Path("/hello")
    public String hello() {
        System.out.println("There's something happening here..." + (scoreService == null));
        return "Hello REST world!";
    }
}
