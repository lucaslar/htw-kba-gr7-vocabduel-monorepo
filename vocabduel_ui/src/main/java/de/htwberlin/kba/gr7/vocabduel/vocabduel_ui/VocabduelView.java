package de.htwberlin.kba.gr7.vocabduel.vocabduel_ui;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

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

    public void printHelp(final List<String[]> actionAndDescription) {
        System.out.println("Here's a list of all actions available in this application:");
        for (final String[] ad : actionAndDescription) System.out.println("... " + ad[0] + " => " + ad[1]);
    }

    public void printQuit() {
        System.out.println("Bye bye");
    }

    public void printNoParamFor(final String actionName, final Pattern regex) {
        System.out.println("! Hint: No params were given for `" + actionName + "`. This is not false, but might not result in what you want. Thus, please consider:");
        System.out.println("!       " + actionName + " -argKey Some Value -<argKey2> 123");
        System.out.println("!       (Must match: \"" + regex+"\")");
    }

    public void printWarningActionKey(final String key) {
        System.err.println("Dev Info: `" + key + "` does already exist and will not be put into the action map. Please fix this problem!");
    }
}
