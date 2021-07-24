package de.htwberlin.kba.gr7.vocabduel.game_administration;

import de.htwberlin.kba.gr7.vocabduel.game_administration.assets.GameDataMock;
import de.htwberlin.kba.gr7.vocabduel.game_administration.dao.*;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.*;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.RunningVocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.VocabduelRound;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.UserService;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.InternalUserModuleException;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.InvalidUserException;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.VocabularyService;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(MockitoJUnitRunner.class)
public class GameServiceImplStartGameTest {

    private GameServiceImpl gameAdministration;
    private RunningVocabduelGame newGame;
    private RunningVocabduelGame newGameRes;
    private GameDataMock mock;
    @Mock
    private UserService userService;
    @Mock
    private EntityManager entityManager;
    @Mock
    private VocabularyService vocabularyService;

    @Before
    public void setup() throws InternalUserModuleException {

        final RunningVocabduelGameDAOImpl runningVocabduelGameDAO = new RunningVocabduelGameDAOImpl();
        runningVocabduelGameDAO.setEntityManager(entityManager);
        final VocabduelRoundDAOImpl vocabduelRoundDAO = new VocabduelRoundDAOImpl();
        vocabduelRoundDAO.setEntityManager(entityManager);
        final FinishedVocabduelGameDAOImpl finishedVocabduelGameDAO = new FinishedVocabduelGameDAOImpl();
        finishedVocabduelGameDAO.setEntityManager(entityManager);

        gameAdministration = new GameServiceImpl(userService, vocabularyService, runningVocabduelGameDAO, vocabduelRoundDAO, finishedVocabduelGameDAO);

        mock = new GameDataMock();
        newGame = mock.mockVocabduelGame();

        List<SupportedLanguage> langList = new ArrayList<>();
        langList.add(mock.mockKnownLanguage());
        langList.add(mock.mockLearntLanguage());
        List<LanguageSet> langset = new ArrayList<>();
        VocableUnit unit = new VocableUnit("new Title");
        unit.setVocableLists(mock.mockVocableLists());
        LanguageSet mySet = new LanguageSet(mock.mockLearntLanguage(), mock.mockKnownLanguage());
        mySet.setVocableUnits(Stream.of(unit).collect(Collectors.toList()));
        langset.add(mySet);

        Mockito.when(userService.getUserDataById(Mockito.anyLong())).thenReturn(mock.mockSampleUser());
        Mockito.when(userService.getUserDataById(12L)).thenReturn(mock.mockOpponent());
        Mockito.when(vocabularyService.getAllLanguageSets()).thenReturn(langset);
    }

    @Test()
    public void shouldGetStartedGameAsInput() throws NotEnoughVocabularyException, InvalidGameSetupException, InvalidUserException, InternalUserModuleException {
        newGameRes = gameAdministration.startGame(
                mock.mockSampleUser(),
                mock.mockOpponent(),
                mock.mockVocableLists()
        );
        Assert.assertNotNull(newGameRes);

        Assert.assertEquals(newGameRes.getKnownLanguage(), mock.mockKnownLanguage());
        Assert.assertEquals(newGameRes.getLearntLanguage(), mock.mockLearntLanguage());
        Assert.assertEquals(newGameRes.getPlayerA(), mock.mockSampleUser());
        Assert.assertEquals(newGameRes.getPlayerB(), mock.mockOpponent());
        Assert.assertEquals(newGameRes.getVocableLists(), mock.mockVocableLists());
        Assert.assertNotNull(newGame.getId());
    }

    @Test()
    public void shouldNewGameWithFixedRoundList() throws NotEnoughVocabularyException, InvalidGameSetupException, InvalidUserException, InternalUserModuleException {

        newGameRes = gameAdministration.startGame(
                mock.mockSampleUser(),
                mock.mockOpponent(),
                mock.mockVocableLists()
        );
        Assert.assertNotNull(newGameRes);
        Assert.assertEquals(newGameRes.getRounds().size(), GameServiceImpl.getFixNumberOfRoundsPerGame());
    }

    @Test()
    public void shouldNewGameWithEnoughVocablesInMultipleLists() throws NotEnoughVocabularyException, InvalidGameSetupException, InvalidUserException, InternalUserModuleException {

        newGameRes = gameAdministration.startGame(
                mock.mockSampleUser(),
                mock.mockOpponent(),
                mock.mockMultipleVocableListsWithEnoughVocabulary()
        );
        Assert.assertNotNull(newGameRes);
        Assert.assertEquals(newGameRes.getRounds().size(), GameServiceImpl.getFixNumberOfRoundsPerGame());
    }

    @Test()
    public void shouldStartGameWithUniqueRounds() throws NotEnoughVocabularyException, InvalidGameSetupException, InvalidUserException, InternalUserModuleException {
        newGameRes = gameAdministration.startGame(
                mock.mockSampleUser(),
                mock.mockOpponent(),
                mock.mockVocableLists()
        );
        Assert.assertNotNull(newGameRes);
        List<VocabduelRound> uniques = newGameRes.getRounds().stream().distinct().collect(Collectors.toList());
        Assert.assertEquals(uniques.size(), newGameRes.getRounds().size());
    }

    @Test
    public void shouldStartGameWithUniqueQuestions() throws NotEnoughVocabularyException, InvalidGameSetupException, InvalidUserException, InternalUserModuleException {
        newGameRes = gameAdministration.startGame(
                mock.mockSampleUser(),
                mock.mockOpponent(),
                mock.mockVocableLists()
        );
        Assert.assertNotNull(newGameRes);
        newGameRes.getRounds().stream().map(r -> r.getQuestion().getVocable())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .forEach((key, value) -> Assert.assertEquals(1, value.intValue()));
    }

    @Test()
    public void shouldStartGameWithCorrectRoundIds() throws NotEnoughVocabularyException, InvalidGameSetupException, InvalidUserException, InternalUserModuleException {
        newGameRes = gameAdministration.startGame(
                mock.mockSampleUser(),
                mock.mockOpponent(),
                mock.mockVocableLists()
        );
        Assert.assertNotNull(newGameRes);
        newGameRes.getRounds().sort(compareById);

        VocabduelRound[] rounds = new VocabduelRound[newGameRes.getRounds().size()];
        newGameRes.getRounds().toArray(rounds);

        for (int i = 1; i <= newGameRes.getRounds().size(); i++) {
            Assert.assertNotNull(newGameRes.getRounds().get(i - 1));
            Assert.assertEquals(i, newGameRes.getRounds().get(i - 1).getRoundNr());
        }
    }

    @Test(expected = InvalidGameSetupException.class)
    public void shouldNotStartGameInSinglePlayerMode() throws NotEnoughVocabularyException, InvalidGameSetupException, InvalidUserException, InternalUserModuleException {
        newGameRes = gameAdministration.startGame(
                mock.mockSampleUser(),
                mock.mockSampleUser(),
                mock.mockVocableLists()
        );
    }

    @Test(expected = NotEnoughVocabularyException.class)
    public void shouldNotStartGameWithoutVocabulary() throws NotEnoughVocabularyException, InvalidGameSetupException, InvalidUserException, InternalUserModuleException {
        newGameRes = gameAdministration.startGame(
                mock.mockSampleUser(),
                mock.mockOpponent(),
                mock.mockVocableListsWithEmptyVocabulary()
        );
    }

    @Test(expected = NotEnoughVocabularyException.class)
    public void shouldNotStartGameWithoutEnoughVocabulary() throws NotEnoughVocabularyException, InvalidGameSetupException, InvalidUserException, InternalUserModuleException {
        newGameRes = gameAdministration.startGame(
                mock.mockSampleUser(),
                mock.mockOpponent(),
                mock.mockVocableListsWithoutEnoughVocabulary()
        );
    }

    @Test(expected = NotEnoughVocabularyException.class)
    public void shouldNotStartGameWithoutEnoughVocabularyInMultipleVocableLists() throws NotEnoughVocabularyException, InvalidGameSetupException, InvalidUserException, InternalUserModuleException {
        newGameRes = gameAdministration.startGame(
                mock.mockSampleUser(),
                mock.mockOpponent(),
                mock.mockMultipleVocableListsWithoutEnoughVocabulary()
        );
    }

    @Test(expected = NotEnoughVocabularyException.class)
    public void shouldNotStartGameWithEmptyVocableLists() throws NotEnoughVocabularyException, InvalidGameSetupException, InvalidUserException, InternalUserModuleException {
        newGameRes = gameAdministration.startGame(
                mock.mockSampleUser(),
                mock.mockOpponent(),
                mock.mockEmptyVocableLists()
        );
    }

    @Test(expected = InvalidGameSetupException.class)
    public void shouldNotStartGameWithNullVocabList() throws NotEnoughVocabularyException, InvalidGameSetupException, InvalidUserException, InternalUserModuleException {
        gameAdministration.startGame(
                mock.mockSampleUser(),
                mock.mockOpponent(),
                Arrays.stream(new VocableList[]{null}).collect(Collectors.toList())
        );
    }

    @Test(expected = InvalidUserException.class)
    public void shouldNotStartGameIfPlayerOneIsUnknown() throws NotEnoughVocabularyException, InvalidGameSetupException, InvalidUserException, InternalUserModuleException {
        Mockito.when(userService.getUserDataById(Mockito.anyLong())).thenReturn(null);
        gameAdministration.startGame(
                mock.mockSampleUser(),
                mock.mockOpponent(),
                mock.mockVocableLists()
        );
    }

    @Test(expected = InvalidUserException.class)
    public void shouldNotStartGameIfPlayerTwoIsUnknown() throws NotEnoughVocabularyException, InvalidGameSetupException, InvalidUserException, InternalUserModuleException {
        Mockito.when(userService.getUserDataById(12L)).thenReturn(null);
        gameAdministration.startGame(
                mock.mockSampleUser(),
                mock.mockOpponent(),
                mock.mockVocableLists()
        );
    }

    @Test(expected = InvalidGameSetupException.class)
    public void shouldNotStartGameIfVocableListsAreOfDifferentLanguageSet() throws NotEnoughVocabularyException, InvalidGameSetupException, InvalidUserException, InternalUserModuleException {
        final VocableList list1 = new VocableList(123L);
        final VocableList list2 = new VocableList(456L);

        list1.setVocables(mock.mockVocableLists().get(0).getVocables());
        list2.setVocables(mock.mockVocableLists().get(0).getVocables());

        final List<LanguageSet> langset = new ArrayList<>();
        final VocableUnit unit = new VocableUnit("Unit 1");
        final VocableUnit unit2 = new VocableUnit("Unidad 2");
        unit.setVocableLists(Arrays.stream(new VocableList[]{list1}).collect(Collectors.toList()));
        unit2.setVocableLists(Arrays.stream(new VocableList[]{list2}).collect(Collectors.toList()));
        final LanguageSet langSet1 = new LanguageSet(mock.mockLearntLanguage(), mock.mockKnownLanguage());
        final LanguageSet langSet2 = new LanguageSet(mock.mockKnownLanguage(), mock.mockLearntLanguage());
        langSet1.setVocableUnits(Stream.of(unit).collect(Collectors.toList()));
        langSet2.setVocableUnits(Stream.of(unit2).collect(Collectors.toList()));
        langset.add(langSet1);
        langset.add(langSet2);

        Mockito.when(userService.getUserDataById(Mockito.anyLong())).thenReturn(mock.mockSampleUser());
        Mockito.when(userService.getUserDataById(12L)).thenReturn(mock.mockOpponent());
        Mockito.when(vocabularyService.getAllLanguageSets()).thenReturn(langset);

        gameAdministration.startGame(
                mock.mockSampleUser(),
                mock.mockOpponent(),
                Arrays.asList(list1, list2)
        );
    }

    @Test(expected = NotEnoughVocabularyException.class)
    public void shouldNotStartGameWithoutEnoughUniqueVocabulary() throws NotEnoughVocabularyException, InvalidGameSetupException, InvalidUserException, InternalUserModuleException {
        final VocableList listWithoutEnoughUniques = new VocableList(42L);

        final List<Vocable> vocablesSharingTranslation = Arrays
                .stream("The quick brown fox jumps over the lazy dog".split(" "))
                .map(t -> {
                    final TranslationGroup vTg = new TranslationGroup(Collections.singletonList(t + " vocab"));
                    final TranslationGroup tTg = new TranslationGroup(Collections.singletonList(t));
                    return new Vocable(vTg, Arrays.asList(tTg, new TranslationGroup(Collections.singletonList("I'm everywhere"))), null, null);
                })
                .collect(Collectors.toList());

        listWithoutEnoughUniques.setVocables(vocablesSharingTranslation);

        gameAdministration.startGame(
                mock.mockSampleUser(),
                mock.mockOpponent(),
                Collections.singletonList(listWithoutEnoughUniques)
        );
    }

    Comparator<VocabduelRound> compareById = Comparator.comparingInt(VocabduelRound::getRoundNr);

}
