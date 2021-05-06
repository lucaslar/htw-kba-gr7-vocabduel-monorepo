package de.htwberlin.kba.gr7.vocabduel.user_administration;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.UserAdministration;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.AlreadyRegisteredMailException;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.AlreadyRegisteredUsernameException;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.NoSuchUserException;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.PasswordsDoNotMatchException;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;

import java.util.List;

public class UserAdministrationImpl implements UserAdministration {
    @Override
    public List<User> findUsersByUsername(String searchString) {
        return null;
    }

    @Override
    public User getUserData(Long id) throws NoSuchUserException {
        return null;
    }

    @Override
    public User getUserData(String email) throws NoSuchUserException {
        return null;
    }

    @Override
    public int updateUser(User user) throws AlreadyRegisteredMailException, AlreadyRegisteredUsernameException {
        return 0;
    }

    @Override
    public int updateUserPassword(User user, String password, String confirmPassword) throws PasswordsDoNotMatchException {
        return 0;
    }

    @Override
    public int deleteUser(User user) {
        return 0;
    }
}
