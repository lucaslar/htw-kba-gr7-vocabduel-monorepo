package de.htwberlin.kba.gr7.vocabduel.vocabduel_ui;

import de.htwberlin.kba.gr7.vocabduel.vocabduel_ui.export.VocabduelController;
import de.htwberlin.kba.gr7.vocabduel.vocabduel_ui.model.VocabduelCliAction;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Consumer;

@Controller
public class VocabduelControllerImpl implements VocabduelController {

    private final VocabduelView VIEW;
    private final HashMap<String, VocabduelCliAction> ACTIONS = new HashMap<String, VocabduelCliAction>();

    private boolean isQuit = false;

    public VocabduelControllerImpl(final VocabduelView view) {
        this.VIEW = view;
    }

    @Override
    public void run() {
        VIEW.printHello();
        initializeFunctions();

        do {
            final String[] userInput = VIEW.scanInput().trim().split("\\s+");
            handleUserInput(userInput[0], Arrays.copyOfRange(userInput, 1, userInput.length));
        } while (!isQuit);
    }

    private void initializeFunctions() {
        // Exemplary function / TODO remove
        final Consumer<String[]> testFn = (String... args) -> {
            System.out.println("Called help fn with " + args.length + " arg(s)");
            for (final String arg : args) System.out.println(" => " + arg);
        };

        final VocabduelCliAction[] actionsList = {
                new VocabduelCliAction("help", "Get a list of all possible actions", testFn, "h"),
                new VocabduelCliAction("quit", "Quit this application", (String... args) -> isQuit = true, "q")
        };

        for (final VocabduelCliAction action : actionsList) {
            ACTIONS.put(action.getName(), action);
            if (action.getShortName() != null) ACTIONS.put(action.getShortName(), action);
        }
    }

    private void handleUserInput(String actionName, String... args) {
        final VocabduelCliAction action = ACTIONS.get(actionName);
        if (action == null) VIEW.printUnknownParam(actionName);
        else action.getAction().accept(args);
    }
}
