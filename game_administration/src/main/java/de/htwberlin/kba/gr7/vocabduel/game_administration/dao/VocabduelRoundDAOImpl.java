package de.htwberlin.kba.gr7.vocabduel.game_administration.dao;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.GameOptimisticLockException;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.VocabduelRound;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;

@Repository
public class VocabduelRoundDAOImpl implements VocabduelRoundDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean updateVocabduelRound(VocabduelRound round) throws GameOptimisticLockException {
        try {
            entityManager.merge(round);
            return true;
        } catch (OptimisticLockException e){
            throw new GameOptimisticLockException(e);
        }
    }

    @Override
    public VocabduelRound selectVocabduelRoundByGameIdAndUser(User player, Long gameId) throws GameOptimisticLockException {
        VocabduelRound round = null;
        try {
            round = (VocabduelRound) entityManager
                    .createQuery("select r from RunningVocabduelGame g inner join g.rounds r where g.id = :gameId and ((g.playerA = :user and r.resultPlayerA is null) or (g.playerB = :user and r.resultPlayerB is null))")
                    .setParameter("user", player)
                    .setParameter("gameId", gameId)
                    .setMaxResults(1)
                    .getSingleResult();
            initializeLazyLoadedRoundVocableData(round);
        } catch (NoResultException ignored) {
            // ignored => return null (round) if no round could be found
        } catch (OptimisticLockException e){
            throw new GameOptimisticLockException(e);
        }
        return round;
    }

    private void initializeLazyLoadedRoundVocableData(final VocabduelRound round) {
        Hibernate.initialize(round.getQuestion().getVocable().getSynonyms());
        Hibernate.initialize(round.getQuestion().getVocable().getAdditionalInfo());
        round.getAnswers().forEach(a -> {
            Hibernate.initialize(a.getSynonyms());
            Hibernate.initialize(a.getAdditionalInfo());
        });
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
