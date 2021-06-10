package de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class LanguageSet {
    @Id
    private Long id;
    @Enumerated(EnumType.STRING)
    private SupportedLanguage knownLanguage;
    @Enumerated(EnumType.STRING)
    private SupportedLanguage learntLanguage;
    @OneToMany
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
