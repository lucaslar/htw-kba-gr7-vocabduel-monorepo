package de.htwberlin.kba.gr7;

import de.htwberlin.kba.gr7.exceptions.UnauthorizedException;
import de.htwberlin.kba.gr7.model.SupportedLanguage;
import de.htwberlin.kba.gr7.model.User;
import de.htwberlin.kba.gr7.model.VocableSet;

import java.sql.SQLException;
import java.util.List;

public interface VocabularyLib {
    VocableSet getVocableSet(int id) throws UnauthorizedException, SQLException;

    List<VocableSet> getVocableSetsOfUser(User user) throws UnauthorizedException, SQLException;

    List<VocableSet> findVocableSetsByString(String searchString) throws UnauthorizedException, SQLException;

    List<VocableSet> findVocableSetsWithKnownLanguage(SupportedLanguage language) throws UnauthorizedException, SQLException;

    List<VocableSet> findVocableSetsWithKnownLanguage(SupportedLanguage language, String searchString) throws UnauthorizedException, SQLException;

    List<VocableSet> findVocableSetsWithLearntLanguage(SupportedLanguage language) throws UnauthorizedException, SQLException;

    List<VocableSet> findVocableSetsWithLearntLanguage(SupportedLanguage language, String searchString) throws UnauthorizedException, SQLException;

    List<VocableSet> findVocableSetsWithLanguage(SupportedLanguage known, SupportedLanguage learnt) throws UnauthorizedException, SQLException;

    List<VocableSet> findVocableSetsWithLanguage(SupportedLanguage known, SupportedLanguage learnt, String searchString) throws UnauthorizedException, SQLException;
}
