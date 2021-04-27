package de.htwberlin.kba.gr7.vocabduel.vocabulary;

import de.htwberlin.kba.gr7.vocabduel.user.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabulary.model.LanguageSet;
import de.htwberlin.kba.gr7.vocabduel.vocabulary.model.VocableList;

import java.util.List;

public interface VocabularyLib {
    VocableList getVocableListById(Long id);

    List<VocableList> getVocableListsOfUser(User user);

    List<LanguageSet> getAllLanguageSets();
}
