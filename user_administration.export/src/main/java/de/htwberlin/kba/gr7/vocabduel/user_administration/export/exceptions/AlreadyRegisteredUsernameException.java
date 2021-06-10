package de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions;

public class AlreadyRegisteredUsernameException extends Exception {
    public AlreadyRegisteredUsernameException(final String message) {
        super(message);
    }
}
