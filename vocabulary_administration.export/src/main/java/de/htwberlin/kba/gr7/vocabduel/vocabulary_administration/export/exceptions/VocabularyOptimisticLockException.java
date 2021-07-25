package de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.exceptions;

public class VocabularyOptimisticLockException extends Exception {
    public VocabularyOptimisticLockException(Exception hidden) {
        super("An Optimistic Lock Exception occurred in Vocabulary Module. Please try again.");
        System.err.println("The following exception is only logged here but will not be made public:");
        hidden.printStackTrace();
    }
}
