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
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(MockitoJUnitRunner.class)
public class VocabularyServiceImplTest {

    private static List<Vocable> mockVocablesEsEn(final String[][] vocables) {
        return Stream.of(vocables).map(words -> {
            final TranslationGroup en = new TranslationGroup();
            final TranslationGroup es = new TranslationGroup();
            es.setSynonyms(Stream.of(words[0]).collect(Collectors.toList()));
            en.setSynonyms(Stream.of(words[1]).collect(Collectors.toList()));
            return new Vocable(es, Collections.singletonList(en));
        }).collect(Collectors.toList());
    }

    private VocabularyServiceImpl vocabularyLib;
    private LanguageSet existingLanguageSet;
    private LanguageSet emptyLanguagesSet;
    private VocableUnit existingVocableUnit;
    private VocableUnit emptyVocableUnit;
    private VocableList existingVocableList1;
    private VocableList existingVocableList2;
    private User author;

    @Before
    public void setup() {
        vocabularyLib = new VocabularyServiceImpl();
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

    @Test(expected = DuplicateVocablesInSetException.class)
    public void shouldNotImportGnuListWithDuplicateVocables() throws DataAlreadyExistsException, DuplicateVocablesInSetException, IncompleteVocableListException, FileNotFoundException {
        final String pathname = "./src/test/assets/gnu_duplicate_vocabulary.txt";
        vocabularyLib.importGnuVocableList(fromFile(pathname), new User(42L));
    }

    @Test(expected = DataAlreadyExistsException.class)
    public void shouldNotImportGnuListWithExistingTitleInUnit() throws DataAlreadyExistsException, DuplicateVocablesInSetException, IncompleteVocableListException, FileNotFoundException {
        final String pathname = "./src/test/assets/gnu_title_already_exists.txt";
        vocabularyLib.importGnuVocableList(fromFile(pathname), new User(42L));
    }

    @Test(expected = IncompleteVocableListException.class)
    public void shouldNotImportGnuListWithIncompleteData() throws DataAlreadyExistsException, DuplicateVocablesInSetException, IncompleteVocableListException, FileNotFoundException {
        final String pathname = "./src/test/assets/gnu_incomplete_list.txt";
        vocabularyLib.importGnuVocableList(fromFile(pathname), new User(42L));
    }

    @Test
    public void shouldImportGnuFile() throws DataAlreadyExistsException, DuplicateVocablesInSetException, IncompleteVocableListException, FileNotFoundException {
        final String pathname = "./src/test/assets/gnu_valid_format.txt";
        final int initialListsLength = existingVocableUnit.getVocableLists().size();
        final int statusCode = vocabularyLib.importGnuVocableList(fromFile(pathname), new User(42L));
        Assert.assertEquals(0, statusCode);

        final String expectedTitle = "GNU Unit title";
        final List<VocableList> refreshedLists = vocabularyLib.getAllLanguageSets().get(0).getVocableUnits().get(0).getVocableLists();
        Assert.assertEquals(initialListsLength + 1, refreshedLists.size());
        final String infoMsg = "Please make sure, your gnu file's vocabulary list title is \"" + expectedTitle + "\"";
        Assert.assertTrue(infoMsg, refreshedLists.stream().anyMatch(vl -> vl.getTitle().equals(expectedTitle)));
    }

    @Test
    public void shouldCreateNewUnitIfRequiredByGnuFile() throws DataAlreadyExistsException, DuplicateVocablesInSetException, IncompleteVocableListException, FileNotFoundException {
        final String pathname = "./src/test/assets/gnu_valid_format_in_new_unit.txt";
        final int initialUnitsLength = existingLanguageSet.getVocableUnits().size();
        final int statusCode = vocabularyLib.importGnuVocableList(fromFile(pathname), new User(42L));
        Assert.assertEquals(0, statusCode);

        final String expectedUnitTitle = "GNU Unit title";
        final List<VocableUnit> refreshedUnits = vocabularyLib.getAllLanguageSets().get(0).getVocableUnits();
        Assert.assertEquals(initialUnitsLength + 1, refreshedUnits.size());
        final String infoMsg = "Please make sure, your gnu file's unit title is \"" + expectedUnitTitle + "\"";
        Assert.assertTrue(infoMsg, refreshedUnits.stream().anyMatch(u -> u.getTitle().equals(expectedUnitTitle)));
        // See previous test for checking if insertion itself works
    }

    @Test
    public void shouldCreateNewLanguageSetIfRequiredByGnuFile() throws DataAlreadyExistsException, DuplicateVocablesInSetException, IncompleteVocableListException, FileNotFoundException {
        final String pathname = "./src/test/assets/gnu_valid_format_in_new_language_set.txt";
        final int initialLanguageSetLength = vocabularyLib.getAllLanguageSets().size();
        final int statusCode = vocabularyLib.importGnuVocableList(fromFile(pathname), new User(42L));
        Assert.assertEquals(0, statusCode);

        final List<LanguageSet> refreshedLanguageSets = vocabularyLib.getAllLanguageSets();
        Assert.assertEquals(initialLanguageSetLength + 1, refreshedLanguageSets.size());
        // See previous test for checking if insertion of units works
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

    // TODO test "references"

    @Test
    public void shouldOnlyListUniqueLanguagesAsSupported() {
        final List<SupportedLanguage> supportedLanguages = vocabularyLib.getAllSupportedLanguages();
        Assert.assertNotNull(supportedLanguages);
        List<SupportedLanguage> uniques = supportedLanguages.stream().distinct().collect(Collectors.toList());
        Assert.assertEquals(uniques.size(), supportedLanguages.size());
    }

    private String fromFile(final String pathname) throws FileNotFoundException {
        Scanner input = new Scanner(new File(pathname));
        StringBuilder s = new StringBuilder();
        while (input.hasNext()) {
            s.append(input.nextLine());
            s.append("\n");
        }
        input.close();
        return s.toString();
    }
}
