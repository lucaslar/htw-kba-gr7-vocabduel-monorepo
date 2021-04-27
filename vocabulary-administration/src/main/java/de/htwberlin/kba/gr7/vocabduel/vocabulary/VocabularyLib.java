package de.htwberlin.kba.gr7.vocabduel.vocabulary;

import de.htwberlin.kba.gr7.vocabduel.user.exceptions.UnauthorizedException;
import de.htwberlin.kba.gr7.vocabduel.user.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabulary.model.SupportedLanguage;
import de.htwberlin.kba.gr7.vocabduel.vocabulary.model.VocableSet;

import java.sql.SQLException;
import java.util.List;

public interface VocabularyLib {
    VocableSet getVocableSet(int id) throws UnauthorizedException, SQLException;

    List<VocableSet> getVocableSetsOfUser(User user) throws UnauthorizedException, SQLException;

    List<VocableSet> findVocableSetsByString(String searchString) throws UnauthorizedException, SQLException;

    List<VocableSet> findVocableSetsByTitle(String titleSearchString) throws UnauthorizedException, SQLException;

    List<VocableSet> findVocableSetsBySourceTitle(String sourceTitleSearchString) throws UnauthorizedException, SQLException;

    List<VocableSet> findVocableSetsWithKnownLanguage(SupportedLanguage language) throws UnauthorizedException, SQLException;

    List<VocableSet> findVocableSetsWithKnownLanguageByString(SupportedLanguage language, String searchString) throws UnauthorizedException, SQLException;

    List<VocableSet> findVocableSetsWithKnownLanguageByTitle(SupportedLanguage language, String titleSearchString) throws UnauthorizedException, SQLException;

    List<VocableSet> findVocableSetsWithKnownLanguageBySourceTitle(SupportedLanguage language, String sourceTitleSearchString) throws UnauthorizedException, SQLException;

    List<VocableSet> findVocableSetsWithLearntLanguage(SupportedLanguage language) throws UnauthorizedException, SQLException;

    List<VocableSet> findVocableSetsWithLearntLanguageByString(SupportedLanguage language, String searchString) throws UnauthorizedException, SQLException;

    List<VocableSet> findVocableSetsWithLearntLanguageByTitle(SupportedLanguage language, String titleSearchString) throws UnauthorizedException, SQLException;

    List<VocableSet> findVocableSetsWithLearntLanguageBySourceTitle(SupportedLanguage language, String sourceTitleSearchString) throws UnauthorizedException, SQLException;

    List<VocableSet> findVocableSetsWithLanguages(SupportedLanguage known, SupportedLanguage learnt) throws UnauthorizedException, SQLException;

    List<VocableSet> findVocableSetsWithLanguagesByString(SupportedLanguage known, SupportedLanguage learnt, String searchString) throws UnauthorizedException, SQLException;

    List<VocableSet> findVocableSetsWithLanguagesByTitle(SupportedLanguage known, SupportedLanguage learnt, String titleSearchString) throws UnauthorizedException, SQLException;

    List<VocableSet> findVocableSetsWithLanguagesBySourceTitle(SupportedLanguage known, SupportedLanguage learnt, String sourceTitleSearchString) throws UnauthorizedException, SQLException;
}
