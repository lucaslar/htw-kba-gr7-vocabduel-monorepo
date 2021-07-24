package de.htwberlin.kba.gr7.vocabduel.game_administration.rest.model;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.VocableList;

import java.util.Date;

public class MinimizedUsedListInfo {
    private Long id;
    private String title;
    private User author;
    private Date timestamp;
    private String unitTitle;

    public MinimizedUsedListInfo(final VocableList list) {
        id = list.getId();
        title = list.getTitle();
        author = list.getAuthor();
        timestamp = list.getTimestamp();
        unitTitle = list.getUnitTitle();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public User getAuthor() {
        return author;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getUnitTitle() {
        return unitTitle;
    }

    @Override
    public String toString() {
        return "MinimizedUsedListInfo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author=" + author +
                ", timestamp=" + timestamp +
                '}';
    }
}