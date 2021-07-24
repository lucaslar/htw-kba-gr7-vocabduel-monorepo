package de.htwberlin.kba.gr7.vocabduel.user_administration.dao;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> selectUsersByUsername(String searchString) {
        List<User> users = null;
        try {
            final String query = "select u from User u where lower(u.username) like :searchString";
            users = (List<User>) entityManager
                    .createQuery(query)
                    .setParameter("searchString", searchString.toLowerCase() + "%")
                    .getResultList();
        } catch (NoResultException ignored) {
        }
        return users;
    }

    @Override
    public User selectUserByEmail(String email) {
        User user = null;
        try {
            final String query = "from User as u where u.email like :email";
            user = (User) entityManager
                    .createQuery(query)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException ignored) {
        }
        return user;
    }

    @Override
    public User selectUserById(Long userId) {
        return entityManager.find(User.class, userId);
    }

    @Override
    public User selectUserByUsername(String username) {
        User user = null;
        try {
            final String query = "from User as u where u.username like :username";
            user = (User) entityManager
                    .createQuery(query)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException ignored) {
        }
        return user;
    }

    @Override
    public boolean updateUser(User user) {
        entityManager.merge(user);
        return true;
    }
}
