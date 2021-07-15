package de.htwberlin.kba.gr7.vocabduel.game_administration.dao;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.RunningVocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

public class RunningVocabduelGameDAOImpl implements RunningVocabduelGameDAO {

    private final EntityManager ENTITY_MANAGER;

    public RunningVocabduelGameDAOImpl(EntityManager entityManager){
        ENTITY_MANAGER = entityManager;
    }

    /**
     * insert RunningVocabduelGame to database.
     * Because whole Game with VocabduelRounds will be inserted, we don't need
     *  a insert method in VocabduelRoundDAO
     * @param game RunningVocabduelGame to be inserted
     */
    @Override
    public void insertRunningVocabduelGame(RunningVocabduelGame game) {
        ENTITY_MANAGER.getTransaction().begin();
        ENTITY_MANAGER.persist(game);
        ENTITY_MANAGER.getTransaction().commit();

    }

    @Override
    public List<RunningVocabduelGame> selectRunningVocabduelGamesByUser(User user) {
        List<RunningVocabduelGame> games = null;
        ENTITY_MANAGER.getTransaction().begin();
        try {
            games = (List<RunningVocabduelGame>) ENTITY_MANAGER
                    .createQuery("select g from RunningVocabduelGame g where (playerB = :user or playerA = :user)")
                    .setParameter("user", user)
                    .getResultList();
        } catch (NoResultException ignored) {
        }
        ENTITY_MANAGER.getTransaction().commit();
        return games;
    }

    @Override
    public RunningVocabduelGame selectRunningVocabduelGameByGameIdAndUser(User player, Long gameId) {
        RunningVocabduelGame myGame = null;
        ENTITY_MANAGER.clear();
        ENTITY_MANAGER.getTransaction().begin();
        try {
            myGame = (RunningVocabduelGame) ENTITY_MANAGER
                    .createQuery("select g from RunningVocabduelGame g where g.id = :gameId and (g.playerA = :user or g.playerB = :user)")
                    .setParameter("user", player)
                    .setParameter("gameId", gameId)
                    .getSingleResult();
        } catch (NoResultException ignored) {
        }
        ENTITY_MANAGER.getTransaction().commit();
        return myGame;
    }

    /**
     * delete RunningVocabduelGame and VocabduelRounds, so that
     *      delete method in VocabduelRoundsDAO is not needed
     * @return boolean true if everything went right
     *                 false if something bad happened
     */
    @Override
    public boolean deleteRunningVocabduelGameWhereUserDoesntExist() {
        boolean res = false;
        ENTITY_MANAGER.clear();
        ENTITY_MANAGER.getTransaction().begin();
        try {
            final List<RunningVocabduelGame> runningGames = (List<RunningVocabduelGame>) ENTITY_MANAGER
                    .createQuery("select r from RunningVocabduelGame r where (playerA_id not in (select id from User) or playerB_id not in (select id from User))")
                    .getResultList();
            runningGames.forEach(ENTITY_MANAGER::remove);
            res = true;
        } catch (NoResultException ignored) {
        }
        ENTITY_MANAGER.getTransaction().commit();
        return res;
    }
}
