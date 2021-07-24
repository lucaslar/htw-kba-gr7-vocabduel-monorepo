package de.htwberlin.kba.gr7.vocabduel.user_administration;

import de.htwberlin.kba.gr7.vocabduel.user_administration.dao.LoginDataDAO;
import de.htwberlin.kba.gr7.vocabduel.user_administration.dao.StoredRefreshTokenDAO;
import de.htwberlin.kba.gr7.vocabduel.user_administration.dao.UserDAO;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.UserService;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.*;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.user_administration.model.Validation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.InvalidNameException;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserDAO USER_DAO;
    private final StoredRefreshTokenDAO STORED_REFRESH_TOKEN_DAO;
    private final LoginDataDAO LOGIN_DATA_DAO;

    public UserServiceImpl(final UserDAO userDao, final StoredRefreshTokenDAO storedRefreshTokenDao, final LoginDataDAO loginDataDao) {
        USER_DAO = userDao;
        STORED_REFRESH_TOKEN_DAO = storedRefreshTokenDao;
        LOGIN_DATA_DAO = loginDataDao;
    }

    @Override
    public List<User> findUsersByUsername(final String searchString) {
        List<User> users = null;
        if (searchString != null) {
            users = USER_DAO.selectUsersByUsername(searchString);
        }
        return users;
    }

    @Override
    public User getUserDataById(Long id) {
        return USER_DAO.selectUserById(id);
    }

    @Override
    public User getUserDataByEmail(String email) {
        User user = null;
        if (email != null) {
            user = USER_DAO.selectUserByEmail(email);
        }
        return user;
    }

    @Override
    public User getUserDataByUsername(String username) {
        User user = null;
        if (username != null) {
            user = USER_DAO.selectUserByUsername(username);
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
        USER_DAO.updateUser(foundUser);

        return 0;
    }

    @Override
    public int deleteUser(final User user) {
        STORED_REFRESH_TOKEN_DAO.deleteStoredRefreshTokenByUserId(user.getId());

        LOGIN_DATA_DAO.deleteLoginDataByUserId(user.getId());

        return 0;
    }
}
