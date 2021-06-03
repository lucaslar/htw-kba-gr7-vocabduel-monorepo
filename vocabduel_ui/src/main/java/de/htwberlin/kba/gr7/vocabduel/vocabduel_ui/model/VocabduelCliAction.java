package de.htwberlin.kba.gr7.vocabduel.vocabduel_ui.model;


import java.util.HashMap;
import java.util.function.Consumer;

public class VocabduelCliAction {
    private final String name;
    private final String description;
    private Consumer<HashMap<String, String>> action;
    private Runnable noArgsAction;
    private String shortName;
    private String[] requiredArgs;

    public VocabduelCliAction(final String name, final String description, final Consumer<HashMap<String, String>> action, final String... requiredArgs) {
        this.name = name;
        this.description = description;
        this.action = action;
        this.requiredArgs = requiredArgs;
    }

    public VocabduelCliAction(final String name, final String description, final String shortName, final Consumer<HashMap<String, String>> action, final String... requiredArgs) {
        this.name = name;
        this.description = description;
        this.action = action;
        this.shortName = shortName;
        this.requiredArgs = requiredArgs;
    }

    public VocabduelCliAction(final String name, final String description, final Runnable action) {
        this.name = name;
        this.description = description;
        this.noArgsAction = action;
    }

    public VocabduelCliAction(final String name, final String description, final String shortName, final Runnable action) {
        this.name = name;
        this.description = description;
        this.shortName = shortName;
        this.noArgsAction = action;
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

    public String[] getRequiredArgs() {
        return requiredArgs;
    }
}
