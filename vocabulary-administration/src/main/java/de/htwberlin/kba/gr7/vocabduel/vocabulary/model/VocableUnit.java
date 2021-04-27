package de.htwberlin.kba.gr7.vocabduel.vocabulary.model;

import java.util.List;

public class VocableUnit {
    private Long id;
    private String title;
    private List<VocableList> vocableLists;

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<VocableList> getVocableLists() {
        return vocableLists;
    }

    public void setVocableLists(List<VocableList> vocableLists) {
        this.vocableLists = vocableLists;
    }
}
