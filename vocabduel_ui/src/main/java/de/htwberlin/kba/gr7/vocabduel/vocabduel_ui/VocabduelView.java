package de.htwberlin.kba.gr7.vocabduel.vocabduel_ui;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.AuthTokens;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.LoggedInUser;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabduel_ui.model.VocabduelCliAction;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.SupportedLanguage;
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
}
