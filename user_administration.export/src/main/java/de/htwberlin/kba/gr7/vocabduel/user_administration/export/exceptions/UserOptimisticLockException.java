package de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions;

public class UserOptimisticLockException extends Exception {
    public UserOptimisticLockException(Exception hidden) {
        super("An Optimistic Lock Exception occurred in User Module. Please try again.");
        System.err.println("The following exception is only logged here but will not be made public:");
        hidden.printStackTrace();
    }
}
