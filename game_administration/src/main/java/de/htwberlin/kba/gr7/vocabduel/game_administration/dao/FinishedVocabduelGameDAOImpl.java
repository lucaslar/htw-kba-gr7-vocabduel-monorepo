package de.htwberlin.kba.gr7.vocabduel.game_administration.dao;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.dao.FinishedVocabduelGameDAO;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.FinishedVocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.Result;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.RunningVocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.Date;
import java.util.List;

public class FinishedVocabduelGameDAOImpl implements FinishedVocabduelGameDAO {

    private final EntityManager ENTITY_MANAGER;

    public FinishedVocabduelGameDAOImpl (EntityManager entityManager){
        ENTITY_MANAGER = entityManager;
    }

    /**
     * insert FinishedVocabduelGame and remove RunningVocabduelGame
     * @param game RunningVocabduelGame
     */
    @Override
    public FinishedVocabduelGame insertFinishedVocabduelGame(RunningVocabduelGame game) {
        ENTITY_MANAGER.getTransaction().begin();
        final FinishedVocabduelGame finishedGame = new FinishedVocabduelGame(game);
        finishedGame.setFinishedTimestamp(new Date());
        finishedGame.setTotalPointsA((int) game.getRounds().stream().filter(r -> r.getResultPlayerA() == Result.WIN).count());
        finishedGame.setTotalPointsB((int) game.getRounds().stream().filter(r -> r.getResultPlayerB() == Result.WIN).count());
        ENTITY_MANAGER.persist(finishedGame);
        ENTITY_MANAGER.remove(game);
        ENTITY_MANAGER.getTransaction().commit();
        return finishedGame;
    }

    @Override
    public List<FinishedVocabduelGame> selectFinishedVocabduelGamesByUser(User user) {
        List<FinishedVocabduelGame> games = null;
        ENTITY_MANAGER.clear();
        ENTITY_MANAGER.getTransaction().begin();
        try {
            games = (List<FinishedVocabduelGame>) ENTITY_MANAGER
                    .createQuery("select g from FinishedVocabduelGame g where (g.playerA = :user or g.playerB = :user)")
                    .setParameter("user", user)
                    .getResultList();

        } catch (NoResultException ignored) {
        }
        ENTITY_MANAGER.getTransaction().commit();
        return games;
    }

    @Override
    public boolean deleteFinishedVocabduelGamesWhereUserDoesntExist() {
        boolean res = false;
        ENTITY_MANAGER.clear();
        ENTITY_MANAGER.getTransaction().begin();
        try {
            final List<FinishedVocabduelGame> finishedGames = (List<FinishedVocabduelGame>) ENTITY_MANAGER
                    .createQuery("select f from FinishedVocabduelGame f where (playerA_id not in (select id from User) and playerB_id not in (select id from User))")
                    .getResultList();
            finishedGames.forEach(ENTITY_MANAGER::remove);
            res = true;
        } catch (NoResultException ignored) {
        }
        ENTITY_MANAGER.getTransaction().commit();
        return res;
    }

}
