package de.htwberlin.kba.gr7.vocabduel.user_administration.export;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.*;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;

import java.util.List;

/**
 * Service for managing users and user data beyond authentication purposes (for auth, incl. registration and login,
 * please check {@link AuthService}).
 *
 * @author Sebastian Kehl, Lucas Larisch
 * @version 1.0, May 2021
 */
public interface UserService {

    /**
     * Searches for users whose <code>username</code> properties match/include the given search string and returns them
     * as list. Useful for identifying opponents for games. The search is case insensitive, i.e. a user "UserX" will be
     * found by entering "userx".
     *
     * @param searchString String users are to be searched by (compared with their usernames).
     * @return List of users matching/including the given search string.
     */
    List<User> findUsersByUsername(String searchString);

    /**
     * @param id ID the respective user is to be returned for.
     * @return User with the given ID or null if no user found.
     */
    User getUserDataById(Long id);

    /**
     * @param email Email the respective user is to be returned for.
     * @return User with the given email or null if no user found.
     */
    User getUserDataByEmail(String email);

    /**
     * @param username Username the respective user is to be returned for.
     * @return User with the given username or null if no user found.
     */
    User getUserDataByUsername(String username);

    /**
     * Updates user data by a given user object.
     *
     * @param user User object containing the (updated) data to be saved.
     * @return int database status of the update query.
     * @throws InvalidOrRegisteredMailException   The user's specified email is either already in use or invalid and, thus, cannot be used again.
     * @throws AlreadyRegisteredUsernameException The user's specified username is already in use and, thus, cannot be used again.
     * @throws IncompleteUserDataException        The passed user object does not contain all required user data.
     */
    int updateUser(User user) throws InvalidOrRegisteredMailException, AlreadyRegisteredUsernameException, IncompleteUserDataException;

    /**
     * Updates a user's password if the current password is correct and the new password is both valid and matching the
     * confirm password.
     *
     * @param user            User whose password is to be changed.
     * @param currentPassword      The user's current password for authentication purposes.
     * @param newPassword     The user's new password - will be stored in a different way than the other user data.
     * @param confirmPassword Repeated password for confirmation purposes (must match with <code>password</code>).
     * @return int database status of the update query.
     * @throws InvalidFirstPwdException     <code>currentPassword</code> is wrong.
     * @throws PasswordsDoNotMatchException <code>newPassword</code> and <code>confirmPassword</code> do not match.
     * @throws PwTooWeakException           The given password is not strong enough.
     */
    int updateUserPassword(User user, String currentPassword, String newPassword, String confirmPassword) throws InvalidFirstPwdException, PasswordsDoNotMatchException, PwTooWeakException;

    /**
     * Deletes a single user.
     *
     * @param user User to be deleted.
     * @return int database status of the delete query.
     */
    int deleteUser(User user);
}