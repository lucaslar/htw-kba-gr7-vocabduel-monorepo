package de.htwberlin.kba.gr7.vocabduel.user_administration.model;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.UserService;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.*;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;

import javax.naming.InvalidNameException;
import java.util.Arrays;
import java.util.regex.Pattern;

public class Validation {
    public static void passwordValidation(final String pwd, final String confirmPwd) throws PasswordsDoNotMatchException, PwTooWeakException {
        if (!pwd.equals(confirmPwd)) throw new PasswordsDoNotMatchException("The given passwords do not match");
        else if (!isValidPassword(pwd)) {
            throw new PwTooWeakException("Pwd too weak. Expected: at least two of four: [lower case, upper case, digit, special character] and min. 8 characters length");
        }
    }

    public static void completeDataValidation(final String... data) throws IncompleteUserDataException {
        if (Arrays.stream(data).anyMatch(d -> d == null || d.isEmpty())) {
            throw new IncompleteUserDataException("One or more of the required user data is null/empty!");
        }
    }

    public static void nameValidation(final String name) throws InvalidNameException {
        final Pattern pattern = Pattern.compile("\\p{Upper}(\\p{Lower}+\\s?)");
        if (!pattern.matcher(name).matches()) {
            throw new InvalidNameException("Invalid name (" + name + ") - validation using regex: " + pattern);
        }
    }

    public static void completeDataValidation(final User user) throws IncompleteUserDataException {
        completeDataValidation(user.getEmail(), user.getUsername(), user.getFirstName(), user.getLastName());
    }

    private static boolean isValidPassword(final String password) {
        int matches = 0;
        if (Pattern.compile("\\d+").matcher(password).find()) matches++;
        if (Pattern.compile("[a-z]+").matcher(password).find()) matches++;
        if (Pattern.compile("[A-Z]+").matcher(password).find()) matches++;
        if (Pattern.compile("(?=.*[-+_!@#$%^&*., ?]).+").matcher(password).find()) matches++;
        return password.length() >= 8 && matches >= 2;
    }

    public static void uniqueUserDataValidation(final String username, final String email, final UserService userService) throws InvalidOrRegisteredMailException, AlreadyRegisteredUsernameException, InternalUserModuleException {
        uniqueUserDataValidation(username, email, userService, null);
    }

    public static void uniqueUserDataValidation(final String username, final String email, final UserService userService, final Long id) throws InvalidOrRegisteredMailException, AlreadyRegisteredUsernameException, InternalUserModuleException {
        if (!Pattern.compile("^(.+)@(.+)$").matcher(email).matches()) {
            throw new InvalidOrRegisteredMailException("Invalid mail format");
        } else {
            final User foundByMail = userService.getUserDataByEmail(email);
            if (foundByMail != null && (id == null || !id.equals(foundByMail.getId()))) {
                throw new InvalidOrRegisteredMailException("Email is already registered");
            }

            final User foundByUsername = userService.getUserDataByUsername(username);
            if (foundByUsername != null && (id == null || !id.equals(foundByUsername.getId()))) {
                throw new AlreadyRegisteredUsernameException("Username is already registered");
            }
        }
    }
}
