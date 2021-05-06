package de.htwberlin.kba.gr7.vocabduel.vocabulary_administration;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.VocabularyLib;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.exceptions.*;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.LanguageSet;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.VocableList;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.VocableUnit;

import java.io.File;
import java.util.List;

public class VocabularyLibImpl implements VocabularyLib {
    @Override
    public int createLanguageSet(LanguageSet languageSet) throws DataAlreadyExistsException {
        return 0;
    }

    @Override
    public int createUnitForLanguageSet(String title, LanguageSet languageSet) throws DataAlreadyExistsException {
        return 0;
    }

    @Override
    public int insertVocableListInUnit(VocableList vocables, VocableUnit unit) throws DuplicateVocablesInSetException, IncompleteVocableListException, DataAlreadyExistsException {
        return 0;
    }

    @Override
    public int importGnuVocableList(File gnuFile) throws DuplicateVocablesInSetException, IncompleteVocableListException, DataAlreadyExistsException {
        return 0;
    }

    @Override
    public int updateVocableList(VocableList vocables) throws DuplicateVocablesInSetException, DifferentAuthorException, IncompleteVocableListException {
        return 0;
    }

    @Override
    public int deleteEmptyLanguagesSet(LanguageSet languageSet) throws NotEmptyException {
        return 0;
    }

    @Override
    public int deleteEmptyVocableUnit(VocableUnit unit) throws NotEmptyException {
        return 0;
    }

    @Override
    public int deleteVocableList(VocableList vocables, User triggeringUser) throws DifferentAuthorException {
        return 0;
    }

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
