package de.htwberlin.kba.gr7.vocabduel.user;

import de.htwberlin.kba.gr7.vocabduel.user.exceptions.AlreadyRegisteredException;
import de.htwberlin.kba.gr7.vocabduel.user.exceptions.NoSuchUserException;
import de.htwberlin.kba.gr7.vocabduel.user.exceptions.PasswordsDoNotMatchException;
import de.htwberlin.kba.gr7.vocabduel.user.exceptions.UnauthorizedException;
import de.htwberlin.kba.gr7.vocabduel.user.model.User;

import java.sql.SQLException;
import java.util.List;

public interface UserAdministration {
    List<User> findUsersByString(String searchValue) throws UnauthorizedException, SQLException;

    List<User> findUsersByEmail(String searchValue) throws UnauthorizedException, SQLException;

    List<User> findUsersByFirstName(String searchValue) throws UnauthorizedException, SQLException;

    List<User> findUsersByLastName(String searchValue) throws UnauthorizedException, SQLException;

    User getUserData(int id) throws UnauthorizedException, NoSuchUserException, SQLException;

    User getUserData(String email) throws UnauthorizedException, NoSuchUserException, SQLException;

    int updateUser(User user) throws UnauthorizedException, AlreadyRegisteredException, SQLException;

    int updateUserPassword(User user, String password, String confirmPassword) throws PasswordsDoNotMatchException, UnauthorizedException, AlreadyRegisteredException, SQLException;

    int deleteUser(User user) throws UnauthorizedException, SQLException;
}
