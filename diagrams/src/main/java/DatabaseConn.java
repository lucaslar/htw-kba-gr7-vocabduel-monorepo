import java.util.List;
import java.util.Objects;


public interface DatabaseConn {

    // static
    DatabaseConn conn = null;

    // get Books with Units and Items
    List<Book> getBooksByUser(UserMgmt user);

    // get User for Games by Books
    List<UserMgmt> getUserByBook(Book myBook);

    // get User with Games and Learnings
    UserMgmt getUserData(String username);

    // add user
    void addUserMgmt(UserMgmt user);

    // add Book
    void addBook(Book book);
    // update Book: added / removed Unit / Item
    void updateBook(Book book);

    // add Game
    void addGame(Game input);

    // add Learning
    void addLearning(Learn input);

    static DatabaseConn getInstance(){
        if (Objects.isNull(conn)){
            return null;
        } else {
            return conn;
        }
    }
}


