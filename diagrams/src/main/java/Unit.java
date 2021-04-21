import java.util.List;

public class Unit extends Book{

    // List of vocabs
    private List<Item> itemList;

    // unit Title
    private String unitTitle;

}
class Item extends Unit{
    private String unknownWord;
    private List<String> translation;
}
