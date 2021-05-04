package de.htwberlin.kba.gr7.vocabduel.user_administration.export;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.AlreadyRegisteredException;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.NoSuchUserException;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.PasswordsDoNotMatchException;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;

import java.util.List;

public interface UserAdministration {

    /**
     * <p>Get list of <code>User</code> found by any string, that can include space(s).
     * Can be useed for identifying opponents for games.
     * Searching for equals in any field of <code>User</code>.</p>
     *
     * @param searchValue
     * @return List of <code>User</code> found
     */
    List<User> findUsersByString(String searchValue);

    /**
     * <p>Get list of user found by mail address. Can be useed for identifying opponents for games.</p>
     *
     * @param searchValue email address of a user
     * @return List of <code>User</code> found
     */
    List<User> findUsersByEmail(String searchValue);

    /**
     * <p>Get list of user found by first name. Can be useed for identifying opponents for games.</p>
     *
     * @param searchValue first name of a user
     * @return List of <code>User</code> found
     */
    List<User> findUsersByFirstName(String searchValue);

    /**
     * <p>get list of user found by last name. Can be used for identifying opponents for games</p>
     *
     * @param searchValue
     * @return List of <code>User</code> found
     */
    List<User> findUsersByLastName(String searchValue);

    /**
     * <p>get user objekct by user id</p>
     *
     * @param id user id // TODO: wo wollen wir die vorher her haben??
     * @return <code>User</code>
     * @throws NoSuchUserException
     */
    User getUserData(Long id) throws NoSuchUserException;

    /**
     * <p>get user data by email address</p>
     *
     * @param email email adress
     *              // TODO: wozu brauchen wir diesen Aufruf?? login() und register() holen schon das ganze User object
     * @return <code>User</code>
     * @throws NoSuchUserException
     */
    User getUserData(String email) throws NoSuchUserException;

    /**
     * <p>update user data by user object.</p>
     *
     * @param user The <code>User</code> with the actual data, that has to be saved
     * @return int database status of update query
     * @throws AlreadyRegisteredException
     */
    int updateUser(User user) throws AlreadyRegisteredException;

    /**
     * <p>update user password</p>
     *
     * @param user            <code>User</code>, whose password to be changed
     * @param password        New password
     * @param confirmPassword New password confirmed
     * @return int database status of update query
     * @throws PasswordsDoNotMatchException
     * @throws AlreadyRegisteredException
     */
    int updateUserPassword(User user, String password, String confirmPassword) throws PasswordsDoNotMatchException, AlreadyRegisteredException;

    /**
     * <p>delete single user.</p>
     *
     * @param user <code>User</code>
     * @return int database status of delte query
     */
    int deleteUser(User user);
}
