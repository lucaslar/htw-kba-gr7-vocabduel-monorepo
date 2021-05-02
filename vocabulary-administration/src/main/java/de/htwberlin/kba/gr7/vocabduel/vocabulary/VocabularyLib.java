package de.htwberlin.kba.gr7.vocabduel.vocabulary;

import de.htwberlin.kba.gr7.vocabduel.user.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabulary.model.LanguageSet;
import de.htwberlin.kba.gr7.vocabduel.vocabulary.model.VocableList;

import java.util.List;

public interface VocabularyLib {

    /**
     * get vocabulary list by list id
     * @param id
     * @return
     */
    VocableList getVocableListById(Long id);

    /**
     * get vocabulary list assigned to a user
     * @param user
     * @return
     */
    List<VocableList> getVocableListsOfUser(User user);

    /**
     * get all available language sets
     * @return
     */
    List<LanguageSet> getAllLanguageSets();
}
