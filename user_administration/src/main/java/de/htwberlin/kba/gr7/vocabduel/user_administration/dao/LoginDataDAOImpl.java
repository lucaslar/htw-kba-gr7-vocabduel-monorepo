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
    private EntityManager entityManager;

    public LoginDataDAOImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public void insertLoginData(LoginData loginData) {
        entityManager.persist(loginData);
    }

    @Override
    public LoginData selectLoginDataByUserEmail(String email) {
        LoginData loginData = null;
        try {
            loginData = (LoginData) entityManager
                    .createQuery("SELECT l FROM LoginData l INNER JOIN l.user u WHERE u.email LIKE :email")
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException ignored) {
        }
        return loginData;
    }

    @Override
    public LoginData selectLoginDataByUser(User user) {
        LoginData loginData = null;
        try {
            loginData = (LoginData) entityManager
                    .createQuery("SELECT l FROM LoginData l INNER JOIN l.user u WHERE u = :user")
                    .setParameter("user", user)
                    .getSingleResult();
        } catch (NoResultException ignored) {
        }
        return loginData;
    }

    @Override
    public boolean deleteLoginDataByUserId(Long userId) {
        boolean res = false;
        try {
            final List<LoginData> loginData = entityManager
                    .createQuery("select l from LoginData l where user_id = :user")
                    .setParameter("user", userId)
                    .getResultList();
            if (loginData != null && !loginData.isEmpty()) loginData.forEach(entityManager::remove);
            res = true;
        } catch (NoResultException ignored) {
        }
        return res;
    }
}
