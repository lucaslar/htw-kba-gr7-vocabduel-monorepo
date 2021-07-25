package de.htwberlin.kba.gr7.vocabduel.game_administration.export;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.*;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.CorrectAnswerResult;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.RunningVocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.VocabduelRound;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.InternalUserModuleException;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.InvalidUserException;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.exceptions.InternalVocabularyModuleException;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.VocableList;

import java.util.List;

/**
 * Service for managing current games and their rounds.
 *
 * @author Sebastian Kehl, Lucas Larisch
 * @version 1.0, May 2021
 */
public interface GameService {

    /**
     * Integer: Fix number of Rounds per game
     */
    int NR_OF_ROUNDS = 9;

    /**
     * Starts a new game as <code>playerA</code> against <code>playerB</code> with the given parameters
     * (languages/vocable lists) and returns an instance of that newly started match.
     *
     * @param playerA        Player initiating the game.
     * @param playerB        Opponent to be invited to play with <code>playerA</code>.
     * @param vocableLists   List of vocable lists to be used in the game. The questions will be randomly picked from these lists.
     * @return New <code>{@link RunningVocabduelGame}</code> instance based on the given params.
     * @throws InvalidUserException                 One of the given users could not be found.
     * @throws InvalidGameSetupException            The setup of the game is not valid for a reason described in the given error message.
     * @throws NotEnoughVocabularyException         The provided VocableLists do not contain enough Vocables for <object>GameAdministration.NR_OF_ROUNDS</object> rounds per game.
     * @throws InternalUserModuleException          An internal error in the user module occurred.
     * @throws InternalVocabularyModuleException    An internal error in the vocabulary module occurred.
     */
    RunningVocabduelGame startGame(User playerA, User playerB, List<VocableList> vocableLists)
            throws InvalidUserException, InvalidGameSetupException, NotEnoughVocabularyException, InternalUserModuleException, InternalVocabularyModuleException;

    /**
     * Collects all pending, i.e. not finished, games a given user has been challenged to and returns them.
     *
     * @param user User the pending/challenged matches of are to be returned.
     * @return List of all unfinished games the given <code>user</code> has been challenged to or has started.
     */
    List<RunningVocabduelGame> getPersonalChallengedGames(User user);

    /**
     * Starts the next round of a given game as a given player, i.e. returns the respective next round object.
     *
     * @param player Player the next round is to be returned for.
     * @param gameId Id of the game the next round of is to be returned for the given <code>user</code>.
     * @return Current <code>{@link VocabduelRound}</code> of a given game for a given user.
     * including the 1 correct and the other wrong answer possibilities without knowing which is what.
     */
    VocabduelRound startRound(User player, long gameId) throws NoAccessException;

    /**
     * Checks and stores the result for an answer submitted in a given <code>round</code> by a given
     * <code>player</code>.
     * The information, which answer the right one is, stays server sided.
     * This request checks whether the submitted answer is right or not and
     * returns feedback for that answer incl. the correct answer in case of
     * having submitted a wrong one.
     * Afterwards only the check whether the user was wrong of right will be stored.
     * The information, which answer the user chose, will be lost.
     *
     * @param player    Player who has answered the question.
     * @param gameId    Id of the game the answer is to be submitted for.
     * @param roundNr   Nr of the round the answer is to be submitted for.
     * @param answerNr  Nr of the answer submitted by the given <code>player</code>.
     * @return Result for the given <code>round</code> from the perspective of the given <code>player</code> incl. the correct answer in case of having submitted a wrong one.
     * @throws InvalidVocabduelGameNrException The question has already been answered by the current user or an invalid answer number has been stated.
     * @throws NoAccessException The given user is no participant of the given round or it could not be found at all.
     */
    CorrectAnswerResult answerQuestion(final User player, final long gameId, final int roundNr, final int answerNr) throws InvalidVocabduelGameNrException, NoAccessException;

    /**
     * Removes all running games with at least one removed user and all finished games with two removed users.
     *
     * @return 0 in case of success
     */
    int removeWidowGames();
}
