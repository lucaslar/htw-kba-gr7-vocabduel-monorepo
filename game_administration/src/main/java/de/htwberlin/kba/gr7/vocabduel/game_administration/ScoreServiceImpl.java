package de.htwberlin.kba.gr7.vocabduel.game_administration;

import de.htwberlin.kba.gr7.vocabduel.game_administration.dao.FinishedVocabduelGameDAOImpl;
import de.htwberlin.kba.gr7.vocabduel.game_administration.dao.RunningVocabduelGameDAOImpl;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.ScoreService;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.UnfinishedGameException;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.NoAccessException;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.*;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.UserService;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.InvalidUserException;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScoreServiceImpl implements ScoreService {
    private final UserService USER_SERVICE;

    private final FinishedVocabduelGameDAOImpl finishedVocabduelGameDAO;
    private final RunningVocabduelGameDAOImpl runningVocabduelGameDAO;

    public ScoreServiceImpl(final UserService userService, final EntityManager entityManager) {
        USER_SERVICE = userService;

        finishedVocabduelGameDAO = new FinishedVocabduelGameDAOImpl(entityManager);
        runningVocabduelGameDAO = new RunningVocabduelGameDAOImpl(entityManager);
    }

    @Override
    public List<PersonalFinishedGame> getPersonalFinishedGames(User user) throws InvalidUserException {
        if (user == null || USER_SERVICE.getUserDataById(user.getId()) == null) {
            throw new InvalidUserException("User could not be found");
        }

        List<FinishedVocabduelGame> games = finishedVocabduelGameDAO.selectFinishedVocabduelGamesByUser(user);

        return games != null
                ? games.stream().map(g -> personifyFinishedGame(user, g)).collect(Collectors.toList())
                : null;
    }

    @Override
    public ScoreRecord getRecordOfUser(User user) throws InvalidUserException {
        final List<PersonalFinishedGame> finishedGames = getPersonalFinishedGames(user);
        if (finishedGames == null || finishedGames.isEmpty()) return new ScoreRecord(user);
        else {
            final long wins = finishedGames.stream().filter(g -> g.getGameResult() == GameResult.WIN).count();
            final long losses = finishedGames.stream().filter(g -> g.getGameResult() == GameResult.LOSS).count();
            final long draws = finishedGames.size() - wins - losses;
            return new ScoreRecord(user, wins, losses, draws);
        }
    }

    @Override
    public PersonalFinishedGame finishGame(User player, long gameId) throws UnfinishedGameException, NoAccessException {
        RunningVocabduelGame game = null;
        if (player != null) {
            game = runningVocabduelGameDAO.selectRunningVocabduelGameByGameIdAndUser(player, gameId);
        }
        if (game == null) {
            throw new NoAccessException("No round found or you do not seem to have access. Are you sure you stated a running game you have access to? Check your games to find out.");
        } else if (game.getRounds().stream().anyMatch(r -> r.getResultPlayerA() == null || r.getResultPlayerB() == null)) {
            throw new UnfinishedGameException("The game has not been finished yet.");
        } else {
            final FinishedVocabduelGame finishedGame = finishedVocabduelGameDAO.insertFinishedVocabduelGame(game);
            return personifyFinishedGame(player, finishedGame);
        }
    }

    private PersonalFinishedGame personifyFinishedGame(final User user, final FinishedVocabduelGame finishedVocabduelGame) {
        final PersonalFinishedGame personified = new PersonalFinishedGame();
        personified.setFinishedTimestamp(finishedVocabduelGame.getFinishedTimestamp());
        personified.setId(finishedVocabduelGame.getId());
        personified.setLearntLanguage(finishedVocabduelGame.getLearntLanguage());
        personified.setKnownLanguage(finishedVocabduelGame.getKnownLanguage());

        if (finishedVocabduelGame.getPlayerA() != null && finishedVocabduelGame.getPlayerA().getId().equals(user.getId())) {
            personified.setOwnPoints(finishedVocabduelGame.getTotalPointsA());
            personified.setOpponentPoints(finishedVocabduelGame.getTotalPointsB());
            personified.setOpponent(finishedVocabduelGame.getPlayerB());
        } else {
            personified.setOwnPoints(finishedVocabduelGame.getTotalPointsB());
            personified.setOpponentPoints(finishedVocabduelGame.getTotalPointsA());
            personified.setOpponent(finishedVocabduelGame.getPlayerA());
        }
        GameResult result = gameResultByPoints(personified.getOwnPoints(), personified.getOpponentPoints());
        personified.setGameResult(result);

        return personified;
    }

    private GameResult gameResultByPoints(final int own, final int opponent) {
        if (own == opponent) return GameResult.DRAW;
        else if (own < opponent) return GameResult.LOSS;
        else return GameResult.WIN;
    }
}
