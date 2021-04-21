import java.util.List;

public interface Unit extends Book{

    // List of vocabs
    List<Item> itemList = null;

    // unit Title
    String unitTitle = "";

    Item getItem(int index);
    void setItem(Item input, int index);

    Unit createUnit(String unitTitle);
    void addItem(Item input);
    List<Item> getItemList();
    String getUnitTitle();

}
interface Item extends Unit{

    String unknownWord = "";
    List<String> translation = null;

    Item createItem(String unknownWord, List<String> translation);
}
