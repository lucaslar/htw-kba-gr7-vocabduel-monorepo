package de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.exceptions.*;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.LanguageSet;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.VocableList;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.VocableUnit;

import java.io.File;
import java.util.List;

/**
 * Service for managing vocabulary, i.e. adding/removing language sets, units and vocabulary lists, and for getting the
 * publicly available vocable data.
 *
 * @author Sebastian Kehl, Lucas Larisch
 * @version 1.0, May 2021
 */
public interface VocabularyLib {

    /**
     * Creates a new language set, i.e. a set of a known and a known and a learnt language vocable units can be added to.
     * For example: DE (known language) &rarr; ES (learnt language).
     *
     * @param languageSet Set of known and learnt language to be created (no units yet).
     * @return int database status of the insert query.
     * @throws DataAlreadyExistsException There is already an existing set with the given known and learnt language that can be used.
     */
    int createLanguageSet(LanguageSet languageSet) throws DataAlreadyExistsException;

    /**
     * Creates a unit for a given <code>{@link LanguageSet}</code>.
     *
     * @param title The new unit's title.
     * @param languageSet Language set the new unit is to be added to
     * @return int database status of the insert query.
     * @throws DataAlreadyExistsException There is already an existing unit with the given title in the given language set.
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
