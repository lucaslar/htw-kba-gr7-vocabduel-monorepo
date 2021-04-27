package de.htwberlin.kba.gr7.vocabduel.vocabulary;

import de.htwberlin.kba.gr7.vocabduel.user.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabulary.model.LanguageSet;
import de.htwberlin.kba.gr7.vocabduel.vocabulary.model.VocableList;

import java.sql.SQLException;
import java.util.List;

public interface VocabularyLib {
    VocableList getVocableListById(Long id) throws SQLException;

    List<VocableList> getVocableListsOfUser(User user) throws SQLException;

    List<LanguageSet> getAllLanguageSets() throws SQLException;
}
