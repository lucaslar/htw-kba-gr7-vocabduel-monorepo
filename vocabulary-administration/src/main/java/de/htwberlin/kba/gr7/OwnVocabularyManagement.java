package de.htwberlin.kba.gr7;

import de.htwberlin.kba.gr7.exceptions.DuplicateVocablesInSet;
import de.htwberlin.kba.gr7.exceptions.IncompleteVocableSetInformation;
import de.htwberlin.kba.gr7.exceptions.UnauthorizedException;
import de.htwberlin.kba.gr7.model.VocableSet;

import java.io.File;
import java.sql.SQLException;

public interface OwnVocabularyManagement {
    int insertVocableSet(VocableSet vocables) throws DuplicateVocablesInSet, IncompleteVocableSetInformation, UnauthorizedException, SQLException;

    int updateVocableSet(VocableSet vocables) throws DuplicateVocablesInSet, IncompleteVocableSetInformation, UnauthorizedException, SQLException;

    int deleteVocableSet(VocableSet vocables) throws UnauthorizedException, SQLException;

    int importGnuVocableSet(File gnuFile) throws DuplicateVocablesInSet, IncompleteVocableSetInformation, UnauthorizedException, SQLException;
}
