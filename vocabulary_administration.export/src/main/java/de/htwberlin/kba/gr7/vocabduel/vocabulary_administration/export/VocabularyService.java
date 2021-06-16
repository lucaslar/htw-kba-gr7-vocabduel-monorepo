package de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.exceptions.*;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.LanguageSet;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.SupportedLanguage;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.VocableList;

import java.util.List;

/**
 * Service for managing vocabulary, i.e. adding/removing language sets, units and vocabulary lists, and for getting the
 * publicly available vocable data.
 *
 * @author Sebastian Kehl, Lucas Larisch
 * @version 1.0, May 2021
 */
public interface VocabularyService {

    /**
     * Imports a simple <code>.txt</code> file exported by GNU (see Moodle). If the stated languages/unit name do already exist, the
     * vocabulary list is inserted there. If not, the respective information is inserted, too.
     *
     * @param gnuContent     Content of a GNU file as string.
     * @param triggeringUser User triggering the import and who will be set as author of the new data.
     * @return int database status of the insert query.
     * @throws DuplicateVocablesInSetException The vocable list contains duplicates concerning the vocabulary in the learnt language. Regardless of whether their respective translations differ or not.
     * @throws IncompleteVocableListException  The vocable list to be imported does not contain all required data.
     * @throws DataAlreadyExistsException      The unit the vocable list is to be added to does already contain a vocable list with the same title.
     * @throws UnknownLanguagesException       One or both given language(s) are not supported or wrongly referred to.
     */
    int importGnuVocableList(String gnuContent, User triggeringUser) throws DuplicateVocablesInSetException, InvalidVocableListException, IncompleteVocableListException, DataAlreadyExistsException, UnknownLanguagesException;

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

    /**
     * @return List containing all references to the given supported language.
     */
    List<String> getSupportedLanguageReferences(SupportedLanguage language);

    /**
     * @param reference Reference the supported language is to be determined by.
     * @return Determined supported language.
     *
     * TODO: Delete if languages are to be removed from start game
     */
    SupportedLanguage getSupportedLanguageByReference(String reference);
}
