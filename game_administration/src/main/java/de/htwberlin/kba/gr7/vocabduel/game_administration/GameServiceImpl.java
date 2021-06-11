package de.htwberlin.kba.gr7.vocabduel.game_administration;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.GameService;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.*;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.CorrectAnswerResult;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.FinishedVocabduelRound;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.VocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.VocabduelRound;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.UserService;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.VocabularyService;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.SupportedLanguage;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.TranslationGroup;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.VocableList;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Random;

@Service
public class GameServiceImpl implements GameService {

    private UserService userService;
    private VocabularyService vocabularyService;

    private final EntityManager ENTITY_MANAGER;

    public static int getFixNumberOfRoundsPerGame() {
        return GameService.NR_OF_ROUNDS;
    }

    public GameServiceImpl() {
        ENTITY_MANAGER = EntityFactoryManagement.getEntityFactory().createEntityManager();
    }

    @Override
    public VocabduelGame startGame(User playerA, User playerB, List<VocableList> vocableLists, SupportedLanguage knownLanguage, SupportedLanguage learntLanguage)
            throws NoSecondPlayerException, KnownLangEqualsLearntLangException, NotEnoughVocableListsException, NotEnoughVocabularyException {
        // checks first
        if (playerA.getId() == 0 || playerB.getId() == 0) {
            throw new NoSecondPlayerException();
        }
        if (learntLanguage == knownLanguage) {
            throw new KnownLangEqualsLearntLangException();
        }
        if (vocableLists.isEmpty()) {
            throw new NotEnoughVocableListsException();
        }
        int vocableCount = 0;
        for (VocableList vocableList : vocableLists) {
            vocableCount += vocableList.getVocables().size();
        }
        if (vocableCount < getFixNumberOfRoundsPerGame()) {
            throw new NotEnoughVocabularyException();
        }

        // persist new Game
        // TODO: kapseln, sonst funzen die Tests nicht mehr
        VocabduelGame newGame = new VocabduelGame(playerA, playerB, knownLanguage, learntLanguage, vocableLists);
        ENTITY_MANAGER.getTransaction().begin();
        ENTITY_MANAGER.persist(newGame);
        ENTITY_MANAGER.getTransaction().commit();
        ENTITY_MANAGER.close();
        return newGame;
    }

    @Override
    public List<VocabduelGame> getPersonalChallengedGames(User user) {
        return null;
    }

    @Override
    public VocabduelRound startRound(User player, VocabduelGame game) throws RoundAlreadyFinishedException {
        // Checks first
        boolean isTrue = false;
        int indexRoundEmpty = getFixNumberOfRoundsPerGame();
        int indexRoundUnfinished = getFixNumberOfRoundsPerGame();
        for (VocabduelRound round : game.getRounds()) {
            if (round != null && round.getClass().isAssignableFrom(FinishedVocabduelRound.class)) {
                isTrue = true;
            } else if (round == null) {
                indexRoundEmpty = game.getRounds().indexOf(null);
                break;
            } else if (!round.getClass().isAssignableFrom(FinishedVocabduelRound.class)) {
                indexRoundUnfinished = game.getRounds().indexOf(round);
            }
        }
        if (isTrue) {
            throw new RoundAlreadyFinishedException();
        }

        VocabduelRound newRound;
        if (indexRoundEmpty != getFixNumberOfRoundsPerGame()) {
            newRound = new VocabduelRound(game.getId(), indexRoundEmpty);
            // generate Question
            while (newRound.getQuestion() == null) {
                Random random = new Random();
                int randomList = random.nextInt(game.getVocableLists().size());
                int randomQuest = random.nextInt(game.getVocableLists().get(randomList).getVocables().size());
                newRound.setQuestion(game.getVocableLists().get(randomList).getVocables().get(randomQuest));
                newRound.setAnswers(game.getVocableLists().get(randomList).getVocables().get(randomQuest).getTranslations());
                for (int i = 0; i < game.getRounds().size(); i++) {
                    if (newRound.getQuestion().equals(game.getRounds().get(i).getQuestion())) {
                        newRound.setQuestion(null);
                        newRound.setAnswers(null);
                    }
                }
            }
            // generate answer possibilities
            while (newRound.getAnswers().size() < 4) { // TODO Anzahl möglicher Antworten in Interface hinterlegen
                Random random = new Random();
                int randomList = random.nextInt(game.getVocableLists().size());
                int randomQuest = random.nextInt(game.getVocableLists().get(randomList).getVocables().size());
                int randomTrans = random.nextInt(game.getVocableLists().get(randomList).getVocables().get(randomQuest).getTranslations().size());
                newRound.getAnswers().add(game.getVocableLists().get(randomList).getVocables().get(randomQuest).getTranslations().get(randomTrans));
            }
            // persist new round
            // TODO: kapseln, sonst funzen die Tests nicht mehr
            ENTITY_MANAGER.getTransaction().begin();
            ENTITY_MANAGER.persist(newRound);
            ENTITY_MANAGER.getTransaction().commit();
            ENTITY_MANAGER.close();
            game.getRounds().set(indexRoundEmpty, newRound);
            return newRound;
        } else {
            return game.getRounds().get(indexRoundUnfinished);
        }
        // TODO: wo / wie die neue Runde an das Spiel anbinden?
    }

    @Override
    public CorrectAnswerResult answerQuestion(User player, VocabduelRound round, TranslationGroup answer) {
        return null;
    }
}
