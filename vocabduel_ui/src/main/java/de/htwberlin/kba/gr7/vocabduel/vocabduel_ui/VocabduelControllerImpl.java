package de.htwberlin.kba.gr7.vocabduel.vocabduel_ui;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.AuthService;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.UserService;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.*;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.LoggedInUser;
import de.htwberlin.kba.gr7.vocabduel.vocabduel_ui.export.VocabduelController;
import de.htwberlin.kba.gr7.vocabduel.vocabduel_ui.model.VocabduelCliAction;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.VocabularyService;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.exceptions.DataAlreadyExistsException;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.exceptions.DuplicateVocablesInSetException;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.exceptions.IncompleteVocableListException;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Controller
public class VocabduelControllerImpl implements VocabduelController {

    private final VocabduelView VIEW;
    private final CliSessionStorage STORAGE;

    private final AuthService AUTH_SERVICE;
    private final UserService USER_SERVICE;
    private final VocabularyService VOCABULARY_SERVICE;

    private final Pattern PARAM_PATTERN = Pattern.compile("--[a-z]+\\s((?!--).)+");

    private HashMap<String, VocabduelCliAction> actions;
    private List<VocabduelCliAction> actionsList;

    private boolean isQuit = false;

    public VocabduelControllerImpl(
            final VocabduelView view,
            final CliSessionStorage storage,
            final AuthService authService,
            final UserService userService,
            final VocabularyService vocabularyService
    ) {
        VIEW = view;
        STORAGE = storage;
        AUTH_SERVICE = authService;
        USER_SERVICE = userService;
        VOCABULARY_SERVICE = vocabularyService;
    }

    @Override
    public void run() {
        VIEW.printHello();
        initializeFunctionsList();
        initializeFunctionsMap();


        // TODO: RM
        try {
            handleUserInput("login", new String[]{"--pwd", "123", "--email", "arnie1947@mail.at"});
        } catch (Exception e) {
            System.err.println("Initial login error");
            e.printStackTrace();
        }

        do {
            try {
                final String input = VIEW.scanInput().trim();
                final String actionName = input.split("--")[0].trim();
                final String[] userInputArgs = input.contains("--")
                        ? input.substring(input.indexOf("--")).split("\\s+")
                        : null;
                handleUserInput(actionName, userInputArgs);
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
        actionsList.add(new VocabduelCliAction(true, "vocab import", "Import a GNU vocabulary list", "vi", this::onVocableImportCalled, "file"));
        actionsList.add(new VocabduelCliAction(true, "vocab import samples", "Import default vocabulary lists", "vis", this::onVocableSampleCalled));
        actionsList.add(new VocabduelCliAction(false, "register", "Sign up as a new user", "r", this::onRegistrationCalled, "email", "username", "firstname", "lastname", "pwd", "confirm"));
        actionsList.add(new VocabduelCliAction(true, "update user", "Update the currently logged in user's data", "u user", this::onUpdateCalled));
        actionsList.add(new VocabduelCliAction(true, "update pwd", "Update the currently logged in user's password", "u pwd", this::onUpdatePwdCalled, "currentpwd", "newpwd", "confirm"));
        actionsList.add(new VocabduelCliAction(true, "whoami", "See current user data", this::onWhoAmICalled));
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

    private void handleUserInput(final String actionName, final String[] userInputArgs) throws Exception {
        final VocabduelCliAction action = actions.get(actionName);
        if (action == null) VIEW.printUnknownParam(actionName);
        else if (action.isGuarded() && STORAGE.getLoggedInUser() == null) {
            VIEW.printActionRequiresLogin();
        } else if (action.getNoArgsAction() != null) action.getNoArgsAction().run();
        else {
            final HashMap<String, String> args = createArgsMap(userInputArgs);
            checkRequiredArgs(action, args);
            action.getAction().accept(args);
        }
    }

    private HashMap<String, String> createArgsMap(final String[] params) throws Exception {
        final HashMap<String, String> map = new HashMap<>();

        if (params != null && params.length > 0) {
            final String joinedParams = (String.join(" ", params));
            final boolean containsIllegalParams = joinedParams.split(String.valueOf(PARAM_PATTERN)).length != 0;
            if (joinedParams.charAt(0) != '-' || joinedParams.charAt(1) != '-' || containsIllegalParams) {
                throw new Exception(
                        "Invalid param format. Please call actions as follows:\n<action name> --<arg key> <arg value (lower case)> --<second arg key (lower case)> <second arg value> (args must match: \""
                                + PARAM_PATTERN + "\")"
                );
            } else {
                final Matcher matcher = PARAM_PATTERN.matcher(joinedParams);
                while (matcher.find()) {
                    final String key = matcher.group(0).split(" ")[0].substring(2);
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
                final String paramsExample = Arrays.stream(action.getRequiredArgs()).map(p -> "--" + p + " <value>").collect(Collectors.joining(" "));
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

    private void onLoginCalled(final HashMap<String, String> args) {
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

    private void onVocableImportCalled(final HashMap<String, String> args) {
        Scanner input = null;
        try {
            input = new Scanner(new File(args.get("file")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (input != null) {
            final StringBuilder gnuContent = new StringBuilder();
            while (input.hasNext()) gnuContent.append(input.nextLine()).append("\n");
            input.close();

            try {
                final int result = VOCABULARY_SERVICE.importGnuVocableList(gnuContent.toString(), STORAGE.getLoggedInUser());
                if (result == 0) VIEW.printGnuImportSuccessful(args.get("file"));
            } catch (DuplicateVocablesInSetException | IncompleteVocableListException | DataAlreadyExistsException e) {
                e.printStackTrace();
            }
        }
    }

    private void onVocableSampleCalled() {
        final String path = "./vocabduel_ui/assets/vocabulary/";
        for (String file : Objects.requireNonNull(new File(path).list())) {
            final HashMap<String, String> args = new HashMap<>();
            args.put("file", path + file);
            onVocableImportCalled(args);
        }
    }

    private void onRegistrationCalled(final HashMap<String, String> args) {
        if (STORAGE.getLoggedInUser() != null) VIEW.printLogoutBeforeLogin(STORAGE.getLoggedInUser());
        else {
            try {
                final LoggedInUser loggedInUser = AUTH_SERVICE.registerUser(args.get("username"), args.get("email"), args.get("firstname"), args.get("lastname"), args.get("pwd"), args.get("confirm"));
                STORAGE.setLoggedInUser(loggedInUser);
                VIEW.printSuccessfulRegistration(loggedInUser);
            } catch (PasswordsDoNotMatchException | PwTooWeakException | InvalidOrRegisteredMailException | AlreadyRegisteredUsernameException | IncompleteUserDataException e) {
                e.printStackTrace();
            }
        }
    }

    private void onUpdateCalled(final HashMap<String, String> args) {
        VIEW.optionalParamsInfo(args.keySet(), "username", "email", "firstname", "lastname");
        final LoggedInUser user = STORAGE.getLoggedInUser();

        final String prevUsername = user.getUsername();
        final String prevFirstname = user.getFirstName();
        final String prevLastname = user.getLastName();
        final String prevEmail = user.getEmail();

        if (args.get("username") != null) user.setUsername(args.get("username"));
        if (args.get("email") != null) user.setEmail(args.get("email"));
        if (args.get("firstname") != null) user.setFirstName(args.get("firstname"));
        if (args.get("lastname") != null) user.setLastName(args.get("lastname"));

        try {
            USER_SERVICE.updateUser(user);
            VIEW.successfulUserUpdate(user);
        } catch (InvalidOrRegisteredMailException | AlreadyRegisteredUsernameException | IncompleteUserDataException e) {
            user.setEmail(prevEmail);
            user.setUsername(prevUsername);
            user.setFirstName(prevFirstname);
            user.setLastName(prevLastname);
            e.printStackTrace();
        }
    }

    private void onUpdatePwdCalled(final HashMap<String, String> args) {
        try {
            USER_SERVICE.updateUserPassword(STORAGE.getLoggedInUser(), args.get("currentpwd"), args.get("newpwd"), args.get("confirm"));
            VIEW.successfulPwdUpdate();
        } catch (InvalidFirstPwdException | PasswordsDoNotMatchException | PwTooWeakException e) {
            e.printStackTrace();
        }
    }

    private void onWhoAmICalled() {
        VIEW.printCurrentlyLoggedInAs(STORAGE.getLoggedInUser());
    }
}
