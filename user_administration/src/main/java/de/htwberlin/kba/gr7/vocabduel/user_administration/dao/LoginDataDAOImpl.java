package de.htwberlin.kba.gr7.vocabduel.user_administration.dao;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.user_administration.model.LoginData;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class LoginDataDAOImpl implements LoginDataDAO {

    @PersistenceContext
    private final EntityManager ENTITY_MANAGER;

    public LoginDataDAOImpl(final EntityManager entityManager) {
        ENTITY_MANAGER = entityManager;
    }

    @Override
    public void insertLoginData(LoginData loginData) {
        ENTITY_MANAGER.getTransaction().begin();
        ENTITY_MANAGER.persist(loginData);
        ENTITY_MANAGER.getTransaction().commit();
    }

    @Override
    public LoginData selectLoginDataByUserEmail(String email) {
        LoginData loginData = null;
        ENTITY_MANAGER.getTransaction().begin();
        try {
            loginData = (LoginData) ENTITY_MANAGER
                    .createQuery("SELECT l FROM LoginData l INNER JOIN l.user u WHERE u.email LIKE :email")
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException ignored) {
        }
        ENTITY_MANAGER.getTransaction().commit();
        return loginData;
    }

    @Override
    public LoginData selectLoginDataByUser(User user) {
        LoginData loginData = null;
        ENTITY_MANAGER.getTransaction().begin();
        try {
            loginData = (LoginData) ENTITY_MANAGER
                    .createQuery("SELECT l FROM LoginData l INNER JOIN l.user u WHERE u = :user")
                    .setParameter("user", user)
                    .getSingleResult();
        } catch (NoResultException ignored) {
        }
        ENTITY_MANAGER.getTransaction().commit();
        return loginData;
    }

    @Override
    public boolean deleteLoginDataByUserId(Long userId) {
        boolean res = false;
        ENTITY_MANAGER.getTransaction().begin();
        try {
            final List<LoginData> loginData = ENTITY_MANAGER
                    .createQuery("select l from LoginData l where user_id = :user")
                    .setParameter("user", userId)
                    .getResultList();
            if (loginData != null && !loginData.isEmpty()) loginData.forEach(ENTITY_MANAGER::remove);
            res = true;
        } catch (NoResultException ignored) {
        }
        ENTITY_MANAGER.getTransaction().commit();
        return res;
    }
}
