import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Hello world!
 * From example Project in Moodle
 *
 */
public interface App {
    // static
    Logger LOGGER = LogManager.getLogger(App.class);

    static void main(String[] args) {
        printGreetings();
    }

    static void printGreetings() {
        LOGGER.debug("Method printGreetings started");
        System.out.println("Hello World!");
        LOGGER.debug("Method printGreetings finished");
    }

}