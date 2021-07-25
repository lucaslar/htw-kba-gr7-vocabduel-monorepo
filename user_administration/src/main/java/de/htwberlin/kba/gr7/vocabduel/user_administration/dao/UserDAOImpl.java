package de.htwberlin.kba.gr7.vocabduel.user_administration.dao;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.UserOptimisticLockException;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public void setEntityManager(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public List<User> selectUsersByUsername(String searchString) throws UserOptimisticLockException {
        List<User> users = null;
        try {
            final String query = "select u from User u where lower(u.username) like :searchString";
            users = (List<User>) entityManager
                    .createQuery(query)
                    .setParameter("searchString", searchString.toLowerCase() + "%")
                    .getResultList();
        } catch (NoResultException ignored) {
            // ignored => return null (users) if no users could be found
        } catch (OptimisticLockException e) {
            throw new UserOptimisticLockException(e);
        }
        return users;
    }

    @Override
    public User selectUserByEmail(String email) throws UserOptimisticLockException {
        User user = null;
        try {
            final String query = "from User as u where u.email like :email";
            user = (User) entityManager
                    .createQuery(query)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException ignored) {
            // ignored => return null (user) if no user could be found
        } catch (OptimisticLockException e) {
            throw new UserOptimisticLockException(e);
        }
        return user;
    }

    @Override
    public User selectUserById(Long userId) throws UserOptimisticLockException {
        try {
            return entityManager.find(User.class, userId);
        } catch (OptimisticLockException e) {
            throw new UserOptimisticLockException(e);
        }
    }

    @Override
    public User selectUserByUsername(String username) throws UserOptimisticLockException {
        User user = null;
        try {
            final String query = "from User as u where u.username like :username";
            user = (User) entityManager
                    .createQuery(query)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException ignored) {
            // ignored => return null (user) if no user could be found
        } catch (OptimisticLockException e) {
            throw new UserOptimisticLockException(e);
        }
        return user;
    }

    @Override
    public boolean updateUser(User user) throws UserOptimisticLockException {
        try {
            entityManager.merge(user);
            return true;
        } catch (OptimisticLockException e) {
            throw new UserOptimisticLockException(e);
        }
    }
}
