package de.htwberlin.kba.gr7.vocabduel.user_administration;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.UserAdministration;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.*;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;

import java.util.List;

public class UserAdministrationImpl implements UserAdministration {

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
        return 0;
    }

    @Override
    public int updateUserPassword(User user, String password, String confirmPassword) throws PasswordsDoNotMatchException, PwTooWeakException {
        return 0;
    }

    @Override
    public int deleteUser(User user) {
        return 0;
    }
}
