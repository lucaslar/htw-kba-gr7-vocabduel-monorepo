package de.htwberlin.kba.gr7.vocabduel.game_administration.rest;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.GameService;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.InvalidGameSetupException;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.InvalidVocabduelGameNrException;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.NoAccessException;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.NotEnoughVocabularyException;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.CorrectAnswerResult;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.RunningVocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.game_administration.rest.model.MinimizedPersonalGameInfo;
import de.htwberlin.kba.gr7.vocabduel.game_administration.rest.model.MinimizedRoundInfo;
import de.htwberlin.kba.gr7.vocabduel.game_administration.rest.model.StartGameData;
import de.htwberlin.kba.gr7.vocabduel.shared_logic.rest.AuthInterceptor;
import de.htwberlin.kba.gr7.vocabduel.shared_logic.rest.model.MissingData;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.UserService;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.InternalUserModuleException;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.InvalidUserException;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.VocabularyService;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.VocableList;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Path("/game")
public class GameServiceRestAdapter {

    private final GameService GAME_SERVICE;
    private final UserService USER_SERVICE;
    private final VocabularyService VOCABULARY_SERVICE;

    private final SessionFactory SESSION_FACTORY;

    @Inject
    public GameServiceRestAdapter(final GameService gameService, final UserService userService, final VocabularyService vocabularyService, final SessionFactory sessionFactory) {
        GAME_SERVICE = gameService;
        USER_SERVICE = userService;
        VOCABULARY_SERVICE = vocabularyService;
        SESSION_FACTORY = sessionFactory;
    }

    @POST
    @Path("/start")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
    public Response startGame(@HeaderParam(AuthInterceptor.USER_HEADER) final Long userId, final StartGameData data) {
        final Session session = SESSION_FACTORY.openSession();
        final Response missingDataResponse = MissingData.createMissingDataResponse(data, "start-game");
        if (missingDataResponse != null) return missingDataResponse;


        try {
            final User self = USER_SERVICE.getUserDataById(userId);
            final User opponent = USER_SERVICE.getUserDataById(data.getOpponentId());
            final List<VocableList> lists = data.getVocableListIds().stream().map(VOCABULARY_SERVICE::getVocableListById).collect(Collectors.toList());
            lists.forEach(session::update);

            final RunningVocabduelGame game = GAME_SERVICE.startGame(self, opponent, lists);
            final MinimizedPersonalGameInfo gameInfo = new MinimizedPersonalGameInfo(game, self);

            System.out.println("Successfully started new game: " + gameInfo);
            return Response.status(Response.Status.CREATED).type(MediaType.APPLICATION_JSON).entity(gameInfo).build();

        } catch (NotEnoughVocabularyException | InvalidGameSetupException e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        } catch (InvalidUserException e) {
            return Response.status(Response.Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        } catch (InternalUserModuleException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/open-games")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPersonalChallengedGames(@HeaderParam(AuthInterceptor.USER_HEADER) final Long userId) {
        try {
            final Session session = SESSION_FACTORY.openSession();
            final User user = USER_SERVICE.getUserDataById(userId);
            final List<RunningVocabduelGame> games = GAME_SERVICE.getPersonalChallengedGames(user);
            games.forEach(session::update);
            final List<MinimizedPersonalGameInfo> minimizedGameInfo = games.stream().map(g -> new MinimizedPersonalGameInfo(g, user)).collect(Collectors.toList());
            return Response.ok(minimizedGameInfo).type(MediaType.APPLICATION_JSON).build();
        } catch (InternalUserModuleException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/current-round/{gameId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response startRound(@HeaderParam(AuthInterceptor.USER_HEADER) final Long userId, @PathParam("gameId") final Long gameId) {
        try {
            final User user = USER_SERVICE.getUserDataById(userId);
            final MinimizedRoundInfo round = new MinimizedRoundInfo(GAME_SERVICE.startRound(user, gameId));
            System.out.println("Successfully started/requested round by user with ID " + userId + " for game " + gameId);
            return Response.ok(round).type(MediaType.APPLICATION_JSON).build();
        } catch (NoAccessException e) {
            e.printStackTrace();
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).type(MediaType.TEXT_PLAIN).build();
        } catch (InternalUserModuleException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/answer/{gameId}/{roundNr}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response answerQuestion(
            @HeaderParam(AuthInterceptor.USER_HEADER) final Long userId,
            @PathParam("gameId") final Long gameId,
            @PathParam("roundNr") final int roundNr,
            final int answerNr
    ) {
        try {
            final User user = USER_SERVICE.getUserDataById(userId);
            final CorrectAnswerResult answer = GAME_SERVICE.answerQuestion(user, gameId, roundNr, answerNr);
            System.out.println("Successfully answered question: " + answer.toString());
            return Response.ok(answer).type(MediaType.APPLICATION_JSON).build();
        } catch (InvalidVocabduelGameNrException e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).type(MediaType.TEXT_PLAIN).build();
        } catch (NoAccessException e) {
            e.printStackTrace();
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).type(MediaType.TEXT_PLAIN).build();
        } catch (InternalUserModuleException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/delete-account-and-game-widows")
    public Response deleteUser(@HeaderParam(AuthInterceptor.USER_HEADER) final Long userId) {
        try {
            final User user = USER_SERVICE.getUserDataById(userId);
            USER_SERVICE.deleteUser(user);
            System.out.println("Successfully deleted user: " + user.toString());
            GAME_SERVICE.removeWidowGames();
            System.out.println("Successfully removed widow games (if existent).");
            return Response.noContent().build();
        } catch (InternalUserModuleException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        }
    }
}
