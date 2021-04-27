package de.htwberlin.kba.gr7.vocabduel.user;

import de.htwberlin.kba.gr7.vocabduel.user.exceptions.AlreadyRegisteredException;
import de.htwberlin.kba.gr7.vocabduel.user.exceptions.PasswordsDoNotMatchException;
import de.htwberlin.kba.gr7.vocabduel.user.exceptions.UnauthorizedException;
import de.htwberlin.kba.gr7.vocabduel.user.model.AuthTokens;
import de.htwberlin.kba.gr7.vocabduel.user.model.LoggedInUser;
import de.htwberlin.kba.gr7.vocabduel.user.model.User;

import java.sql.SQLException;

public interface Auth {
    LoggedInUser registerUser(User user, String password, String confirmPassword) throws PasswordsDoNotMatchException, AlreadyRegisteredException, SQLException;

    LoggedInUser loginUser(String email, String password) throws UnauthorizedException, SQLException;

    User fetchUser(String token) throws UnauthorizedException, SQLException;

    AuthTokens refreshAuthToken(String refreshToken) throws UnauthorizedException, SQLException;

    boolean hasAccessRights(String token) throws UnauthorizedException, SQLException;
}
