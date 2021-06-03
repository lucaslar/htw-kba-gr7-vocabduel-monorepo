package de.htwberlin.kba.gr7.vocabduel.vocabduel_ui.model;


import java.util.HashMap;
import java.util.function.Consumer;

public class VocabduelCliAction {
    private final String name;
    private final String description;
    private Consumer<HashMap<String, String>> action;
    private Runnable noArgsAction;
    private String shortName;

    public VocabduelCliAction(String name, String description, Consumer<HashMap<String, String>> action) {
        this.name = name;
        this.description = description;
        this.action = action;
    }

    public VocabduelCliAction(String name, String description, Consumer<HashMap<String, String>> action, String shortName) {
        this.name = name;
        this.description = description;
        this.action = action;
        this.shortName = shortName;
    }

    public VocabduelCliAction(String name, String description, Runnable action) {
        this.name = name;
        this.description = description;
        this.noArgsAction = action;
    }

    public VocabduelCliAction(String name, String description, Runnable action, String shortName) {
        this.name = name;
        this.description = description;
        this.noArgsAction = action;
        this.shortName = shortName;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Consumer<HashMap<String, String>> getAction() {
        return action;
    }

    public Runnable getNoArgsAction() {
        return noArgsAction;
    }

    public String getShortName() {
        return shortName;
    }
}
