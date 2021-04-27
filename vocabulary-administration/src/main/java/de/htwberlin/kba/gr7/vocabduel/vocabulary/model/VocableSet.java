package de.htwberlin.kba.gr7.vocabduel.vocabulary.model;

import de.htwberlin.kba.gr7.vocabduel.user.model.User;

import java.util.Date;
import java.util.List;

public class VocableSet {
    private int id;
    private User author;
    private String title;
    private String sourceName; // e.g. Book
    private SupportedLanguage knownLanguage;
    private SupportedLanguage learntLanguage;
    private Date timestamp;
    private List<Vocable> vocables;

    public int getId() {
        return id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public List<Vocable> getVocables() {
        return vocables;
    }

    public void setVocables(List<Vocable> vocables) {
        this.vocables = vocables;
    }
}
