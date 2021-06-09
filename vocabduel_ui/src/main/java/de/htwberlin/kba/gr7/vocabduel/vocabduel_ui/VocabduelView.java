package de.htwberlin.kba.gr7.vocabduel.vocabduel_ui;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.LoggedInUser;
import de.htwberlin.kba.gr7.vocabduel.vocabduel_ui.model.VocabduelCliAction;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class VocabduelView {

    private final Scanner SCANNER = new Scanner(System.in);

    public String scanInput() {
        System.out.print("\n$ ");
        return SCANNER.nextLine();
    }

    public void printHello() {
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
                    final String requiredArgs = a.getRequiredArgs() != null
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

    public void printCurrentlyLoggedInAs(final LoggedInUser user) {
        System.out.println("Logged in as " + user.getFirstName() + " " + user.getLastName() + " (" + user.getUsername() + " / " + user.getEmail() + ")");
    }

    public void printLogoutBeforeLogin(final LoggedInUser user) {
        System.out.println("You are already logged in. Please logout before re-logging in.");
        printCurrentlyLoggedInAs(user);
    }

    public void printSuccessfulLogin(final LoggedInUser user) {
        System.out.println("Successful login!");
        printCurrentlyLoggedInAs(user);
    }

    public void printLogoutSuccessful() {
        System.out.println("Successfully logged out!");
    }

    public void printActionRequiresLogin() {
        System.out.println("The action you tried to call requires a preceding login!");
    }
}
