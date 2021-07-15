package de.htwberlin.kba.gr7.vocabduel.game_administration.rest;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Controller
@Path("/game")
public class GameServiceRestAdapter {

    @Autowired
    private GameService gameService;

    @GET
    @Path("/hello")
    public String hello() {
        System.out.println("There's something happening here..." + (gameService == null));
        return "Hello REST world!";
    }
}
