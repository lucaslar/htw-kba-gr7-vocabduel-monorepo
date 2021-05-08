package de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.exceptions.*;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.LanguageSet;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.SupportedLanguage;
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
     * @return int database status of the insert query
     * @throws DataAlreadyExistsException There is already an existing unit with the given title in the given language set.
     */
    int createUnitForLanguageSet(String title, LanguageSet languageSet) throws DataAlreadyExistsException;

    /**
     * Addsa  new VocableList  to an existing unit if not existing yet
     * @param vocables add new VocableList to an existing unit in database
     * @param unit  the specific unit, which already exists
     * @return int database status of insert query
     * @throws DuplicateVocablesInSetException the VocableList inherits words that already exist in other lists
     * @throws IncompleteVocableListException the VocableList object to be added does not have all required data
     * @throws DataAlreadyExistsException the VocableList has already been added to a unit
     */
    int insertVocableListInUnit(VocableList vocables, VocableUnit unit) throws DuplicateVocablesInSetException, IncompleteVocableListException, DataAlreadyExistsException;

    /**
     * Creates unit/language set if not existing yet
     * @param gnuFile the VocableList as gnuFile provided in Moodle
     * @return int database status of insert query
     * @throws DuplicateVocablesInSetException the VocableList inherits words that already exist in other lists
     * @throws IncompleteVocableListException the VocableList object to be added does not have all required data
     * @throws DataAlreadyExistsException the VocableList has already been added to a unit
     */
    int importGnuVocableList(File gnuFile) throws DuplicateVocablesInSetException, IncompleteVocableListException, DataAlreadyExistsException;

    /**
     * Edit the VocableList: add more words or change its title etc. Only the user who created the VocableList can edit it.
     * @param vocables a VocableList that already exists in database
     * @return int database status of update query
     * @throws DuplicateVocablesInSetException the modified VocableList is still the same as before editing
     * @throws DifferentAuthorException the VocableList in question was created by another user
     * @throws IncompleteVocableListException the modified VocableList is incomplete e.g. a translation is missing or the title should be deleted
     */
    int updateVocableList(VocableList vocables) throws DuplicateVocablesInSetException, DifferentAuthorException, IncompleteVocableListException;

    /**
     * delete unnecessary LanguageSets cause it does not contain VocableUnits
     * @param languageSet the LanguageSet in question
     * @return int database status of delete query
     * @throws NotEmptyException the LanguageSet to be deleted is not empty
     */
    int deleteEmptyLanguagesSet(LanguageSet languageSet) throws NotEmptyException;

    /**
     *delete unnecessary VocableUnits cause it does not contain VocableLists
     * @param unit the VocableUnit in question
     * @return int database status of delete query
     * @throws NotEmptyException the VocableUnit to be deleted is not empty
     */
    int deleteEmptyVocableUnit(VocableUnit unit) throws NotEmptyException;

    /**
     * delete unnecessary VocableLists cause it does not contain Vocables
     * only the user who created the VocableList can delete it
     * @param vocables the VocableList in question
     * @param triggeringUser the user who wants to delete the VocableList
     * @return int database status of delete query
     * @throws DifferentAuthorException the user who wants to delete the list has not the right to do so
     */
    int deleteVocableList(VocableList vocables, User triggeringUser) throws DifferentAuthorException;

    /**
     * get vocabulary list by list id
     * @param id VocableList id
     * @return VocableList
     */
    VocableList getVocableListById(Long id);

    /**
     * get vocabulary list created by a user
     * @param user the author of the VocableList
     * @return List of assigned <code>{@link VocableList}</code>
     */
    List<VocableList> getVocableListsOfUser(User user);

    /**
     * get all available language sets
     * @return List of <code>{@link LanguageSet}</code>
     */
    List<LanguageSet> getAllLanguageSets();

    /**
     * get all Languages to learn from or to learn , which are supported in this application
     * @return List of <code>{@link SupportedLanguage}</code>
     */
    List<SupportedLanguage> getAllSupportedLanguage();
}
