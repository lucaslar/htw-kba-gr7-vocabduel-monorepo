package de.htwberlin.kba.gr7.vocabduel.vocabulary.model;

public class UntranslatedVocable {
    private int id;
    private TranslationGroup vocable;

    public int getId() {
        return id;
    }

    public TranslationGroup getVocable() {
        return vocable;
    }

    public void setVocable(TranslationGroup vocable) {
        this.vocable = vocable;
    }
}
