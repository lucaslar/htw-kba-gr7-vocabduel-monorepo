package de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.exceptions.*;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.LanguageSet;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.VocableUnit;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.VocableList;

import java.io.File;

public interface OwnVocabularyManagement {

    /**
     *
     * @param languageSet
     * @return
     * @throws DataAlreadyExistsException
     */
    int createLanguageSet(LanguageSet languageSet) throws DataAlreadyExistsException;

    /**
     *
     * @param title
     * @param languageSet
     * @return
     * @throws DataAlreadyExistsException
     */
    int createUnitForLanguageSet(String title, LanguageSet languageSet) throws DataAlreadyExistsException;

    /**
     *
     * @param vocables
     * @param unit
     * @return
     * @throws DuplicateVocablesInSetException
     * @throws IncompleteVocableListException
     * @throws DataAlreadyExistsException
     */
    int insertVocableListInUnit(VocableList vocables, VocableUnit unit) throws DuplicateVocablesInSetException, IncompleteVocableListException, DataAlreadyExistsException;

    /**
     * Creates unit/language set if not existing yet
     * @param gnuFile
     * @return
     * @throws DuplicateVocablesInSetException
     * @throws IncompleteVocableListException
     * @throws DataAlreadyExistsException
     */
    int importGnuVocableList(File gnuFile) throws DuplicateVocablesInSetException, IncompleteVocableListException, DataAlreadyExistsException;

    /**
     *
     * @param vocables
     * @return
     * @throws DuplicateVocablesInSetException
     * @throws DifferentAuthorException
     * @throws IncompleteVocableListException
     */
    int updateVocableList(VocableList vocables) throws DuplicateVocablesInSetException, DifferentAuthorException, IncompleteVocableListException;

    /**
     *
     * @param languageSet
     * @return
     * @throws NotEmptyException
     */
    int deleteEmptyLanguagesSet(LanguageSet languageSet) throws NotEmptyException;

    /**
     *
     * @param unit
     * @return
     * @throws NotEmptyException
     */
    int deleteEmptyVocableUnit(VocableUnit unit) throws NotEmptyException;

    /**
     *
     * @param vocables
     * @param triggeringUser
     * @return
     * @throws DifferentAuthorException
     */
    int deleteVocableList(VocableList vocables, User triggeringUser) throws DifferentAuthorException;
}
