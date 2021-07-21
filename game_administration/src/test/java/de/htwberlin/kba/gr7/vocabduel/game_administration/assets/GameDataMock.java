package de.htwberlin.kba.gr7.vocabduel.game_administration.assets;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.GameService;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.RunningVocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.VocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.VocabduelRound;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.*;

import java.sql.Timestamp;
import java.util.*;

public class GameDataMock {
    private final User playerA;
    private final User playerB;
    private final RunningVocabduelGame newGame;
    private final RunningVocabduelGame newGame2;

    public GameDataMock() {
        playerA = new User(11L);
        playerB = new User(12L);
        List<VocableList> vocableList = new ArrayList<>();
        SupportedLanguage knownLanguage = SupportedLanguage.DE;
        SupportedLanguage learntLanguage = SupportedLanguage.EN;

        newGame = new RunningVocabduelGame(1L, playerA, playerB, knownLanguage, learntLanguage, vocableList);

        // only important for check if unfinished games > 1 in order to check game uniqueness:
        newGame2 = new RunningVocabduelGame(2L, playerB, playerA, knownLanguage, learntLanguage, vocableList);

        // add 10 Rounds, with gameId, with roundId, with answers, with question
        List<VocabduelRound> tempRoundList = newGame.getRounds();
        for (int i = 1; i <= GameService.NR_OF_ROUNDS; i++) {
            VocabduelRound newRound = new VocabduelRound(i);
            newRound.setGame(newGame);
            newRound.setAnswers(Arrays.asList(
                    new TranslationGroup((long) (i + 10), Collections.singletonList("wrongAnswer1")),
                    new TranslationGroup((long) i, Collections.singletonList("translation" + i)),
                    new TranslationGroup((long) (i + 11), Collections.singletonList("wrongAnswer2")),
                    new TranslationGroup((long) (i + 12), Collections.singletonList("wrongAnswer3"))
            ));
            newRound.setQuestion(new UntranslatedVocable((long) i, new TranslationGroup(Collections.singletonList("vocableInQuestion" + i))));
            if (tempRoundList == null) {
                tempRoundList = new ArrayList<>();
            }
            tempRoundList.set(i - 1, newRound);
        }
        newGame.setRounds(tempRoundList);

        // normal VocableList with 10 Entries (each Round one Vocable)
        VocableList myVocList = new VocableList(42L);
        myVocList.setAuthor(playerA);
        myVocList.setTimestamp(new Timestamp(System.currentTimeMillis()));
        myVocList.setTitle("myTitle");

        // set Vocables
        List<Vocable> tempVocableList = myVocList.getVocables();
        for (int i = 1; i <= GameService.NR_OF_ROUNDS; i++) {
            Vocable myVoc = new Vocable(
                    (long) i,
                    new TranslationGroup((long) i, Collections.singletonList("unknownWord" + i)),
                    Collections.singletonList(new TranslationGroup((long) i, Collections.singletonList("translation" + i))),
                    null,
                    null
            );
            if (tempVocableList == null) {
                tempVocableList = new ArrayList<>();
            }
            tempVocableList.add(myVoc);
        }
        myVocList.setVocables(tempVocableList);
        newGame.setVocableLists(Collections.singletonList(myVocList));
    }

    public RunningVocabduelGame mockVocabduelGame() {
        return newGame;
    }

    public VocabduelRound mockVocabduelRound() {
        return this.newGame.getRounds().get(0);
    }

    public User mockSampleUser() {
        return playerA;
    }

    public User mockOpponent() {
        return playerB;
    }

    public List<VocableList> mockVocableLists() {
        return newGame.getVocableLists();
    } // normal VocableList with 10 Vocables

    public SupportedLanguage mockKnownLanguage() {
        return newGame.getKnownLanguage();
    }

    public SupportedLanguage mockLearntLanguage() {
        return newGame.getLearntLanguage();
    }

    public List<VocableList> mockVocableListsWithEmptyVocabulary() {
        List<VocableList> myList = newGame.getVocableLists();
        myList.get(0).getVocables().clear();
        return myList;
    } // normal VocableList with 0 Vocables

    public List<VocableList> mockVocableListsWithoutEnoughVocabulary() {
        List<VocableList> myList = newGame.getVocableLists();
        myList.get(0).getVocables().remove(myList.get(0).getVocables().size() - 1);
        return myList;
    } // normal VocableList with 9 Vocables

    public List<VocableList> mockMultipleVocableListsWithoutEnoughVocabulary() {
        List<VocableList> myList = newGame.getVocableLists();
        myList.get(0).getVocables().clear();
        myList.get(0).setVocables(Collections.singletonList(
                new Vocable(
                        new TranslationGroup(Collections.singletonList("unknownWord")),
                        Collections.singletonList(new TranslationGroup(Collections.singletonList("translation"))),
                        null,
                        null
                )
        ));
        myList = new ArrayList<>(myList);
        myList.add(new VocableList(11L, mockSampleUser(), "myTitle2"));
        myList.get(myList.size() - 1).setVocables(
                Collections.singletonList((
                                new Vocable(
                                        new TranslationGroup(Collections.singletonList("unknownWord2")),
                                        Collections.singletonList(new TranslationGroup(Collections.singletonList("translation2"))),
                                        null,
                                        null
                                )
                        )
                ));
        return myList;
    } // 2 VocableLists each with 1 Vocable

    public List<VocableList> mockMultipleVocableListsWithEnoughVocabulary() {
        List<VocableList> myList = newGame.getVocableLists();
        myList.get(0).getVocables().clear();
        List<Vocable> tempVocList = myList.get(0).getVocables();

        myList.get(0).setVocables(addVocables(tempVocList, myList.get(0).getTitle()));
        myList = new ArrayList<>(myList);
        myList.add(new VocableList(11L, mockSampleUser(), "myTitle2"));
        tempVocList = myList.get(myList.size() - 1).getVocables();
        myList.get(myList.size() - 1).setVocables(addVocables(tempVocList, myList.get(myList.size() - 1).getTitle()));

        return myList;
    } // 2 VocableLists each with 5 Vocables

    public List<VocableList> mockEmptyVocableLists() {
        return new ArrayList<>();
    } // empty List<VocableList>

    public List<VocabduelGame> mockUnfinishedGameList() {
        List<VocabduelGame> myList = new ArrayList<>();
        myList.add(newGame);
        myList.add(newGame2);
        return myList;
    }

    public List<VocabduelGame> mockEmptyUnfinishedGameList() {
        return new ArrayList<>();
    }

    private List<Vocable> addVocables(List<Vocable> input, String title) {
        for (int i = 1; i <= ((GameService.NR_OF_ROUNDS / 2) + 1); i++) {
            Vocable tempVoc = new Vocable(
                    new TranslationGroup(Collections.singletonList(title + "unknownWord" + i)),
                    Collections.singletonList(new TranslationGroup(Collections.singletonList(title + "translation" + i))),
                    null,
                    null
            );
            if (input == null) input = new ArrayList<>();
            input.add(tempVoc);
        }
        return input;
    }
}
