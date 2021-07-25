package de.htwberlin.kba.gr7.vocabduel.game_administration.rest;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.ScoreService;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.InternalGameModuleException;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.NoAccessException;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.UnfinishedGameException;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.PersonalFinishedGame;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.ScoreRecord;
import de.htwberlin.kba.gr7.vocabduel.shared_logic.rest.AuthInterceptor;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.UserService;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.InternalUserModuleException;
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
    private final UserService USER_SERVICE;

    @Inject
    public ScoreServiceRestAdapter(final ScoreService scoreService, final UserService userService) {
        SCORE_SERVICE = scoreService;
        USER_SERVICE = userService;
    }

    @POST
    @Path("/finish-game")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response finishGame(@HeaderParam(AuthInterceptor.USER_HEADER) final Long userId, final long gameId) {
        try {
            final User user = USER_SERVICE.getUserDataById(userId);
            final PersonalFinishedGame game = SCORE_SERVICE.finishGame(user, gameId);
            System.out.println("Successfully finished Game: " + game.toString());
            return Response.ok(game).type(MediaType.APPLICATION_JSON_TYPE).build();
        } catch (NoAccessException e) {
            e.printStackTrace();
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).type(MediaType.TEXT_PLAIN_TYPE).build();
        } catch (UnfinishedGameException e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).type(MediaType.TEXT_PLAIN_TYPE).build();
        } catch (InternalUserModuleException | InternalGameModuleException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/finished-games")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response getPersonalFinishedGames(@HeaderParam(AuthInterceptor.USER_HEADER) final Long userId) {
        try {
            final User user = USER_SERVICE.getUserDataById(userId);
            List<PersonalFinishedGame> games = SCORE_SERVICE.getPersonalFinishedGames(user);
            return Response.ok(games).type(MediaType.APPLICATION_JSON).build();
        } catch (InvalidUserException e) {
            return Response.status(Response.Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        } catch (InternalUserModuleException | InternalGameModuleException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/record")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response getOwnRecord(@HeaderParam(AuthInterceptor.USER_HEADER) final Long userId) {
        return getRecordCore(userId);
    }

    @GET
    @Path("/record/{userId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response getRecordOfUser(@PathParam("userId") final Long userId) {
        return getRecordCore(userId);
    }

    private Response getRecordCore(final Long userId) {
        try {
            final User user = USER_SERVICE.getUserDataById(userId);
            final ScoreRecord record = SCORE_SERVICE.getRecordOfUser(user);
            return Response.ok(record).type(MediaType.APPLICATION_JSON).build();
        } catch (InvalidUserException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).type(MediaType.TEXT_PLAIN).build();
        } catch (InternalUserModuleException | InternalGameModuleException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        }
    }
}
