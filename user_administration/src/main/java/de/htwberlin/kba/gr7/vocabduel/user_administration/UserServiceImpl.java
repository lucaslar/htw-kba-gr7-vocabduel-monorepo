package de.htwberlin.kba.gr7.vocabduel.user_administration;

import de.htwberlin.kba.gr7.vocabduel.user_administration.dao.LoginDataDAOImpl;
import de.htwberlin.kba.gr7.vocabduel.user_administration.dao.StoredRefreshTokenDAOImpl;
import de.htwberlin.kba.gr7.vocabduel.user_administration.dao.UserDAOImpl;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.UserService;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.*;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.user_administration.model.Validation;
import org.springframework.stereotype.Service;

import javax.naming.InvalidNameException;
import javax.persistence.EntityManager;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserDAOImpl userDAO;
    private final StoredRefreshTokenDAOImpl storedRefreshTokenDAO;
    private final LoginDataDAOImpl loginDataDAO;

    public UserServiceImpl(final EntityManager entityManager) {
        userDAO = new UserDAOImpl(entityManager);
        storedRefreshTokenDAO = new StoredRefreshTokenDAOImpl(entityManager);
        loginDataDAO = new LoginDataDAOImpl(entityManager);
    }

    @Override
    public List<User> findUsersByUsername(final String searchString) {
        List<User> users = null;
        if (searchString != null) {
            users = userDAO.selectUsersByUsername(searchString);
        }
        return users;
    }

    @Override
    public User getUserDataById(Long id) {
        return userDAO.selectUserById(id);
    }

    @Override
    public User getUserDataByEmail(String email) {
        User user = null;
        if (email != null) {
            user = userDAO.selectUserByEmail(email);
        }
        return user;
    }

    @Override
    public User getUserDataByUsername(String username) {
        User user = null;
        if (username != null) {
            user = userDAO.selectUserByUsername(username);
        }
        return user;
    }

    @Override
    public int updateUser(final User user) throws InvalidUserException, InvalidOrRegisteredMailException, AlreadyRegisteredUsernameException, IncompleteUserDataException, InvalidNameException {
        if (user == null) throw new InvalidUserException("Invalid/not found user");
        final User foundUser = getUserDataById(user.getId());
        if (foundUser == null) throw new InvalidUserException("User could not be found");

        Validation.completeDataValidation(user);
        Validation.nameValidation(user.getFirstName());
        Validation.nameValidation(user.getLastName());
        Validation.uniqueUserDataValidation(user.getUsername(), user.getEmail(), this, user.getId());

        foundUser.setUsername(user.getUsername());
        foundUser.setEmail(user.getEmail());
        foundUser.setFirstName(user.getFirstName());
        foundUser.setLastName(user.getLastName());
        userDAO.updateUser(foundUser);

        return 0;
    }

    @Override
    public int deleteUser(final User user) {
        storedRefreshTokenDAO.deleteStoredRefreshTokenByUserId(user.getId());

        loginDataDAO.deleteLoginDataByUserId(user.getId());

        return 0;
    }
}
