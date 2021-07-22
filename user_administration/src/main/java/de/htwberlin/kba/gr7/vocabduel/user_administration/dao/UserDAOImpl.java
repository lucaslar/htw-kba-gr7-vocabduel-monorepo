package de.htwberlin.kba.gr7.vocabduel.user_administration.dao;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO{

    @PersistenceContext
    private final EntityManager ENTITY_MANAGER;

    public UserDAOImpl(final EntityManager entityManager){
        ENTITY_MANAGER = entityManager;
    }

    @Override
    public List<User> selectUsersByUsername(String searchString) {
        List<User> users = null;
        ENTITY_MANAGER.getTransaction().begin();
        try {
            final String query = "select u from User u where lower(u.username) like :searchString";
            users = (List<User>) ENTITY_MANAGER
                    .createQuery(query)
                    .setParameter("searchString", searchString.toLowerCase() + "%")
                    .getResultList();
        } catch (NoResultException ignored) {
        }
        ENTITY_MANAGER.getTransaction().commit();
        return users;
    }

    @Override
    public User selectUserByEmail(String email) {
        User user = null;
        ENTITY_MANAGER.getTransaction().begin();
        try {
            final String query = "from User as u where u.email like :email";
            user = (User) ENTITY_MANAGER
                    .createQuery(query)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException ignored) {
        }
        ENTITY_MANAGER.getTransaction().commit();
        return user;
    }

    @Override
    public User selectUserById(Long userId) {
        return ENTITY_MANAGER.find(User.class, userId);
    }

    @Override
    public User selectUserByUsername(String username) {
        User user = null;
        ENTITY_MANAGER.getTransaction().begin();
        try {
            final String query = "from User as u where u.username like :username";
            user = (User) ENTITY_MANAGER
                    .createQuery(query)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException ignored) {
        }
        ENTITY_MANAGER.getTransaction().commit();
        return user;
    }

    @Override
    public boolean updateUser(User user) {
        ENTITY_MANAGER.getTransaction().begin();
        ENTITY_MANAGER.merge(user);
        ENTITY_MANAGER.getTransaction().commit();
        return true;
    }
}
