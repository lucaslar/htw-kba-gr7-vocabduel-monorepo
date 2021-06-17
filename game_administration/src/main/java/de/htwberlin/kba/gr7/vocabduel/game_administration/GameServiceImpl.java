package de.htwberlin.kba.gr7.vocabduel.game_administration;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.GameService;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.*;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.*;
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

    public GameServiceImpl(final UserService userService, final VocabularyService vocabularyService, final EntityManager entityManager){
        ENTITY_MANAGER = entityManager;
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
        if (round == null)
            throw new NoAccessException("No round found or you do not seem to have access. Are you sure you stated a running game that you still have open questions in? Check your games to find out.");
        else return round;
    }

    @Override
    public CorrectAnswerResult answerQuestion(final User player, final long gameId, final int roundNr, final int answerNr) throws InvalidAnswerNrException, NoAccessException {
        if (answerNr < 0 || answerNr > 3) {
            throw new InvalidAnswerNrException("Invalid answer nr. Must be 0-3 (a = 0, b = 1, ...)");
        } else if (roundNr < 1 || roundNr > getFixNumberOfRoundsPerGame()) {
            throw new InvalidAnswerNrException("Invalid round nr. Must be 1-" + getFixNumberOfRoundsPerGame());
        } else if (player != null) {
            final VocabduelRound round = startRound(player, gameId);

            if (round == null) {
                throw new NoAccessException("No round found or you do not seem to have access. Are you sure you stated a round you have access to and not answered before?");
            } else if (round.getRoundNr() != roundNr) {
                throw new NoAccessException("Invalid round. Your next round is round " + round.getRoundNr());
            } else {
                final TranslationGroup selection = round.getAnswers().get(answerNr);
                final Optional<Vocable> questionedVocab = round.getGame().getVocableLists()
                        .stream()
                        .flatMap(vl -> vl.getVocables().stream())
                        .filter(v -> v.getId().equals(round.getQuestion().getId()))
                        .findFirst();

                assert (questionedVocab.isPresent());
                final Optional<TranslationGroup> correctAnswer = questionedVocab.get().getTranslations()
                        .stream()
                        .filter(t -> round.getAnswers().stream().anyMatch(a -> a.getId().equals(t.getId())))
                        .findFirst();
                assert (correctAnswer.isPresent());
                final Result result = correctAnswer.get().getId().equals(selection.getId()) ? Result.WIN : Result.LOSS;
                final CorrectAnswerResult correctAnswerResult = new CorrectAnswerResult(result);

                if (round.getGame().getPlayerA().getId().equals(player.getId())) round.setResultPlayerA(result);
                else round.setResultPlayerB(result);

                ENTITY_MANAGER.getTransaction().begin();
                ENTITY_MANAGER.merge(round); // TODO state is stored, but object (Java) is not updated (only for second player)
                ENTITY_MANAGER.getTransaction().commit();

                if (result == Result.LOSS) correctAnswerResult.setCorrectAnswer(correctAnswer.get());
                return correctAnswerResult;
            }
        }
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
            throw new KnownLangEqualsLearntLangException("Known and learnt language must be different, but where 2x " + learntLanguage);
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
            throw new InvalidGameSetupException("Known language (" + knownLanguage + ") is not supported (or not set)");
        }

        if (VOCABULARY_SERVICE.getAllSupportedLanguages().stream().noneMatch(lang -> lang == learntLanguage)) {
            throw new InvalidGameSetupException("Learnt language (" + learntLanguage + ") is not supported (or not set)");
        }

        // TODO: Check if all lists are of same language set (remove lang from / lang to? / requires db changes in order to support bi-directionality vocable list ( => unit) <> language set)
    }

    private VocabduelGame createVocaduelGameWithRounds(final User playerA, final User playerB, final SupportedLanguage knownLanguage, final SupportedLanguage learntLanguage, final List<VocableList> vocableLists) throws NotEnoughVocabularyException {
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
            // false answers:
            final List<TranslationGroup> answers = flatVocables
                    .stream()
                    // Avoid that synonym stored to another vocable is questioned
                    // E.g. Buenos Dias & Buenas Tares = Guten Tag
                    // => question "Buenos Dias" would be misleading with those two answer options
                    .filter(v -> v.getTranslations()
                            .stream().noneMatch(t -> t.getSynonyms()
                                    .stream().anyMatch(s -> vocable.getTranslations()
                                            .stream().anyMatch(vt -> vt.getSynonyms()
                                                    .stream().anyMatch(vs -> vs.equals(s))))))
                    .limit(3)
                    .map(x -> x.getTranslations().get((int) (Math.random() * x.getTranslations().size())))
                    .collect(Collectors.toList());

            if (answers.size() != 3) {
                throw new NotEnoughVocabularyException("Not enough unique vocabulary to add false options for " + vocable.getVocable().getSynonyms() + ".");
            }

            // correct answer:
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
