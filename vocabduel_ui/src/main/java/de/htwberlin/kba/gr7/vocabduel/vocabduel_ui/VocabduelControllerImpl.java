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

    private final String LO_KEY = "logout";
    private final String LO_SHORT = "lo";
    private final String GA_KEY = "game answer";
    private final String GQ_KEY = "game question";

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


        // TODO: RM
        try {
            handleUserInput("login", new String[]{"--pwd", "123arnoLD", "--email", "arnie1947@mail.at"});
        } catch (Exception e) {
            System.err.println("Initial login error");
            e.printStackTrace();
        }

        runCore();
    }

    private void runCore() {
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
        runCore();
    }

    private void initializeFunctionsList() {
        actionsList = new ArrayList<>();
        actionsList.add(new VocabduelCliAction(false, "help", "Get a list of all possible actions", "h", this::onHelpCalled));
        actionsList.add(new VocabduelCliAction(false, "quit", "Quit this application", "q", this::onQuitCalled));
        actionsList.add(new VocabduelCliAction(false, "login", "Sign in to your existing account", "li", this::onLoginCalled, "email", "pwd"));
        actionsList.add(new VocabduelCliAction(false, "login jwt", "Sign in with to your existing account using JWT tokens", "lt", this::onLoginJwtCalled, "token", "refresh"));
        actionsList.add(new VocabduelCliAction(true, LO_KEY, "Log out from the application", LO_SHORT, this::onLogoutCalled));
        actionsList.add(new VocabduelCliAction(true, "vocab import", "Import a GNU vocabulary list", "v i", this::onVocableImportCalled, "file"));
        actionsList.add(new VocabduelCliAction(true, "vocab import samples", "Import default vocabulary lists", "v i s", this::onVocableSampleCalled));
        actionsList.add(new VocabduelCliAction(false, "register", "Sign up as a new user", "r", this::onRegistrationCalled, "email", "username", "firstname", "lastname", "pwd", "confirm"));
        actionsList.add(new VocabduelCliAction(true, "user update", "Update the currently logged in user's data", "u u", this::onUpdateCalled));
        actionsList.add(new VocabduelCliAction(true, "user update pwd", "Update the currently logged in user's password", "u u pwd", this::onUpdatePwdCalled, "currentpwd", "newpwd", "confirm"));
        actionsList.add(new VocabduelCliAction(true, "whoami", "See current user data", this::onWhoAmICalled));
        actionsList.add(new VocabduelCliAction(false, "lang ls", "See a list of all supported languages", this::onVocabSupportedCalled));
        actionsList.add(new VocabduelCliAction(false, "lang ls codes", "See a list of all supported languages (codes only)", "lang ls c", this::onVocabSupportedCodesCalled));
        actionsList.add(new VocabduelCliAction(false, "vocab ls", "See a list of all language sets and their units/lists (based on optional params)", "v ls", this::onVocabListsCalled));
        actionsList.add(new VocabduelCliAction(false, "vocab find", "Get a vocable list by ID", "v find", this::onFindVocabListCalled, "id"));
        actionsList.add(new VocabduelCliAction(false, "vocab ls user", "Get all vocable lists imported by a given user (determined by optional params)", "v ls u", this::onGetVocabListsByUserCalled));
        actionsList.add(new VocabduelCliAction(true, "vocab ls own", "Get all vocable lists imported by the currently logged in user", "v ls o", this::onGetOwnVocabListsCalled));
        actionsList.add(new VocabduelCliAction(false, "user find", "Find a user (optional params for determination)", "u find", this::onFindUserCalled));
        actionsList.add(new VocabduelCliAction(true, "vocab rm", "Delete a vocab list by id (you have to be the list's author)", "v rm", this::onDeleteVocabListCalled, "id"));
        actionsList.add(new VocabduelCliAction(false, "user search", "Search for users with a given search string to be compared with user names (case insensitive)", "u search", this::onUserSearchCalled, "str"));
        actionsList.add(new VocabduelCliAction(true, "game start", "Start a new game (Vocable lists can be separated by spaces)", "g s", this::onGameStarted, "opponent", "vocablelists"));
        actionsList.add(new VocabduelCliAction(true, GQ_KEY, "See the next question of a current game", "g q", this::onGameRoundStarted, "id"));
        actionsList.add(new VocabduelCliAction(true, GA_KEY, "Answer the question of a given round (by game id/round)", "g a", this::onGameRoundAnswered, "id", "round", "answer"));
        actionsList.add(new VocabduelCliAction(true, "game ls", "See a list of all current running games", "g ls", this::onGameListCalled));
        actionsList.add(new VocabduelCliAction(true, "score ls", "See a list of all your scores, i.e. the results of finished games", "s ls", this::onScoreHistCalled));
        actionsList.add(new VocabduelCliAction(true, "score ls user", "See a list of all scores of another user (determined by optional params)", "s ls u", this::onScoreUserCalled));
        actionsList.add(new VocabduelCliAction(true, "score record", "See your own record", "s r", this::onScoreRecordCalled));
        actionsList.add(new VocabduelCliAction(false, "score record user", "See the record of another user (determined by optional parans)", "s r u", this::onScoreRecordUserCalled));
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
        } else if (action.isGuarded() && !actionName.equals(LO_KEY) && !actionName.equals(LO_SHORT) && !AUTH_SERVICE.hasAccessRights(STORAGE.getLoggedInUser().getAuthTokens().getToken())) {
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
        System.exit(0);
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

    private void onLoginJwtCalled(final HashMap<String, String> args) {
        if (STORAGE.getLoggedInUser() != null) VIEW.printLogoutBeforeLogin(STORAGE.getLoggedInUser());
        else {
            String token = args.get("token");
            String refreshToken = args.get("refresh");
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
            } catch (DuplicateVocablesInSetException | IncompleteVocableListException | DataAlreadyExistsException | UnknownLanguagesException | InvalidVocableListException e) {
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
            } catch (PasswordsDoNotMatchException | PwTooWeakException | InvalidOrRegisteredMailException | AlreadyRegisteredUsernameException | IncompleteUserDataException | InvalidNameException e) {
                e.printStackTrace();
            }
        }
    }

    private void onUpdateCalled(final HashMap<String, String> args) {
        VIEW.printOptionalParamsInfo(args.keySet(), "username", "email", "firstname", "lastname");
        final LoggedInUser user = STORAGE.getLoggedInUser();

        final String prevUsername = user.getUsername();
        final String prevFirstname = user.getFirstName();
        final String prevLastname = user.getLastName();
        final String prevEmail = user.getEmail();

        if (args.get("username") != null) user.setUsername(args.get("username"));
        if (args.get("email") != null) user.setEmail(args.get("email"));
        if (args.get("firstname") != null) user.setFirstName(args.get("firstname"));
        if (args.get("lastname") != null) user.setLastName(args.get("lastname"));

        if (args.get("username") != null || args.get("email") != null || args.get("firstname") != null || args.get("lastname") != null) {
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
            AUTH_SERVICE.updateUserPassword(STORAGE.getLoggedInUser(), args.get("currentpwd"), args.get("newpwd"), args.get("confirm"));
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
        VIEW.printLanguageSets(languageSets, args == null ? null : args.get("level"));

        if (languageSets != null && !languageSets.isEmpty()) {
            List<String[]> options = new ArrayList<>();
            options.add(new String[]{"lang", "Only list the language sets"});
            options.add(new String[]{"unit", "See previous command + vocable units of each language set"});
            options.add(new String[]{"list", "See previous command + vocable lists of each unit"});
            options.add(new String[]{"vocab", "See previous command + vocables of each list"});
            VIEW.printConfigurableThroughParam("level", options);
        }
    }

    private void onFindVocabListCalled(final HashMap<String, String> args) {
        try {
            final Long id = Long.parseLong(args.get("id"));
            final VocableList list = VOCABULARY_SERVICE.getVocableListById(id);
            if (list != null) VIEW.printVocableList(list);
            else VIEW.printNoVocableListFound();
        } catch (NumberFormatException e) {
            VIEW.printInvalidIdFormat(args.get("id"));
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
        VIEW.printOptionalParamsInfo(args.keySet(), "id", "username", "email");
        User user = null;
        if (args.get("id") != null) {
            VIEW.printDeterminingUserBy("id");
            try {
                final Long id = Long.parseLong(args.get("id"));
                user = USER_SERVICE.getUserDataById(id);
            } catch (NumberFormatException e) {
                VIEW.printInvalidIdFormat(args.get("id"));
            }
        } else if (args.get("username") != null) {
            VIEW.printDeterminingUserBy("username");
            user = USER_SERVICE.getUserDataByUsername(args.get("username"));
        } else if (args.get("email") != null) {
            VIEW.printDeterminingUserBy("email");
            user = USER_SERVICE.getUserDataByEmail(args.get("email"));
        } else VIEW.printPleaseAddParamForUser();

        if (user == null) VIEW.printCouldNotDetermineUser();

        return user;
    }

    private void onDeleteVocabListCalled(final HashMap<String, String> args) {
        try {
            final long id = Long.parseLong(args.get("id"));
            final VocableList vocableList = VOCABULARY_SERVICE.getVocableListById(id);
            if (vocableList == null) VIEW.printNoVocableListFound();
            else {
                VOCABULARY_SERVICE.deleteVocableList(vocableList, STORAGE.getLoggedInUser());
                VIEW.printSuccessfullyDeletedVocabList(id);
            }
        } catch (NumberFormatException e) {
            VIEW.printInvalidIdFormat(args.get("id"));
        } catch (DifferentAuthorException e) {
            e.printStackTrace();
        } catch (PersistenceException e) {
            VIEW.printVocableListInRunningGame();
        }
    }

    private void onUserSearchCalled(final HashMap<String, String> args) {
        final String searchStr = args.get("str");
        final List<User> users = USER_SERVICE.findUsersByUsername(searchStr);
        if (users == null || users.size() == 0) VIEW.printNoUsersFound();
        else VIEW.printFoundUsers(users, searchStr);
    }

    private void onGameStarted(final HashMap<String, String> args) {
        User opponent = null;

        try {
            opponent = USER_SERVICE.getUserDataById(Long.parseLong(args.get("opponent")));
        } catch (NumberFormatException e) {
            VIEW.printInvalidIdFormat(args.get("opponent"));
        }

        if (opponent != null) {
            try {
                final List<Long> uniqueVocabIds = new ArrayList<>(new HashSet<>(
                        Arrays.stream(args.get("vocablelists").split("\\s"))
                                .map(Long::parseLong)
                                .collect(Collectors.toList())
                ));

                final List<VocableList> vocableLists = uniqueVocabIds
                        .stream().map(VOCABULARY_SERVICE::getVocableListById).collect(Collectors.toList());

                final VocabduelGame game = GAME_SERVICE.startGame(STORAGE.getLoggedInUser(), opponent, vocableLists);
                VIEW.printSuccessfullyStaredGame(game, GQ_KEY);
            } catch (NumberFormatException e) {
                VIEW.printInvalidIdPartFormat(args.get("vocablelists"));
            } catch (NotEnoughVocabularyException | InvalidGameSetupException | InvalidUserException e) {
                e.printStackTrace();
            }
        } else VIEW.printCouldNotDetermineUser();
    }

    private void onGameRoundAnswered(final HashMap<String, String> args) {
        final String[] options = new String[]{"a", "b", "c", "d"};
        Long gameId = null;

        if (Arrays.stream(options).noneMatch(v -> v.equals(args.get("answer")))) {
            VIEW.printNoValidAnswer();
        } else {
            try {
                gameId = Long.parseLong(args.get("id"));
            } catch (NumberFormatException e) {
                VIEW.printInvalidIdFormat(args.get("id"));
            }

            if (gameId != null) {
                try {
                    final int roundNr = Integer.parseInt(args.get("round"));
                    final int answer = ((int) args.get("answer").charAt(0)) - 97;
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
                    VIEW.printInvalidIdFormat(args.get("round"));
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
            final long id = Long.parseLong(args.get("id"));
            final VocabduelRound round = GAME_SERVICE.startRound(STORAGE.getLoggedInUser(), id);
            VIEW.printQuestionAndAnswers(round, GA_KEY);
        } catch (NumberFormatException e) {
            VIEW.printInvalidIdFormat(args.get("id"));
        } catch (NoAccessException e) {
            e.printStackTrace();
        }
    }

    private void onGameListCalled() {
        final List<VocabduelGame> games = GAME_SERVICE.getPersonalChallengedGames(STORAGE.getLoggedInUser());
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
}
