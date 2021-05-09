package de.htwberlin.kba.gr7.vocabduel.game_administration;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.UnfinishedGameException;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.UserIsNotPlayerException;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.*;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(MockitoJUnitRunner.class)
public class ScoreServiceImplTest {

    private static FinishedVocabduelGame mockFinishedGame(User playerA, int pointsA, User playerB, int pointsB) {
        final FinishedVocabduelGame finishedGame = new FinishedVocabduelGame();
        finishedGame.setPlayerA(playerA);
        finishedGame.setPlayerB(playerB);
        finishedGame.setTotalPointsA(pointsA);
        finishedGame.setTotalPointsB(pointsB);
        return finishedGame;
    }

    private static FinishedVocabduelRound mockedFinishedRound() {
        final FinishedVocabduelRound finishedVocabduelRound = new FinishedVocabduelRound(11L);
        finishedVocabduelRound.setResultPlayerA(Result.WIN);
        finishedVocabduelRound.setResultPlayerA(Result.LOSS);
        return finishedVocabduelRound;
    }

    private User playerA; // 0 wins, 2 losses, 1 against B, 1 against C
    private User playerB;  // 1 win against A, 1 loss against C
    private User playerC; // 2 wins, 1 against A, 1 against B
    private User playerD; // 0 - 0

    private ScoreServiceImpl scoreAdministration;

    @Before
    public void setup() {
        scoreAdministration = new ScoreServiceImpl();

        playerA = new User(4711L);
        playerB = new User(42L);
        playerC = new User(2020L);
        playerD = new User(2021L);

        final FinishedVocabduelGame game1 = mockFinishedGame(playerA, 0, playerB, 3);
        final FinishedVocabduelGame game2 = mockFinishedGame(playerA, 1, playerC, 2);
        final FinishedVocabduelGame game3 = mockFinishedGame(playerB, 0, playerC, 3);
        final List<FinishedVocabduelGame> finishedGames = Stream.of(game1, game2, game3).collect(Collectors.toList());

        // Mock existing languages (private field => Whitebox#setInternalState)
        Whitebox.setInternalState(scoreAdministration, "allFinishedGames", finishedGames);
    }

    @Test
    public void shouldNotReturnPersonalFinishedGamesForUserWithoutGames() {
        final List<PersonalFinishedGame> games = scoreAdministration.getPersonalFinishedGames(playerD);
        Assert.assertNotNull(games);
        Assert.assertTrue(games.isEmpty());
    }

    @Test
    public void shouldReturnCorrectPersonalFinishedGamesIfOnlyLost() {
        final User player = playerA;
        final List<PersonalFinishedGame> games = scoreAdministration.getPersonalFinishedGames(player);
        sharedPersonalFinishedGameLogic(games, player);

        games.forEach(g -> {
            Assert.assertTrue(g.getOwnPoints() < g.getOpponentPoints());
            Assert.assertEquals(Result.LOSS, g.getGameResult());
        });
    }

    @Test
    public void shouldReturnCorrectPersonalFinishedGamesIfOnlyWon() {
        final User player = playerC;
        final List<PersonalFinishedGame> games = scoreAdministration.getPersonalFinishedGames(player);
        sharedPersonalFinishedGameLogic(games, player);

        games.forEach(g -> {
            Assert.assertTrue(g.getOwnPoints() > g.getOpponentPoints());
            Assert.assertEquals(Result.WIN, g.getGameResult());
        });
    }

    @Test
    public void shouldReturnCorrectPersonalFinishedGamesIfWonAndLost() {
        final User player = playerB;
        final List<PersonalFinishedGame> games = scoreAdministration.getPersonalFinishedGames(player);
        sharedPersonalFinishedGameLogic(games, player);
        Assert.assertTrue(games.stream().anyMatch(g -> g.getGameResult() == Result.WIN && g.getOwnPoints() > g.getOpponentPoints()));
        Assert.assertTrue(games.stream().anyMatch(g -> g.getGameResult() == Result.LOSS && g.getOwnPoints() < g.getOpponentPoints()));
    }

    @Test
    public void shouldReturnZeroWinsForUserWithNoFinishedGames() {
        Assert.assertEquals(0, scoreAdministration.getTotalWinsOfUser(playerD));
    }

    @Test
    public void shouldReturnZeroWinsForUserIfOnlyLost() {
        Assert.assertEquals(0, scoreAdministration.getTotalWinsOfUser(playerA));
    }

    @Test
    public void shouldReturnNrOfWinsIfWonAndLost() {
        Assert.assertEquals(1, scoreAdministration.getTotalWinsOfUser(playerB));
    }

    @Test
    public void shouldReturnNrOfWinsIfOnlyWon() {
        Assert.assertEquals(2, scoreAdministration.getTotalWinsOfUser(playerC));
    }

    @Test
    public void shouldReturnZeroLossesForUserWithNoFinishedGames() {
        Assert.assertEquals(0, scoreAdministration.getTotalLossesOfUser(playerD));
    }

    @Test
    public void shouldReturnZeroLossesForUserIfOnlyWon() {
        Assert.assertEquals(0, scoreAdministration.getTotalLossesOfUser(playerC));
    }

    @Test
    public void shouldReturnNrOfLossesIfWonAndLost() {
        Assert.assertEquals(1, scoreAdministration.getTotalLossesOfUser(playerB));
    }

    @Test
    public void shouldReturnNrOfLossesIfOnlyLost() {
        Assert.assertEquals(2, scoreAdministration.getTotalLossesOfUser(playerA));
    }

    @Test(expected = UnfinishedGameException.class)
    public void shouldThrowExceptionIfToBePersonalizedForOtherUserThanPlayer() throws UnfinishedGameException, UserIsNotPlayerException {
        final VocabduelGame game = new VocabduelGame();
        game.setPlayerA(playerA);
        game.setPlayerA(playerB);
        game.setRounds(Stream.of(mockedFinishedRound(), mockedFinishedRound(), mockedFinishedRound()).collect(Collectors.toList()));
        scoreAdministration.finishGame(playerC, game);
    }

    @Test(expected = UserIsNotPlayerException.class)
    public void shouldNotFinishGameWithOpenRounds() throws UnfinishedGameException, UserIsNotPlayerException {
        final VocabduelGame game = new VocabduelGame();
        game.setPlayerA(playerA);
        game.setPlayerA(playerB);
        game.setRounds(Stream.of(mockedFinishedRound(), mockedFinishedRound(),  new VocabduelRound(game.getId())).collect(Collectors.toList()));
        scoreAdministration.finishGame(playerA, game);
    }

    @Test
    public void shouldFinishGameWithFinishedRoundsProperly() throws UnfinishedGameException, UserIsNotPlayerException {
        final VocabduelGame game = new VocabduelGame();
        game.setPlayerA(playerA);
        game.setPlayerA(playerB);
        game.setRounds(Stream.of(mockedFinishedRound(), mockedFinishedRound(), mockedFinishedRound()).collect(Collectors.toList()));

        final PersonalFinishedGame finishedGame = scoreAdministration.finishGame(playerA, game);
        Assert.assertNotNull(finishedGame);
        Assert.assertEquals(Result.WIN, finishedGame.getGameResult());
        Assert.assertEquals(3, finishedGame.getOwnPoints());
        Assert.assertEquals(0, finishedGame.getOpponentPoints());
    }

    private void sharedPersonalFinishedGameLogic(final List<PersonalFinishedGame> games, final User player) {
        Assert.assertNotNull(games);
        Assert.assertEquals(2, games.size()); // see mocked tests
        games.forEach(g -> Assert.assertNotEquals(g.getOpponent().toString(), player.toString()));
    }
}
