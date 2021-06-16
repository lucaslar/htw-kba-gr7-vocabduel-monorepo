package de.htwberlin.kba.gr7.vocabduel.game_administration;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.GameService;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.*;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.CorrectAnswerResult;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.VocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.VocabduelRound;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.UserService;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.InvalidUserException;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.VocabularyService;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.*;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GameServiceImpl implements GameService {

    private final EntityManager ENTITY_MANAGER;

    private final UserService USER_SERVICE;
    private final VocabularyService VOCABULARY_SERVICE;

    public static int getFixNumberOfRoundsPerGame() {
        return GameService.NR_OF_ROUNDS;
    }

    public GameServiceImpl(final UserService userService, final VocabularyService vocabularyService) {
        ENTITY_MANAGER = EntityFactoryManagement.getEntityFactory().createEntityManager();
        USER_SERVICE = userService;
        VOCABULARY_SERVICE = vocabularyService;
    }

    @Override
    public VocabduelGame startGame(User playerA, User playerB, List<VocableList> vocableLists, SupportedLanguage knownLanguage, SupportedLanguage learntLanguage)
            throws InvalidUserException, InvalidGameSetupException, KnownLangEqualsLearntLangException, NotEnoughVocabularyException {
        verifyGameSetup(playerA, playerB, vocableLists, knownLanguage, learntLanguage);

        // persist new Game
        // TODO: kapseln, sonst funzen die Tests nicht mehr
        final VocabduelGame newGame = createVocaduelGameWithRounds(playerA, playerB, knownLanguage, learntLanguage, vocableLists);
        ENTITY_MANAGER.getTransaction().begin();
        ENTITY_MANAGER.persist(newGame);
        ENTITY_MANAGER.getTransaction().commit();
        // ENTITY_MANAGER.close(); // TODO: Needed?
        return newGame;
    }

    @Override
    public List<VocabduelGame> getPersonalChallengedGames(User user) {
        List<VocabduelGame> games = null;
        if (user != null) {
            ENTITY_MANAGER.getTransaction().begin();
            try {
                games = (List<VocabduelGame>) ENTITY_MANAGER
                        .createQuery("select g from VocabduelGame g where g.playerB = :user or g.playerA = :user")
                        .setParameter("user", user)
                        .getResultList();

                // TODO: Exclude finished games (might require db changes)

            } catch (NoResultException ignored) {
            }
            ENTITY_MANAGER.getTransaction().commit();
        }
        return games;
    }

    @Override
    public VocabduelRound startRound(User player, long gameId) throws NoAccessException {
//        // Checks first
//        boolean isTrue = false;
//        int indexRoundEmpty = getFixNumberOfRoundsPerGame();
//        int indexRoundUnfinished = getFixNumberOfRoundsPerGame();
//        for (VocabduelRound round : game.getRounds()) {
//            if (round != null && round.getClass().isAssignableFrom(FinishedVocabduelRound.class)) {
//                isTrue = true;
//            } else if (round == null) {
//                indexRoundEmpty = game.getRounds().indexOf(null);
//                break;
//            } else if (!round.getClass().isAssignableFrom(FinishedVocabduelRound.class)) {
//                indexRoundUnfinished = game.getRounds().indexOf(round);
//            }
//        }
//        if (isTrue) {
//            throw new RoundAlreadyFinishedException();
//        }
//
//        VocabduelRound newRound;
//        if (indexRoundEmpty != getFixNumberOfRoundsPerGame()) {
//            newRound = new VocabduelRound(indexRoundEmpty);
//            // generate Question
//            while (newRound.getQuestion() == null) {
//                Random random = new Random();
//                int randomList = random.nextInt(game.getVocableLists().size());
//                int randomQuest = random.nextInt(game.getVocableLists().get(randomList).getVocables().size());
//                newRound.setQuestion(game.getVocableLists().get(randomList).getVocables().get(randomQuest));
//                newRound.setAnswers(game.getVocableLists().get(randomList).getVocables().get(randomQuest).getTranslations());
//                for (int i = 0; i < game.getRounds().size(); i++) {
//                    if (newRound.getQuestion().equals(game.getRounds().get(i).getQuestion())) {
//                        newRound.setQuestion(null);
//                        newRound.setAnswers(null);
//                    }
//                }
//            }
//            // generate answer possibilities
//            while (newRound.getAnswers().size() < 4) { // TODO Anzahl möglicher Antworten in Interface hinterlegen
//                Random random = new Random();
//                int randomList = random.nextInt(game.getVocableLists().size());
//                int randomQuest = random.nextInt(game.getVocableLists().get(randomList).getVocables().size());
//                int randomTrans = random.nextInt(game.getVocableLists().get(randomList).getVocables().get(randomQuest).getTranslations().size());
//                newRound.getAnswers().add(game.getVocableLists().get(randomList).getVocables().get(randomQuest).getTranslations().get(randomTrans));
//            }
//            // persist new round
//            // TODO: kapseln, sonst funzen die Tests nicht mehr
//            ENTITY_MANAGER.getTransaction().begin();
//            ENTITY_MANAGER.persist(newRound);
//            ENTITY_MANAGER.getTransaction().commit();
//            ENTITY_MANAGER.close();
//            game.getRounds().set(indexRoundEmpty, newRound);
//            return newRound;
//        } else {
//            return game.getRounds().get(indexRoundUnfinished);
//        }

        // TODO: Remove above?

        VocabduelRound round = null;
        if (player != null) {
            ENTITY_MANAGER.getTransaction().begin();
            try {
                round = (VocabduelRound) ENTITY_MANAGER
                        .createQuery("select r from VocabduelGame g inner join g.rounds r where g.id = :gameId and ((g.playerA = :user and r.resultPlayerA is null) or (g.playerB = :user and r.resultPlayerB is null))")
                        .setParameter("user", player)
                        .setParameter("gameId", gameId)
                        .setMaxResults(1)
                        .getSingleResult();
            } catch (NoResultException ignored) {
            }
            ENTITY_MANAGER.getTransaction().commit();
        }
        if (round == null) throw new NoAccessException("No round found or you do not seem to have access. Are you sure you stated a running game that you still have open questions in? Check your games to find out.");
        else return round;
    }

    @Override
    public CorrectAnswerResult answerQuestion(User player, long roundId, int answerNr) throws QuestionAlreadyAnsweredException, NoAccessException {
        return null;
    }

    private void verifyGameSetup(User playerA, User playerB, List<VocableList> vocableLists, SupportedLanguage knownLanguage, SupportedLanguage learntLanguage) throws InvalidGameSetupException, KnownLangEqualsLearntLangException, NotEnoughVocabularyException, InvalidUserException {
        if (vocableLists == null) throw new InvalidGameSetupException("No vocable lists provided!");
        if (playerA == null) throw new InvalidGameSetupException("No user object given! (initiator)");
        if (playerB == null) throw new InvalidGameSetupException("No user object given! (opponent)");
        if (knownLanguage == null) throw new InvalidGameSetupException("No known language given!");
        if (learntLanguage == null) throw new InvalidGameSetupException("No learnt language given!");

        if (vocableLists.stream().anyMatch(Objects::isNull)) {
            throw new InvalidGameSetupException("At least one vocable list is null!");
        }

        if (playerA.getId().equals(playerB.getId())) {
            throw new InvalidGameSetupException("You cannot play against yourself.");
        }

        if (learntLanguage == knownLanguage) {
            throw new KnownLangEqualsLearntLangException("Known and learnt language must be different, but where 2x " + learntLanguage.toString());
        }

        final int nrOfVocabs = vocableLists.stream().map(l -> l.getVocables().size()).reduce(0, Integer::sum);
        if (nrOfVocabs < getFixNumberOfRoundsPerGame()) {
            throw new NotEnoughVocabularyException("Not enough vocabulary to fit the nr of rounds (required: min. " + getFixNumberOfRoundsPerGame() + ", given: " + nrOfVocabs + ") => please add more lists to start.");
        }

        if (USER_SERVICE.getUserDataById(playerA.getId()) == null) {
            throw new InvalidUserException("Player A (initiator) is not a known user!");
        }

        if (USER_SERVICE.getUserDataById(playerB.getId()) == null) {
            throw new InvalidUserException("Player B (opponent) is not a known user!");
        }

        if (VOCABULARY_SERVICE.getAllSupportedLanguages().stream().noneMatch(lang -> lang == knownLanguage)) {
            throw new InvalidGameSetupException("Known language (" + knownLanguage.toString() + ") is not supported (or not set)");
        }

        if (VOCABULARY_SERVICE.getAllSupportedLanguages().stream().noneMatch(lang -> lang == learntLanguage)) {
            throw new InvalidGameSetupException("Learnt language (" + learntLanguage.toString() + ") is not supported (or not set)");
        }

        // TODO: Check if all lists are of same language set (remove lang from / lang to? / requires db changes in order to support bi-directionality vocable list ( => unit) <> language set)
    }

    private VocabduelGame createVocaduelGameWithRounds(final User playerA, final User playerB, final SupportedLanguage knownLanguage, final SupportedLanguage learntLanguage, final List<VocableList> vocableLists) {
        final VocabduelGame game = new VocabduelGame(playerA, playerB, knownLanguage, learntLanguage, vocableLists);

        final List<Vocable> flatVocables = vocableLists.stream().flatMap(vl -> vl.getVocables().stream()).collect(Collectors.toList());
        Collections.shuffle(flatVocables);
        final List<Vocable> questionedVocabs = flatVocables.stream().limit(getFixNumberOfRoundsPerGame()).collect(Collectors.toList());
        final List<VocabduelRound> rounds = new ArrayList<>();

        int i = 1;
        for (final Vocable vocable : questionedVocabs) {
            final VocabduelRound round = new VocabduelRound(i++);
            final UntranslatedVocable untranslated = new UntranslatedVocable();
            untranslated.setVocable(vocable.getVocable());

            Collections.shuffle(flatVocables);
            final List<TranslationGroup> answers = flatVocables.stream().limit(3).map(x -> x.getTranslations().get((int) (Math.random() * x.getTranslations().size()))).collect(Collectors.toList());
            answers.add(vocable.getTranslations().get((int) (Math.random() * vocable.getTranslations().size())));
            Collections.shuffle(answers);

            round.setGame(game);
            round.setQuestion(vocable);
            round.setAnswers(answers);
            rounds.add(round);
        }

        game.setRounds(rounds);
        return game;
    }
}
