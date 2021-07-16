package de.htwberlin.kba.gr7.vocabduel.game_administration.rest;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.ScoreService;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Controller
@Path("/score")
public class ScoreServiceRestAdapter {

    private final ScoreService SCORE_SERVICE;

    @Inject
    public ScoreServiceRestAdapter(ScoreService scoreService) {
        SCORE_SERVICE = scoreService;
    }

    @GET
    @Path("/hello")
    public String hello() {
        System.out.println("There's something happening here...");
        System.out.println("Score Service is" + (SCORE_SERVICE == null ? "n't" : "") + " initialized");
        return "Hello REST world!";
    }
}
