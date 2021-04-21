import java.time.LocalDateTime;
import java.util.List;

public interface UserMgmt{

    // books to learn for the user
    List<Book> myBooks = null;

    // username
    String username = "";

    // Games
    List<Game> games = null;

    // learnings
    List<Learn> learnings = null;

    List<Game> getTotalLosses();
    List<Game> getTotalWins();
    List<Game> getGamesWithSpecialOpponent(UserMgmt opponent);
    List<Game> getGamesByBook(Book input);
    List<UserMgmt> findOpponentForGame(Book book);

    List<Learn> getTotalRemembered();
    List<Learn> getTotalNonRemembered();
    List<Learn> getLearningByDate(LocalDateTime date);
    List<Learn> getLearningByBook(Book input);

}

interface Game extends UserMgmt{
    UserMgmt opponent1 = null;

    // game opponent
    UserMgmt opponent2 = null;

    // book in the game used
    Book book = null;

    // flg whether battle is won
    boolean wonBattle = false;

    // List with used Items in this game
    List<Item> usedItems = null;

    Game getGame();
    void startGame(UserMgmt opp1, UserMgmt opp2, Book book);

}

interface Learn extends UserMgmt{
    // user who learned
    UserMgmt user = null;

    // book that should be learned
    Book book = null;

    // Item that should be learned
    Item usedItem = null;

    // Timestamp of learning
    LocalDateTime tmstmp = null;

    // flg whether item got remembered
    boolean remembered = false;

    Learn getLearn();
    void startLearn(UserMgmt user, Book book);

}
