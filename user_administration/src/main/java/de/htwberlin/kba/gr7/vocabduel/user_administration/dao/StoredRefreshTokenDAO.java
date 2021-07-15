package de.htwberlin.kba.gr7.vocabduel.user_administration.dao;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.user_administration.model.StoredRefreshToken;

public interface StoredRefreshTokenDAO {

     StoredRefreshToken selectStoredRefreshTokenByUserAndToken(User user, String refreshToken);

     void insertStoredRefreshTokenByUserAndToken(User user, String refreshToken);

     boolean deleteStoredRefreshTokenByUserId(Long userId);

     boolean deleteStoredRefreshToken(StoredRefreshToken storedRefreshToken);

}
