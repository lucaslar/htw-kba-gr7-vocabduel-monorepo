package de.htwberlin.kba.gr7.vocabduel.user_administration.dao;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.UserOptimisticLockException;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.user_administration.model.StoredRefreshToken;

public interface StoredRefreshTokenDAO {

    StoredRefreshToken selectStoredRefreshTokenByUserAndToken(User user, String refreshToken) throws UserOptimisticLockException;

    void insertStoredRefreshTokenByUserAndToken(User user, String refreshToken) throws UserOptimisticLockException;

    void removeUserTokensIfFiveOrMorePresent(User user) throws UserOptimisticLockException;

    boolean deleteStoredRefreshTokenByUserId(Long userId) throws UserOptimisticLockException;

    boolean deleteStoredRefreshToken(StoredRefreshToken storedRefreshToken) throws UserOptimisticLockException;

}
