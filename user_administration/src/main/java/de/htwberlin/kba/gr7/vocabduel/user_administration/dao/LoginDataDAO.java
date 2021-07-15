package de.htwberlin.kba.gr7.vocabduel.user_administration.dao;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.user_administration.model.LoginData;

public interface LoginDataDAO {

    void insertLoginData(LoginData loginData);

    LoginData selectLoginDataByUserEmail(String email);

    LoginData selectLoginDataByUser(User user);

    boolean deleteLoginDataByUserId(Long userId);

}
