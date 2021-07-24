package de.htwberlin.kba.gr7.vocabduel.user_administration.dao;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.InternalUserModuleException;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.user_administration.model.StoredRefreshToken;

public interface StoredRefreshTokenDAO {

    StoredRefreshToken selectStoredRefreshTokenByUserAndToken(User user, String refreshToken) throws InternalUserModuleException;

    void insertStoredRefreshTokenByUserAndToken(User user, String refreshToken) throws InternalUserModuleException;

    void removeUserTokensIfFiveOrMorePresent(User user) throws InternalUserModuleException;

    boolean deleteStoredRefreshTokenByUserId(Long userId) throws InternalUserModuleException;

    boolean deleteStoredRefreshToken(StoredRefreshToken storedRefreshToken) throws InternalUserModuleException;

}
