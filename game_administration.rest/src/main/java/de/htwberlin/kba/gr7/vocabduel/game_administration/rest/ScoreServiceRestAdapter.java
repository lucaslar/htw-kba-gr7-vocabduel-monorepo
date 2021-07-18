package de.htwberlin.kba.gr7.vocabduel.game_administration.rest;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.ScoreService;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.NoAccessException;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.UnfinishedGameException;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.PersonalFinishedGame;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.ScoreRecord;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.InvalidUserException;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Controller
@Path("/score")
public class ScoreServiceRestAdapter {

    private final ScoreService SCORE_SERVICE;

    @Inject
    public ScoreServiceRestAdapter(ScoreService scoreService) {
        SCORE_SERVICE = scoreService;
    }

    @GET
    @Path("/get-finished-games")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response getPersonalFinishedGames(final User user) {
        List<PersonalFinishedGame> games;
        try {
            games = SCORE_SERVICE.getPersonalFinishedGames(user);
        } catch (InvalidUserException e) {
            e.printStackTrace();
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .build();
        }
        return Response.ok(games).type(MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/get-record")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response getRecordOfUser(final User user) {
        ScoreRecord record;
        try {
            record = SCORE_SERVICE.getRecordOfUser(user);
        } catch (InvalidUserException e) {
            e.printStackTrace();
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .build();
        }
        return Response.ok(record).type(MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/finish-game")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response finishGame(final User user, final long gameId) {
        PersonalFinishedGame game;
        try {
            game = SCORE_SERVICE.finishGame(user, gameId);
        } catch (UnfinishedGameException | NoAccessException e) {
            e.printStackTrace();
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .build();
        }
        System.out.println("Successfully finished Game: " + game.toString());
        return Response.ok(game).type(MediaType.APPLICATION_JSON_TYPE).build();
    }
}
