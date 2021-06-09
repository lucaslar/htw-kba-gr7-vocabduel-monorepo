package de.htwberlin.kba.gr7.vocabduel.vocabduel_ui;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.AuthService;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.LoggedInUser;
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
    private final CliSessionStorage STORAGE;

    private final AuthService AUTH_SERVICE;

    private final Pattern PARAM_PATTERN = Pattern.compile("-[a-z]+\\s((?!-).)+");

    private HashMap<String, VocabduelCliAction> actions;
    private List<VocabduelCliAction> actionsList;

    private boolean isQuit = false;

    public VocabduelControllerImpl(
            final VocabduelView view,
            final CliSessionStorage storage,
            final AuthService authService
    ) {
        VIEW = view;
        STORAGE = storage;
        AUTH_SERVICE = authService;
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
        actionsList.add(new VocabduelCliAction(false, "help", "Get a list of all possible actions", "h", this::onHelpCalled));
        actionsList.add(new VocabduelCliAction(false, "quit", "Quit this application", "q", this::onQuitCalled));
        actionsList.add(new VocabduelCliAction(false, "login", "Sign in with an existing account", "li", this::onLoginCalled, "email", "pwd"));
        actionsList.add(new VocabduelCliAction(true, "logout", "Log out from the application", "lo", this::onLogoutCalled));

        // TODO only for testing => rm
        actionsList.add(new VocabduelCliAction(false, "argtest", "test fn to be removed soon!", "at", (HashMap<String, String> args) -> {
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
        else if (action.isGuarded() && STORAGE.getLoggedInUser() == null) {
            VIEW.printActionRequiresLogin();
        }
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
        VIEW.printHelp(actionsList);
    }

    private void onQuitCalled() {
        VIEW.printQuit();
        isQuit = true;
    }

    private void onLoginCalled(HashMap<String, String> args) {
        if (STORAGE.getLoggedInUser() != null) VIEW.printLogoutBeforeLogin(STORAGE.getLoggedInUser());
        else {
            final LoggedInUser user = AUTH_SERVICE.loginUser(args.get("email"), args.get("pwd"));
            if (user == null) VIEW.printInvalidLogin();
            else {
                STORAGE.setLoggedInUser(user);
                VIEW.printSuccessfulLogin(user);
            }
        }
    }

    private void onLogoutCalled() {
        STORAGE.setLoggedInUser(null);
        VIEW.printLogoutSuccessful();
    }
}
