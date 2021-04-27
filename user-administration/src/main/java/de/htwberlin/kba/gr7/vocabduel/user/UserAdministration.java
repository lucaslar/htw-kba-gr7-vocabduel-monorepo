package de.htwberlin.kba.gr7.vocabduel.user;

import de.htwberlin.kba.gr7.vocabduel.user.exceptions.AlreadyRegisteredException;
import de.htwberlin.kba.gr7.vocabduel.user.exceptions.NoSuchUserException;
import de.htwberlin.kba.gr7.vocabduel.user.exceptions.PasswordsDoNotMatchException;
import de.htwberlin.kba.gr7.vocabduel.user.model.User;

import java.sql.SQLException;
import java.util.List;

public interface UserAdministration {
    User register(User user, String password, String confirmPassword) throws PasswordsDoNotMatchException, AlreadyRegisteredException, SQLException;

    User login(String email, String password) throws SQLException;

    List<User> findUsersByString(String searchValue) throws SQLException;

    List<User> findUsersByEmail(String searchValue) throws SQLException;

    List<User> findUsersByFirstName(String searchValue) throws SQLException;

    List<User> findUsersByLastName(String searchValue) throws SQLException;

    User getUserData(int id) throws NoSuchUserException, SQLException;

    User getUserData(String email) throws NoSuchUserException, SQLException;

    int updateUser(User user) throws AlreadyRegisteredException, SQLException;

    int updateUserPassword(User user, String password, String confirmPassword) throws PasswordsDoNotMatchException, AlreadyRegisteredException, SQLException;

    int deleteUser(User user) throws SQLException;
}
