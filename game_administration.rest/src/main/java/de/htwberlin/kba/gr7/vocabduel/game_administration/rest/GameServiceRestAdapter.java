package de.htwberlin.kba.gr7.vocabduel.game_administration.rest;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.GameService;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.InvalidAnswerNrException;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.InvalidGameSetupException;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.NoAccessException;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.NotEnoughVocabularyException;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.CorrectAnswerResult;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.RunningVocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.VocabduelRound;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.InvalidUserException;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.VocableList;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Controller
@Path("/game")
public class GameServiceRestAdapter {

    private final GameService GAME_SERVICE;

    @Inject
    public GameServiceRestAdapter(final GameService gameService) {
        GAME_SERVICE = gameService;
    }

    @POST
    @Path("start-game")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response startGame(final User playerA, final User playerB, List<VocableList> vocableLists){
        RunningVocabduelGame game;
        try{
            game = GAME_SERVICE.startGame(playerA, playerB, vocableLists);
        } catch (NotEnoughVocabularyException | InvalidUserException | InvalidGameSetupException e) {
            e.printStackTrace();
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .build();
        }
        System.out.println("Successfully started new Game: " + game.toString());
        return Response
                .status(Response.Status.OK)
                .entity(game)
                .type(MediaType.TEXT_PLAIN_TYPE)
                .build();

    }

    @GET
    @Path("/get-started-games")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response getPersonalChallengedGames(final User user){
        List<RunningVocabduelGame> games = GAME_SERVICE.getPersonalChallengedGames(user);
        return Response
                .status(Response.Status.OK)
                .entity(games)
                .type(MediaType.TEXT_PLAIN_TYPE)
                .build();
    }

    @POST
    @Path("/start-round")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response startRound(final User user, final Long gameId){
        VocabduelRound round;
        try{
            round = GAME_SERVICE.startRound(user, gameId);
        } catch (NoAccessException e) {
            e.printStackTrace();
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .build();
        }
        System.out.println("Successfully started Round: " + round.toString());
        return Response
                .status(Response.Status.OK)
                .entity(round)
                .type(MediaType.TEXT_PLAIN_TYPE)
                .build();
    }

    @POST
    @Path("/answer-question")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response answerQuestion(final User user, final Long gameId, final int roundNr, final int answerNr){
        CorrectAnswerResult answer;
        try{
            answer = GAME_SERVICE.answerQuestion(user, gameId, roundNr, answerNr);
        } catch (InvalidAnswerNrException | NoAccessException e) {
            e.printStackTrace();
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .build();
        }
        System.out.println("Successfully answered Question: " + answer.toString());
        return Response
                .status(Response.Status.OK)
                .entity(answer)
                .type(MediaType.TEXT_PLAIN_TYPE)
                .build();
    }

    @POST
    @Path("/delete-widows")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response deleteWidowGames(){
        GAME_SERVICE.removeWidowGames();
        System.out.println("Successfully removed widow games.");
        return Response.status(javax.ws.rs.core.Response.Status.OK).build();
    }

    // TODO Remove example for auth-guarded route
    @GET
    @Path("/guarded")
    public Response guardedTest () {
        return Response.status(javax.ws.rs.core.Response.Status.OK).build();
    }
}
