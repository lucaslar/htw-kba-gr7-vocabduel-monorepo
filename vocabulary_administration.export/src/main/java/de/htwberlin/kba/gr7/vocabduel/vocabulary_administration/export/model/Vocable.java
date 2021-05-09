package de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model;

import java.util.Collections;
import java.util.List;

public class Vocable extends UntranslatedVocable {
    private List<TranslationGroup> translations;

    public Vocable(){ super(new TranslationGroup(Collections.singletonList(""))); }

    public Vocable(TranslationGroup vocable, List<TranslationGroup> translations ){
        super(vocable);
        this.translations = translations;
    }

    public List<TranslationGroup> getTranslations() {
        return translations;
    }

    public void setTranslations(List<TranslationGroup> translations) {
        this.translations = translations;
    }
}
