package de.htwberlin.kba.gr7.vocabduel.vocabulary_administration;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.VocabularyLib;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.LanguageSet;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.VocableList;

import java.util.List;

public class VocabularyLibImpl implements VocabularyLib {
    @Override
    public VocableList getVocableListById(Long id) {
        return null;
    }

    @Override
    public List<VocableList> getVocableListsOfUser(User user) {
        return null;
    }

    @Override
    public List<LanguageSet> getAllLanguageSets() {
        return null;
    }
}
