package de.htwberlin.kba.gr7.vocabduel.game_administration;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.GameAdministration;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.*;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.CorrectAnswerResult;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.VocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.VocabduelRound;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.UserAdministration;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.VocabularyLib;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.SupportedLanguage;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.TranslationGroup;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.VocableList;

import java.util.List;

public class GameAdministrationImpl implements GameAdministration {

    private UserAdministration userService;
    private VocabularyLib vocabularyService;

    public static int getFixNumberOfRoundsPerGame(){
        return GameAdministration.NR_OF_ROUNDS;
    }

    @Override
    public VocabduelGame startGame(User playerA, User playerB, List<VocableList> vocableLists, SupportedLanguage knownLanguage, SupportedLanguage learntLanguage)
        throws NoSecondPlayerException, KnownLangEqualsLearntLangException, NotEnoughVocableListsException, NotEnoughVocabularyException {
        return null;
    }

    @Override
    public List<VocabduelGame> getPersonalChallengedGames(User user) {
        return null;
    }

    @Override
    public VocabduelRound startRound(User player, VocabduelGame game) throws RoundAlreadyFinishedException {
        return null;
    }

    @Override
    public CorrectAnswerResult answerQuestion(User player, VocabduelRound round, TranslationGroup answer) {
        return null;
    }
}
