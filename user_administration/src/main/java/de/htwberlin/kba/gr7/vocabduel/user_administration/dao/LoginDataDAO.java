package de.htwberlin.kba.gr7.vocabduel.user_administration.dao;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.UserOptimisticLockException;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.user_administration.model.LoginData;

public interface LoginDataDAO {

    void insertLoginData(LoginData loginData) throws UserOptimisticLockException;

    LoginData selectLoginDataByUserEmail(String email) throws UserOptimisticLockException;

    LoginData selectLoginDataByUser(User user) throws UserOptimisticLockException;

    /**
     * delete LoginData cascades to User.
     * so no delete user method in UserDAO is needed.
     * @param userId Long
     * @return boolean true if everything gone right
     *                 false if something gone wrong
     */
    boolean deleteLoginDataByUserId(Long userId) throws UserOptimisticLockException;

}
