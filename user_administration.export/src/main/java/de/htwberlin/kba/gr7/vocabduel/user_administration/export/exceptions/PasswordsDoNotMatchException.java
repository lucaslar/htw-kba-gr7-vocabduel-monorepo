package de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions;

public class PasswordsDoNotMatchException extends Exception {
    public PasswordsDoNotMatchException(final String message) {
        super(message);
    }
}
