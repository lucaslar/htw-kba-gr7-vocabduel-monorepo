package de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model;

import javax.persistence.*;

@Entity
public class UntranslatedVocable {
    @Id
    private Long id;
    @OneToOne
    private TranslationGroup vocable;

    public UntranslatedVocable(TranslationGroup vocable){ this.vocable = vocable; }

    public UntranslatedVocable(Long id, TranslationGroup vocable){ this.id = id; this.vocable = vocable; }

    public Long getId() {
        return id;
    }

    public TranslationGroup getVocable() {
        return vocable;
    }

    public void setVocable(TranslationGroup vocable) {
        this.vocable = vocable;
    }
}
