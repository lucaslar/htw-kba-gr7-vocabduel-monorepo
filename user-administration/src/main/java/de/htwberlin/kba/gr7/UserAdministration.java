package de.htwberlin.kba.gr7;

import de.htwberlin.kba.gr7.exceptions.AlreadyRegisteredException;
import de.htwberlin.kba.gr7.exceptions.NoSuchUserException;
import de.htwberlin.kba.gr7.exceptions.PasswordsDoNotMatchException;
import de.htwberlin.kba.gr7.exceptions.UnauthorizedException;
import de.htwberlin.kba.gr7.model.User;

import java.sql.SQLException;
import java.util.List;

public interface UserAdministration {
    List<User> findUsersByString(String searchValue) throws UnauthorizedException, SQLException;

    User getUserData(int id) throws UnauthorizedException, NoSuchUserException, SQLException;

    User getUserData(String email) throws UnauthorizedException, NoSuchUserException, SQLException;

    int updateUser(User user) throws UnauthorizedException, AlreadyRegisteredException, SQLException;

    int updateUserPassword(User user, String password, String confirmPassword) throws PasswordsDoNotMatchException, UnauthorizedException, AlreadyRegisteredException, SQLException;

    int deleteUser(User user) throws UnauthorizedException, SQLException;
}
