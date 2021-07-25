package de.htwberlin.kba.gr7.vocabduel.game_administration.dao;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.GameOptimisticLockException;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.RunningVocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RunningVocabduelGameDAOImpl implements RunningVocabduelGameDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void insertRunningVocabduelGame(RunningVocabduelGame game) throws GameOptimisticLockException {
        try {
            entityManager.persist(game);
        } catch (OptimisticLockException e){
            throw new GameOptimisticLockException(e);
        }
    }

    @Override
    public List<RunningVocabduelGame> selectRunningVocabduelGamesByUser(User user) throws GameOptimisticLockException {
        List<RunningVocabduelGame> games = null;
        try {
            games = (List<RunningVocabduelGame>) entityManager
                    .createQuery("select g from RunningVocabduelGame g where (playerB = :user or playerA = :user)")
                    .setParameter("user", user)
                    .getResultList();
        } catch (NoResultException ignored) {
            // ignored => return null (games) in case of no result
        } catch (OptimisticLockException e){
            throw new GameOptimisticLockException(e);
        }
        return games;
    }

    @Override
    public RunningVocabduelGame selectRunningVocabduelGameByGameIdAndUser(User player, Long gameId) throws GameOptimisticLockException {
        RunningVocabduelGame myGame = null;
        entityManager.clear();
        try {
            myGame = (RunningVocabduelGame) entityManager
                    .createQuery("select g from RunningVocabduelGame g where g.id = :gameId and (g.playerA = :user or g.playerB = :user)")
                    .setParameter("user", player)
                    .setParameter("gameId", gameId)
                    .getSingleResult();
        } catch (NoResultException ignored) {
            // ignored => return null (myGame) in case of no result
        } catch (OptimisticLockException e){
            throw new GameOptimisticLockException(e);
        }
        return myGame;
    }

    @Override
    public boolean deleteRunningVocabduelGameWhereUserDoesntExist() throws GameOptimisticLockException {
        boolean res = false;
        entityManager.clear();
        try {
            final List<RunningVocabduelGame> runningGames = (List<RunningVocabduelGame>) entityManager
                    .createQuery("select r from RunningVocabduelGame r where (playerA_id not in (select id from User) or playerB_id not in (select id from User))")
                    .getResultList();

            for (final RunningVocabduelGame game : runningGames) {
                entityManager.remove(game);
                entityManager.flush();
            }
            res = true;
        } catch (NoResultException ignored) {
            // ignored => a user might have no finished game => not a problem
        } catch (OptimisticLockException e){
            throw new GameOptimisticLockException(e);
            // ignored => a user might have no running game => not a problem
        } catch (Exception e){
            throw new InternalGameModuleException(e);
        }
        return res;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
