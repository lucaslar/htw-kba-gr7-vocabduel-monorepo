package de.htwberlin.kba.gr7.vocabduel.game_administration.dao;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.RunningVocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RunningVocabduelGameDAOImpl implements RunningVocabduelGameDAO {

    @PersistenceContext
    private final EntityManager ENTITY_MANAGER;

    public RunningVocabduelGameDAOImpl(final EntityManager entityManager) {
        ENTITY_MANAGER = entityManager;
    }

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
