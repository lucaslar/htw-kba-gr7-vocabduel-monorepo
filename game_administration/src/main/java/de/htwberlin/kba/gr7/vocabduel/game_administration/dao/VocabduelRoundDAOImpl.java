package de.htwberlin.kba.gr7.vocabduel.game_administration.dao;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.dao.VocabduelRoundDAO;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.RunningVocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.VocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.VocabduelRound;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

public class VocabduelRoundDAOImpl implements VocabduelRoundDAO {

    private final EntityManager ENTITY_MANAGER;

    public VocabduelRoundDAOImpl (EntityManager entityManager){
        ENTITY_MANAGER = entityManager;
    }

    @Override
    public boolean updateVocabduelRound(VocabduelRound round) {
        ENTITY_MANAGER.getTransaction().begin();
        ENTITY_MANAGER.merge(round);
        ENTITY_MANAGER.getTransaction().commit();
        return true;
    }

    @Override
    public boolean deleteVocabduelRoundByGame(VocabduelGame game) {
        return false;
    }

    @Override
    public VocabduelRound selectVocabduelRoundByGameAndUser(RunningVocabduelGame game, User player) {
        VocabduelRound round = null;
        ENTITY_MANAGER.getTransaction().begin();
        try {
            round = (VocabduelRound) ENTITY_MANAGER
                    .createQuery("select r from RunningVocabduelGame g inner join g.rounds r where g.id = :gameId and ((g.playerA = :user and r.resultPlayerA is null) or (g.playerB = :user and r.resultPlayerB is null))")
                    .setParameter("user", player)
                    .setParameter("gameId", game.getId())
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException ignored) {
        }
        ENTITY_MANAGER.getTransaction().commit();
        return round;
    }
}
