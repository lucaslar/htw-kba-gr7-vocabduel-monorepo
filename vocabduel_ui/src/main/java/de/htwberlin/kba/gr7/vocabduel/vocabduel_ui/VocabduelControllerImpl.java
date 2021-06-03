package de.htwberlin.kba.gr7.vocabduel.vocabduel_ui;

import de.htwberlin.kba.gr7.vocabduel.vocabduel_ui.export.VocabduelController;
import de.htwberlin.kba.gr7.vocabduel.vocabduel_ui.model.VocabduelCliAction;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class VocabduelControllerImpl implements VocabduelController {

    private final VocabduelView VIEW;

    private HashMap<String, VocabduelCliAction> actions;
    private List<VocabduelCliAction> actionsList;

    private boolean isQuit = false;

    public VocabduelControllerImpl(final VocabduelView view) {
        this.VIEW = view;
    }

    @Override
    public void run() {
        VIEW.printHello();
        initializeFunctionsList();
        initializeFunctionsMap();

        do {
            handleUserInput(VIEW.scanInput().trim().split("\\s+"));
        } while (!isQuit);
    }

    private void initializeFunctionsList() {
        actionsList = new ArrayList<>();
        actionsList.add(new VocabduelCliAction("help", "Get a list of all possible actions", this::onHelpCalled, "h"));
        actionsList.add(new VocabduelCliAction("quit", "Quit this application", this::onQuitCalled, "q"));
    }

    private void initializeFunctionsMap() {
        actions = new HashMap<>();
        for (final VocabduelCliAction action : actionsList) {
            actions.put(action.getName(), action);
            if (action.getShortName() != null) actions.put(action.getShortName(), action);
        }
    }

    private void handleUserInput(final String[] userInput) {
        final String actionName = userInput[0];
        final VocabduelCliAction action = actions.get(actionName);

        if (action == null) VIEW.printUnknownParam(actionName);
        else if (action.getNoArgsAction() != null) action.getNoArgsAction().run();
        else action.getAction().accept(createArgsMap(userInput));
    }

    private HashMap<String, String> createArgsMap(final String[] userInput) {
        final HashMap<String, String> map = new HashMap<>();
        final String[] params = Arrays.copyOfRange(userInput, 1, userInput.length);
        // TODO Implement
        return map;
    }

    private void onHelpCalled() {
        VIEW.printHelp(
                actionsList.stream().map(a -> new String[]{
                        a.getShortName() != null
                                ? "`" + a.getName() + "` or `" + a.getShortName() + "`"
                                : "`" + a.getName() + "`",
                        a.getDescription()
                }).collect(Collectors.toList())
        );
    }

    private void onQuitCalled() {
        VIEW.printQuit();
        isQuit = true;
    }

}
