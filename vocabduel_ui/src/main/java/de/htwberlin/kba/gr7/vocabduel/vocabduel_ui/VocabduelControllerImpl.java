package de.htwberlin.kba.gr7.vocabduel.vocabduel_ui;

import de.htwberlin.kba.gr7.vocabduel.vocabduel_ui.export.VocabduelController;
import de.htwberlin.kba.gr7.vocabduel.vocabduel_ui.model.VocabduelCliAction;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Controller
public class VocabduelControllerImpl implements VocabduelController {

    private final VocabduelView VIEW;

    private final Pattern PARAM_PATTERN = Pattern.compile("-[a-z]+\\s((?!-).)+");

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
            try {
                handleUserInput(VIEW.scanInput().trim().split("\\s+"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (!isQuit);
    }

    private void initializeFunctionsList() {
        actionsList = new ArrayList<>();
        actionsList.add(new VocabduelCliAction("help", "Get a list of all possible actions", this::onHelpCalled, "h"));
        actionsList.add(new VocabduelCliAction("quit", "Quit this application", this::onQuitCalled, "q"));

        // TODO only for testing => rm
        actionsList.add(new VocabduelCliAction("argtest", "test fn", (HashMap<String, String> args) -> {
            System.out.println("Test fn has been called with " + args.keySet().size() + " arg(s)");
            args.keySet().forEach(k -> System.out.println("..." + k + " => " + args.get(k)));
        }, "at"));
    }

    private void initializeFunctionsMap() {
        actions = new HashMap<>();
        for (final VocabduelCliAction action : actionsList) {
            savePutFunctionToMap(action.getName(), action);
            if (action.getShortName() != null) savePutFunctionToMap(action.getShortName(), action);
        }
    }

    private void savePutFunctionToMap(String key, VocabduelCliAction value) {
        if (actions.get(key) == null) actions.put(key, value);
        else VIEW.printWarningActionKey(key);
    }

    private void handleUserInput(final String[] userInput) throws Exception {
        final String actionName = userInput[0];
        final VocabduelCliAction action = actions.get(actionName);

        if (action == null) VIEW.printUnknownParam(actionName);
        else if (action.getNoArgsAction() != null) action.getNoArgsAction().run();
        else action.getAction().accept(createArgsMap(userInput));
    }

    private HashMap<String, String> createArgsMap(final String[] userInput) throws Exception {
        final HashMap<String, String> map = new HashMap<>();
        final String[] params = Arrays.copyOfRange(userInput, 1, userInput.length);

        if (params.length == 0) VIEW.printNoParamFor(userInput[0], PARAM_PATTERN);
        else {
            final String joinedParams = (String.join(" ", params));
            final boolean containsIllegalParams = joinedParams.split(String.valueOf(PARAM_PATTERN)).length != 0;
            if (joinedParams.charAt(0) != '-' || containsIllegalParams) {
                throw new Exception(
                        "Invalid param format. Please call actions as follows:\n<action name> -<arg key> <arg value (lower case)> -<second arg key (lower case)> <second arg value> (must match: \""
                                + PARAM_PATTERN + "\")"
                );
            } else {
                final Matcher matcher = PARAM_PATTERN.matcher(joinedParams);
                while (matcher.find()) {
                    final String key = matcher.group(0).split(" ")[0].substring(1);
                    final String value = matcher.group(0).substring(key.length() + 2).trim();
                    map.put(key, value);
                }
            }
        }

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
