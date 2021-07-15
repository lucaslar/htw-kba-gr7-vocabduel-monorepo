package de.htwberlin.kba.gr7.vocabduel.user_administration.dao;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;

import java.util.List;

public interface UserDAO {

    List<User> selectUsersByUsername(String username);

    User selectUserByEmail(String email);

    User selectUserById(Long userId);

    User selectUserByUsername(String username);

    boolean updateUser(User user);

}
