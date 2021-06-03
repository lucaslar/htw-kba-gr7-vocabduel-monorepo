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
        actionsList.add(new VocabduelCliAction("help", "Get a list of all possible actions", "h", this::onHelpCalled));
        actionsList.add(new VocabduelCliAction("quit", "Quit this application", "q", this::onQuitCalled));
        actionsList.add(new VocabduelCliAction("login", "Sign in with an existing account", "l", this::onLoginCalled, "user", "pwd"));

        // TODO only for testing => rm
        actionsList.add(new VocabduelCliAction("argtest", "test fn to be removed soon!", "at", (HashMap<String, String> args) -> {
            System.out.println("Test fn has been called with " + args.keySet().size() + " arg(s)");
            args.keySet().forEach(k -> System.out.println("..." + k + " => " + args.get(k)));
        }, "reqa", "reqb"));
    }

    private void initializeFunctionsMap() {
        actions = new HashMap<>();
        for (final VocabduelCliAction action : actionsList) {
            safelyPutFunctionToMap(action.getName(), action);
            if (action.getShortName() != null) safelyPutFunctionToMap(action.getShortName(), action);
        }
    }

    private void safelyPutFunctionToMap(String key, VocabduelCliAction value) {
        if (actions.get(key) == null) actions.put(key, value);
        else VIEW.printWarningActionKey(key);
    }

    private void handleUserInput(final String[] userInput) throws Exception {
        final String actionName = userInput[0];
        final VocabduelCliAction action = actions.get(actionName);

        if (action == null) VIEW.printUnknownParam(actionName);
        else if (action.getNoArgsAction() != null) action.getNoArgsAction().run();
        else {
            final HashMap<String, String> args = createArgsMap(userInput);
            checkRequiredArgs(action, args);
            action.getAction().accept(args);
        }
    }

    private HashMap<String, String> createArgsMap(final String[] userInput) throws Exception {
        final HashMap<String, String> map = new HashMap<>();
        final String[] params = Arrays.copyOfRange(userInput, 1, userInput.length);

        if (params.length > 0) {
            final String joinedParams = (String.join(" ", params));
            final boolean containsIllegalParams = joinedParams.split(String.valueOf(PARAM_PATTERN)).length != 0;
            if (joinedParams.charAt(0) != '-' || containsIllegalParams) {
                throw new Exception(
                        "Invalid param format. Please call actions as follows:\n<action name> -<arg key> <arg value (lower case)> -<second arg key (lower case)> <second arg value> (args must match: \""
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

    private void checkRequiredArgs(final VocabduelCliAction action, final HashMap<String, String> args) throws Exception {
        if (action.getRequiredArgs() != null) {
            final List<String> missingParams = Arrays
                    .stream(action.getRequiredArgs())
                    .filter(r -> args.get(r) == null)
                    .collect(Collectors.toList());
            if (missingParams.size() > 0) {
                final String missingJoined = String.join(", ", missingParams);
                final String paramsExample = Arrays.stream(action.getRequiredArgs()).map(p -> "-" + p + " <value>").collect(Collectors.joining(" "));
                throw new Exception("The following params are required for `" + action.getName() + "` but where missing in your command: "
                        + missingJoined + "\nPlease run: " + action.getName() + " " + paramsExample);
            }
        }
    }

    private void onHelpCalled() {
        VIEW.printHelp(actionsList.stream().map(a -> {
                    final String cmd = a.getShortName() != null
                            ? "`" + a.getName() + "` or `" + a.getShortName() + "`"
                            : "`" + a.getName() + "`";
                    final String requiredArgs = a.getRequiredArgs() != null
                            ? "[Required args: " + String.join(", ", a.getRequiredArgs()) + "]"
                            : "[No required args]";
                    return cmd + requiredArgs + " => " + " " + a.getDescription();
                }
        ).sorted().collect(Collectors.toList()));
    }

    private void onQuitCalled() {
        VIEW.printQuit();
        isQuit = true;
    }

    private void onLoginCalled(HashMap<String, String> args) {
        // TODO implement (Example for function with args)
        System.out.println("login as user \"" + args.get("user") + "\" with password \"" + args.get("pwd") + "\"");
    }
}
