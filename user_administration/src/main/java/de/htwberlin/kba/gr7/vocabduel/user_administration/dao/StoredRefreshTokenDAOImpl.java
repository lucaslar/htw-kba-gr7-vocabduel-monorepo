package de.htwberlin.kba.gr7.vocabduel.user_administration.dao;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.UserOptimisticLockException;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.user_administration.model.StoredRefreshToken;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class StoredRefreshTokenDAOImpl implements StoredRefreshTokenDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public StoredRefreshToken selectStoredRefreshTokenByUserAndToken(User user, String refreshToken) throws NoResultException, UserOptimisticLockException {
        try {
            return (StoredRefreshToken) entityManager
                    .createQuery("from StoredRefreshToken where user = :user and refreshToken = :token")
                    .setParameter("user", user)
                    .setParameter("token", refreshToken)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw e;
        } catch (OptimisticLockException e) {
            throw new UserOptimisticLockException(e);
        }
    }

    @Override
    public void insertStoredRefreshTokenByUserAndToken(User user, String refreshToken) throws UserOptimisticLockException {
        try {
            entityManager.persist(new StoredRefreshToken(user, refreshToken));
        } catch (OptimisticLockException e) {
            throw new UserOptimisticLockException(e);
        }
    }

    @Override
    public void removeUserTokensIfFiveOrMorePresent(User user) throws UserOptimisticLockException {
        try {
            final List<StoredRefreshToken> storedRefreshTokens = (List<StoredRefreshToken>) entityManager
                    .createQuery("from StoredRefreshToken where user = :user")
                    .setParameter("user", user)
                    .getResultList();
            if (storedRefreshTokens.size() > 4) storedRefreshTokens.forEach(entityManager::remove);
        } catch (NoResultException ignored) {
            // ignored => a user might not have any refresh token stored in the database. Thus, it's no problem if none to be deleted are found
        } catch (OptimisticLockException e) {
            throw new UserOptimisticLockException(e);
        }
    }

    @Override
    public boolean deleteStoredRefreshTokenByUserId(Long userId) throws UserOptimisticLockException {
        boolean res = false;
        try {
            final List<StoredRefreshToken> tokens = entityManager
                    .createQuery("select s from StoredRefreshToken s where user_id = :user")
                    .setParameter("user", userId)
                    .getResultList();
            if (tokens != null && !tokens.isEmpty()) tokens.forEach(entityManager::remove);
            res = true;
        } catch (NoResultException ignored) {
            // ignored => a user might not have any refresh token stored in the database. Thus, it's no problem if none to be deleted are found
        } catch (OptimisticLockException e) {
            throw new UserOptimisticLockException(e);
        }
        return res;
    }

    @Override
    public boolean deleteStoredRefreshToken(StoredRefreshToken storedRefreshToken) throws UserOptimisticLockException {
        try {
            entityManager.remove(storedRefreshToken);
            return true;
        } catch (OptimisticLockException e) {
            throw new UserOptimisticLockException(e);
        }
    }

    public void setEntityManager(EntityManager entityManager){
        this.entityManager = entityManager;
    }
}
