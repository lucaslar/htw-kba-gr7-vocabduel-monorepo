package de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class LanguageSet implements Serializable {
    @Id
    @Enumerated(EnumType.STRING)
    private SupportedLanguage knownLanguage;
    @Id
    @Enumerated(EnumType.STRING)
    private SupportedLanguage learntLanguage;
    //    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "parentLanguageSet")
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<VocableUnit> vocableUnits;

    public LanguageSet() {
    }

    public LanguageSet( final SupportedLanguage learntLanguage, final SupportedLanguage knownLanguage) {
        this.knownLanguage = knownLanguage;
        this.learntLanguage = learntLanguage;
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
