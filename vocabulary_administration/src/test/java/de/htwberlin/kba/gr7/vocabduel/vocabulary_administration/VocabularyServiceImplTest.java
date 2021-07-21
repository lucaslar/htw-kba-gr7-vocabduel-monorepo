package de.htwberlin.kba.gr7.vocabduel.vocabulary_administration;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.exceptions.*;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.*;
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
            return new Vocable(es, Collections.singletonList(en), null, null);
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
    private List<LanguageSet> languages;

    @Mock
    private EntityManagerFactory emf;
    @Mock
    private EntityManager entityManager;
    @Mock
    private EntityTransaction entityTransaction;
    @Mock
    private Query queryMock;

    @Before
    public void setup() {
        vocabularyLib = new VocabularyServiceImpl(entityManager);
        Mockito.when(entityManager.getTransaction()).thenReturn(entityTransaction);
        Mockito.when(entityManager.createQuery(Mockito.anyString())).thenReturn(queryMock);
        Mockito.when(queryMock.setParameter(Mockito.anyString(), Mockito.any())).thenReturn(queryMock);
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

        existingVocableUnit = new VocableUnit(123L);
        existingVocableUnit.setTitle("ES => EN - Vocabduel I");
        existingVocableUnit.setVocableLists(Stream.of(existingVocableList1, existingVocableList2).collect(Collectors.toList()));

        emptyVocableUnit = new VocableUnit(456L);
        emptyVocableUnit.setTitle("This unit is empty");

        existingLanguageSet = new LanguageSet();
        existingLanguageSet.setKnownLanguage(SupportedLanguage.EN);
        existingLanguageSet.setLearntLanguage(SupportedLanguage.ES);
        existingLanguageSet.setVocableUnits(Stream.of(existingVocableUnit, emptyVocableUnit).collect(Collectors.toList()));

        emptyLanguagesSet = new LanguageSet();
        emptyLanguagesSet.setKnownLanguage(SupportedLanguage.AR);
        emptyLanguagesSet.setLearntLanguage(SupportedLanguage.JA);

        languages = Stream.of(existingLanguageSet, emptyLanguagesSet).collect(Collectors.toList());
    }

    @Test(expected = DuplicateVocablesInSetException.class)
    public void shouldNotImportGnuListWithDuplicateVocables() throws DataAlreadyExistsException, DuplicateVocablesInSetException, IncompleteVocableListException, FileNotFoundException, UnknownLanguagesException, InvalidVocableListException {
        final String pathname = "./src/test/assets/gnu_duplicate_vocabulary.txt";
        vocabularyLib.importGnuVocableList(fromFile(pathname), new User(42L));
    }

    @Test(expected = DataAlreadyExistsException.class)
    public void shouldNotImportGnuListWithExistingTitleInUnit() throws DataAlreadyExistsException, DuplicateVocablesInSetException, IncompleteVocableListException, FileNotFoundException, UnknownLanguagesException, InvalidVocableListException {
        Mockito.when(queryMock.getSingleResult()).thenReturn(existingLanguageSet);
        final String pathname = "./src/test/assets/gnu_title_already_exists.txt";
        vocabularyLib.importGnuVocableList(fromFile(pathname), new User(42L));
    }

    @Test(expected = IncompleteVocableListException.class)
    public void shouldNotImportGnuListWithIncompleteData() throws DataAlreadyExistsException, DuplicateVocablesInSetException, IncompleteVocableListException, FileNotFoundException, UnknownLanguagesException, InvalidVocableListException {
        final String pathname = "./src/test/assets/gnu_incomplete_list.txt";
        vocabularyLib.importGnuVocableList(fromFile(pathname), new User(42L));
    }

    @Test(expected = InvalidVocableListException.class)
    public void shouldNotImportGnuListWithInvalidFormat() throws DataAlreadyExistsException, DuplicateVocablesInSetException, IncompleteVocableListException, FileNotFoundException, UnknownLanguagesException, InvalidVocableListException {
        final String pathname = "./src/test/assets/gnu_invalid_format.txt";
        vocabularyLib.importGnuVocableList(fromFile(pathname), new User(42L));
    }

    @Test(expected = IncompleteVocableListException.class)
    public void shouldNotImportGnuListWithInvalidHeadline() throws DataAlreadyExistsException, DuplicateVocablesInSetException, IncompleteVocableListException, FileNotFoundException, UnknownLanguagesException, InvalidVocableListException {
        final String pathname = "./src/test/assets/gnu_invalid_headline.txt";
        vocabularyLib.importGnuVocableList(fromFile(pathname), new User(42L));
    }

    @Test(expected = InvalidVocableListException.class)
    public void shouldNotImportGnuListWithInvalidVocable() throws DataAlreadyExistsException, DuplicateVocablesInSetException, IncompleteVocableListException, FileNotFoundException, UnknownLanguagesException, InvalidVocableListException {
        final String pathname = "./src/test/assets/gnu_invalid_translation.txt";
        vocabularyLib.importGnuVocableList(fromFile(pathname), new User(42L));
    }

    @Test(expected = InvalidVocableListException.class)
    public void shouldNotImportGnuListWithInvalidTranslation() throws DataAlreadyExistsException, DuplicateVocablesInSetException, IncompleteVocableListException, FileNotFoundException, UnknownLanguagesException, InvalidVocableListException {
        final String pathname = "./src/test/assets/gnu_invalid_vocable.txt";
        vocabularyLib.importGnuVocableList(fromFile(pathname), new User(42L));
    }

    @Test(expected = UnknownLanguagesException.class)
    public void shouldNotImportGnuListWithUnknownLanguage() throws DataAlreadyExistsException, DuplicateVocablesInSetException, IncompleteVocableListException, FileNotFoundException, UnknownLanguagesException, InvalidVocableListException {
        final String pathname = "./src/test/assets/gnu_unknown_langs.txt";
        vocabularyLib.importGnuVocableList(fromFile(pathname), new User(42L));
    }

    @Test
    public void shouldImportGnuFile() throws DataAlreadyExistsException, DuplicateVocablesInSetException, IncompleteVocableListException, FileNotFoundException, UnknownLanguagesException, InvalidVocableListException {
        Mockito.when(queryMock.getSingleResult()).thenThrow(NoResultException.class);
        final String pathname = "./src/test/assets/gnu_valid_format.txt";
        final int initialListsLength = existingVocableUnit.getVocableLists().size();
        Assert.assertNotNull(vocabularyLib.importGnuVocableList(fromFile(pathname), new User(42L)));

        final String expectedTitle = "GNU Unit title";
        existingVocableList1.setTitle(expectedTitle);
        languages.get(0).getVocableUnits().get(0).getVocableLists().add(existingVocableList1);
        Mockito.when(queryMock.getResultList()).thenReturn(languages);
        final List<VocableList> refreshedLists = vocabularyLib.getAllLanguageSets().get(0).getVocableUnits().get(0).getVocableLists();
        Assert.assertEquals(initialListsLength + 1, refreshedLists.size());
        final String infoMsg = "Please make sure, your gnu file's vocabulary list title is \"" + expectedTitle + "\"";
        Assert.assertTrue(infoMsg, refreshedLists.stream().anyMatch(vl -> vl.getTitle().equals(expectedTitle)));
    }

    @Test
    public void shouldCreateNewUnitIfRequiredByGnuFile() throws DataAlreadyExistsException, DuplicateVocablesInSetException, IncompleteVocableListException, FileNotFoundException, UnknownLanguagesException, InvalidVocableListException {
        Mockito.when(queryMock.getSingleResult()).thenReturn(existingLanguageSet);
        final String pathname = "./src/test/assets/gnu_valid_format_in_new_unit.txt";
        final int initialUnitsLength = existingLanguageSet.getVocableUnits().size();
        Assert.assertNotNull(vocabularyLib.importGnuVocableList(fromFile(pathname), new User(42L)));

        final String expectedUnitTitle = "GNU Unit title";
        existingVocableUnit.setTitle(expectedUnitTitle);
        Mockito.when(queryMock.getResultList()).thenReturn(languages);
        final List<VocableUnit> refreshedUnits = vocabularyLib.getAllLanguageSets().get(0).getVocableUnits();
        Assert.assertEquals(initialUnitsLength + 1, refreshedUnits.size());
        final String infoMsg = "Please make sure, your gnu file's unit title is \"" + expectedUnitTitle + "\"";
        Assert.assertTrue(infoMsg, refreshedUnits.stream().anyMatch(u -> u.getTitle().equals(expectedUnitTitle)));
        // See previous test for checking if insertion itself works
    }

    @Test
    public void shouldCreateNewLanguageSetIfRequiredByGnuFile() throws DataAlreadyExistsException, DuplicateVocablesInSetException, IncompleteVocableListException, FileNotFoundException, UnknownLanguagesException, InvalidVocableListException {
        Mockito.when(queryMock.getSingleResult()).thenThrow(NoResultException.class);
        final String pathname = "./src/test/assets/gnu_valid_format_in_new_language_set.txt";
        final int initialLanguageSetLength = vocabularyLib.getAllLanguageSets().size();
        Assert.assertNotNull(vocabularyLib.importGnuVocableList(fromFile(pathname), new User(42L)));

        Mockito.when(queryMock.getResultList()).thenReturn(Stream.of(existingLanguageSet).collect(Collectors.toList()));
        final List<LanguageSet> refreshedLanguageSets = vocabularyLib.getAllLanguageSets();
        Assert.assertEquals(initialLanguageSetLength + 1, refreshedLanguageSets.size());
        // See previous test for checking if insertion of units works
    }

    @Test(expected = DifferentAuthorException.class)
    public void shouldNotDeleteVocableListIfNotTriggeredByAuthor() throws DifferentAuthorException, PersistenceException {
        vocabularyLib.deleteVocableList(existingVocableList1, new User(4711L));
    }

    @Test(expected = PersistenceException.class)
    public void shouldThrowPersistenceExceptionIfErrorOccurred() throws DifferentAuthorException, PersistenceException {
        Mockito.when(queryMock.getSingleResult()).thenReturn(existingVocableUnit);
        Mockito.doThrow(new PersistenceException()).when(entityManager).remove(Mockito.any());
        vocabularyLib.deleteVocableList(existingVocableList1, author);
    }

    @Test
    public void shouldDeleteVocableListIfTriggeredByAuthor() throws DifferentAuthorException {
        final int initialSize = existingVocableUnit.getVocableLists().size();
        Mockito.when(queryMock.getSingleResult()).thenReturn(existingVocableUnit);
        final int statusCode = vocabularyLib.deleteVocableList(existingVocableList1, author);
        Assert.assertEquals(initialSize - 1, existingVocableUnit.getVocableLists().size());
        Assert.assertEquals(0, statusCode);
    }

    @Test
    public void shouldDeleteEmptyUnitList() throws DifferentAuthorException {
        final int initialSize = existingLanguageSet.getVocableUnits().size();
        existingVocableUnit.setVocableLists(existingVocableUnit.getVocableLists().stream().filter(l -> l == existingVocableList1).collect(Collectors.toList()));
        Mockito.when(queryMock.getSingleResult()).thenReturn(existingVocableUnit, existingLanguageSet);
        final int statusCode = vocabularyLib.deleteVocableList(existingVocableList1, author);
        Assert.assertEquals(initialSize - 1, existingLanguageSet.getVocableUnits().size());
        Assert.assertEquals(0, statusCode);
    }

    @Test
    public void shouldFindVocableListByExistingId() {
        Mockito.when(entityManager.find(Mockito.eq(VocableList.class), Mockito.eq(existingVocableList2.getId()))).thenReturn(existingVocableList2);
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
        Mockito.when(queryMock.getResultList()).thenReturn(existingVocableUnit.getVocableLists());
        final List<VocableList> foundLists = vocabularyLib.getVocableListsOfUser(author);
        Assert.assertNotNull(foundLists);
        Assert.assertEquals(2, foundLists.size());
        foundLists.forEach(fl -> Assert.assertEquals(fl.getAuthor().toString(), author.toString()));
    }

    @Test
    public void shouldReturnNullIfUserHasNotImportedAnyList() {
        Mockito.when(queryMock.getResultList()).thenThrow(NoResultException.class);
        final List<VocableList> foundLists = vocabularyLib.getVocableListsOfUser(new User(4711L));
        Assert.assertNull(foundLists);
    }

    @Test
    public void shouldReturnAllLanguageSets() {
        Mockito.when(queryMock.getResultList()).thenReturn(languages);
        final List<LanguageSet> languageSets = vocabularyLib.getAllLanguageSets();
        Assert.assertNotNull(languageSets);
        Assert.assertEquals(2, languageSets.size());
    }

    @Test
    public void shouldReturnNullIfNoLanguageSetsYet() {
        Mockito.when(queryMock.getResultList()).thenThrow(NoResultException.class);
        final List<LanguageSet> languageSets = vocabularyLib.getAllLanguageSets();
        Assert.assertNull(languageSets);
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

    @Test
    public void shouldHaveAtLeastThreeReferencesForEachSupportedLanguage() {
        vocabularyLib.getAllSupportedLanguages().forEach(
                sl -> {
                    final List<String> references = vocabularyLib.getSupportedLanguageReferences(sl);
                    Assert.assertNotNull(references);
                    Assert.assertTrue(references.size() >= 3);
                }
        );
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
