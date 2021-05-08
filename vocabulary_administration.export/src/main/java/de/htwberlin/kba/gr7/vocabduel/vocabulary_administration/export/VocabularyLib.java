package de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.exceptions.*;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.LanguageSet;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.SupportedLanguage;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.VocableList;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.VocableUnit;

import java.io.File;
import java.text.ParseException;
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
     * @param title       The new unit's title.
     * @param languageSet Language set the new unit is to be added to
     * @return int database status of the insert query.
     * @throws DataAlreadyExistsException There is already an existing unit with the given title in the given language set.
     */
    int createUnitForLanguageSet(String title, LanguageSet languageSet) throws DataAlreadyExistsException;

    /**
     * Adds a new <code>{@link VocableList}</code> to an existing unit.
     *
     * @param vocables Vocable list to be added to the existing unit.
     * @param unit     Already existing unit the vocable list is to be added to.
     * @return int database status of the insert query.
     * @throws DuplicateVocablesInSetException The vocable list contains duplicates concerning the vocabulary in the learnt language. Regardless of whether their respective translations differ or not.
     * @throws IncompleteVocableListException  The vocable list to be inserted does not contain all required data.
     * @throws DataAlreadyExistsException      The unit the vocable list is to be added to does already contain a vocable list with the same title.
     */
    int insertVocableListInUnit(VocableList vocables, VocableUnit unit) throws DuplicateVocablesInSetException, IncompleteVocableListException, DataAlreadyExistsException;

    /**
     * Imports a simple <code>.txt</code> file exported by GNU (see Moodle). If the stated languages/unit name do already exist, the
     * vocabulary list is inserted there. If not, the respective information is inserted, too.
     *
     * @param gnuFile        Vocable list as GNU file.
     * @param triggeringUser User triggering the import and who will be set as author of the new data.
     * @return int database status of the insert query.
     * @throws DuplicateVocablesInSetException The vocable list contains duplicates concerning the vocabulary in the learnt language. Regardless of whether their respective translations differ or not.
     * @throws IncompleteVocableListException  The vocable list to be imported does not contain all required data.
     * @throws DataAlreadyExistsException      The unit the vocable list is to be added to does already contain a vocable list with the same title.
     * @throws ParseException                  The file is either invalid or not recognized as GNU export.
     */
    int importGnuVocableList(File gnuFile, User triggeringUser) throws DuplicateVocablesInSetException, IncompleteVocableListException, DataAlreadyExistsException, ParseException;
    // TODO Find better exception than ParseException?

    /**
     * Updates the given vocable list. Possible changes include its title, adding more words, etc. Please note that this operation will
     * throw an error if the <code>triggeringUser</code> is not the vocable list's author.
     *
     * @param vocables       Existing vocable list to be updated.
     * @param triggeringUser User triggering the update. If the user is not the vocable list's author, an exception will be thrown.
     * @return int database status of the update query.
     * @throws DuplicateVocablesInSetException Caused by the changes, the vocable list contains duplicates concerning the vocabulary in the learnt language. Regardless of whether their respective translations differ or not.
     * @throws DifferentAuthorException        The triggering user is not authorized to update the vocable list, i.e. is not the list's author.
     * @throws DataAlreadyExistsException      The unit the vocable list is to be added to does already contain a vocable list with the same name.
     * @throws IncompleteVocableListException  The modified vocable list is incomplete e.g. a translation is missing or the title is empty.
     */
    int updateVocableList(VocableList vocables, User triggeringUser) throws DuplicateVocablesInSetException, DifferentAuthorException, DataAlreadyExistsException, IncompleteVocableListException;

    /**
     * Deletes a <code>{@link LanguageSet}</code> dispensable due to not containing vocable units.
     *
     * @param languageSet The empty language set to be deleted.
     * @return int database status of the delete query.
     * @throws NotEmptyException The language set to be deleted is not empty.
     */
    int deleteEmptyLanguagesSet(LanguageSet languageSet) throws NotEmptyException;

    /**
     * Deletes a <code>{@link VocableUnit}</code> dispensable due to not containing vocable lists.
     *
     * @param unit The empty vocable unit to be deleted.
     * @return int database status of the delete query.
     * @throws NotEmptyException The vocable unit to be deleted is not empty.
     */
    int deleteEmptyVocableUnit(VocableUnit unit) throws NotEmptyException;

    /**
     * Deletes a vocable list if the triggering user is its author.
     *
     * @param vocables       The vocable list to be deleted.
     * @param triggeringUser User triggering the deletion process.
     * @return int database status of the delete query.
     * @throws DifferentAuthorException The user who wants to delete the list has not the rights to do so, i.e. is not the list's author.
     */
    int deleteVocableList(VocableList vocables, User triggeringUser) throws DifferentAuthorException;

    /**
     * @param id ID the vocable list for is to be returned.
     * @return Vocable list with the given <code>id</code> or <code>null</code> if no list could be found.
     */
    VocableList getVocableListById(Long id);

    /**
     * @param user User whose vocable lists, i.e. the lists she/he is the author of, are to be returned.
     * @return The given user's vocable lists (can be empty).
     */
    List<VocableList> getVocableListsOfUser(User user);

    /**
     * @return List containing all available language sets.
     */
    List<LanguageSet> getAllLanguageSets();

    /**
     * @return List containing all languages for learning/translating supported by this application.
     */
    List<SupportedLanguage> getAllSupportedLanguages();
}
