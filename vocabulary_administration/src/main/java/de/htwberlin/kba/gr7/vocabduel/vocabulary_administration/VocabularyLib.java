package de.htwberlin.kba.gr7.vocabduel.vocabulary_administration;

import de.htwberlin.kba.gr7.vocabduel.user_administration.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.model.LanguageSet;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.model.VocableList;

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
