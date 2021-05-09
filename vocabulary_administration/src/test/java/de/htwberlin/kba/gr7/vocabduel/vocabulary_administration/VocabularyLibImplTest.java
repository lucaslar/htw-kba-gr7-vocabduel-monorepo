package de.htwberlin.kba.gr7.vocabduel.vocabulary_administration;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.exceptions.*;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(MockitoJUnitRunner.class)
public class VocabularyLibImplTest {

    private static List<Vocable> mockVocablesEsEn(final String[][] vocables) {
        return Stream.of(vocables).map(words -> {
            final Vocable vocable = new Vocable();
            final TranslationGroup en = new TranslationGroup();
            final TranslationGroup es = new TranslationGroup();
            es.setSynonyms(Stream.of(words[0]).collect(Collectors.toList()));
            en.setSynonyms(Stream.of(words[1]).collect(Collectors.toList()));
            vocable.setVocable(es);
            vocable.setVocable(en);
            return vocable;
        }).collect(Collectors.toList());
    }

    private VocabularyLibImpl vocabularyLib;
    private LanguageSet existingLanguageSet;
    private LanguageSet emptyLanguagesSet;
    private VocableUnit existingVocableUnit;
    private VocableUnit emptyVocableUnit;
    private VocableList existingVocableList1;
    private VocableList existingVocableList2;
    private User author;

    @Before
    public void setup() {
        vocabularyLib = new VocabularyLibImpl();
        author = new User(42L);

        final String[][] weekdaysEsEn = {
                {"Lunes", "Monday"},
                {"Martes", "Tuesday"},
                {"Miércoles", "Wednesday"},
                {"Jueves", "Thursday"},
                {"Viernes", "Friday"},
                {"Sábado", "Saturday"},
                {"Domingo", "Sunday"},
        };

        final String[][] fruitsEsEn = {
                {"Manzana", "Apple"},
                {"Plátano", "Banana"},
                {"Pera", "Pear"},
                {"Fresa", "Strawberry"},
                {"Arándano", "Blueberry"},
                {"Frambuesa", "Raspberry"},
        };

        existingVocableList1 = new VocableList(42L);
        existingVocableList1.setTitle("Los Dias De La Semana");
        existingVocableList1.setAuthor(author);
        existingVocableList1.setTimestamp(new Date());
        existingVocableList1.setVocables(mockVocablesEsEn(weekdaysEsEn));

        existingVocableList2 = new VocableList(4711L);
        existingVocableList2.setTitle("Frutas");
        existingVocableList2.setAuthor(author);
        existingVocableList2.setTimestamp(new Date());
        existingVocableList2.setVocables(mockVocablesEsEn(fruitsEsEn));

        existingVocableUnit = new VocableUnit();
        existingVocableUnit.setTitle("Español => Inglés - Vocabduel I");
        existingVocableUnit.setVocableLists(Stream.of(existingVocableList1, existingVocableList2).collect(Collectors.toList()));

        emptyVocableUnit = new VocableUnit();
        emptyVocableUnit.setTitle("This unit is empty");

        existingLanguageSet = new LanguageSet();
        existingLanguageSet.setKnownLanguage(SupportedLanguage.EN);
        existingLanguageSet.setLearntLanguage(SupportedLanguage.ES);
        existingLanguageSet.setVocableUnits(Stream.of(existingVocableUnit, emptyVocableUnit).collect(Collectors.toList()));

        emptyLanguagesSet = new LanguageSet();
        emptyLanguagesSet.setKnownLanguage(SupportedLanguage.AR);
        emptyLanguagesSet.setLearntLanguage(SupportedLanguage.JA);

        final List<LanguageSet> languages = Stream.of(existingLanguageSet, emptyLanguagesSet).collect(Collectors.toList());
        // Mock existing languages (private field => Whitebox#setInternalState)
        Whitebox.setInternalState(vocabularyLib, "allLanguageSets", languages);
    }

    @Test(expected = DataAlreadyExistsException.class)
    public void shouldNotCreateExistingLanguageSet() throws DataAlreadyExistsException {
        vocabularyLib.createLanguageSet(existingLanguageSet);
    }

    @Test
    public void shouldCreateNewLanguageSet() throws DataAlreadyExistsException {
        final LanguageSet set = new LanguageSet();
        set.setLearntLanguage(SupportedLanguage.IT);
        set.setKnownLanguage(SupportedLanguage.CMN);
        sharedSuccessfulLanguageSetInsertionAssertions(set);
    }

    @Test
    public void shouldCreateInvertedExistingLanguageSet() throws DataAlreadyExistsException {
        final LanguageSet invertedSet = new LanguageSet();
        invertedSet.setLearntLanguage(existingLanguageSet.getKnownLanguage());
        invertedSet.setKnownLanguage(existingLanguageSet.getLearntLanguage());
        sharedSuccessfulLanguageSetInsertionAssertions(invertedSet);
    }

    @Test(expected = DataAlreadyExistsException.class)
    public void shouldNotCreateUnitWithSameTitleInLanguageSet() throws DataAlreadyExistsException {
        final String existingTitle = existingVocableUnit.getTitle();
        vocabularyLib.createUnitForLanguageSet(existingTitle, existingLanguageSet);
    }

    @Test
    public void shouldCreateUnitWithDifferentTitleInLanguageSet() throws DataAlreadyExistsException {
        final int initialNrOfUnits = existingLanguageSet.getVocableUnits().size();
        final String newTitle = "Some new title";
        final int statusCode = vocabularyLib.createUnitForLanguageSet(newTitle, existingLanguageSet);

        Assert.assertEquals(0, statusCode);

        final List<VocableUnit> refreshedUnits = vocabularyLib.getAllLanguageSets().get(0).getVocableUnits();
        Assert.assertEquals(initialNrOfUnits + 1, refreshedUnits.size());
        Assert.assertTrue(refreshedUnits.stream().anyMatch(u -> u.getTitle().equals(newTitle)));
    }

    @Test(expected = DataAlreadyExistsException.class)
    public void shouldNotInertVocableListWithExistingTitle() throws DataAlreadyExistsException, DuplicateVocablesInSetException, IncompleteVocableListException {
        final VocableList vocableList = new VocableList(123L);
        vocableList.setTitle(existingVocableUnit.getTitle());
        vocabularyLib.insertVocableListInUnit(vocableList, existingVocableUnit);
    }

    // The following two tests could be implemented for each potentially null/empty trimmed field (same for update)
    // using a parameterized test. However, this would lead to an unnecessarily large number of tests.

    @Test(expected = IncompleteVocableListException.class)
    public void shouldNotInsertIncompleteVocableData() throws DataAlreadyExistsException, DuplicateVocablesInSetException, IncompleteVocableListException {
        final TranslationGroup unknownWord = new TranslationGroup();
        unknownWord.setSynonyms(Stream.of("I'm untranslated").collect(Collectors.toList()));

        final Vocable untranslatedVocable = new Vocable();
        untranslatedVocable.setVocable(unknownWord);

        final VocableList vocableList = new VocableList(123L);
        vocableList.setTitle("Some new title");
        vocableList.setVocables(Stream.of(untranslatedVocable).collect(Collectors.toList()));

        vocabularyLib.insertVocableListInUnit(vocableList, existingVocableUnit);
    }

    @Test(expected = IncompleteVocableListException.class)
    public void shouldNotInsertEmptyTrimmedVocableData() throws DataAlreadyExistsException, DuplicateVocablesInSetException, IncompleteVocableListException {
        final TranslationGroup unknownWord = new TranslationGroup();
        unknownWord.setSynonyms(Stream.of("I have a blank translation").collect(Collectors.toList()));

        final TranslationGroup emptyTrimmedTranslation = new TranslationGroup();
        emptyTrimmedTranslation.setSynonyms(Stream.of("     ").collect(Collectors.toList()));

        final Vocable untranslatedVocable = new Vocable();
        untranslatedVocable.setVocable(unknownWord);
        untranslatedVocable.setTranslations(Stream.of(emptyTrimmedTranslation).collect(Collectors.toList()));

        final VocableList vocableList = new VocableList(123L);
        vocableList.setTitle("Some new title");
        vocableList.setVocables(Stream.of(untranslatedVocable).collect(Collectors.toList()));

        vocabularyLib.insertVocableListInUnit(vocableList, existingVocableUnit);
    }

    @Test(expected = DuplicateVocablesInSetException.class)
    public void shouldNotInsertListWithSameVocables() throws DataAlreadyExistsException, DuplicateVocablesInSetException, IncompleteVocableListException {
        final String vocableSynonym = "Double trouble";

        final TranslationGroup duplicate1 = new TranslationGroup();
        duplicate1.setSynonyms(Stream.of(vocableSynonym).collect(Collectors.toList()));

        final TranslationGroup duplicate2 = new TranslationGroup();
        duplicate2.setSynonyms(Stream.of(vocableSynonym).collect(Collectors.toList()));

        final TranslationGroup translation1 = new TranslationGroup();
        translation1.setSynonyms(Stream.of("Translation 1").collect(Collectors.toList()));

        final TranslationGroup translation2 = new TranslationGroup();
        translation2.setSynonyms(Stream.of("Translation 2").collect(Collectors.toList()));

        final Vocable vocable1 = new Vocable();
        vocable1.setVocable(duplicate1);
        vocable1.setTranslations(Stream.of(translation1).collect(Collectors.toList()));

        final Vocable vocable2 = new Vocable();
        vocable2.setVocable(duplicate2);
        vocable2.setTranslations(Stream.of(translation2).collect(Collectors.toList()));

        final VocableList vocableList = new VocableList(123L);
        vocableList.setTitle("Some new title");
        vocableList.setVocables(Stream.of(vocable1, vocable2).collect(Collectors.toList()));

        vocabularyLib.insertVocableListInUnit(vocableList, existingVocableUnit);
    }

    @Test
    public void shouldInsertVocables() throws DataAlreadyExistsException, DuplicateVocablesInSetException, IncompleteVocableListException {
        final String newTitle = "Some new title";

        final TranslationGroup unknownWord = new TranslationGroup();
        unknownWord.setSynonyms(Stream.of("Some word").collect(Collectors.toList()));
        unknownWord.setExemplarySentencesOrAdditionalInfo(Stream.of("This sentence will help me...").collect(Collectors.toList()));

        final TranslationGroup translation = new TranslationGroup();
        translation.setSynonyms(Stream.of("Some translation").collect(Collectors.toList()));
        unknownWord.setExemplarySentencesOrAdditionalInfo(Stream.of("This sentence will help me, too...").collect(Collectors.toList()));

        final Vocable vocable = new Vocable();
        vocable.setVocable(unknownWord);
        vocable.setTranslations(Stream.of(translation).collect(Collectors.toList()));

        final VocableList vocableList = new VocableList(123L);
        vocableList.setTitle(newTitle);
        vocableList.setVocables(Stream.of(vocable).collect(Collectors.toList()));

        final VocableUnit unit = vocabularyLib.getAllLanguageSets().get(0).getVocableUnits().get(0);
        final int initialListsLength = unit.getVocableLists().size();
        final int statusCode = vocabularyLib.insertVocableListInUnit(vocableList, unit);
        Assert.assertEquals(0, statusCode);

        final List<VocableList> refreshedLists = vocabularyLib.getAllLanguageSets().get(0).getVocableUnits().get(0).getVocableLists();
        Assert.assertEquals(initialListsLength + 1, refreshedLists.size());
        Assert.assertTrue(refreshedLists.stream().anyMatch(u -> u.getTitle().equals(newTitle)));
    }

    @Test(expected = DuplicateVocablesInSetException.class)
    public void shouldNotImportGnuListWithDuplicateVocables() throws DataAlreadyExistsException, DuplicateVocablesInSetException, IncompleteVocableListException, ParseException {
        final String pathname = "./src/test/assets/gnu_duplicate_vocabulary.txt";
        final File f = new File(pathname);
        Assert.assertTrue("This test requires a file: " + pathname, f.exists());
        vocabularyLib.importGnuVocableList(f, new User(42L));
    }

    @Test(expected = DataAlreadyExistsException.class)
    public void shouldNotImportGnuListWithExistingTitleInUnit() throws DataAlreadyExistsException, DuplicateVocablesInSetException, IncompleteVocableListException, ParseException {
        final String pathname = "./src/test/assets/gnu_title_already_exists.txt";
        final File f = new File(pathname);
        Assert.assertTrue("This test requires a file: " + pathname, f.exists());
        vocabularyLib.importGnuVocableList(f, new User(42L));
    }

    @Test(expected = IncompleteVocableListException.class)
    public void shouldNotImportGnuListWithIncompleteData() throws DataAlreadyExistsException, DuplicateVocablesInSetException, IncompleteVocableListException, ParseException {
        final String pathname = "./src/test/assets/gnu_incomplete_list.txt";
        final File f = new File(pathname);
        Assert.assertTrue("This test requires a file: " + pathname, f.exists());
        vocabularyLib.importGnuVocableList(f, new User(42L));
    }

    @Test(expected = ParseException.class)
    public void shouldNotImportGnuListWithInvalidFormat() throws DataAlreadyExistsException, DuplicateVocablesInSetException, IncompleteVocableListException, ParseException {
        final String pathname = "./src/test/assets/gnu_invalid_format.txt";
        final File f = new File(pathname);
        Assert.assertTrue("This test requires a file: " + pathname, f.exists());
        vocabularyLib.importGnuVocableList(f, new User(42L));
    }

    @Test(expected = ParseException.class)
    public void shouldThrowExceptionOnImportingNotExistingGnuFile() throws DataAlreadyExistsException, DuplicateVocablesInSetException, IncompleteVocableListException, ParseException {
        final String pathname = "./src/test/assets/not_existing_file.txt";
        final File f = new File(pathname);
        Assert.assertFalse("This file must not exist for this test: " + pathname, f.exists());
        vocabularyLib.importGnuVocableList(f, new User(42L));
    }

    @Test
    public void shouldImportGnuFile() throws DataAlreadyExistsException, DuplicateVocablesInSetException, IncompleteVocableListException, ParseException {
        final String pathname = "./src/test/assets/gnu_valid_format.txt";
        final File f = new File(pathname);
        Assert.assertTrue("This test requires a file: " + pathname, f.exists());

        final int initialListsLength = existingVocableUnit.getVocableLists().size();
        final int statusCode = vocabularyLib.importGnuVocableList(f, new User(42L));
        Assert.assertEquals(0, statusCode);

        final String expectedTitle = "GNU Unit title";
        final List<VocableList> refreshedLists = vocabularyLib.getAllLanguageSets().get(0).getVocableUnits().get(0).getVocableLists();
        Assert.assertEquals(initialListsLength + 1, refreshedLists.size());
        final String infoMsg = "Please make sure, your gnu file's vocabulary list title is \"" + expectedTitle + "\"";
        Assert.assertTrue(infoMsg, refreshedLists.stream().anyMatch(vl -> vl.getTitle().equals(expectedTitle)));
    }

    @Test
    public void shouldCreateNewUnitIfRequiredByGnuFile() throws DataAlreadyExistsException, DuplicateVocablesInSetException, IncompleteVocableListException, ParseException {
        final String pathname = "./src/test/assets/gnu_valid_format_in_new_unit.txt";
        final File f = new File(pathname);
        Assert.assertTrue("This test requires a file: " + pathname, f.exists());

        final int initialUnitsLength = existingLanguageSet.getVocableUnits().size();
        final int statusCode = vocabularyLib.importGnuVocableList(f, new User(42L));
        Assert.assertEquals(0, statusCode);

        final String expectedUnitTitle = "GNU Unit title";
        final List<VocableUnit> refreshedUnits = vocabularyLib.getAllLanguageSets().get(0).getVocableUnits();
        Assert.assertEquals(initialUnitsLength + 1, refreshedUnits.size());
        final String infoMsg = "Please make sure, your gnu file's unit title is \"" + expectedUnitTitle + "\"";
        Assert.assertTrue(infoMsg, refreshedUnits.stream().anyMatch(u -> u.getTitle().equals(expectedUnitTitle)));
        // See previous test for checking if insertion itself works
    }

    @Test
    public void shouldCreateNewLanguageSetIfRequiredByGnuFile() throws DataAlreadyExistsException, DuplicateVocablesInSetException, IncompleteVocableListException, ParseException {
        final String pathname = "./src/test/assets/gnu_valid_format_in_new_language_set.txt";
        final File f = new File(pathname);
        Assert.assertTrue("This test requires a file: " + pathname, f.exists());

        final int initialLanguageSetLength = vocabularyLib.getAllLanguageSets().size();
        final int statusCode = vocabularyLib.importGnuVocableList(f, new User(42L));
        Assert.assertEquals(0, statusCode);

        final List<LanguageSet> refreshedLanguageSets = vocabularyLib.getAllLanguageSets();
        Assert.assertEquals(initialLanguageSetLength + 1, refreshedLanguageSets.size());
        // See previous test for checking if insertion of units works
    }

    @Test(expected = DifferentAuthorException.class)
    public void shouldNotUpdateVocablesIfTriggeringUserIsNotAuthor() throws DataAlreadyExistsException, DifferentAuthorException, DuplicateVocablesInSetException, IncompleteVocableListException {
        vocabularyLib.updateVocableList(existingVocableList1, new User(4711L));
    }

    @Test(expected = DataAlreadyExistsException.class)
    public void shouldNotUpdateVocablesIfSameTitleExistsInUnit() throws DataAlreadyExistsException, DifferentAuthorException, DuplicateVocablesInSetException, IncompleteVocableListException {
        existingVocableList1.setTitle(existingVocableList2.getTitle());
        vocabularyLib.updateVocableList(existingVocableList1, author);
    }

    @Test(expected = DuplicateVocablesInSetException.class)
    public void shouldNotUpdateVocablesListContainingDuplicates() throws DataAlreadyExistsException, DifferentAuthorException, DuplicateVocablesInSetException, IncompleteVocableListException {
        final List<Vocable> vocables = existingVocableList1.getVocables();
        final Vocable duplicate = new Vocable();

        final TranslationGroup duplicateTg = new TranslationGroup();
        duplicateTg.setSynonyms(vocables.get(0).getVocable().getSynonyms());

        final TranslationGroup someTranslation = new TranslationGroup();
        someTranslation.setSynonyms(Stream.of("some translation").collect(Collectors.toList()));

        duplicate.setVocable(duplicateTg);
        duplicate.setTranslations(Stream.of(someTranslation).collect(Collectors.toList()));
        vocabularyLib.updateVocableList(existingVocableList1, author);
    }

    @Test(expected = IncompleteVocableListException.class)
    public void shouldNotUpdateVocableListWithEmptyTrimmedTranslation() throws DataAlreadyExistsException, DifferentAuthorException, DuplicateVocablesInSetException, IncompleteVocableListException {
        final TranslationGroup unknownWord = new TranslationGroup();
        unknownWord.setSynonyms(Stream.of("I have a blank translation").collect(Collectors.toList()));

        final TranslationGroup emptyTrimmedTranslation = new TranslationGroup();
        emptyTrimmedTranslation.setSynonyms(Stream.of("     ").collect(Collectors.toList()));

        final Vocable untranslatedVocable = new Vocable();
        untranslatedVocable.setVocable(unknownWord);
        untranslatedVocable.setTranslations(Stream.of(emptyTrimmedTranslation).collect(Collectors.toList()));

        existingVocableList1.getVocables().add(untranslatedVocable);
        vocabularyLib.updateVocableList(existingVocableList1, author);
    }

    @Test
    public void shouldUpdateVocableListTitle() throws DataAlreadyExistsException, DifferentAuthorException, DuplicateVocablesInSetException, IncompleteVocableListException {
        final String newTitle = "New title for vocable list";
        existingVocableList1.setTitle(newTitle);
        final int statusCode = vocabularyLib.updateVocableList(existingVocableList1, author);
        Assert.assertEquals(0, statusCode);

        final VocableList foundVocables = vocabularyLib.getVocableListById(existingVocableList1.getId());
        Assert.assertNotNull(foundVocables);
        Assert.assertEquals(newTitle, foundVocables.getTitle());
    }

    @Test
    public void shouldUpdateVocableListByRm() throws DataAlreadyExistsException, DifferentAuthorException, DuplicateVocablesInSetException, IncompleteVocableListException {
        final int initialLength = existingVocableList1.getVocables().size();
        existingVocableList1.getVocables().remove(existingVocableList1.getVocables().get(0));

        final int statusCode = vocabularyLib.updateVocableList(existingVocableList1, author);
        Assert.assertEquals(0, statusCode);

        final VocableList foundVocables = vocabularyLib.getVocableListById(existingVocableList1.getId());
        Assert.assertNotNull(foundVocables);
        Assert.assertEquals(initialLength - 1, foundVocables.getVocables().size());
    }

    @Test
    public void shouldUpdateVocableListByAdding() throws DataAlreadyExistsException, DifferentAuthorException, DuplicateVocablesInSetException, IncompleteVocableListException {
        final int initialLength = existingVocableList1.getVocables().size();

        final TranslationGroup unknownWord = new TranslationGroup();
        unknownWord.setSynonyms(Stream.of("Some word").collect(Collectors.toList()));
        unknownWord.setExemplarySentencesOrAdditionalInfo(Stream.of("This sentence will help me...").collect(Collectors.toList()));

        final TranslationGroup translation = new TranslationGroup();
        translation.setSynonyms(Stream.of("Some translation").collect(Collectors.toList()));
        unknownWord.setExemplarySentencesOrAdditionalInfo(Stream.of("This sentence will help me, too...").collect(Collectors.toList()));

        final Vocable vocable = new Vocable();
        vocable.setVocable(unknownWord);
        vocable.setTranslations(Stream.of(translation).collect(Collectors.toList()));

        existingVocableList1.getVocables().add(vocable);
        final int statusCode = vocabularyLib.updateVocableList(existingVocableList1, author);
        Assert.assertEquals(0, statusCode);

        final VocableList foundVocables = vocabularyLib.getVocableListById(existingVocableList1.getId());
        Assert.assertNotNull(foundVocables);
        Assert.assertEquals(initialLength + 1, foundVocables.getVocables().size());
    }

    @Test(expected = NotEmptyException.class)
    public void shouldNotDeleteLanguagesSetWithUnits() throws NotEmptyException {
        vocabularyLib.deleteEmptyLanguagesSet(existingLanguageSet);
    }

    @Test
    public void shouldDeleteEmptyLanguagesSet() throws NotEmptyException {
        final int statusCode = vocabularyLib.deleteEmptyLanguagesSet(emptyLanguagesSet);
        Assert.assertEquals(0, statusCode);
    }

    @Test(expected = NotEmptyException.class)
    public void shouldNotDeleteVocableUnitWithVocableLists() throws NotEmptyException {
        vocabularyLib.deleteEmptyVocableUnit(existingVocableUnit);
    }

    @Test
    public void shouldDeleteEmptyVocableUnit() throws NotEmptyException {
        final int statusCode = vocabularyLib.deleteEmptyVocableUnit(emptyVocableUnit);
        Assert.assertEquals(0, statusCode);
    }

    @Test(expected = DifferentAuthorException.class)
    public void shouldNotDeleteVocableListIfNotTriggeredByAuthor() throws DifferentAuthorException {
        vocabularyLib.deleteVocableList(existingVocableList1, new User(4711L));
    }

    @Test
    public void shouldDeleteVocableListIfTriggeredByAuthor() throws DifferentAuthorException {
        final int statusCode = vocabularyLib.deleteVocableList(existingVocableList1, author);
        Assert.assertEquals(0, statusCode);
    }

    @Test
    public void shouldFindVocableListByExistingId() {
        final VocableList foundList = vocabularyLib.getVocableListById(existingVocableList2.getId());
        Assert.assertNotNull(foundList);
        Assert.assertEquals(foundList.toString(), existingVocableList2.toString());
    }

    @Test
    public void shouldNotFindVocableListIfUnknownId() {
        Assert.assertNull(vocabularyLib.getVocableListById(123456L));
    }

    @Test
    public void shouldFindVocableListsOfUser() {
        final List<VocableList> foundLists = vocabularyLib.getVocableListsOfUser(author);
        Assert.assertNotNull(foundLists);
        Assert.assertEquals(2, foundLists.size());
        foundLists.forEach(fl -> Assert.assertEquals(fl.getAuthor().toString(), author.toString()));
    }

    @Test
    public void shouldReturnEmptyListForUserWhoIsNoAuthor() {
        final List<VocableList> foundLists = vocabularyLib.getVocableListsOfUser(new User(4711L));
        Assert.assertNotNull(foundLists);
        Assert.assertEquals(0, foundLists.size());
    }

    @Test
    public void shouldReturnAllLanguageSets() {
        final List<LanguageSet> languageSets = vocabularyLib.getAllLanguageSets();
        Assert.assertNotNull(languageSets);
        Assert.assertEquals(2, languageSets.size());
    }

    @Test
    public void shouldSupportAtLeastFiveLanguages() {
        // at least two are required for the game to make sense. 5 can be seen as some sort of goal for the application to be built
        final List<SupportedLanguage> supportedLanguages = vocabularyLib.getAllSupportedLanguages();
        Assert.assertNotNull(supportedLanguages);
        Assert.assertTrue(supportedLanguages.size() >= 5);
    }

    @Test
    public void shouldOnlyListUniqueLanguagesAsSupported() {
        final List<SupportedLanguage> supportedLanguages = vocabularyLib.getAllSupportedLanguages();
        Assert.assertNotNull(supportedLanguages);
        List<SupportedLanguage> uniques = supportedLanguages.stream().distinct().collect(Collectors.toList());
        Assert.assertEquals(uniques.size(), supportedLanguages.size());
    }

    private void sharedSuccessfulLanguageSetInsertionAssertions(final LanguageSet languageSet) throws DataAlreadyExistsException {
        final int initialLanguagesLength = vocabularyLib.getAllLanguageSets().size();
        final int statusCode = vocabularyLib.createLanguageSet(languageSet);
        Assert.assertEquals(0, statusCode);

        final List<LanguageSet> refreshedLanguages = vocabularyLib.getAllLanguageSets();
        Assert.assertEquals(initialLanguagesLength + 1, refreshedLanguages.size());

        final Optional<LanguageSet> foundLanguageSet = refreshedLanguages
                .stream()
                .filter(ls -> ls.getKnownLanguage().equals(languageSet.getKnownLanguage()) && ls.getLearntLanguage().equals(languageSet.getLearntLanguage()))
                .findFirst();
        Assert.assertTrue(foundLanguageSet.isPresent());
        Assert.assertEquals(foundLanguageSet.toString(), languageSet.toString());
    }
}
