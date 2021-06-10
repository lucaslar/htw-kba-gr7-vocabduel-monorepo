package de.htwberlin.kba.gr7.vocabduel.user_administration.model;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.PasswordsDoNotMatchException;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.PwTooWeakException;

import java.util.regex.Pattern;

public class Validation {
    public static void passwordValidation(final String pwd, final String confirmPwd) throws PasswordsDoNotMatchException, PwTooWeakException {
        if (!pwd.equals(confirmPwd)) throw new PasswordsDoNotMatchException();
        else if (!isValidPassword(pwd)) {
            throw new PwTooWeakException("Pwd too weak. Expected: at least two of four: [lower case, upper case, digit, special character] and min. 8 characters length");
        }
    }

    private static boolean isValidPassword(final String password) {
        int matches = 0;
        if (Pattern.compile("\\d+").matcher(password).find()) matches++;
        if (Pattern.compile("[a-z]+").matcher(password).find()) matches++;
        if (Pattern.compile("[A-Z]+").matcher(password).find()) matches++;
        if (Pattern.compile("(?=.*[-+_!@#$%^&*., ?]).+").matcher(password).find()) matches++;
        return password.length() >= 8 && matches >= 2;
    }
}
