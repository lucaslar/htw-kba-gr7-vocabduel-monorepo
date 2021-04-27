package de.htwberlin.kba.gr7.vocabduel.vocabulary;

import de.htwberlin.kba.gr7.vocabduel.vocabulary.exceptions.DuplicateVocablesInSetException;
import de.htwberlin.kba.gr7.vocabduel.vocabulary.exceptions.IncompleteVocableSetInformation;
import de.htwberlin.kba.gr7.vocabduel.vocabulary.model.VocableSet;

import java.io.File;
import java.sql.SQLException;

public interface OwnVocabularyManagement {
    int insertVocableSet(VocableSet vocables) throws DuplicateVocablesInSetException, IncompleteVocableSetInformation, SQLException;

    int updateVocableSet(VocableSet vocables) throws DuplicateVocablesInSetException, IncompleteVocableSetInformation, SQLException;

    int deleteVocableSet(VocableSet vocables) throws SQLException;

    int importGnuVocableSet(File gnuFile) throws DuplicateVocablesInSetException, IncompleteVocableSetInformation, SQLException;
}
