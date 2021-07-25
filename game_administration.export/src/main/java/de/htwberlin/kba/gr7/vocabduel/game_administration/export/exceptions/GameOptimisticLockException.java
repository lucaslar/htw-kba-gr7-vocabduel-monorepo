package de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions;

public class GameOptimisticLockException extends Exception {
    public GameOptimisticLockException(Exception hidden) {
        super("An internal Game Module Exception occurred. Please contact a system administrator.");
        System.err.println("The following exception is only logged here but will not be made public:");
        hidden.printStackTrace();
    }
}
