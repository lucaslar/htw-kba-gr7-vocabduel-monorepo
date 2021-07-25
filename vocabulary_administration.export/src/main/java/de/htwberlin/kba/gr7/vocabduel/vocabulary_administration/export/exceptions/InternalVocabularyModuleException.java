package de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.exceptions;

public class InternalVocabularyModuleException extends Exception {
    public InternalVocabularyModuleException(Exception hidden) {
        super("An internal Vocabulary Module Exception occurred. Please contact a system administrator.");
        System.err.println("The following exception is only logged here but will not be made public:");
        hidden.printStackTrace();
    }
}
