package de.htwberlin.kba.gr7.vocabduel.user_administration.dao;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.UserOptimisticLockException;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.user_administration.model.LoginData;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class LoginDataDAOImpl implements LoginDataDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void insertLoginData(LoginData loginData) throws UserOptimisticLockException {
        try {
            entityManager.persist(loginData);
        } catch (OptimisticLockException e) {
            throw new UserOptimisticLockException(e);
        }
    }

    @Override
    public LoginData selectLoginDataByUserEmail(String email) throws UserOptimisticLockException {
        LoginData loginData = null;
        try {
            loginData = (LoginData) entityManager
                    .createQuery("SELECT l FROM LoginData l INNER JOIN l.user u WHERE u.email LIKE :email")
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException ignored) {
            // ignored => return null (loginData) if no entry could be found
        } catch (OptimisticLockException e) {
            throw new UserOptimisticLockException(e);
        }
        return loginData;
    }

    @Override
    public LoginData selectLoginDataByUser(User user) throws UserOptimisticLockException {
        LoginData loginData = null;
        try {
            loginData = (LoginData) entityManager
                    .createQuery("SELECT l FROM LoginData l INNER JOIN l.user u WHERE u = :user")
                    .setParameter("user", user)
                    .getSingleResult();
        } catch (NoResultException ignored) {
            // ignored => return null (loginData) if no user could be found
        } catch (OptimisticLockException e) {
            throw new UserOptimisticLockException(e);
        }
        return loginData;
    }

    @Override
    public boolean deleteLoginDataByUserId(Long userId) throws UserOptimisticLockException {
        boolean res = false;
        try {
            final List<LoginData> loginData = entityManager
                    .createQuery("select l from LoginData l where user_id = :user")
                    .setParameter("user", userId)
                    .getResultList();
            if (loginData != null && !loginData.isEmpty()) loginData.forEach(entityManager::remove);
            res = true;
        } catch (NoResultException ignored) {
            // ignored => a user might not have any login data stored in the database. Thus, it's no problem if none to be deleted are found
        } catch (OptimisticLockException e) {
            throw new UserOptimisticLockException(e);
        }
        return res;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
