package de.htwberlin.kba.gr7;

import de.htwberlin.kba.gr7.exceptions.AlreadyRegisteredException;
import de.htwberlin.kba.gr7.exceptions.PasswordsDoNotMatchException;
import de.htwberlin.kba.gr7.exceptions.UnauthorizedException;
import de.htwberlin.kba.gr7.model.AuthTokens;
import de.htwberlin.kba.gr7.model.LoggedInUser;
import de.htwberlin.kba.gr7.model.User;

import java.sql.SQLException;

public interface Auth {
    LoggedInUser registerUser(User user, String password, String confirmPassword) throws PasswordsDoNotMatchException, AlreadyRegisteredException, SQLException;

    LoggedInUser loginUser(String email, String password) throws UnauthorizedException, SQLException;

    User fetchUser(String token) throws UnauthorizedException, SQLException;

    AuthTokens refreshAuthToken(String refreshToken) throws UnauthorizedException, SQLException;

    boolean hasAccessRights(String token) throws UnauthorizedException, SQLException;
}
