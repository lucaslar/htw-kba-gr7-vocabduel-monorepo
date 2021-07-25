package de.htwberlin.kba.gr7.vocabduel.game_administration.dao;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.InternalGameModuleException;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.FinishedVocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.Result;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.RunningVocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@Repository
public class FinishedVocabduelGameDAOImpl implements FinishedVocabduelGameDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public FinishedVocabduelGame insertFinishedVocabduelGame(RunningVocabduelGame game) throws InternalGameModuleException {
        try {
            final FinishedVocabduelGame finishedGame = new FinishedVocabduelGame(game);
            finishedGame.setFinishedTimestamp(new Date());
            finishedGame.setTotalPointsA((int) game.getRounds().stream().filter(r -> r.getResultPlayerA() == Result.WIN).count());
            finishedGame.setTotalPointsB((int) game.getRounds().stream().filter(r -> r.getResultPlayerB() == Result.WIN).count());
            entityManager.persist(finishedGame);
            entityManager.remove(game);
            return finishedGame;
        } catch (Exception e){
            throw new InternalGameModuleException(e);
        }
    }

    @Override
    public List<FinishedVocabduelGame> selectFinishedVocabduelGamesByUser(User user) throws InternalGameModuleException {
        List<FinishedVocabduelGame> games = null;
        entityManager.clear();
        try {
            games = (List<FinishedVocabduelGame>) entityManager
                    .createQuery("select g from FinishedVocabduelGame g where (g.playerA = :user or g.playerB = :user)")
                    .setParameter("user", user)
                    .getResultList();

        } catch (NoResultException ignored) {
            // ignored => return null (games) if a user has no finished games yet
        } catch (Exception e){
            throw new InternalGameModuleException(e);
        }
        return games;
    }

    @Override
    public boolean deleteFinishedVocabduelGamesWhereUserDoesntExist() throws InternalGameModuleException {
        boolean res = false;
        entityManager.clear();
        try {
            final List<FinishedVocabduelGame> finishedGames = (List<FinishedVocabduelGame>) entityManager
                    .createQuery("select f from FinishedVocabduelGame f where (playerA_id not in (select id from User) and playerB_id not in (select id from User))")
                    .getResultList();
            finishedGames.forEach(entityManager::remove);
            res = true;
        } catch (NoResultException ignored) {
            // ignored => if no orphaned finished games to be deleted are found, it's not a problem
        } catch (Exception e){
            throw new InternalGameModuleException(e);
        }
        return res;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
