package de.htwberlin.kba.gr7;

import de.htwberlin.kba.gr7.exceptions.UnauthorizedException;
import de.htwberlin.kba.gr7.model.*;

import java.sql.SQLException;
import java.util.List;

public interface GameAdministration {
    VocabduelGame startGame(User playerA, User playerB) throws UnauthorizedException, SQLException;

    List<VocabduelGame> getPersonalChallengedGames(User user) throws UnauthorizedException, SQLException;

    VocabduelRound startRound(User player, VocabduelGame game) throws UnauthorizedException, SQLException;

    CorrectAnswerResult answerQuestion(User player, VocabduelGame game) throws UnauthorizedException, SQLException;
}
