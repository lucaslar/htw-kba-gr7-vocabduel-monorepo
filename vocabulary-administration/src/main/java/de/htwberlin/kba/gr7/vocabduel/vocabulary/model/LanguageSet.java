package de.htwberlin.kba.gr7.vocabduel.vocabulary.model;

import java.util.List;

public class LanguageSet {
    private Long id;
    private SupportedLanguage knownLanguage;
    private SupportedLanguage learntLanguage;
    private List<VocableUnit> vocableUnits;

    public Long getId() {
        return id;
    }

    public SupportedLanguage getKnownLanguage() {
        return knownLanguage;
    }

    public void setKnownLanguage(SupportedLanguage knownLanguage) {
        this.knownLanguage = knownLanguage;
    }

    public SupportedLanguage getLearntLanguage() {
        return learntLanguage;
    }

    public void setLearntLanguage(SupportedLanguage learntLanguage) {
        this.learntLanguage = learntLanguage;
    }

    public List<VocableUnit> getVocableUnits() {
        return vocableUnits;
    }

    public void setVocableUnits(List<VocableUnit> vocableUnits) {
        this.vocableUnits = vocableUnits;
    }
}
