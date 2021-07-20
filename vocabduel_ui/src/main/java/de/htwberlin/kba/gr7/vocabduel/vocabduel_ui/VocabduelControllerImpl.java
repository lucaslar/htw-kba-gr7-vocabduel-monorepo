package de.htwberlin.kba.gr7.vocabduel.vocabduel_ui;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.GameService;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.ScoreService;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.*;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.*;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.AuthService;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.UserService;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.*;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.AuthTokens;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.LoggedInUser;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabduel_ui.export.VocabduelController;
import de.htwberlin.kba.gr7.vocabduel.vocabduel_ui.model.VocabduelCliAction;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.VocabularyService;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.exceptions.*;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.LanguageSet;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.VocableList;
import org.springframework.stereotype.Controller;

import javax.naming.InvalidNameException;
import javax.persistence.PersistenceException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Controller
public class VocabduelControllerImpl implements VocabduelController {
    private final String ACTION_KEY_HELP = "help";
    private final String ACTION_KEY_HELP_SHORT = "h";
    private final String ACTION_KEY_QUIT = "quit";
    private final String ACTION_KEY_QUIT_SHORT = "q";
    private final String ACTION_KEY_LOGIN = "login";
    private final String ACTION_KEY_LOGIN_SHORT = "li";
    private final String ACTION_KEY_LOGIN_JWT = "login jwt";
    private final String ACTION_KEY_LOGIN_JWT_SHORT = "lt";
    private final String ACTION_KEY_LOGOUT = "logout";
    private final String ACTION_KEY_LOGOUT_SHORT = "lo";
    private final String ACTION_KEY_VOCAB_IMPORT = "vocab import";
    private final String ACTION_KEY_VOCAB_IMPORT_SHORT = "v i";
    private final String ACTION_KEY_VOCAB_IMPORT_SAMPLES = "vocab import samples";
    private final String ACTION_KEY_VOCAB_IMPORT_SAMPLES_SHORT = "v i s";
    private final String ACTION_KEY_REGISTER = "register";
    private final String ACTION_KEY_REGISTER_SHORT = "r";
    private final String ACTION_KEY_USER_UPDATE = "user update";
    private final String ACTION_KEY_USER_UPDATE_SHORT = "u u";
    private final String ACTION_KEY_USER_UPDATE_PWD = "user update pwd";
    private final String ACTION_KEY_USER_UPDATE_PWD_SHORT = "u u pwd";
    private final String ACTION_KEY_WHOAMI = "whoami";
    private final String ACTION_KEY_LANG_LS = "lang ls";
    private final String ACTION_KEY_LANG_LS_CODES = "lang ls codes";
    private final String ACTION_KEY_LANG_LS_CODES_SHORT = "lang ls c";
    private final String ACTION_KEY_VOCAB_LS = "vocab ls";
    private final String ACTION_KEY_VOCAB_LS_SHORT = "v ls";
    private final String ACTION_KEY_VOCAB_FIND = "vocab find";
    private final String ACTION_KEY_VOCAB_FIND_SHORT = "v find";
    private final String ACTION_KEY_VOCAB_LS_USER = "vocab ls user";
    private final String ACTION_KEY_VOCAB_LS_USER_SHORT = "v ls u";
    private final String ACTION_KEY_VOCAB_LS_OWN = "vocab ls own";
    private final String ACTION_KEY_VOCAB_LS_OWN_SHORT = "v ls o";
    private final String ACTION_KEY_USER_FIND = "user find";
    private final String ACTION_KEY_USER_FIND_SHORT = "u find";
    private final String ACTION_KEY_VOCAB_RM = "vocab rm";
    private final String ACTION_KEY_VOCAB_RM_SHORT = "v rm";
    private final String ACTION_KEY_USER_SEARCH = "user search";
    private final String ACTION_KEY_USER_SEARCH_SHORT = "u search";
    private final String ACTION_KEY_GAME_START = "game start";
    private final String ACTION_KEY_GAME_START_SHORT = "g s";
    private final String ACTION_KEY_GAME_QUESTION = "game question";
    private final String ACTION_KEY_GAME_QUESTION_SHORT = "g q";
    private final String ACTION_KEY_GAME_ANSWER = "game answer";
    private final String ACTION_KEY_GAME_ANSWER_SHORT = "g a";
    private final String ACTION_KEY_GAME_LS = "game ls";
    private final String ACTION_KEY_GAME_LS_SHORT = "g ls";
    private final String ACTION_KEY_SCORE_LS = "score ls";
    private final String ACTION_KEY_SCORE_LS_SHORT = "s ls";
    private final String ACTION_KEY_SCORE_LS_USER = "score ls user";
    private final String ACTION_KEY_SCORE_LS_USER_SHORT = "s ls u";
    private final String ACTION_KEY_SCORE_RECORD = "score record";
    private final String ACTION_KEY_SCORE_RECORD_SHORT = "s r";
    private final String ACTION_KEY_SCORE_RECORD_USER = "score record user";
    private final String ACTION_KEY_SCORE_RECORD_USER_SHORT = "s r u";
    private final String ACTION_KEY_USER_RM = "user rm";
    private final String ACTION_KEY_USER_RM_SHORT = "u rm";

    private final String ACTION_ARG_EMAIL = "email";
    private final String ACTION_ARG_PWD = "pwd";
    private final String ACTION_ARG_TOKEN = "token";
    private final String ACTION_ARG_REFRESH = "refresh";
    private final String ACTION_ARG_FILE = "file";
    private final String ACTION_ARG_USERNAME = "username";
    private final String ACTION_ARG_FIRSTNAME = "firstname";
    private final String ACTION_ARG_LASTNAME = "lastname";
    private final String ACTION_ARG_CONFIRM = "confirm";
    private final String ACTION_ARG_CURRENT_PWD = "currentpwd";
    private final String ACTION_ARG_NEW_PWD = "newpwd";
    private final String ACTION_ARG_ID = "id";
    private final String ACTION_ARG_OPPONENT = "opponent";
    private final String ACTION_ARG_VOCABLE_LISTS = "vocablelists";
    private final String ACTION_ARG_STR = "str";
    private final String ACTION_ARG_ROUND = "round";
    private final String ACTION_ARG_ANSWER = "answer";
    private final String ACTION_ARG_LEVEL = "level";
    private final String ACTION_ARG_LANG = "lang";
    private final String ACTION_ARG_UNIT = "unit";
    private final String ACTION_ARG_LIST = "list";
    private final String ACTION_ARG_VOCAB = "vocab";
    private final String ACTION_ARG_A = "a";
    private final String ACTION_ARG_B = "b";
    private final String ACTION_ARG_C = "c";
    private final String ACTION_ARG_D = "d";

    private final VocabduelView VIEW;
    private final CliSessionStorage STORAGE;

    private final AuthService AUTH_SERVICE;
    private final UserService USER_SERVICE;
    private final VocabularyService VOCABULARY_SERVICE;
    private final GameService GAME_SERVICE;
    private final ScoreService SCORE_SERVICE;

    private final Pattern PARAM_PATTERN = Pattern.compile("--[a-z]+\\s((?!--).)+");

    private HashMap<String, VocabduelCliAction> actions;
    private List<VocabduelCliAction> actionsList;
    private boolean isRunning = true;

    public VocabduelControllerImpl(
            final VocabduelView view,
            final CliSessionStorage storage,
            final AuthService authService,
            final UserService userService,
            final VocabularyService vocabularyService,
            final GameService gameService,
            final ScoreService scoreService
    ) {
        VIEW = view;
        STORAGE = storage;
        AUTH_SERVICE = authService;
        USER_SERVICE = userService;
        VOCABULARY_SERVICE = vocabularyService;
        GAME_SERVICE = gameService;
        SCORE_SERVICE = scoreService;
    }

    @Override
    public void run() {
        VIEW.printHello();
        initializeFunctionsList();
        initializeFunctionsMap();

        while (isRunning) {
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
        }
    }

    private void initializeFunctionsList() {
        actionsList = new ArrayList<>();
        actionsList.add(new VocabduelCliAction(false, ACTION_KEY_HELP, "Get a list of all possible actions", ACTION_KEY_HELP_SHORT, this::onHelpCalled));
        actionsList.add(new VocabduelCliAction(false, ACTION_KEY_QUIT, "Quit this application", ACTION_KEY_QUIT_SHORT, this::onQuitCalled));
        actionsList.add(new VocabduelCliAction(false, ACTION_KEY_LOGIN, "Sign in to your existing account", ACTION_KEY_LOGIN_SHORT, this::onLoginCalled, ACTION_ARG_EMAIL, ACTION_ARG_PWD));
        actionsList.add(new VocabduelCliAction(false, ACTION_KEY_LOGIN_JWT, "Sign in with to your existing account using JWT tokens", ACTION_KEY_LOGIN_JWT_SHORT, this::onLoginJwtCalled, ACTION_ARG_TOKEN, ACTION_ARG_REFRESH));
        actionsList.add(new VocabduelCliAction(true, ACTION_KEY_LOGOUT, "Log out from the application", ACTION_KEY_LOGOUT_SHORT, this::onLogoutCalled));
        actionsList.add(new VocabduelCliAction(true, ACTION_KEY_VOCAB_IMPORT, "Import a GNU vocabulary list", ACTION_KEY_VOCAB_IMPORT_SHORT, this::onVocableImportCalled, ACTION_ARG_FILE));
        actionsList.add(new VocabduelCliAction(true, ACTION_KEY_VOCAB_IMPORT_SAMPLES, "Import default vocabulary lists", ACTION_KEY_VOCAB_IMPORT_SAMPLES_SHORT, this::onVocableSampleCalled));
        actionsList.add(new VocabduelCliAction(false, ACTION_KEY_REGISTER, "Sign up as a new user", ACTION_KEY_REGISTER_SHORT, this::onRegistrationCalled, ACTION_ARG_EMAIL, ACTION_ARG_USERNAME, ACTION_ARG_FIRSTNAME, ACTION_ARG_LASTNAME, ACTION_ARG_PWD, ACTION_ARG_CONFIRM));
        actionsList.add(new VocabduelCliAction(true, ACTION_KEY_USER_UPDATE, "Update the currently logged in user's data", ACTION_KEY_USER_UPDATE_SHORT, this::onUpdateCalled));
        actionsList.add(new VocabduelCliAction(true, ACTION_KEY_USER_UPDATE_PWD, "Update the currently logged in user's password", ACTION_KEY_USER_UPDATE_PWD_SHORT, this::onUpdatePwdCalled, ACTION_ARG_CURRENT_PWD, ACTION_ARG_NEW_PWD, ACTION_ARG_CONFIRM));
        actionsList.add(new VocabduelCliAction(true, ACTION_KEY_WHOAMI, "See current user data", this::onWhoAmICalled));
        actionsList.add(new VocabduelCliAction(false, ACTION_KEY_LANG_LS, "See a list of all supported languages", this::onVocabSupportedCalled));
        actionsList.add(new VocabduelCliAction(false, ACTION_KEY_LANG_LS_CODES, "See a list of all supported languages (codes only)", ACTION_KEY_LANG_LS_CODES_SHORT, this::onVocabSupportedCodesCalled));
        actionsList.add(new VocabduelCliAction(false, ACTION_KEY_VOCAB_LS, "See a list of all language sets and their units/lists (based on optional params)", ACTION_KEY_VOCAB_LS_SHORT, this::onVocabListsCalled));
        actionsList.add(new VocabduelCliAction(false, ACTION_KEY_VOCAB_FIND, "Get a vocable list by ID", ACTION_KEY_VOCAB_FIND_SHORT, this::onFindVocabListCalled, ACTION_ARG_ID));
        actionsList.add(new VocabduelCliAction(false, ACTION_KEY_VOCAB_LS_USER, "Get all vocable lists imported by a given user (determined by optional params)", ACTION_KEY_VOCAB_LS_USER_SHORT, this::onGetVocabListsByUserCalled));
        actionsList.add(new VocabduelCliAction(true, ACTION_KEY_VOCAB_LS_OWN, "Get all vocable lists imported by the currently logged in user", ACTION_KEY_VOCAB_LS_OWN_SHORT, this::onGetOwnVocabListsCalled));
        actionsList.add(new VocabduelCliAction(false, ACTION_KEY_USER_FIND, "Find a user (optional params for determination)", ACTION_KEY_USER_FIND_SHORT, this::onFindUserCalled));
        actionsList.add(new VocabduelCliAction(true, ACTION_KEY_VOCAB_RM, "Delete a vocab list by id (you have to be the list's author)", ACTION_KEY_VOCAB_RM_SHORT, this::onDeleteVocabListCalled, ACTION_ARG_ID));
        actionsList.add(new VocabduelCliAction(false, ACTION_KEY_USER_SEARCH, "Search for users with a given search string to be compared with user names (case insensitive)", ACTION_KEY_USER_SEARCH_SHORT, this::onUserSearchCalled, ACTION_ARG_STR));
        actionsList.add(new VocabduelCliAction(true, ACTION_KEY_GAME_START, "Start a new game (Vocable lists can be separated by spaces)", ACTION_KEY_GAME_START_SHORT, this::onGameStarted, ACTION_ARG_OPPONENT, ACTION_ARG_VOCABLE_LISTS));
        actionsList.add(new VocabduelCliAction(true, ACTION_KEY_GAME_QUESTION, "See the next question of a current game", ACTION_KEY_GAME_QUESTION_SHORT, this::onGameRoundStarted, ACTION_ARG_ID));
        actionsList.add(new VocabduelCliAction(true, ACTION_KEY_GAME_ANSWER, "Answer the question of a given round (by game id/round)", ACTION_KEY_GAME_ANSWER_SHORT, this::onGameRoundAnswered, ACTION_ARG_ID, ACTION_ARG_ROUND, ACTION_ARG_ANSWER));
        actionsList.add(new VocabduelCliAction(true, ACTION_KEY_GAME_LS, "See a list of all current running games", ACTION_KEY_GAME_LS_SHORT, this::onGameListCalled));
        actionsList.add(new VocabduelCliAction(true, ACTION_KEY_SCORE_LS, "See a list of all your scores, i.e. the results of finished games", ACTION_KEY_SCORE_LS_SHORT, this::onScoreHistCalled));
        actionsList.add(new VocabduelCliAction(false, ACTION_KEY_SCORE_LS_USER, "See a list of all scores of another user (determined by optional params)", ACTION_KEY_SCORE_LS_USER_SHORT, this::onScoreUserCalled));
        actionsList.add(new VocabduelCliAction(true, ACTION_KEY_SCORE_RECORD, "See your own record", ACTION_KEY_SCORE_RECORD_SHORT, this::onScoreRecordCalled));
        actionsList.add(new VocabduelCliAction(false, ACTION_KEY_SCORE_RECORD_USER, "See the record of a given user (determined by optional params)", ACTION_KEY_SCORE_RECORD_USER_SHORT, this::onScoreRecordUserCalled));
        actionsList.add(new VocabduelCliAction(true, ACTION_KEY_USER_RM, "Delete the current user account", ACTION_KEY_USER_RM_SHORT, this::onUserDeleteCalled));
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
        } else if (action.isGuarded() && !actionName.equals(ACTION_KEY_LOGOUT) && !actionName.equals(ACTION_KEY_LOGOUT_SHORT) && !AUTH_SERVICE.hasAccessRights(STORAGE.getLoggedInUser().getAuthTokens().getToken())) {
            VIEW.printInvalidAuthToken();
            final AuthTokens tokens = AUTH_SERVICE.refreshAuthTokens(STORAGE.getLoggedInUser().getAuthTokens().getRefreshToken());
            if (tokens != null) {
                STORAGE.getLoggedInUser().setAuthTokens(tokens);
                VIEW.printSuccessfullyRefreshedTokens(tokens);
                handleUserInput(actionName, userInputArgs);
            } else {
                VIEW.printInvalidRefreshToken();
                STORAGE.setLoggedInUser(null);
            }
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
        isRunning = false;
        System.exit(0);
    }

    private void onLoginCalled(final HashMap<String, String> args) {
        if (STORAGE.getLoggedInUser() != null) VIEW.printLogoutBeforeLogin(STORAGE.getLoggedInUser());
        else {
            final LoggedInUser user = AUTH_SERVICE.loginUser(args.get(ACTION_ARG_EMAIL), args.get(ACTION_ARG_PWD));
            if (user == null) VIEW.printInvalidLogin();
            else {
                STORAGE.setLoggedInUser(user);
                VIEW.printSuccessfulLogin(user);
            }
        }
    }

    private void onLoginJwtCalled(final HashMap<String, String> args) {
        if (STORAGE.getLoggedInUser() != null) VIEW.printLogoutBeforeLogin(STORAGE.getLoggedInUser());
        else {
            String token = args.get(ACTION_ARG_TOKEN);
            String refreshToken = args.get(ACTION_ARG_REFRESH);
            final boolean isValidInitialToken = AUTH_SERVICE.hasAccessRights(token);

            if (!isValidInitialToken) {
                VIEW.printInvalidAuthTokenInLogin();
                final AuthTokens newTokens = AUTH_SERVICE.refreshAuthTokens(refreshToken);
                if (newTokens == null) token = null;
                else {
                    token = newTokens.getToken();
                    refreshToken = newTokens.getRefreshToken();
                }
            }

            LoggedInUser user = null;
            if (token != null) {
                final User fetchedUser = AUTH_SERVICE.fetchUser(token);
                if (fetchedUser != null) {
                    user = new LoggedInUser(fetchedUser);
                    user.setAuthTokens(new AuthTokens(refreshToken, token));
                    STORAGE.setLoggedInUser(user);
                    if (isValidInitialToken) VIEW.printSuccessfulLoginWithToken(user);
                    else VIEW.printSuccessfulLoginWithRefreshedToken(user);
                }
            }

            if (user == null) VIEW.printInvalidLoginWithToken();
        }
    }

    private void onLogoutCalled() {
        STORAGE.setLoggedInUser(null);
        VIEW.printLogoutSuccessful();
    }

    private void onVocableImportCalled(final HashMap<String, String> args) {
        Scanner input = null;
        try {
            input = new Scanner(new File(args.get(ACTION_ARG_FILE)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (input != null) {
            final StringBuilder gnuContent = new StringBuilder();
            while (input.hasNext()) gnuContent.append(input.nextLine()).append("\n");
            input.close();

            try {
                final VocableList result = VOCABULARY_SERVICE.importGnuVocableList(gnuContent.toString(), STORAGE.getLoggedInUser());
                if (result != null) VIEW.printGnuImportSuccessful(args.get(ACTION_ARG_FILE));
            } catch (DuplicateVocablesInSetException | IncompleteVocableListException | DataAlreadyExistsException | UnknownLanguagesException | InvalidVocableListException e) {
                e.printStackTrace();
            }
        }
    }

    private void onVocableSampleCalled() {
        final String path = "./vocabduel_ui/assets/vocabulary/";
        for (String file : Objects.requireNonNull(new File(path).list())) {
            final HashMap<String, String> args = new HashMap<>();
            args.put(ACTION_ARG_FILE, path + file);
            onVocableImportCalled(args);
        }
    }

    private void onRegistrationCalled(final HashMap<String, String> args) {
        if (STORAGE.getLoggedInUser() != null) VIEW.printLogoutBeforeLogin(STORAGE.getLoggedInUser());
        else {
            try {
                final LoggedInUser loggedInUser = AUTH_SERVICE.registerUser(args.get(ACTION_ARG_USERNAME), args.get(ACTION_ARG_EMAIL), args.get(ACTION_ARG_FIRSTNAME), args.get(ACTION_ARG_LASTNAME), args.get(ACTION_ARG_PWD), args.get(ACTION_ARG_CONFIRM));
                STORAGE.setLoggedInUser(loggedInUser);
                VIEW.printSuccessfulRegistration(loggedInUser);
            } catch (PasswordsDoNotMatchException | PwTooWeakException | InvalidOrRegisteredMailException | AlreadyRegisteredUsernameException | IncompleteUserDataException | InvalidNameException e) {
                e.printStackTrace();
            }
        }
    }

    private void onUpdateCalled(final HashMap<String, String> args) {
        VIEW.printOptionalParamsInfo(args.keySet(), ACTION_ARG_USERNAME, ACTION_ARG_EMAIL, ACTION_ARG_FIRSTNAME, ACTION_ARG_LASTNAME);
        final LoggedInUser user = STORAGE.getLoggedInUser();

        final String prevUsername = user.getUsername();
        final String prevFirstname = user.getFirstName();
        final String prevLastname = user.getLastName();
        final String prevEmail = user.getEmail();

        if (args.get(ACTION_ARG_USERNAME) != null) user.setUsername(args.get(ACTION_ARG_USERNAME));
        if (args.get(ACTION_ARG_EMAIL) != null) user.setEmail(args.get(ACTION_ARG_EMAIL));
        if (args.get(ACTION_ARG_FIRSTNAME) != null) user.setFirstName(args.get(ACTION_ARG_FIRSTNAME));
        if (args.get(ACTION_ARG_LASTNAME) != null) user.setLastName(args.get(ACTION_ARG_LASTNAME));

        if (args.get(ACTION_ARG_USERNAME) != null || args.get(ACTION_ARG_EMAIL) != null || args.get(ACTION_ARG_FIRSTNAME) != null || args.get(ACTION_ARG_LASTNAME) != null) {
            try {
                USER_SERVICE.updateUser(LoggedInUser.asUser(user));
                VIEW.printSuccessfulUserUpdate(user);
            } catch (InvalidOrRegisteredMailException | AlreadyRegisteredUsernameException | IncompleteUserDataException | InvalidUserException | InvalidNameException e) {
                user.setEmail(prevEmail);
                user.setUsername(prevUsername);
                user.setFirstName(prevFirstname);
                user.setLastName(prevLastname);
                e.printStackTrace();
            }
        } else VIEW.printPleaseAddParamForUserUpdate();
    }

    private void onUpdatePwdCalled(final HashMap<String, String> args) {
        try {
            AUTH_SERVICE.updateUserPassword(STORAGE.getLoggedInUser(), args.get(ACTION_ARG_CURRENT_PWD), args.get(ACTION_ARG_NEW_PWD), args.get(ACTION_ARG_CONFIRM));
            VIEW.printSuccessfulPwdUpdate();
        } catch (InvalidFirstPwdException | PasswordsDoNotMatchException | PwTooWeakException | InvalidUserException e) {
            e.printStackTrace();
        }
    }

    private void onWhoAmICalled() {
        VIEW.printCurrentlyLoggedInAs(STORAGE.getLoggedInUser());
    }

    private void onVocabSupportedCalled() {
        final List<List<String>> languages = VOCABULARY_SERVICE.getAllSupportedLanguages()
                .stream()
                .sorted()
                .map(key -> {
                    final List<String> references = VOCABULARY_SERVICE.getSupportedLanguageReferences(key);
                    references.add(0, key.toString());
                    return references;
                })
                .collect(Collectors.toList());
        VIEW.printSupportedLanguages(languages);
    }

    private void onVocabSupportedCodesCalled() {
        VIEW.printSupportedLanguagesCodeOnly(VOCABULARY_SERVICE.getAllSupportedLanguages());
    }

    private void onVocabListsCalled(final HashMap<String, String> args) {
        final List<LanguageSet> languageSets = VOCABULARY_SERVICE.getAllLanguageSets();
        VIEW.printLanguageSets(languageSets, args == null ? null : args.get(ACTION_ARG_LEVEL));

        if (languageSets != null && !languageSets.isEmpty()) {
            List<String[]> options = new ArrayList<>();
            options.add(new String[]{ACTION_ARG_LANG, "Only list the language sets"});
            options.add(new String[]{ACTION_ARG_UNIT, "See previous command + vocable units of each language set"});
            options.add(new String[]{ACTION_ARG_LIST, "See previous command + vocable lists of each unit"});
            options.add(new String[]{ACTION_ARG_VOCAB, "See previous command + vocables of each list"});
            VIEW.printConfigurableThroughParam(ACTION_ARG_LEVEL, options);
        }
    }

    private void onFindVocabListCalled(final HashMap<String, String> args) {
        try {
            final Long id = Long.parseLong(args.get(ACTION_ARG_ID));
            final VocableList list = VOCABULARY_SERVICE.getVocableListById(id);
            if (list != null) VIEW.printVocableList(list);
            else VIEW.printNoVocableListFound();
        } catch (NumberFormatException e) {
            VIEW.printInvalidIdFormat(args.get(ACTION_ARG_ID));
        }
    }

    private void onGetVocabListsByUserCalled(final HashMap<String, String> args) {
        final User user = findUser(args);
        if (user != null) {
            List<VocableList> lists = VOCABULARY_SERVICE.getVocableListsOfUser(user);
            if (lists != null && lists.size() > 0) VIEW.printVocableListsByUser(user, lists);
            else VIEW.printNoVocableListsByUser(user);
        }
    }

    private void onGetOwnVocabListsCalled() {
        List<VocableList> lists = VOCABULARY_SERVICE.getVocableListsOfUser(STORAGE.getLoggedInUser());
        if (lists != null && lists.size() > 0) VIEW.printVocableListsByUser(STORAGE.getLoggedInUser(), lists);
        else VIEW.printNoVocableListsByUser(STORAGE.getLoggedInUser());
    }

    private void onFindUserCalled(final HashMap<String, String> args) {
        final User user = findUser(args);
        if (user != null) VIEW.printUser(user);
    }

    private User findUser(final HashMap<String, String> args) {
        VIEW.printOptionalParamsInfo(args.keySet(), ACTION_ARG_ID, ACTION_ARG_USERNAME, ACTION_ARG_EMAIL);
        User user = null;
        if (args.get(ACTION_ARG_ID) != null) {
            VIEW.printDeterminingUserBy(ACTION_ARG_ID);
            try {
                final Long id = Long.parseLong(args.get(ACTION_ARG_ID));
                user = USER_SERVICE.getUserDataById(id);
            } catch (NumberFormatException e) {
                VIEW.printInvalidIdFormat(args.get(ACTION_ARG_ID));
            }
        } else if (args.get(ACTION_ARG_USERNAME) != null) {
            VIEW.printDeterminingUserBy(ACTION_ARG_USERNAME);
            user = USER_SERVICE.getUserDataByUsername(args.get(ACTION_ARG_USERNAME));
        } else if (args.get(ACTION_ARG_EMAIL) != null) {
            VIEW.printDeterminingUserBy(ACTION_ARG_EMAIL);
            user = USER_SERVICE.getUserDataByEmail(args.get(ACTION_ARG_EMAIL));
        } else VIEW.printPleaseAddParamForUser();

        if (user == null) VIEW.printCouldNotDetermineUser();

        return user;
    }

    private void onDeleteVocabListCalled(final HashMap<String, String> args) {
        try {
            final long id = Long.parseLong(args.get(ACTION_ARG_ID));
            final VocableList vocableList = VOCABULARY_SERVICE.getVocableListById(id);
            if (vocableList == null) VIEW.printNoVocableListFound();
            else {
                VOCABULARY_SERVICE.deleteVocableList(vocableList, STORAGE.getLoggedInUser());
                VIEW.printSuccessfullyDeletedVocabList(id);
            }
        } catch (NumberFormatException e) {
            VIEW.printInvalidIdFormat(args.get(ACTION_ARG_ID));
        } catch (DifferentAuthorException e) {
            e.printStackTrace();
        } catch (PersistenceException e) {
            VIEW.printVocableListInRunningGame();
        }
    }

    private void onUserSearchCalled(final HashMap<String, String> args) {
        final String searchStr = args.get(ACTION_ARG_STR);
        final List<User> users = USER_SERVICE.findUsersByUsername(searchStr);
        if (users == null || users.size() == 0) VIEW.printNoUsersFound();
        else VIEW.printFoundUsers(users, searchStr);
    }

    private void onGameStarted(final HashMap<String, String> args) {
        User opponent = null;

        try {
            opponent = USER_SERVICE.getUserDataById(Long.parseLong(args.get(ACTION_ARG_OPPONENT)));
        } catch (NumberFormatException e) {
            VIEW.printInvalidIdFormat(args.get(ACTION_ARG_OPPONENT));
        }

        if (opponent != null) {
            try {
                final List<Long> uniqueVocabIds = new ArrayList<>(new HashSet<>(
                        Arrays.stream(args.get(ACTION_ARG_VOCABLE_LISTS).split("\\s"))
                                .map(Long::parseLong)
                                .collect(Collectors.toList())
                ));

                final List<VocableList> vocableLists = uniqueVocabIds
                        .stream().map(VOCABULARY_SERVICE::getVocableListById).collect(Collectors.toList());

                final RunningVocabduelGame game = GAME_SERVICE.startGame(STORAGE.getLoggedInUser(), opponent, vocableLists);
                VIEW.printSuccessfullyStaredGame(game, ACTION_KEY_GAME_QUESTION);
            } catch (NumberFormatException e) {
                VIEW.printInvalidIdPartFormat(args.get(ACTION_ARG_VOCABLE_LISTS));
            } catch (NotEnoughVocabularyException | InvalidGameSetupException | InvalidUserException e) {
                e.printStackTrace();
            }
        } else VIEW.printCouldNotDetermineUser();
    }

    private void onGameRoundAnswered(final HashMap<String, String> args) {
        final String[] options = new String[]{ACTION_ARG_A, ACTION_ARG_B, ACTION_ARG_C, ACTION_ARG_D};
        Long gameId = null;

        if (Arrays.stream(options).noneMatch(v -> v.equals(args.get(ACTION_ARG_ANSWER)))) {
            VIEW.printNoValidAnswer();
        } else {
            try {
                gameId = Long.parseLong(args.get(ACTION_ARG_ID));
            } catch (NumberFormatException e) {
                VIEW.printInvalidIdFormat(args.get(ACTION_ARG_ID));
            }

            if (gameId != null) {
                try {
                    final int roundNr = Integer.parseInt(args.get(ACTION_ARG_ROUND));
                    final int answer = ((int) args.get(ACTION_ARG_ANSWER).charAt(0)) - 97;
                    final CorrectAnswerResult result = GAME_SERVICE.answerQuestion(STORAGE.getLoggedInUser(), gameId, roundNr, answer);
                    assert (result != null);
                    if (result.getResult() == Result.WIN) VIEW.printRoundResultWin();
                    else VIEW.printRoundResultLoss(result.getCorrectAnswer());

                    if (roundNr < GameService.NR_OF_ROUNDS) onGameRoundStarted(args);
                    else {
                        PersonalFinishedGame finishedGame = SCORE_SERVICE.finishGame(STORAGE.getLoggedInUser(), gameId);
                        VIEW.printFinishedGame(finishedGame);
                    }
                } catch (NumberFormatException e) {
                    VIEW.printInvalidIdFormat(args.get(ACTION_ARG_ROUND));
                } catch (InvalidAnswerNrException | NoAccessException e) {
                    e.printStackTrace();
                } catch (UnfinishedGameException e) {
                    VIEW.printGameNotFinishedByOpponent();
                }
            }
        }
    }

    private void onGameRoundStarted(final HashMap<String, String> args) {
        try {
            final long id = Long.parseLong(args.get(ACTION_ARG_ID));
            final VocabduelRound round = GAME_SERVICE.startRound(STORAGE.getLoggedInUser(), id);
            VIEW.printQuestionAndAnswers(round, ACTION_KEY_GAME_ANSWER);
        } catch (NumberFormatException e) {
            VIEW.printInvalidIdFormat(args.get(ACTION_ARG_ID));
        } catch (NoAccessException e) {
            e.printStackTrace();
        }
    }

    private void onGameListCalled() {
        final List<RunningVocabduelGame> games = GAME_SERVICE.getPersonalChallengedGames(STORAGE.getLoggedInUser());
        VIEW.printGames(games, STORAGE.getLoggedInUser());
    }

    private void onScoreHistCalled() {
        try {
            final List<PersonalFinishedGame> hist = SCORE_SERVICE.getPersonalFinishedGames(STORAGE.getLoggedInUser());
            if (hist != null && !hist.isEmpty()) VIEW.printOwnScores(hist);
            else VIEW.printNoFinishedGamesYet();
        } catch (InvalidUserException e) {
            e.printStackTrace();
        }
    }

    private void onScoreUserCalled(final HashMap<String, String> args) {
        final User user = findUser(args);
        if (user != null) {
            final List<PersonalFinishedGame> hist;
            try {
                hist = SCORE_SERVICE.getPersonalFinishedGames(user);
                if (hist != null && !hist.isEmpty()) VIEW.printUserScores(hist, user);
                else VIEW.printNoFinishedGamesYet();
            } catch (InvalidUserException e) {
                e.printStackTrace();
            }
        }
    }

    private void onScoreRecordCalled() {
        try {
            VIEW.printRecord(SCORE_SERVICE.getRecordOfUser(STORAGE.getLoggedInUser()));
        } catch (InvalidUserException e) {
            e.printStackTrace();
        }
    }

    private void onScoreRecordUserCalled(final HashMap<String, String> args) {
        final User user = findUser(args);
        if (user != null) {
            try {
                VIEW.printRecord(SCORE_SERVICE.getRecordOfUser(user));
            } catch (InvalidUserException e) {
                e.printStackTrace();
            }
        }
    }

    private void onUserDeleteCalled(final HashMap<String, String> args) {
        final String firstname = STORAGE.getLoggedInUser().getFirstName();
        final String confirmFlag = "--" + ACTION_ARG_CONFIRM + " true";
        if (args.get(ACTION_ARG_CONFIRM) != null && args.get(ACTION_ARG_CONFIRM).equals("true")) {
            USER_SERVICE.deleteUser(STORAGE.getLoggedInUser());
            GAME_SERVICE.removeWidowGames();
            STORAGE.setLoggedInUser(null);
            VIEW.printYouWillBeMissed(firstname);
        } else VIEW.printConfirmUserDeletion(firstname, confirmFlag);
    }
}
