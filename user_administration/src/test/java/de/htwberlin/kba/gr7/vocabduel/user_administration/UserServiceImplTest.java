package de.htwberlin.kba.gr7.vocabduel.user_administration;

import de.htwberlin.kba.gr7.vocabduel.user_administration.dao.*;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.*;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.naming.InvalidNameException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    private UserServiceImpl userAdministration;
    @Mock
    private EntityManager entityManager;
    @Mock
    private Query queryMock;

    private final Long EXISTING_USER_ID = 4711L;
    private final String EXISTING_EMAIL1 = "user1@vocabduel.de";
    private final String EXISTING_USERNAME_PART = "username";
    private final String UNKNOWN_MAIL = "doesntexist@vocabduel.de";

    private User user1, user2, user3, user4;
    private List<User> usersList;

    @Before
    public void setUp() {
//        final UserDAO userDAO = new UserDAOImpl(entityManager);
//        final StoredRefreshTokenDAO storedRefreshTokenDAO = new StoredRefreshTokenDAOImpl(entityManager);
//        final LoginDataDAO loginDataDAO = new LoginDataDAOImpl(entityManager);
//
//        userAdministration = new UserServiceImpl(userDAO, storedRefreshTokenDAO, loginDataDAO);

        user1 = new User(42L);
        user2 = new User(123L);
        user3 = new User(456L);
        user4 = new User(EXISTING_USER_ID);

        user1.setUsername(EXISTING_USERNAME_PART);
        user2.setUsername("prefix" + EXISTING_USERNAME_PART);
        user3.setUsername(EXISTING_USERNAME_PART + "suffix");
        user4.setUsername("nutzername");

        user1.setEmail(EXISTING_EMAIL1);
        user2.setEmail("user2@vocabduel.de");
        user3.setEmail("user3@vocabduel.de");
        user4.setEmail("user4@vocabduel.de");

        usersList = Stream.of(user1, user2, user3, user4).collect(Collectors.toList());

        Mockito.when(entityManager.createQuery(Mockito.anyString())).thenReturn(queryMock);
        Mockito.when(queryMock.setParameter(Mockito.anyString(), Mockito.any())).thenReturn(queryMock);
        Mockito.when(queryMock.getSingleResult()).thenReturn(null);
    }

    @Test
    public void shouldFindThreeUsersContainingUsernameStr() {
        Mockito.when(queryMock.getResultList()).thenReturn(usersList.stream().filter(t ->
                t.getUsername().contains(EXISTING_USERNAME_PART)).collect(Collectors.toList()));
        final List<User> results = userAdministration.findUsersByUsername(EXISTING_USERNAME_PART);
        Assert.assertNotNull(results);
        Assert.assertEquals(results.size(), 3); // => users 1, 2 & 3

        final Pattern pattern = Pattern.compile(EXISTING_USERNAME_PART, Pattern.CASE_INSENSITIVE);

        results.forEach(result -> {
            final String name = result.getUsername();
            final boolean isMatching = pattern.matcher(name).find();
            final String assertMsg = "Expected username \"" + name + "\" to contain \"" + EXISTING_USERNAME_PART + "\"";
            Assert.assertTrue(assertMsg, isMatching);
        });
    }

    @Test
    public void shouldReturnNullIfNoUsersFound() {
        Mockito.when(queryMock.getResultList()).thenThrow(NoResultException.class);
        final List<User> results = userAdministration.findUsersByUsername(EXISTING_USERNAME_PART);
        Assert.assertNull(results);
    }

    @Test
    public void shouldFindThreeUsersContainingUsernameStrIgnoringCase() {
        final String upperCaseStr = EXISTING_USERNAME_PART.toUpperCase();
        Mockito.when(queryMock.getResultList()).thenReturn(usersList.stream().filter(t ->
                t.getUsername().toLowerCase().contains(EXISTING_USERNAME_PART)).collect(Collectors.toList()));
        final List<User> results = userAdministration.findUsersByUsername(upperCaseStr);
        Assert.assertNotNull(results);
        Assert.assertEquals(results.size(), 3); // => users 1, 2 & 3

        final Pattern pattern = Pattern.compile(EXISTING_USERNAME_PART, Pattern.CASE_INSENSITIVE);

        results.forEach(result -> {
            final String name = result.getUsername();
            final boolean isMatching = pattern.matcher(name).find();
            final String assertMsg = "Expected username \"" + name + "\" to contain \"" + upperCaseStr + "\" (ignoring case)";
            Assert.assertTrue(assertMsg, isMatching);
        });
    }

    @Test
    public void shouldNotGetUserDataByIdIfNull() {
        Assert.assertNull(userAdministration.getUserDataById(null));
    }

    @Test
    public void shouldNotGetUserDataByEmailIfNull() {
        Assert.assertNull(userAdministration.getUserDataByEmail(null));
    }

    @Test
    public void shouldNotGetUserDataByUsernameIfNull() {
        Assert.assertNull(userAdministration.getUserDataByUsername(null));
    }

    @Test
    public void shouldNotGetUserDataByIdIfUnknown() {
        final Long wrongId = definitelyUnusedId();
        Assert.assertNull(userAdministration.getUserDataById(wrongId));
    }

    @Test
    public void shouldNotGetUserDataByEmailIfUnknown() {
        Mockito.when(queryMock.getSingleResult()).thenThrow(new NoResultException());
        Assert.assertNull(userAdministration.getUserDataByEmail(UNKNOWN_MAIL));
    }

    @Test
    public void shouldNotGetUserDataByUsernameIfUnknown() {
        final String wrongUsername = definitelyUnusedUsername();
        Mockito.when(queryMock.getSingleResult()).thenThrow(new NoResultException());
        Assert.assertNull(userAdministration.getUserDataByUsername(wrongUsername));
    }

    @Test
    public void shouldGetUserDataById() {
        Mockito.when(entityManager.find(Mockito.eq(User.class), Mockito.any())).thenReturn(user2);
        final User foundUser = userAdministration.getUserDataById(EXISTING_USER_ID);
        Assert.assertNotNull(foundUser);
        Assert.assertEquals(foundUser.toString(), user2.toString());
    }

    @Test
    public void shouldGetUserDataByEmail() {
        Mockito.when(queryMock.getSingleResult()).thenReturn(user1);
        final User foundUser = userAdministration.getUserDataByEmail(EXISTING_EMAIL1);
        Assert.assertNotNull(foundUser);
        Assert.assertEquals(foundUser.toString(), user1.toString());
    }

    @Test
    public void shouldGetUserDataByUsername() {
        Mockito.when(entityManager.find(Mockito.eq(User.class), Mockito.any())).thenReturn(user4);
        final User foundUser = userAdministration.getUserDataById(EXISTING_USER_ID);
        Assert.assertNotNull(foundUser);
        Assert.assertEquals(foundUser.toString(), user4.toString());
    }

    @Test(expected = InvalidOrRegisteredMailException.class)
    public void shouldThrowExceptionOnUpdatingIfMailAlreadyUsed() throws AlreadyRegisteredUsernameException, InvalidOrRegisteredMailException, IncompleteUserDataException, InvalidUserException, InvalidNameException {
        user3.setEmail(user2.getEmail());
        user3.setFirstName("John");
        user3.setLastName("Doe");
        Mockito.when(entityManager.find(Mockito.eq(User.class), Mockito.eq(user3.getId()))).thenReturn(user3);
        Mockito.when(queryMock.getSingleResult()).thenReturn(user2);
        userAdministration.updateUser(user3);
    }

    @Test(expected = InvalidUserException.class)
    public void shouldThrowExceptionOnUpdatingIfUserToUpdateIsNull() throws AlreadyRegisteredUsernameException, InvalidOrRegisteredMailException, IncompleteUserDataException, InvalidUserException, InvalidNameException {
        userAdministration.updateUser(null);
    }

    @Test(expected = InvalidUserException.class)
    public void shouldThrowExceptionOnUpdatingIfUserToUpdateCannotBeFound() throws AlreadyRegisteredUsernameException, InvalidOrRegisteredMailException, IncompleteUserDataException, InvalidUserException, InvalidNameException {
        Mockito.when(entityManager.find(Mockito.eq(User.class), Mockito.eq(user3.getId()))).thenReturn(null);
        userAdministration.updateUser(user3);
    }

    @Test(expected = InvalidOrRegisteredMailException.class)
    public void shouldThrowExceptionOnUpdatingIfMailInvalid() throws AlreadyRegisteredUsernameException, InvalidOrRegisteredMailException, IncompleteUserDataException, InvalidUserException, InvalidNameException {
        Mockito.when(entityManager.find(Mockito.eq(User.class), Mockito.any())).thenReturn(user3);
        user3.setEmail("invalidmail");
        user3.setFirstName("Max");
        user3.setLastName("Mustermann");
        userAdministration.updateUser(user3);
    }

    @Test(expected = AlreadyRegisteredUsernameException.class)
    public void shouldThrowExceptionOnUpdatingIfUsernameAlreadyUsed() throws AlreadyRegisteredUsernameException, InvalidOrRegisteredMailException, IncompleteUserDataException, InvalidUserException, InvalidNameException {
        Mockito.when(entityManager.find(Mockito.eq(User.class), Mockito.any())).thenReturn(user3);
        Mockito.when(queryMock.getSingleResult()).thenReturn(user3, user2);
        user3.setUsername(user1.getUsername());
        user3.setFirstName("Max");
        user3.setLastName("Mustermann");
        userAdministration.updateUser(user3);
    }

    @Test(expected = IncompleteUserDataException.class)
    public void shouldNotUpdateUserWithNullValueEmail() throws AlreadyRegisteredUsernameException, InvalidOrRegisteredMailException, IncompleteUserDataException, InvalidUserException, InvalidNameException {
        user3.setEmail(null);
        user3.setUsername(definitelyUnusedUsername());
        user3.setFirstName("Max");
        user3.setLastName("Mustermann");
        Mockito.when(entityManager.find(Mockito.eq(User.class), Mockito.any())).thenReturn(user3);
        userAdministration.updateUser(user3);
    }

    @Test(expected = IncompleteUserDataException.class)
    public void shouldNotUpdateUserWithNullValueUsername() throws AlreadyRegisteredUsernameException, InvalidOrRegisteredMailException, IncompleteUserDataException, InvalidUserException, InvalidNameException {
        user3.setEmail(UNKNOWN_MAIL);
        user3.setUsername(null);
        user3.setFirstName("Max");
        user3.setLastName("Mustermann");
        Mockito.when(entityManager.find(Mockito.eq(User.class), Mockito.any())).thenReturn(user3);
        userAdministration.updateUser(user3);
    }

    @Test(expected = IncompleteUserDataException.class)
    public void shouldNotUpdateUserWithNullValueFirstname() throws AlreadyRegisteredUsernameException, InvalidOrRegisteredMailException, IncompleteUserDataException, InvalidUserException, InvalidNameException {
        user3.setEmail(UNKNOWN_MAIL);
        user3.setUsername(definitelyUnusedUsername());
        user3.setFirstName(null);
        user3.setLastName("Mustermann");
        Mockito.when(entityManager.find(Mockito.eq(User.class), Mockito.any())).thenReturn(user3);
        userAdministration.updateUser(user3);
    }

    @Test(expected = IncompleteUserDataException.class)
    public void shouldNotUpdateUserWithNullValueLastname() throws AlreadyRegisteredUsernameException, InvalidOrRegisteredMailException, IncompleteUserDataException, InvalidUserException, InvalidNameException {
        user3.setEmail(UNKNOWN_MAIL);
        user3.setUsername(definitelyUnusedUsername());
        user3.setFirstName("max");
        user3.setLastName(null);
        Mockito.when(entityManager.find(Mockito.eq(User.class), Mockito.any())).thenReturn(user3);
        userAdministration.updateUser(user3);
    }

    @Test
    public void shouldUpdateUserData() throws AlreadyRegisteredUsernameException, InvalidOrRegisteredMailException, IncompleteUserDataException, InvalidUserException, InvalidNameException {
        user3.setEmail(UNKNOWN_MAIL);
        user3.setUsername(definitelyUnusedUsername());
        user3.setFirstName("Max");
        user3.setLastName("Mustermann");
        Mockito.when(entityManager.find(Mockito.eq(User.class), Mockito.any())).thenReturn(user3);
        final int statusCode = userAdministration.updateUser(user3);
        Assert.assertEquals(0, statusCode);

        final User foundUser = userAdministration.getUserDataById(user3.getId());
        Assert.assertNotNull(foundUser);
        Assert.assertEquals(foundUser.toString(), user3.toString());
    }

    @Test
    public void shouldDeleteUser() {
        final Long deletedUserId = user1.getId();
        final int statusCode = userAdministration.deleteUser(user1);
        Assert.assertEquals(0, statusCode);
        Assert.assertNull(userAdministration.getUserDataById(deletedUserId));
    }

    @Test
    public void shouldDeleteUserIfNoStoredAuthTokenFound() {
        final Long deletedUserId = user1.getId();
        Mockito.when(queryMock.getResultList()).thenThrow(new NoResultException());
        final int statusCode = userAdministration.deleteUser(user1);
        Assert.assertEquals(0, statusCode);
        Assert.assertNull(userAdministration.getUserDataById(deletedUserId));
    }

    private Long definitelyUnusedId() {
        // Definitely not defined id (sum of ids, fallback = -1):
        return usersList.stream().map(User::getId).reduce(Long::sum).orElse(-1L);
    }

    private String definitelyUnusedUsername() {
        // Definitely not defined username (joined existing usernames, fallback = "idontexist"):
        return usersList.stream().map(User::getUsername).reduce(String::join).orElse("idontexist");
    }
}
