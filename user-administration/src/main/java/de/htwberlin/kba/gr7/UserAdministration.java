package de.htwberlin.kba.gr7;

import de.htwberlin.kba.gr7.exceptions.AlreadyRegisteredException;
import de.htwberlin.kba.gr7.exceptions.NoSuchUserException;
import de.htwberlin.kba.gr7.model.User;

import java.sql.SQLException;

public interface UserAdministration {
    int insertUser(User user) throws AlreadyRegisteredException, SQLException;

    User getUserData(int id) throws NoSuchUserException, SQLException;

    int updateUser(User user) throws AlreadyRegisteredException, SQLException;

    int deleteUser(User user) throws SQLException;

    int deleteUser(int id) throws SQLException;
}
