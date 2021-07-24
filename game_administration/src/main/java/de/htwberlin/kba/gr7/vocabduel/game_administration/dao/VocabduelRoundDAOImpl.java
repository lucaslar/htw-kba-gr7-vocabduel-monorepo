package de.htwberlin.kba.gr7.vocabduel.game_administration.dao;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.VocabduelRound;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class VocabduelRoundDAOImpl implements VocabduelRoundDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean updateVocabduelRound(VocabduelRound round) {
        entityManager.merge(round);
        return true;
    }

    @Override
    public VocabduelRound selectVocabduelRoundByGameIdAndUser(User player, Long gameId) {
        VocabduelRound round = null;
        try {
            round = (VocabduelRound) entityManager
                    .createQuery("select r from RunningVocabduelGame g inner join g.rounds r where g.id = :gameId and ((g.playerA = :user and r.resultPlayerA is null) or (g.playerB = :user and r.resultPlayerB is null))")
                    .setParameter("user", player)
                    .setParameter("gameId", gameId)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException ignored) {
        }
        return round;
    }
}
