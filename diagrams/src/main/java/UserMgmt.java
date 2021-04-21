import java.time.LocalDateTime;
import java.util.List;

public class UserMgmt {

    // books to learn for the user
    private List<Book> myBooks;

    // username
    private String username;

    // Games
    private List<Game> games;

    // learnings
    private List<Learn> learnungs;

}

class Game{
    private UserMgmt opponent1;

    // game opponent
    private UserMgmt opponent2;

    // book in the game used
    private Book book;

    // flg whether battle is won
    private boolean wonBattle;

    // List with used Items in this game
    private List<Item> usedItems;

}

class Learn{
    // user who learned
    private UserMgmt user;

    // book that should be learned
    private Book book;

    // Item that should be learned
    private Item usedItem;

    // Timestamp of learning
    private LocalDateTime tmstmp;

    // flg whether item got remembered
    private boolean remembered;

}
