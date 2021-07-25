package de.htwberlin.kba.gr7.vocabduel.user_administration.dao;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.UserOptimisticLockException;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;

import java.util.List;

public interface UserDAO {

    List<User> selectUsersByUsername(String username) throws UserOptimisticLockException;

    User selectUserByEmail(String email) throws UserOptimisticLockException;

    User selectUserById(Long userId) throws UserOptimisticLockException;

    User selectUserByUsername(String username) throws UserOptimisticLockException;

    boolean updateUser(User user) throws UserOptimisticLockException;

}
