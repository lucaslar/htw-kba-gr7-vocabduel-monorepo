package de.htwberlin.kba.gr7.vocabduel.vocabduel_ui.model;


import java.util.function.Consumer;

public class VocabduelCliAction {
    private final String name;
    private final String description;
    private final Consumer<String[]> action;
    private String shortName;

    public VocabduelCliAction(String name, String description, Consumer<String[]> action) {
        this.name = name;
        this.description = description;
        this.action = action;
    }

    public VocabduelCliAction(String name, String description, Consumer<String[]> action, String shortName) {
        this.name = name;
        this.description = description;
        this.action = action;
        this.shortName = shortName;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Consumer<String[]> getAction() {
        return action;
    }

    public String getShortName() {
        return shortName;
    }
}
