package de.htwberlin.kba.gr7.vocabduel.game.model;

import de.htwberlin.kba.gr7.vocabduel.user.model.User;

import java.util.Date;

public class GameScore {
    private Long id;
    private User playerA;
    private User playerB;
    private int pointsA;
    private int pointsB;
    private Date timestamp;

    public Long getId() {
        return id;
    }

    public User getPlayerA() {
        return playerA;
    }

    public void setPlayerA(User playerA) {
        this.playerA = playerA;
    }

    public User getPlayerB() {
        return playerB;
    }

    public void setPlayerB(User playerB) {
        this.playerB = playerB;
    }

    public int getPointsA() {
        return pointsA;
    }

    public void setPointsA(int pointsA) {
        this.pointsA = pointsA;
    }

    public int getPointsB() {
        return pointsB;
    }

    public void setPointsB(int pointsB) {
        this.pointsB = pointsB;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
