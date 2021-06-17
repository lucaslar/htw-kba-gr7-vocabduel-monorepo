package de.htwberlin.kba.gr7.vocabduel.game_administration;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.ScoreService;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.UnfinishedGameException;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.NoAccessException;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.*;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.Date;
import java.util.List;

@Service
public class ScoreServiceImpl implements ScoreService {

    private final EntityManager ENTITY_MANAGER;

    public ScoreServiceImpl() {
        ENTITY_MANAGER = EntityFactoryManagement.getEntityFactory().createEntityManager();
    }

    @Override
    public List<PersonalFinishedGame> getPersonalFinishedGames(User user) {
        return null;
    }

    @Override
    public int getTotalWinsOfUser(User user) {
        return 0;
    }

    @Override
    public int getTotalLossesOfUser(User user) {
        return 0;
    }

    @Override
    public PersonalFinishedGame finishGame(User player, long gameId) throws UnfinishedGameException, NoAccessException {
        VocabduelGame game = null;
        if (player != null) {
            ENTITY_MANAGER.getTransaction().begin();
            try {
                game = (VocabduelGame) ENTITY_MANAGER
                        .createQuery("select g from VocabduelGame g where g.id = :gameId and (g.playerA = :user or g.playerB = :user)")
                        .setParameter("user", player)
                        .setParameter("gameId", gameId)
                        .getSingleResult();

            } catch (NoResultException ignored) {
            }
            ENTITY_MANAGER.getTransaction().commit();
        }
        if (game == null) {
            throw new NoAccessException("No round found or you do not seem to have access. Are you sure you stated a running game you have access to? Check your games to find out.");
        } else if (game.getRounds().stream().anyMatch(r -> r.getResultPlayerA() == null || r.getResultPlayerB() == null)) {
            throw new UnfinishedGameException("The game has not been finished yet.");
        } else {
            final FinishedVocabduelGame finishedGame = new FinishedVocabduelGame(game);
            finishedGame.setFinishedTimestamp(new Date());
            finishedGame.setTotalPointsA((int) game.getRounds().stream().filter(r -> r.getResultPlayerA() == Result.WIN).count());
            finishedGame.setTotalPointsB((int) game.getRounds().stream().filter(r -> r.getResultPlayerB() == Result.WIN).count());

            ENTITY_MANAGER.getTransaction().begin();
            ENTITY_MANAGER.merge(finishedGame);
            ENTITY_MANAGER.getTransaction().commit();

            return personifyFinishedGame(player, finishedGame);
        }
    }

    private PersonalFinishedGame personifyFinishedGame (final User user, final FinishedVocabduelGame finishedVocabduelGame) {
        final PersonalFinishedGame personified = new PersonalFinishedGame();
        personified.setFinishedTimestamp(finishedVocabduelGame.getFinishedTimestamp());

        if (finishedVocabduelGame.getPlayerA().getId().equals(user.getId())) {
            personified.setOwnPoints(finishedVocabduelGame.getTotalPointsA());
            personified.setOpponentPoints(finishedVocabduelGame.getTotalPointsB());
            personified.setOpponent(finishedVocabduelGame.getPlayerB());
        } else {
            personified.setOwnPoints(finishedVocabduelGame.getTotalPointsB());
            personified.setOpponentPoints(finishedVocabduelGame.getTotalPointsA());
            personified.setOpponent(finishedVocabduelGame.getPlayerA());
        }
        GameResult result = gameResultByPoints(personified.getOwnPoints(),personified.getOpponentPoints());
        personified.setGameResult(result);

        return personified;
    }

    private GameResult gameResultByPoints (final int own, final int opponent) {
        if (own == opponent) return GameResult.DRAW;
        else if (own < opponent) return GameResult.LOSS;
        else return GameResult.WIN;
    }
}
