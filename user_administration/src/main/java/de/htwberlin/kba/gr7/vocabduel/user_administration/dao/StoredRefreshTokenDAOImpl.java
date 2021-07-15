package de.htwberlin.kba.gr7.vocabduel.user_administration.dao;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.user_administration.model.StoredRefreshToken;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

public class StoredRefreshTokenDAOImpl implements StoredRefreshTokenDAO{

    private final EntityManager ENTITY_MANAGER;

    public StoredRefreshTokenDAOImpl(EntityManager entityManager){
        ENTITY_MANAGER = entityManager;
    }

    @Override
    public StoredRefreshToken selectStoredRefreshTokenByUserAndToken(User user, String refreshToken) {
        ENTITY_MANAGER.getTransaction().begin();
        final StoredRefreshToken foundToken = (StoredRefreshToken) ENTITY_MANAGER
                .createQuery("from StoredRefreshToken where user = :user and refreshToken = :token")
                .setParameter("user", user)
                .setParameter("token", refreshToken)
                .getSingleResult();
        ENTITY_MANAGER.getTransaction().commit();
        return foundToken;
    }

    @Override
    public void insertStoredRefreshTokenByUserAndToken(User user, String refreshToken) {
        ENTITY_MANAGER.getTransaction().begin();
        try {
            final List<StoredRefreshToken> storedRefreshTokens = (List<StoredRefreshToken>) ENTITY_MANAGER
                    .createQuery("from StoredRefreshToken where user = :user")
                    .setParameter("user", user)
                    .getResultList();
            if (storedRefreshTokens.size() > 4) storedRefreshTokens.forEach(ENTITY_MANAGER::remove);
        } catch (NoResultException ignored) {
        }

        ENTITY_MANAGER.persist(new StoredRefreshToken(user, refreshToken));
        ENTITY_MANAGER.getTransaction().commit();
    }

    @Override
    public boolean deleteStoredRefreshTokenByUserId(Long userId) {
        boolean res = false;
        ENTITY_MANAGER.getTransaction().begin();
        try {
            final List<StoredRefreshToken> tokens = ENTITY_MANAGER
                    .createQuery("select s from StoredRefreshToken s where user_id = :user")
                    .setParameter("user", userId)
                    .getResultList();
            if (tokens != null && !tokens.isEmpty()) tokens.forEach(ENTITY_MANAGER::remove);
            res = true;
        } catch (NoResultException ignored) {
        }
        ENTITY_MANAGER.getTransaction().commit();
        return res;
    }

    @Override
    public boolean deleteStoredRefreshToken(StoredRefreshToken storedRefreshToken) {
        ENTITY_MANAGER.getTransaction().begin();
        ENTITY_MANAGER.remove(storedRefreshToken);
        ENTITY_MANAGER.getTransaction().commit();
        return true;
    }
}
