package de.htwberlin.kba.gr7.vocabduel.game_administration.export.model;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;

public class ScoreRecord {
    private final User USER;
    private long wins = 0;
    private long losses = 0;
    private long draws = 0;

    public ScoreRecord(final User user) {
        this.USER = user;
    }

    public ScoreRecord(final User user, final long wins, final long losses, final long draws) {
        this.USER = user;
        this.wins = wins;
        this.losses = losses;
        this.draws = draws;
    }

    public User getUser() {
        return USER;
    }

    public long getWins() {
        return wins;
    }

    public long getLosses() {
        return losses;
    }

    public long getDraws() {
        return draws;
    }

    @Override
    public String toString() {
        return "Record for " + USER + ":\n" + wins + "-" + losses + "-" + draws + (" (Wins - Losses - Draws)");
    }
}
