package de.htwberlin.kba.gr7.vocabduel.user_administration.dao;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.InternalUserModuleException;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;

import java.util.List;

public interface UserDAO {

    List<User> selectUsersByUsername(String username) throws InternalUserModuleException;

    User selectUserByEmail(String email) throws InternalUserModuleException;

    User selectUserById(Long userId) throws InternalUserModuleException;

    User selectUserByUsername(String username) throws InternalUserModuleException;

    boolean updateUser(User user) throws InternalUserModuleException;

}
