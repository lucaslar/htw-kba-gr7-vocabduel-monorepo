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
    private EntityManager entityManager;

    @Override
    public void insertRunningVocabduelGame(RunningVocabduelGame game) {
        entityManager.persist(game);
    }

    @Override
    public List<RunningVocabduelGame> selectRunningVocabduelGamesByUser(User user) {
        List<RunningVocabduelGame> games = null;
        try {
            games = (List<RunningVocabduelGame>) entityManager
                    .createQuery("select g from RunningVocabduelGame g where (playerB = :user or playerA = :user)")
                    .setParameter("user", user)
                    .getResultList();
        } catch (NoResultException ignored) {
        }
        return games;
    }

    @Override
    public RunningVocabduelGame selectRunningVocabduelGameByGameIdAndUser(User player, Long gameId) {
        RunningVocabduelGame myGame = null;
        entityManager.clear();
        try {
            myGame = (RunningVocabduelGame) entityManager
                    .createQuery("select g from RunningVocabduelGame g where g.id = :gameId and (g.playerA = :user or g.playerB = :user)")
                    .setParameter("user", player)
                    .setParameter("gameId", gameId)
                    .getSingleResult();
        } catch (NoResultException ignored) {
        }
        return myGame;
    }

    @Override
    public boolean deleteRunningVocabduelGameWhereUserDoesntExist() {
        boolean res = false;
        entityManager.clear();
        try {
            final List<RunningVocabduelGame> runningGames = (List<RunningVocabduelGame>) entityManager
                    .createQuery("select r from RunningVocabduelGame r where (playerA_id not in (select id from User) or playerB_id not in (select id from User))")
                    .getResultList();
            runningGames.forEach(entityManager::remove);
            res = true;
        } catch (NoResultException ignored) {
        }
        return res;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
