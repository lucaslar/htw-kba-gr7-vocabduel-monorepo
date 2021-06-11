package de.htwberlin.kba.gr7.vocabduel.user_administration;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.UserService;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.*;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.user_administration.model.Validation;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.LinkedList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final EntityManager ENTITY_MANAGER;

    // TODO: In the future, use db instead of list and adjust tests
    private final List<User> users = new LinkedList<User>();

    public UserServiceImpl() {
        ENTITY_MANAGER = EntityFactoryManagement.getEntityFactory().createEntityManager();
    }

    @Override
    public List<User> findUsersByUsername(String searchString) {
        return null;
    }

    @Override
    public User getUserDataById(Long id) {
        return null;
    }

    @Override
    public User getUserDataByEmail(String email) {
        return null;
    }

    @Override
    public User getUserDataByUsername(String username) {
        return null;
    }

    @Override
    public int updateUser(User user) throws InvalidOrRegisteredMailException, AlreadyRegisteredUsernameException, IncompleteUserDataException {
        Validation.completeDataValidation(user);
        Validation.uniqueUserDataValidation(user.getUsername(), user.getEmail(), this, user.getId());
        // TODO Update in DB
        return 0;
    }

    @Override
    public int updateUserPassword(User user, String currentPassword, String password, String confirmPassword) throws InvalidFirstPwdException, PasswordsDoNotMatchException, PwTooWeakException {
        // TODO check current pwd
        Validation.passwordValidation(password, confirmPassword);
        // TODO Implement
        return 0;
    }

    @Override
    public int deleteUser(User user) {
        return 0;
    }
}
