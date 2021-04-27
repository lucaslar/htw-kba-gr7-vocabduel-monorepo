package de.htwberlin.kba.gr7.vocabduel.vocabulary;

import de.htwberlin.kba.gr7.vocabduel.user.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabulary.exceptions.*;
import de.htwberlin.kba.gr7.vocabduel.vocabulary.model.LanguageSet;
import de.htwberlin.kba.gr7.vocabduel.vocabulary.model.VocableUnit;
import de.htwberlin.kba.gr7.vocabduel.vocabulary.model.VocableList;

import java.io.File;
import java.sql.SQLException;

public interface OwnVocabularyManagement {
    int createLanguageSet(LanguageSet languageSet) throws SQLException, DataAlreadyExistsException;

    int createUnitForLanguageSet(String title, LanguageSet languageSet) throws SQLException, DataAlreadyExistsException;

    int insertVocableListInUnit(VocableList vocables, VocableUnit unit) throws DuplicateVocablesInSetException, IncompleteVocableListInformation, DataAlreadyExistsException, SQLException;

    // Creates unit/language set if not existing yet
    int importGnuVocableList(File gnuFile) throws DuplicateVocablesInSetException, IncompleteVocableListInformation, SQLException, DataAlreadyExistsException;

    int updateVocableList(VocableList vocables) throws DuplicateVocablesInSetException, DifferentAuthorException, IncompleteVocableListInformation, SQLException;

    int deleteEmptyLanguagesSet(LanguageSet languageSet) throws SQLException, NotEmptyException;

    int deleteEmptyVocableUnit(VocableUnit unit) throws SQLException, NotEmptyException;

    int deleteVocableList(VocableList vocables, User triggeringUser) throws SQLException, DifferentAuthorException;
}
