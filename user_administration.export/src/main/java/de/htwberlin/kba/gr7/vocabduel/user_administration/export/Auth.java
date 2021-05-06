package de.htwberlin.kba.gr7.vocabduel.user_administration.export;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.AlreadyRegisteredMailException;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.AlreadyRegisteredUsernameException;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.PasswordsDoNotMatchException;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.PwTooWeakException;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.AuthTokens;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.LoggedInUser;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;

/**
 * Service for managing authentication, i.e. user logins/registrations, checking access rights using auth tokens and
 * token management.
 *
 * @author Sebastian Kehl, Lucas Larisch
 * @version 1.0, May 2021
 */
public interface Auth {

    /**
     * Registers a new user who can take actions in this application. After having successfully stored the new user data,
     * a <code>{@link LoggedInUser}</code> object is returned which is used for authentication using tokens without
     * revealing the user's password. (see <code>{@link Auth#loginUser}</code>).
     *
     * @param user            Dataset of the new user.
     * @param password        Password for the new user, will be stored in a different way than the other user data
     * @param confirmPassword Repeated password for confirmation purposes (must match with <code>password</code>).
     * @return The object of the new user. Password will never be revealed, instead, this object contains auth tokens.
     * @throws PasswordsDoNotMatchException       <code>password</code> and <code>confirmPassword</code> do not match.
     * @throws PwTooWeakException                 The given password is not strong enough.
     * @throws AlreadyRegisteredMailException     The user's specified email is already in use and, thus, cannot be used again.
     * @throws AlreadyRegisteredUsernameException The user's specified username is already in use and, thus, cannot be used again.
     */
    LoggedInUser registerUser(User user, String password, String confirmPassword) throws PasswordsDoNotMatchException, PwTooWeakException, AlreadyRegisteredMailException, AlreadyRegisteredUsernameException;

    /**
     * Authenticates an existing user in order to take actions in this application. The returned user object contains all
     * relevant user data including auth tokens without revealing the user's password. If the login fails, <code>null</code>
     * is returned instead of an object.
     *
     * @param email    Unique email address the user signs in with.
     * @param password The user's password.
     * @return User object including authentication tokens / <code>null</code> if the login was not successful.
     */
    LoggedInUser loginUser(String email, String password);

    /**
     * Verifies a given auth token, decodes it and returns the user it belongs to. If no or no valid token is given,
     * <code>null</code> is returned.
     *
     * @param token Auth token the user is to be determined by.
     * @return User the given <code>token</code> belongs to or <code>null</code> in case of no/no valid token.
     */
    User fetchUser(String token);

    /**
     * Updates both refresh and auth token and returns the two tokens if the given refresh token is valid. Otherwise,
     * <code>null</code> is returned.
     * Note: After having called this method successfully, the <code>refreshToken</code> passed in as a param is
     * obsolete and instead, the new refresh token (<code>{@link AuthTokens#getRefreshToken()}</code>) is to be used.
     *
     * @param refreshToken Token to be used for refreshing auth/refresh token.
     * @return Object containing refreshed tokens, <code>null</code> if the operation was not successful.
     */
    AuthTokens refreshAuthToken(String refreshToken);

    /**
     * Checks whether a given auth is valid, i.e. if the user has access rights or not.
     *
     * @param token Auth token to be checked.
     * @return <code>true</code> if the token is valid, <code>false</code> if not.
     */
    boolean hasAccessRights(String token);
}
