package de.htwberlin.kba.gr7.vocabduel.vocabulary;

import de.htwberlin.kba.gr7.vocabduel.user.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabulary.exceptions.*;
import de.htwberlin.kba.gr7.vocabduel.vocabulary.model.LanguageSet;
import de.htwberlin.kba.gr7.vocabduel.vocabulary.model.VocableUnit;
import de.htwberlin.kba.gr7.vocabduel.vocabulary.model.VocableList;

import java.io.File;

public interface OwnVocabularyManagement {
    int createLanguageSet(LanguageSet languageSet) throws DataAlreadyExistsException;

    int createUnitForLanguageSet(String title, LanguageSet languageSet) throws DataAlreadyExistsException;

    int insertVocableListInUnit(VocableList vocables, VocableUnit unit) throws DuplicateVocablesInSetException, IncompleteVocableListException, DataAlreadyExistsException;

    // Creates unit/language set if not existing yet
    int importGnuVocableList(File gnuFile) throws DuplicateVocablesInSetException, IncompleteVocableListException, DataAlreadyExistsException;

    int updateVocableList(VocableList vocables) throws DuplicateVocablesInSetException, DifferentAuthorException, IncompleteVocableListException;

    int deleteEmptyLanguagesSet(LanguageSet languageSet) throws NotEmptyException;

    int deleteEmptyVocableUnit(VocableUnit unit) throws NotEmptyException;

    int deleteVocableList(VocableList vocables, User triggeringUser) throws DifferentAuthorException;
}
