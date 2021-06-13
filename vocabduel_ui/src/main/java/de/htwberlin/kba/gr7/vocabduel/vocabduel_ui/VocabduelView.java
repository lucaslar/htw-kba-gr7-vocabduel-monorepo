package de.htwberlin.kba.gr7.vocabduel.vocabduel_ui;

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

    public void printSuccessfulRegistration(User loggedInUser) {
        System.out.println("Welcome on board, " + loggedInUser.getFirstName() + "! (successful registration)");
    }

    public void printOptionalParamsInfo(final Set<String> givenKeys, final String... argKeys) {
        final List<String> validGiven = givenKeys.stream()
                .filter(gk -> Arrays.asList(argKeys).contains(gk))
                .collect(Collectors.toList());

        System.out.println("This function accepts the following optional params: " + Arrays.stream(argKeys).collect(Collectors.toList()));
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
        System.out.println("\nNot seeing all information you would like to see? This action is configurable though the param \"" + param + "\"");
        int i = 0;
        for (String[] o : options) {
            System.out.print("  --" + param + " " + o[0]);
            if (i++ == 0) System.out.print(" (default)");
            System.out.println(" => " + o[1]);
        }
    }

    public void printLanguageSets(final List<LanguageSet> languageSets, String level) {
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
                " (@" +
                vocableList.getAuthor().getUsername() +
                ") on " +
                vocableList.getTimestamp().toString() +
                ")\n";
    }

    public void printInvalidIdFormat(final String id) {
        System.out.println("Invalid Id. \"" + id + "\" cannot be parsed!");
    }

    public void printNoVocableListFound() {
        System.out.println("No vocable list found for the given ID.");
    }
}
