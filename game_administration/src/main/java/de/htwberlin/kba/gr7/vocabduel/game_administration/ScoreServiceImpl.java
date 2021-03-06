package de.htwberlin.kba.gr7.vocabduel.game_administration;

import de.htwberlin.kba.gr7.vocabduel.game_administration.dao.FinishedVocabduelGameDAO;
import de.htwberlin.kba.gr7.vocabduel.game_administration.dao.RunningVocabduelGameDAO;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.ScoreService;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.GameOptimisticLockException;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.UnfinishedGameException;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.NoAccessException;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.*;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.UserService;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.UserOptimisticLockException;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.InvalidUserException;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ScoreServiceImpl implements ScoreService {
    private final UserService USER_SERVICE;

    private final FinishedVocabduelGameDAO finishedVocabduelGameDAO;
    private final RunningVocabduelGameDAO runningVocabduelGameDAO;

    public ScoreServiceImpl(final UserService userService, final FinishedVocabduelGameDAO finishedVocabduelGameDao, final RunningVocabduelGameDAO runningVocabduelGameDao) {
        USER_SERVICE = userService;
        finishedVocabduelGameDAO = finishedVocabduelGameDao;
        runningVocabduelGameDAO = runningVocabduelGameDao;
    }

    @Override
    public List<PersonalFinishedGame> getPersonalFinishedGames(User user) throws InvalidUserException, UserOptimisticLockException, GameOptimisticLockException {
        if (user == null || USER_SERVICE.getUserDataById(user.getId()) == null) {
            throw new InvalidUserException("User could not be found");
        }

        List<FinishedVocabduelGame> games = finishedVocabduelGameDAO.selectFinishedVocabduelGamesByUser(user);

        return games != null
                ? games.stream().map(g -> personifyFinishedGame(user, g)).collect(Collectors.toList())
                : null;
    }

    @Override
    public ScoreRecord getRecordOfUser(User user) throws InvalidUserException, UserOptimisticLockException, GameOptimisticLockException {
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
    public PersonalFinishedGame finishGame(User player, long gameId) throws UnfinishedGameException, NoAccessException, GameOptimisticLockException {
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
