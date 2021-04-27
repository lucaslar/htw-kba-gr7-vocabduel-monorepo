package de.htwberlin.kba.gr7.vocabduel.user;

import de.htwberlin.kba.gr7.vocabduel.user.exceptions.AlreadyRegisteredException;
import de.htwberlin.kba.gr7.vocabduel.user.exceptions.NoSuchUserException;
import de.htwberlin.kba.gr7.vocabduel.user.exceptions.PasswordsDoNotMatchException;
import de.htwberlin.kba.gr7.vocabduel.user.model.User;

import java.util.List;

public interface UserAdministration {
    User register(User user, String password, String confirmPassword) throws PasswordsDoNotMatchException, AlreadyRegisteredException;

    User login(String email, String password);

    List<User> findUsersByString(String searchValue);

    List<User> findUsersByEmail(String searchValue);

    List<User> findUsersByFirstName(String searchValue);

    List<User> findUsersByLastName(String searchValue);

    User getUserData(Long id) throws NoSuchUserException;

    User getUserData(String email) throws NoSuchUserException;

    int updateUser(User user) throws AlreadyRegisteredException;

    int updateUserPassword(User user, String password, String confirmPassword) throws PasswordsDoNotMatchException, AlreadyRegisteredException;

    int deleteUser(User user);
}
