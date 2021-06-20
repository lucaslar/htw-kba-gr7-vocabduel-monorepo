package de.htwberlin.kba.gr7.vocabduel.user_administration;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.UserService;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.*;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.user_administration.model.LoginData;
import de.htwberlin.kba.gr7.vocabduel.user_administration.model.StoredRefreshToken;
import de.htwberlin.kba.gr7.vocabduel.user_administration.model.Validation;
import org.springframework.stereotype.Service;

import javax.naming.InvalidNameException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final EntityManager ENTITY_MANAGER;

    public UserServiceImpl() {
        ENTITY_MANAGER = EntityFactoryManagement.getManager();
    }

    @Override
    public List<User> findUsersByUsername(final String searchString) {
        List<User> users = null;
        if (searchString != null) {
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
        }
        return users;
    }

    @Override
    public User getUserDataById(Long id) {
        return ENTITY_MANAGER.find(User.class, id);
    }

    @Override
    public User getUserDataByEmail(String email) {
        User user = null;
        if (email != null) {
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
        }
        return user;
    }

    @Override
    public User getUserDataByUsername(String username) {
        User user = null;
        if (username != null) {
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
        }
        return user;
    }

    @Override
    public int updateUser(final User user) throws InvalidUserException, InvalidOrRegisteredMailException, AlreadyRegisteredUsernameException, IncompleteUserDataException, InvalidNameException {
        if (user == null || getUserDataById(user.getId()) == null) {
            throw new InvalidUserException("Invalid/not found user");
        }

        Validation.completeDataValidation(user);
        Validation.nameValidation(user.getFirstName());
        Validation.nameValidation(user.getLastName());
        Validation.uniqueUserDataValidation(user.getUsername(), user.getEmail(), this, user.getId());

        ENTITY_MANAGER.getTransaction().begin();
        ENTITY_MANAGER.merge(user);
        ENTITY_MANAGER.getTransaction().commit();

        return 0;
    }

    @Override
    public int deleteUser(final User user) {
        ENTITY_MANAGER.getTransaction().begin();

        try {
            final List<LoginData> loginData = ENTITY_MANAGER
                    .createQuery("select l from LoginData l where user_id = :user")
                    .setParameter("user", user.getId())
                    .getResultList();
            if (loginData != null && !loginData.isEmpty()) loginData.forEach(ENTITY_MANAGER::remove);
        } catch (NoResultException ignored) {
        }

        try {
            final List<StoredRefreshToken> tokens = ENTITY_MANAGER
                    .createQuery("select s from StoredRefreshToken s where user_id = :user")
                    .setParameter("user", user.getId())
                    .getResultList();
            if (tokens != null && !tokens.isEmpty()) tokens.forEach(ENTITY_MANAGER::remove);
        } catch (NoResultException ignored) {
        }

        ENTITY_MANAGER.getTransaction().commit();
        return 0;
    }
}
