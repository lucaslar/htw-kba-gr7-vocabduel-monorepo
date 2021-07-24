package de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions;

public class InternalUserModuleException extends Exception {
    public InternalUserModuleException(Exception hidden) {
        super("An internal User Module Exception occurred. Please contact a system administrator.");
        System.err.println("The following exception is only logged here but will not be made public:");
        hidden.printStackTrace();
    }
}
