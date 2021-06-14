package de.htwberlin.kba.gr7.vocabduel.user_administration.export;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.*;
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
public interface AuthService {

    /**
     * Registers a new user who can take actions in this application. After having successfully stored the new user data,
     * a <code>{@link LoggedInUser}</code> object is returned which is used for authentication using tokens without
     * revealing the user's password. (see <code>{@link AuthService#loginUser}</code>).
     *
     * @param username        Username to be set.
     * @param email           Email to be set.
     * @param firstname       First name of the new user.
     * @param lastname        Family name of the new user.
     * @param password        Password for the new user, will be stored in a different way than the other user data
     * @param confirmPassword Repeated password for confirmation purposes (must match with <code>password</code>).
     * @return The object of the new user. Password will never be revealed, instead, this object contains auth tokens.
     * @throws PasswordsDoNotMatchException       <code>password</code> and <code>confirmPassword</code> do not match.
     * @throws PwTooWeakException                 The given password is not strong enough.
     * @throws InvalidOrRegisteredMailException   The user's specified email is either already in use or invalid and, thus, cannot be used again.
     * @throws AlreadyRegisteredUsernameException The user's specified username is already in use and, thus, cannot be used again.
     * @throws IncompleteUserDataException        The passed user object does not contain all required user data.
     */
    LoggedInUser registerUser(String username, String email, String firstname, String lastname, String password, String confirmPassword) throws PasswordsDoNotMatchException, PwTooWeakException, InvalidOrRegisteredMailException, AlreadyRegisteredUsernameException, IncompleteUserDataException;

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
    AuthTokens refreshAuthTokens(String refreshToken);

    /**
     * Checks whether a given auth token is valid, i.e. if the user has access rights or not.
     *
     * @param token Auth token to be checked.
     * @return <code>true</code> if the token is valid, <code>false</code> if not/<code>null</code>.
     */
    boolean hasAccessRights(String token);

    /**
     * Updates a user's password if the current password is correct and the new password is both valid and matching the
     * confirm password.
     *
     * @param user            User whose password is to be changed.
     * @param currentPassword The user's current password for authentication purposes.
     * @param newPassword     The user's new password - will be stored in a different way than the other user data.
     * @param confirmPassword Repeated password for confirmation purposes (must match with <code>password</code>).
     * @return int database status of the update query.
     * @throws InvalidFirstPwdException     <code>currentPassword</code> is wrong.
     * @throws PasswordsDoNotMatchException <code>newPassword</code> and <code>confirmPassword</code> do not match.
     * @throws PwTooWeakException           The given password is not strong enough.
     */
    int updateUserPassword(User user, String currentPassword, String newPassword, String confirmPassword) throws InvalidFirstPwdException, PasswordsDoNotMatchException, PwTooWeakException;
}
