package com.giassi.microservice.demo2.rest.users.services.validation;

import com.giassi.microservice.demo2.rest.users.exceptions.InvalidUserDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class PasswordValidatorTest {

    public PasswordValidator passwordValidator;

    @BeforeEach
    public void initTest() {
        passwordValidator = new PasswordValidator();
    }

    @Test
    public void givenNullPasswordWhenCheckPasswordThrowInvalidUserDataException() {
        String password = null;
        assertThrows(InvalidUserDataException.class, () -> passwordValidator.checkPassword(password));
    }

    @Test
    public void givenEmptyPasswordWhenCheckPasswordThrowInvalidUserDataException() {
        String password = "";
        assertThrows(InvalidUserDataException.class, () -> passwordValidator.checkPassword(password));
    }

    @Test
    public void givenTooLongPasswordWhenCheckPasswordThrowInvalidUserDataException() {
        String password = "01234567890123456789012345678901234567890123456789012345678901234567890123456789";
        assertThrows(InvalidUserDataException.class, () -> passwordValidator.checkPassword(password));
    }

    @Test
    public void givenNotValidPasswordWhenCheckPasswordThrowInvalidUserDataException() {
        String password = "aaaa asdasd1234";
        assertThrows(InvalidUserDataException.class, () -> passwordValidator.checkPassword(password));
    }

    @Test
    public void givenInvalidPasswordLessThan8CharsWhenCheckPasswordThenOK() {
        String password = "Andrea";
        assertThrows(InvalidUserDataException.class, () -> passwordValidator.checkPassword(password));
    }

    @Test
    public void givenInvalidPasswordWithSpacesWhenCheckPasswordThenOK() {
        String password = "Andrea test";
        assertThrows(InvalidUserDataException.class, () -> passwordValidator.checkPassword(password));
    }

    @Test
    public void givenInvalidPasswordNoUppercaseLetterWhenCheckPasswordThenOK() {
        String password = "andrea!123";
        assertThrows(InvalidUserDataException.class, () -> passwordValidator.checkPassword(password));
    }

    @Test
    public void givenInvalidPasswordNoLowercaseLetterWhenCheckPasswordThenOK() {
        String password = "ANDREA!123";
        assertThrows(InvalidUserDataException.class, () -> passwordValidator.checkPassword(password));
    }

    @Test
    public void givenInvalidPasswordNoSpecialCharsWhenCheckPasswordThenOK() {
        String password = "ANDREA123";
        assertThrows(InvalidUserDataException.class, () -> passwordValidator.checkPassword(password));
    }
    @Test
    public void given_valid_password_when_checkPassword_then_OK() {
        String password = "Andrea!123";
        passwordValidator.checkPassword(password);
    }

}
