package de.htwberlin.kba.gr7.vocabduel.configuration;

import de.htwberlin.kba.gr7.vocabduel.vocabduel_ui.export.VocabduelController;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ConfigurationSpringImpl {
    private final static ConfigurableApplicationContext CONTEXT = new AnnotationConfigApplicationContext("de.htwberlin.kba.gr7.vocabduel");

    public static void main(String[] args) {
        try {
            CONTEXT.getBean(ConfigurationMySQLImpl.class).init();
            CONTEXT.getBean(VocabduelController.class).run();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("\nUI has not been started due to the error above.");
        }
    }
}
