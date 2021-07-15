package de.htwberlin.kba.gr7.vocabduel.user_administration.dao;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.user_administration.model.LoginData;

public interface LoginDataDAO {

    void insertLoginData(LoginData loginData);

    LoginData selectLoginDataByUserEmail(String email);

    LoginData selectLoginDataByUser(User user);

    /**
     * delete LoginData cascades to User.
     * so no delete user method in UserDAO is needed.
     * @param userId Long
     * @return boolean true if everything gone right
     *                 false if something gone wrong
     */
    boolean deleteLoginDataByUserId(Long userId);

}
