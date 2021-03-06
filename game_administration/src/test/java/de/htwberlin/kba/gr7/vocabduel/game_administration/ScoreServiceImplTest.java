package de.htwberlin.kba.gr7.vocabduel.game_administration;

import de.htwberlin.kba.gr7.vocabduel.game_administration.dao.*;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.GameOptimisticLockException;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.UnfinishedGameException;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.NoAccessException;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.*;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.UserService;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.UserOptimisticLockException;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.InvalidUserException;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(MockitoJUnitRunner.class)
public class ScoreServiceImplTest {

    private static long gameId = 123L;

    private static FinishedVocabduelGame mockFinishedGame(User playerA, int pointsA, User playerB, int pointsB) {
        final FinishedVocabduelGame finishedGame = new FinishedVocabduelGame(
                new RunningVocabduelGame(gameId++, null, null, null, null, null)
        );
        finishedGame.setPlayerA(playerA);
        finishedGame.setPlayerB(playerB);
        finishedGame.setTotalPointsA(pointsA);
        finishedGame.setTotalPointsB(pointsB);
        return finishedGame;
    }

    private static VocabduelRound mockedFinishedRound() {
        final VocabduelRound finishedVocabduelRound = new VocabduelRound();
        finishedVocabduelRound.setResultPlayerA(Result.WIN);
        finishedVocabduelRound.setResultPlayerB(Result.LOSS);
        return finishedVocabduelRound;
    }

    private User playerA; // 0 wins, 2 losses, 1 against B, 1 against C
    private User playerB;  // 1 win against A, 1 loss against C
    private User playerC; // 2 wins, 1 against A, 1 against B
    private User playerD; // 0 - 0
    private User playerE; // 2 Draw against F
    private List<FinishedVocabduelGame> finishedGames;

    private ScoreServiceImpl scoreAdministration;
    @Mock
    private UserService userService;
    @Mock
    private EntityManager entityManager;
    @Mock
    private Query queryMock;

    @Before
    public void setup() {

        final RunningVocabduelGameDAOImpl runningVocabduelGameDAO = new RunningVocabduelGameDAOImpl();
        runningVocabduelGameDAO.setEntityManager(entityManager);
        final FinishedVocabduelGameDAOImpl finishedVocabduelGameDAO = new FinishedVocabduelGameDAOImpl();
        finishedVocabduelGameDAO.setEntityManager(entityManager);

        scoreAdministration = new ScoreServiceImpl(userService, finishedVocabduelGameDAO, runningVocabduelGameDAO);

        playerA = new User(4711L);
        playerB = new User(42L);
        playerC = new User(2020L);
        playerD = new User(2021L);
        playerE = new User(900L);
        final User playerF = new User(901L);

        final FinishedVocabduelGame game1 = mockFinishedGame(playerA, 0, playerB, 3);
        final FinishedVocabduelGame game2 = mockFinishedGame(playerA, 1, playerC, 2);
        final FinishedVocabduelGame game3 = mockFinishedGame(playerB, 0, playerC, 3);
        final FinishedVocabduelGame game4 = mockFinishedGame(playerE, 2, playerF, 2);
        final FinishedVocabduelGame game5 = mockFinishedGame(playerF, 1, playerE, 1);
        finishedGames = Stream.of(game1, game2, game3, game4, game5).collect(Collectors.toList());

        Mockito.when(entityManager.createQuery(Mockito.anyString())).thenReturn(queryMock);
        Mockito.when(queryMock.setParameter(Mockito.anyString(), Mockito.any())).thenReturn(queryMock);
    }

    @Test(expected = InvalidUserException.class)
    public void shouldThrowExceptionIfUserInvalid() throws InvalidUserException, UserOptimisticLockException, GameOptimisticLockException {
        Mockito.when(userService.getUserDataById(Mockito.anyLong())).thenReturn(null);
        scoreAdministration.getPersonalFinishedGames(playerA);
    }

    @Test
    public void shouldNotReturnPersonalFinishedGamesForUserWithoutGames() throws InvalidUserException, UserOptimisticLockException, GameOptimisticLockException {
        Mockito.when(queryMock.getResultList()).thenThrow(NoResultException.class);
        Mockito.when(userService.getUserDataById(Mockito.anyLong())).thenReturn(playerD);
        final List<PersonalFinishedGame> games = scoreAdministration.getPersonalFinishedGames(playerD);
        Assert.assertNull(games);
    }

    @Test
    public void shouldReturnCorrectPersonalFinishedGamesIfOnlyLost() throws InvalidUserException, UserOptimisticLockException, GameOptimisticLockException {
        final User player = playerA;
        Mockito.when(queryMock.getResultList()).thenReturn(finishedGames.stream().filter(g ->
                g.getPlayerB().getId().equals(player.getId()) ||
                        g.getPlayerA().getId().equals(player.getId())).collect(Collectors.toList()));
        Mockito.when(userService.getUserDataById(Mockito.anyLong())).thenReturn(player);
        final List<PersonalFinishedGame> games = scoreAdministration.getPersonalFinishedGames(player);
        sharedPersonalFinishedGameLogic(games, player);

        games.forEach(g -> {
            Assert.assertTrue(g.getOwnPoints() < g.getOpponentPoints());
            Assert.assertEquals(GameResult.LOSS, g.getGameResult());
        });
    }

    @Test
    public void shouldReturnCorrectPersonalFinishedGamesIfOnlyWon() throws InvalidUserException, UserOptimisticLockException, GameOptimisticLockException {
        final User player = playerC;
        Mockito.when(queryMock.getResultList()).thenReturn(finishedGames.stream().filter(g ->
                g.getPlayerB().getId().equals(player.getId()) ||
                        g.getPlayerA().getId().equals(player.getId())).collect(Collectors.toList()));
        Mockito.when(userService.getUserDataById(Mockito.anyLong())).thenReturn(player);
        final List<PersonalFinishedGame> games = scoreAdministration.getPersonalFinishedGames(player);
        sharedPersonalFinishedGameLogic(games, player);

        games.forEach(g -> {
            Assert.assertTrue(g.getOwnPoints() > g.getOpponentPoints());
            Assert.assertEquals(GameResult.WIN, g.getGameResult());
        });
    }

    @Test
    public void shouldReturnCorrectPersonalFinishedGamesIfDraw() throws InvalidUserException, UserOptimisticLockException, GameOptimisticLockException {
        final User player = playerE;
        Mockito.when(queryMock.getResultList()).thenReturn(finishedGames.stream().filter(g ->
                g.getPlayerB().getId().equals(player.getId()) ||
                        g.getPlayerA().getId().equals(player.getId())).collect(Collectors.toList()));
        Mockito.when(userService.getUserDataById(Mockito.anyLong())).thenReturn(player);
        final List<PersonalFinishedGame> games = scoreAdministration.getPersonalFinishedGames(player);
        sharedPersonalFinishedGameLogic(games, player);

        games.forEach(g -> {
            Assert.assertEquals(g.getOwnPoints(), g.getOpponentPoints());
            Assert.assertEquals(GameResult.DRAW, g.getGameResult());
        });
    }

    @Test
    public void shouldReturnCorrectPersonalFinishedGamesIfWonAndLost() throws InvalidUserException, UserOptimisticLockException, GameOptimisticLockException {
        final User player = playerB;
        Mockito.when(queryMock.getResultList()).thenReturn(finishedGames.stream().filter(g ->
                g.getPlayerB().getId().equals(player.getId()) ||
                        g.getPlayerA().getId().equals(player.getId())).collect(Collectors.toList()));
        Mockito.when(userService.getUserDataById(Mockito.anyLong())).thenReturn(player);
        final List<PersonalFinishedGame> games = scoreAdministration.getPersonalFinishedGames(player);
        sharedPersonalFinishedGameLogic(games, player);
        Assert.assertTrue(games.stream().anyMatch(g -> g.getGameResult() == GameResult.WIN && g.getOwnPoints() > g.getOpponentPoints()));
        Assert.assertTrue(games.stream().anyMatch(g -> g.getGameResult() == GameResult.LOSS && g.getOwnPoints() < g.getOpponentPoints()));
    }

    @Test
    public void shouldReturnScoreRecordsForUserWithNoFinishedGames() throws InvalidUserException, UserOptimisticLockException, GameOptimisticLockException {
        Mockito.when(queryMock.getResultList()).thenReturn(new ArrayList());
        Mockito.when(userService.getUserDataById(Mockito.anyLong())).thenReturn(playerD);
        Assert.assertTrue(new ReflectionEquals(new ScoreRecord(playerD))
                .matches(scoreAdministration.getRecordOfUser(playerD)));
    }

    @Test
    public void shouldReturnScoreRecordsForUserIfOnlyLost() throws InvalidUserException, UserOptimisticLockException, GameOptimisticLockException {
        Mockito.when(queryMock.getResultList()).thenReturn(finishedGames.stream().filter(g ->
                g.getPlayerB().getId().equals(playerA.getId()) ||
                        g.getPlayerA().getId().equals(playerA.getId())).collect(Collectors.toList()));
        Mockito.when(userService.getUserDataById(Mockito.anyLong())).thenReturn(playerA);
        Assert.assertTrue(new ReflectionEquals(new ScoreRecord(playerA, 0, 2, 0))
                .matches(scoreAdministration.getRecordOfUser(playerA)));
    }

    @Test
    public void shouldReturnScoreRecordsIfWonAndLost() throws InvalidUserException, UserOptimisticLockException, GameOptimisticLockException {
        Mockito.when(queryMock.getResultList()).thenReturn(finishedGames.stream().filter(g ->
                g.getPlayerB().getId().equals(playerB.getId()) ||
                        g.getPlayerA().getId().equals(playerB.getId())).collect(Collectors.toList()));
        Mockito.when(userService.getUserDataById(Mockito.anyLong())).thenReturn(playerB);
        Assert.assertTrue(new ReflectionEquals(new ScoreRecord(playerB, 1, 1, 0))
                .matches(scoreAdministration.getRecordOfUser(playerB)));
    }

    @Test
    public void shouldReturnScoreRecordsForUserIfOnlyWon() throws InvalidUserException, UserOptimisticLockException, GameOptimisticLockException {
        Mockito.when(queryMock.getResultList()).thenReturn(finishedGames.stream().filter(t ->
                t.getPlayerB().getId().equals(playerC.getId()) ||
                        t.getPlayerA().getId().equals(playerC.getId())).collect(Collectors.toList()));
        Mockito.when(userService.getUserDataById(Mockito.anyLong())).thenReturn(playerC);
        Assert.assertTrue(new ReflectionEquals(new ScoreRecord(playerC, 2, 0, 0))
                .matches(scoreAdministration.getRecordOfUser(playerC)));
    }

    @Test
    public void shouldReturnScoreRecordsForUserIfDraw() throws InvalidUserException, UserOptimisticLockException, GameOptimisticLockException {
        Mockito.when(queryMock.getResultList()).thenReturn(finishedGames.stream().filter(g ->
                g.getPlayerB().getId().equals(playerE.getId()) ||
                        g.getPlayerA().getId().equals(playerE.getId())).collect(Collectors.toList()));
        Mockito.when(userService.getUserDataById(Mockito.anyLong())).thenReturn(playerE);
        Assert.assertTrue(new ReflectionEquals(new ScoreRecord(playerC, 0, 0, 2))
                .matches(scoreAdministration.getRecordOfUser(playerC)));
    }


    @Test(expected = NoAccessException.class)
    public void shouldThrowExceptionIfToBePersonalizedForOtherUserThanPlayer() throws UnfinishedGameException, NoAccessException, GameOptimisticLockException {
        final RunningVocabduelGame game = new RunningVocabduelGame(11L, playerA, playerB, null, null, null);
        game.setRounds(Stream.of(mockedFinishedRound(), mockedFinishedRound(), mockedFinishedRound()).collect(Collectors.toList()));
        Mockito.when(queryMock.getSingleResult()).thenThrow(NoResultException.class);
        scoreAdministration.finishGame(playerC, game.getId());
    }

    @Test(expected = UnfinishedGameException.class)
    public void shouldNotFinishGameWithOpenRounds() throws UnfinishedGameException, NoAccessException, GameOptimisticLockException {
        final RunningVocabduelGame game = new RunningVocabduelGame(11L, playerA, playerB, null, null, null);
        game.setRounds(Stream.of(mockedFinishedRound(), mockedFinishedRound(), new VocabduelRound()).collect(Collectors.toList()));
        Mockito.when(queryMock.getSingleResult()).thenReturn(game);
        scoreAdministration.finishGame(playerA, game.getId());
    }

    @Test
    public void shouldFinishGameWithFinishedRoundsProperly() throws UnfinishedGameException, NoAccessException, GameOptimisticLockException {
        final RunningVocabduelGame game = new RunningVocabduelGame(11L, playerA, playerB, null, null, null);
        game.setRounds(Stream.of(mockedFinishedRound(), mockedFinishedRound(), mockedFinishedRound()).collect(Collectors.toList()));
        Mockito.when(queryMock.getSingleResult()).thenReturn(game);
        final PersonalFinishedGame finishedGame = scoreAdministration.finishGame(playerA, game.getId());
        Assert.assertNotNull(finishedGame);
        Assert.assertEquals(GameResult.WIN, finishedGame.getGameResult());
        Assert.assertEquals(3, finishedGame.getOwnPoints());
        Assert.assertEquals(0, finishedGame.getOpponentPoints());
    }

    private void sharedPersonalFinishedGameLogic(final List<PersonalFinishedGame> games, final User player) {
        Assert.assertNotNull(games);
        Assert.assertEquals(2, games.size()); // see mocked tests
        games.forEach(g -> Assert.assertNotEquals(g.getOpponent().toString(), player.toString()));
    }
}
