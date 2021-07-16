package de.htwberlin.kba.gr7.vocabduel.game_administration.rest;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.GameService;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Controller
@Path("/game")
public class GameServiceRestAdapter {

    private final GameService GAME_SERVICE;

    @Inject
    public GameServiceRestAdapter(GameService gameService) {
        GAME_SERVICE = gameService;
    }

    @GET
    @Path("/hello")
    public String hello() {
        System.out.println("There's something happening here...");
        System.out.println("Game Service is" + (GAME_SERVICE == null ? "n't" : "") + " initialized");
        return "Hello REST world!";
    }
}
