package de.htwberlin.kba.gr7.vocabduel.vocabduel_ui;

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

    public void printHelp(final List<String[]> actionAndDescription) {
        System.out.println("Here's a list of all actions available in this application:");
        for (final String[] ad : actionAndDescription) System.out.println("... " + ad[0] + " => " + ad[1]);
    }

    public void printQuit() {
        System.out.println("Bye bye");
    }
}
