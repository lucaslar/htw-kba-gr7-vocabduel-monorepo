package de.htwberlin.kba.gr7.model;

import java.util.List;

public class Vocable extends UntranslatedVocable {
    private List<TranslationGroup> translations;

    public List<TranslationGroup> getTranslations() {
        return translations;
    }

    public void setTranslations(List<TranslationGroup> translations) {
        this.translations = translations;
    }
}
