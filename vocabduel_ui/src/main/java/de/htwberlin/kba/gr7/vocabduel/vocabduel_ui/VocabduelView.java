package de.htwberlin.kba.gr7.vocabduel.vocabduel_ui;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.GameService;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.*;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.AuthTokens;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.LoggedInUser;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabduel_ui.model.VocabduelCliAction;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class VocabduelView {

    private final Scanner SCANNER = new Scanner(System.in);

    public String scanInput() {
        System.out.print("\n$ ");
        return SCANNER.nextLine();
    }

    public void printHello() {
        System.out.println("\n###############################################");
        System.out.println("###                                         ###");
        System.out.println("###              VOCABDUEL CLI              ###");
        System.out.println("###                                         ###");
        System.out.println("###############################################\n");
        System.out.println("Welcome to the gr7-vocabduel command line interface!");
        System.out.println("If you haven't used this tool before, type `help` or `h` to get a list of all possible actions.");
    }

    public void printUnknownParam(final String action) {
        System.out.println("Unknown action \"" + action + "\" => type `help` or `h` in order to get a list of all possible actions");
    }

    public void printHelp(final List<VocabduelCliAction> actions) {
        System.out.println("Here's a list of all actions available in this application:");
        actions.stream().map(a -> {
                    final String cmd = a.getShortName() != null
                            ? "`" + a.getName() + "` or `" + a.getShortName() + "`"
                            : "`" + a.getName() + "`";
                    final String requiredArgs = a.getRequiredArgs() != null && a.getRequiredArgs().length > 0
                            ? " [Required args: " + String.join(", ", a.getRequiredArgs()) + "]"
                            : "";
                    final String guardedInfo = a.isGuarded() ? " [Requires preceding login]" : "";
                    return "..." + cmd + requiredArgs + guardedInfo + " => " + a.getDescription();
                }
        ).sorted().forEach(System.out::println);
    }

    public void printQuit() {
        System.out.println("Bye bye");
    }

    public void printWarningActionKey(final String key) {
        System.err.println("Dev Info: `" + key + "` does already exist and will not be put into the action map. Please fix this problem!");
    }

    public void printInvalidLogin() {
        System.out.println("Invalid login, please try again.");
    }

    public void printCurrentlyLoggedInAs(final User user) {
        System.out.println("Logged in as " + user.toString());
    }

    public void printLogoutBeforeLogin(final User user) {
        System.out.println("You are currently logged in. Please logout before re-logging in/registration.");
        printCurrentlyLoggedInAs(user);
    }

    public void printSuccessfulLogin(final LoggedInUser user) {
        System.out.println("Successful login!");
        printCurrentlyLoggedInAs(user);
        printTokens(user.getAuthTokens());
    }

    public void printTokens(final AuthTokens tokens) {
        System.out.println("Until refreshed, you can directly use these tokens to log in:");
        System.out.println(" --token " + tokens.getToken());
        System.out.println(" --refresh " + tokens.getRefreshToken());
    }

    public void printLogoutSuccessful() {
        System.out.println("Successfully logged out!");
    }

    public void printActionRequiresLogin() {
        System.out.println("The action you tried to call requires a preceding login!");
    }

    public void printGnuImportSuccessful(final String file) {
        System.out.println("The following GNU vocabulary has been imported successfully: " + file);
    }

    public void printSuccessfulRegistration(final LoggedInUser loggedInUser) {
        System.out.println("Welcome on board, " + loggedInUser.getFirstName() + "! (successful registration)");
        printTokens(loggedInUser.getAuthTokens());
    }

    public void printOptionalParamsInfo(final Set<String> givenKeys, final String... argKeys) {
        final List<String> validGiven = givenKeys.stream()
                .filter(gk -> Arrays.asList(argKeys).contains(gk))
                .collect(Collectors.toList());

        System.out.println("This command accepts the following optional params: " + Arrays.stream(argKeys).collect(Collectors.toList()));
        System.out.println("Calling it with param(s): " + validGiven + " (unknown params are ignored in this list)");
    }

    public void printSuccessfulUserUpdate(final User user) {
        System.out.println("User data has been updated successfully: " + user.toString());
    }

    public void printSuccessfulPwdUpdate() {
        System.out.println("Password has been updated successfully!");
    }

    public void printInvalidAuthToken() {
        System.out.println("Invalid/expired auth token. Don't worry. Token is tried to be refreshed...");
    }

    public void printInvalidRefreshToken() {
        System.out.println("Invalid/expired refresh token. You have been logged out.");
    }

    public void printSuccessfullyRefreshedTokens(final AuthTokens tokens) {
        System.out.println("Successfully refreshed tokens!");
        printTokens(tokens);
    }

    public void printInvalidAuthTokenInLogin() {
        System.out.println("Invalid/expired auth token. Don't worry. The stated refresh token is tried to be used...");
    }

    public void printInvalidLoginWithToken() {
        System.out.println("Invalid login. This means that either both the auth and the refresh token are invalid or that they do not belong to an existing user.");
    }

    public void printSuccessfulLoginWithToken(final LoggedInUser user) {
        System.out.println("Successful login using JWT token(s)!");
        printCurrentlyLoggedInAs(user);
    }

    public void printSuccessfulLoginWithRefreshedToken(final LoggedInUser user) {
        printSuccessfulLoginWithToken(user);
        System.out.println("The tokens have been updated during this process.");
        printTokens(user.getAuthTokens());
    }

    public void printSupportedLanguages(final List<List<String>> languages) {
        System.out.println("Here's a list of all supported languages incl. names they can be referred to:");
        languages.forEach(langList -> {
            System.out.print(" - " + String.format("%-3s", langList.get(0)));
            System.out.println(" => [" + langList.stream().skip(1).collect(Collectors.joining(" - ")) + "]");
        });
    }

    public void printSupportedLanguagesCodeOnly(final List<SupportedLanguage> languages) {
        System.out.println("Here's a list of all supported languages (codes only):");
        languages.stream().sorted().forEach(l -> System.out.println(" - " + l));
    }

    public void printConfigurableThroughParam(final String param, final List<String[]> options) {
        System.out.println("Not seeing all information you would like to see? This action is configurable through the param \"" + param + "\"");
        int i = 0;
        for (String[] o : options) {
            System.out.print("  --" + param + " " + o[0]);
            if (i++ == 0) System.out.print(" (default)");
            System.out.println(" => " + o[1]);
        }
    }

    public void printLanguageSets(final List<LanguageSet> languageSets, String level) {
        if (languageSets != null && !languageSets.isEmpty()) {
            int depth = 1;
            if (level != null && level.equals("unit")) depth = 2;
            else if (level != null && level.equals("list")) depth = 3;
            else if (level != null && level.equals("vocab")) depth = 4;

            final List<LanguageSet> sortedLs = languageSets
                    .stream()
                    .sorted(Comparator.comparing(ls -> ls.getLearntLanguage() + " " + ls.getKnownLanguage()))
                    .collect(Collectors.toList());

            final StringBuilder toBePrinted = new StringBuilder();

            for (int i = 0; i < sortedLs.size(); i++) {
                toBePrinted
                        .append("|-- ")
                        .append(String.format("%-3s", sortedLs.get(i).getLearntLanguage()))
                        .append(" => ")
                        .append(String.format("%-3s", sortedLs.get(i).getKnownLanguage()))
                        .append("(")
                        .append(sortedLs.get(i).getVocableUnits().size())
                        .append(" vocable units)\n");

                if (depth > 1) {
                    final List<VocableUnit> sortedVu = languageSets.get(i).getVocableUnits()
                            .stream().sorted(Comparator.comparing(VocableUnit::getTitle))
                            .collect(Collectors.toList());
                    for (int j = 0; j < sortedVu.size(); j++) {
                        if (i == sortedLs.size() - 1) toBePrinted.append("    |-- ");
                        else toBePrinted.append("|   |-- ");
                        toBePrinted
                                .append(sortedVu.get(j).getTitle())
                                .append(" (")
                                .append(sortedVu.get(j).getVocableLists().size())
                                .append(" vocable lists)\n");

                        if (depth > 2) {
                            final List<VocableList> sortedVl = sortedVu.get(j).getVocableLists()
                                    .stream().sorted(Comparator.comparing(VocableList::getTitle))
                                    .collect(Collectors.toList());
                            for (int k = 0; k < sortedVl.size(); k++) {
                                if (i == sortedLs.size() - 1) toBePrinted.append("    ");
                                else toBePrinted.append("|   ");
                                if (j == sortedVu.size() - 1) toBePrinted.append("    ");
                                else toBePrinted.append("|   ");
                                toBePrinted.append("|-- ");

                                toBePrinted.append(createVocableListTitleString(sortedVl.get(k)));

                                if (depth > 3) {
                                    StringBuilder prefix = new StringBuilder();
                                    if (i == sortedLs.size() - 1) prefix.append("    ");
                                    else prefix.append("|   ");
                                    if (j == sortedVu.size() - 1) prefix.append("    ");
                                    else prefix.append("|   ");
                                    if (k == sortedVl.size() - 1) prefix.append("    ");
                                    else prefix.append("|   ");
                                    toBePrinted.append(createVocableListString(prefix.toString(), sortedVl.get(k)));
                                }
                            }
                        }
                    }
                }
            }

            System.out.println(toBePrinted);
        } else System.out.println("No language sets/vocables yet. Be the one to change that by importing a GNU file!");
    }

    public void printVocableList(final VocableList vocableList) {
        System.out.println(createVocableListTitleString(vocableList) + createVocableListString(vocableList));
    }

    private String createVocableListString(final VocableList vocableList) {
        return createVocableListString("", vocableList);
    }

    private String createVocableListString(final String vocablePrefix, final VocableList vocableList) {
        final StringBuilder toBePrinted = new StringBuilder();
        final List<Vocable> sortedVocables = vocableList.getVocables()
                .stream().sorted(Comparator.comparing(v -> v.getVocable().getSynonyms().get(0)))
                .collect(Collectors.toList());
        for (final Vocable vocable : sortedVocables) {
            toBePrinted.append(vocablePrefix).append("|-- ").append(vocable.getVocable().getSynonyms());
            if (vocable.getVocable().getExemplarySentencesOrAdditionalInfo() != null &&
                    vocable.getVocable().getExemplarySentencesOrAdditionalInfo().size() > 0
            ) {
                toBePrinted.append(vocable.getVocable().getExemplarySentencesOrAdditionalInfo());
            }
            toBePrinted.append(" => ");

            vocable.getTranslations().forEach(t -> {
                toBePrinted
                        .append("[")
                        .append(String.join(", ", t.getSynonyms()));
                if (t.getExemplarySentencesOrAdditionalInfo() != null &&
                        t.getExemplarySentencesOrAdditionalInfo().size() > 0
                ) {
                    toBePrinted.append(" (");
                    t.getExemplarySentencesOrAdditionalInfo().forEach(toBePrinted::append);
                    toBePrinted.append(")");
                }
                toBePrinted.append("]");
            });

            toBePrinted.append("\n");
        }
        return toBePrinted.toString();
    }

    private String createVocableListTitleString(final VocableList vocableList) {
        return "[ID: " +
                vocableList.getId() +
                "] " +
                vocableList.getTitle() +
                " (" +
                vocableList.getVocables().size() +
                " vocables, added by user " +
                vocableList.getAuthor().getFirstName() +
                " " +
                vocableList.getAuthor().getLastName() +
                " (" +
                vocableList.getAuthor().getUsername() +
                ") on " +
                vocableList.getTimestamp().toString() +
                ")\n";
    }

    public void printInvalidIdFormat(final String id) {
        System.out.println("Invalid Id. \"" + id + "\" cannot be parsed!");
    }

    public void printInvalidIdPartFormat(final String ids) {
        System.out.println("At least one id is not valid. \"" + ids + "\" cannot (white spaces = split) be parsed!");
    }

    public void printNoVocableListFound() {
        System.out.println("No vocable list found for the given ID.");
    }

    public void printDeterminingUserBy(final String param) {
        System.out.println("Determining user by " + param + "...");
    }

    public void printCouldNotDetermineUser() {
        System.out.println("Could not determine user.");
    }

    public void printPleaseAddParamForUser() {
        System.out.println("Please add one of the params listed above in order to find a user.");
    }

    public void printPleaseAddParamForUserUpdate() {
        System.out.println("Please add one of the params listed above in order to update a user property.");
    }

    public void printVocableListsByUser(final User user, final List<VocableList> vocableLists) {
        System.out.println(user.toString() + " has imported the following " + vocableLists.size() + " vocable list(s):");
        System.out.println(vocableLists
                .stream()
                .map(vl -> " - " + createVocableListTitleString(vl) + createVocableListString("   ", vl))
                .collect(Collectors.joining("\n"))
        );
        System.out.println("[end of lists imported by " + user + "]");
    }

    public void printNoVocableListsByUser(final User user) {
        System.out.println("No vocable lists for " + user.toString());
    }

    public void printUser(final User user) {
        System.out.println(user.toString());
    }

    public void printSuccessfullyDeletedVocabList(final long id) {
        System.out.println("Successfully deleted vocable list with ID " + id);
    }

    public void printNoUsersFound() {
        System.out.println("No users found for your search string.");
    }

    public void printFoundUsers(final List<User> users, final String searchStr) {
        System.out.println("Here's a list of all users found for your search string \"" + searchStr + "\" (" + users.size() + " hit(s)):");
        for (final User u : users) System.out.println("..." + u);
    }

    public void printSuccessfullyStaredGame(final RunningVocabduelGame game, final String seeRoundCmd) {
        System.out.println("Game against \"" + game.getPlayerB().getUsername() + "\" has been started successfully. [Game ID: " + game.getId() + "]");
        System.out.println("In order to start a round/see your first question, run: " + seeRoundCmd + " --id " + game.getId());
    }

    public void printGames(final List<RunningVocabduelGame> games, final User self) {
        if (games == null || games.isEmpty()) System.out.println("No games yet.");
        else {
            StringBuilder str = new StringBuilder("Here's a list of your current games:\n");
            games.forEach(g -> {
                str.append("... [Game with ID ").append(g.getId()).append("] ");
                final boolean isSelfInitiator = self.getId().equals(g.getPlayerA().getId());
                final long countA = g.getRounds().stream().filter(r -> r.getResultPlayerA() != null).count();
                final long countB = g.getRounds().stream().filter(r -> r.getResultPlayerB() != null).count();
                if (isSelfInitiator) {
                    str
                            .append("You challenged \"")
                            .append(g.getPlayerB().getUsername())
                            .append("\" (ID: ")
                            .append(g.getPlayerB().getId())
                            .append(")! ");
                } else {
                    str
                            .append("\"")
                            .append(g.getPlayerA().getUsername())
                            .append("\" (ID: ")
                            .append(g.getPlayerA().getId())
                            .append(") has challenged you! ");
                }
                str
                        .append("[You finished: ")
                        .append(isSelfInitiator ? countA : countB)
                        .append("/").append(GameService.NR_OF_ROUNDS)
                        .append(" rounds, opponent: ")
                        .append(isSelfInitiator ? countB : countA)
                        .append("/")
                        .append(GameService.NR_OF_ROUNDS)
                        .append("]\n");
            });
            System.out.println(str);
        }
    }

    public void printQuestionAndAnswers(final VocabduelRound round, final String answerCmd) {
        final StringBuilder qAndA = new StringBuilder("Round " + round.getRoundNr() + ") " + round.getQuestion().getVocable().getSynonyms());
        final List<String> hints = round.getQuestion().getVocable().getExemplarySentencesOrAdditionalInfo();
        if (hints != null && !hints.isEmpty()) qAndA.append(" (Hint/additional information: ").append(hints);

        final int asciiA = 97;
        for (int i = asciiA; i < asciiA + round.getAnswers().size(); i++) {
            final TranslationGroup translationGroup = round.getAnswers().get(i - asciiA);
            final List<String> answerHints = translationGroup.getExemplarySentencesOrAdditionalInfo();
            qAndA
                    .append("\n...")
                    .append((char) i)
                    .append(") ")
                    .append(translationGroup.getSynonyms());
            if (answerHints != null && !answerHints.isEmpty()) {
                qAndA.append(" (Hint/additional information: ").append(answerHints);
            }
        }

        qAndA.
                append("\n\nTo answer, run \"")
                .append(answerCmd)
                .append(" --id ")
                .append(round.getGame().getId())
                .append(" --round ")
                .append(round.getRoundNr())
                .append(" --answer <'a', 'b', 'c' or 'd'>\"");
        System.out.println(qAndA);
    }

    public void printRoundResultWin() {
        System.out.println("That's true! Well done.");
    }

    public void printRoundResultLoss(final TranslationGroup correctAnswer) {
        final StringBuilder builder = new StringBuilder("That's wrong! The right answer would've been: " + correctAnswer.getSynonyms());
        final List<String> hints = correctAnswer.getExemplarySentencesOrAdditionalInfo();
        if (hints != null && !hints.isEmpty()) builder.append(" (Hint/additional information: ").append(hints);
        System.out.println(builder);
    }

    public void printNoValidAnswer() {
        System.out.println("No valid answer! Must be 'a', 'b', 'c' or 'd'");
    }

    public void printGameNotFinishedByOpponent() {
        System.out.println("Your opponent has not answered all questions yet. Only then, the game will be marked as closed. Come back and check your score stats in order to see if you won!");
    }

    public void printOwnScores(final List<PersonalFinishedGame> hist) {
        final StringBuilder str = new StringBuilder("Here's a list of your scores:");
        hist.stream().sorted(Comparator.comparing(PersonalFinishedGame::getFinishedTimestamp))
                .forEach(h -> str
                        .append("\n...")
                        .append(h.getFinishedTimestamp())
                        .append(" => ")
                        .append(h.getGameResult())
                        .append(" (Your points: ")
                        .append(h.getOwnPoints())
                        .append(" - opponent's: ")
                        .append(h.getOpponentPoints())
                        .append(") against ")
                        .append(h.getOpponent().toString()));
        System.out.println(str);
    }

    public void printUserScores(final List<PersonalFinishedGame> hist, final User user) {
        final StringBuilder str = new StringBuilder("Here's a list of scores for " + user.toString() + ":");
        hist.stream().sorted(Comparator.comparing(PersonalFinishedGame::getFinishedTimestamp))
                .forEach(h -> str
                        .append("\n...")
                        .append(h.getFinishedTimestamp())
                        .append(" => ")
                        .append(h.getGameResult())
                        .append(" (Points: ")
                        .append(h.getOwnPoints())
                        .append(" - opponent's: ")
                        .append(h.getOpponentPoints())
                        .append(") against ")
                        .append(h.getOpponent().toString()));
        System.out.println(str);
    }

    public void printNoFinishedGamesYet() {
        System.out.println("The given user does not seem to have finished any game yet!");
    }

    public void printRecord(final ScoreRecord recordOfUser) {
        System.out.println(recordOfUser.toString());
    }

    public void printFinishedGame(final PersonalFinishedGame finishedGame) {
        System.out.println(
                "Game finished: " + finishedGame.getGameResult().toString() +
                        " (Your points: " + (finishedGame.getOwnPoints()) +
                        (" - Opponent \"" + finishedGame.getOpponent().getUsername() + "\": ") +
                        (finishedGame.getOpponentPoints() + ")")
        );
    }

    public void printVocableListInRunningGame() {
        System.out.println("This list seems to be referenced by at least one running game. Until finished, this list cannot be deleted.");
    }
}
