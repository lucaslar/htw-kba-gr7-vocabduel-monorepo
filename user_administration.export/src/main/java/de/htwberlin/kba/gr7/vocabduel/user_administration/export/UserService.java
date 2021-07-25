package de.htwberlin.kba.gr7.vocabduel.user_administration.export;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.*;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;

import javax.naming.InvalidNameException;
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
     * @throws UserOptimisticLockException An OptimisticLock error in the user module occurred.
     */
    List<User> findUsersByUsername(String searchString) throws UserOptimisticLockException;

    /**
     * @param id ID the respective user is to be returned for.
     * @return User with the given ID or null if no user found.
     * @throws UserOptimisticLockException An OptimisticLock error in the user module occurred.
     */
    User getUserDataById(Long id) throws UserOptimisticLockException;

    /**
     * @param email Email the respective user is to be returned for.
     * @return User with the given email or null if no user found.
     * @throws UserOptimisticLockException An OptimisticLock error in the user module occurred.
     */
    User getUserDataByEmail(String email) throws UserOptimisticLockException;

    /**
     * @param username Username the respective user is to be returned for.
     * @return User with the given username or null if no user found.
     * @throws UserOptimisticLockException An OptimisticLock error in the user module occurred.
     */
    User getUserDataByUsername(String username) throws UserOptimisticLockException;

    /**
     * Updates user data by a given user object.
     *
     * @param user User object containing the (updated) data to be saved.
     * @return int database status of the update query.
     * @throws InvalidOrRegisteredMailException   The user's specified email is either already in use or invalid and, thus, cannot be used again.
     * @throws AlreadyRegisteredUsernameException The user's specified username is already in use and, thus, cannot be used again.
     * @throws IncompleteUserDataException        The passed user object does not contain all required user data.
     * @throws InvalidUserException               The given user is null or does not exist.
     * @throws UserOptimisticLockException        An OptimisticLock error in the user module occurred.
     */
    int updateUser(User user) throws InvalidOrRegisteredMailException, AlreadyRegisteredUsernameException, IncompleteUserDataException, InvalidUserException, InvalidNameException, UserOptimisticLockException;

    /**
     * Deletes a single user.
     *
     * @param user User to be deleted.
     * @return int database status of the delete query.
     * @throws UserOptimisticLockException An OptimisticLock error in the user module occurred.
     */
    int deleteUser(User user)throws UserOptimisticLockException;
}
