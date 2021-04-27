package de.htwberlin.kba.gr7.vocabduel.vocabulary.model;

import java.util.List;

public class UntranslatedVocable {
    private int id;
    private String vocable;
    private List<String> additionalForms; // e.g. "get", "got", "gotten"

    public int getId() {
        return id;
    }

    public String getVocable() {
        return vocable;
    }

    public void setVocable(String vocable) {
        this.vocable = vocable;
    }

    public List<String> getAdditionalForms() {
        return additionalForms;
    }

    public void setAdditionalForms(List<String> additionalForms) {
        this.additionalForms = additionalForms;
    }
}
