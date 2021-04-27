package de.htwberlin.kba.gr7.vocabduel.vocabulary;

import de.htwberlin.kba.gr7.vocabduel.user.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabulary.model.SupportedLanguage;
import de.htwberlin.kba.gr7.vocabduel.vocabulary.model.VocableSet;

import java.sql.SQLException;
import java.util.List;

public interface VocabularyLib {
    VocableSet getVocableSet(int id) throws SQLException;

    List<VocableSet> getVocableSetsOfUser(User user) throws SQLException;

    List<VocableSet> findVocableSetsByString(String searchString) throws SQLException;

    List<VocableSet> findVocableSetsByTitle(String titleSearchString) throws SQLException;

    List<VocableSet> findVocableSetsBySourceTitle(String sourceTitleSearchString) throws SQLException;

    List<VocableSet> findVocableSetsWithKnownLanguage(SupportedLanguage language) throws SQLException;

    List<VocableSet> findVocableSetsWithKnownLanguageByString(SupportedLanguage language, String searchString) throws SQLException;

    List<VocableSet> findVocableSetsWithKnownLanguageByTitle(SupportedLanguage language, String titleSearchString) throws SQLException;

    List<VocableSet> findVocableSetsWithKnownLanguageBySourceTitle(SupportedLanguage language, String sourceTitleSearchString) throws SQLException;

    List<VocableSet> findVocableSetsWithLearntLanguage(SupportedLanguage language) throws SQLException;

    List<VocableSet> findVocableSetsWithLearntLanguageByString(SupportedLanguage language, String searchString) throws SQLException;

    List<VocableSet> findVocableSetsWithLearntLanguageByTitle(SupportedLanguage language, String titleSearchString) throws SQLException;

    List<VocableSet> findVocableSetsWithLearntLanguageBySourceTitle(SupportedLanguage language, String sourceTitleSearchString) throws SQLException;

    List<VocableSet> findVocableSetsWithLanguages(SupportedLanguage known, SupportedLanguage learnt) throws SQLException;

    List<VocableSet> findVocableSetsWithLanguagesByString(SupportedLanguage known, SupportedLanguage learnt, String searchString) throws SQLException;

    List<VocableSet> findVocableSetsWithLanguagesByTitle(SupportedLanguage known, SupportedLanguage learnt, String titleSearchString) throws SQLException;

    List<VocableSet> findVocableSetsWithLanguagesBySourceTitle(SupportedLanguage known, SupportedLanguage learnt, String sourceTitleSearchString) throws SQLException;
}
