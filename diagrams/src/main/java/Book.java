import java.util.List;

public interface Book {


    // unit Title
    String bookTitle = "";

    // book has number of units
    List<Unit> unitList = null;

    // language from which to translate
    String langFrom = "";

    // language in which to be translated
    String langTo = "";

    Book createBook(String bookTitle, String langFrom, String langTo);
    void addUnit(Unit input);
    List<Unit> getUnitList();
    Unit getUnit(int index);
    void setUnit(Unit input, int index);
    String getLangFrom();
    String getLangTo();
    String getBookTitle();
}
