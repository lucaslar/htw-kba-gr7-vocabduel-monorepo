package de.htwberlin.kba.gr7.vocabduel.vocabulary_administration;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.exceptions.DataAlreadyExistsException;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.exceptions.DuplicateVocablesInSetException;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.exceptions.IncompleteVocableListException;
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
    private VocableUnit existingVocableUnit;
    private VocableList existingVocableList;
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

        existingVocableList = new VocableList();
        existingVocableList.setTitle("Los Dias De La Semana");
        existingVocableList.setAuthor(author);
        existingVocableList.setTimestamp(new Date());
        existingVocableList.setVocables(mockVocablesEsEn(weekdaysEsEn));

        final VocableList secondExistingSet = new VocableList();
        secondExistingSet.setTitle("Frutas");
        secondExistingSet.setAuthor(author);
        existingVocableList.setTimestamp(new Date());
        existingVocableList.setVocables(mockVocablesEsEn(fruitsEsEn));

        existingVocableUnit = new VocableUnit();
        existingVocableUnit.setTitle("Español => Inglés - Vocabduel I");
        existingVocableUnit.setVocableLists(Stream.of(existingVocableList).collect(Collectors.toList()));

        final LanguageSet ls = new LanguageSet();
        ls.setKnownLanguage(SupportedLanguage.EN);
        ls.setLearntLanguage(SupportedLanguage.ES);
        ls.setVocableUnits(Stream.of(existingVocableUnit).collect(Collectors.toList()));

        final List<LanguageSet> languages = Stream.of(ls).collect(Collectors.toList());
        // Mock existing languages (private field => Whitebox#setInternalState)
        Whitebox.setInternalState(vocabularyLib, "allLanguageSets", languages);
    }

    @Test(expected = DataAlreadyExistsException.class)
    public void shouldNotCreateExistingLanguageSet() throws DataAlreadyExistsException {
        // Make sure the language set exists in this test by calling mocked method
        final LanguageSet existingSet = vocabularyLib.getAllLanguageSets().get(0);
        vocabularyLib.createLanguageSet(existingSet);
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
        final LanguageSet existingSet = vocabularyLib.getAllLanguageSets().get(0);
        final LanguageSet invertedSet = new LanguageSet();
        invertedSet.setLearntLanguage(existingSet.getKnownLanguage());
        invertedSet.setKnownLanguage(existingSet.getLearntLanguage());
        sharedSuccessfulLanguageSetInsertionAssertions(invertedSet);
    }

    @Test(expected = DataAlreadyExistsException.class)
    public void shouldNotCreateUnitWithSameTitleInLanguageSet() throws DataAlreadyExistsException {
        final LanguageSet languageSet = vocabularyLib.getAllLanguageSets().get(0);
        final String existingTitle = languageSet.getVocableUnits().get(0).getTitle();
        vocabularyLib.createUnitForLanguageSet(existingTitle, languageSet);
    }

    @Test
    public void shouldCreateUnitWithDifferentTitleInLanguageSet() throws DataAlreadyExistsException {
        final LanguageSet languageSet = vocabularyLib.getAllLanguageSets().get(0);
        final int initialNrOfUnits = languageSet.getVocableUnits().size();
        final String newTitle = "Some new title";
        final int statusCode = vocabularyLib.createUnitForLanguageSet(newTitle, languageSet);

        Assert.assertEquals(0, statusCode);

        final List<VocableUnit> refreshedUnits = vocabularyLib.getAllLanguageSets().get(0).getVocableUnits();
        Assert.assertEquals(initialNrOfUnits + 1, refreshedUnits.size());
        Assert.assertTrue(refreshedUnits.stream().anyMatch(u -> u.getTitle().equals(newTitle)));
    }

    @Test(expected = DataAlreadyExistsException.class)
    public void shouldNotInertVocableListWithExistingTitle() throws DataAlreadyExistsException, DuplicateVocablesInSetException, IncompleteVocableListException {
        final VocableUnit unit = vocabularyLib.getAllLanguageSets().get(0).getVocableUnits().get(0);
        final VocableList vocableList = new VocableList();
        final String existingTitle = unit.getVocableLists().get(0).getTitle();
        vocableList.setTitle(existingTitle);
        vocabularyLib.insertVocableListInUnit(vocableList, unit);
    }

    // TODO: Following two tests to be added for each potentially null/empty trimmed field?

    @Test(expected = IncompleteVocableListException.class)
    public void shouldNotInsertIncompleteVocableData() throws DataAlreadyExistsException, DuplicateVocablesInSetException, IncompleteVocableListException {
        final TranslationGroup unknownWord = new TranslationGroup();
        unknownWord.setSynonyms(Stream.of("I'm untranslated").collect(Collectors.toList()));

        final Vocable untranslatedVocable = new Vocable();
        untranslatedVocable.setVocable(unknownWord);

        final VocableList vocableList = new VocableList();
        vocableList.setTitle("Some new title");
        vocableList.setVocables(Stream.of(untranslatedVocable).collect(Collectors.toList()));

        final VocableUnit unit = vocabularyLib.getAllLanguageSets().get(0).getVocableUnits().get(0);
        vocabularyLib.insertVocableListInUnit(vocableList, unit);
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

        final VocableList vocableList = new VocableList();
        vocableList.setTitle("Some new title");
        vocableList.setVocables(Stream.of(untranslatedVocable).collect(Collectors.toList()));

        final VocableUnit unit = vocabularyLib.getAllLanguageSets().get(0).getVocableUnits().get(0);
        vocabularyLib.insertVocableListInUnit(vocableList, unit);
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

        final VocableList vocableList = new VocableList();
        vocableList.setTitle("Some new title");
        vocableList.setVocables(Stream.of(vocable1, vocable2).collect(Collectors.toList()));

        final VocableUnit unit = vocabularyLib.getAllLanguageSets().get(0).getVocableUnits().get(0);
        vocabularyLib.insertVocableListInUnit(vocableList, unit);
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

        final VocableList vocableList = new VocableList();
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

        final int initialListsLength = vocabularyLib.getAllLanguageSets().get(0).getVocableUnits().get(0).getVocableLists().size();
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

        final int initialUnitsLength = vocabularyLib.getAllLanguageSets().get(0).getVocableUnits().size();
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

    // TODO: Continue testing update(...) and following functions

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
