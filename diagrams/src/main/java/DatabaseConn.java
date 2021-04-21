//import Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


class DatabaseConn {

    private static DatabaseConn conn;

    public List<Book> getBooksByUser(UserMgmt user){
        return new ArrayList<>();
    }

    public List<UserMgmt> getUserByBook(Book myBook){
        return new ArrayList<>();
    }

    public UserMgmt getUserData(String username){
        return new UserMgmt();
    }

    public static DatabaseConn getInstance(){
        if (Objects.isNull(conn)){
            return new DatabaseConn();
        } else {
            return conn;
        }
    }
}


