package de.htwberlin.kba.gr7.model;

public class UntranslatedVocable {
    private int id;
    private String vocable;
    private String additionalForms; // e.g. "get", "got", "gotten"

    public int getId() {
        return id;
    }

    public String getVocable() {
        return vocable;
    }

    public void setVocable(String vocable) {
        this.vocable = vocable;
    }

    public String getAdditionalForms() {
        return additionalForms;
    }

    public void setAdditionalForms(String additionalForms) {
        this.additionalForms = additionalForms;
    }
}
