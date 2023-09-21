package com.giassi.microservice.demo2.rest.users.services.validation;

import com.giassi.microservice.demo2.rest.users.exceptions.InvalidUserDataException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class EmailValidatorTest {

    private static EmailValidator emailValidator;

    @BeforeAll
    public static void initTest() {
        emailValidator = new EmailValidator();
    }

    @Test
    public void givenNullEmailWhenCheckEmailThrowInvalidUserDataException() {
        String email = null;
        assertThrows(InvalidUserDataException.class, () -> emailValidator.checkEmail(email));
    }

    @Test
    public void givenEmptyEmailWhenCheckEmailThrowInvalidUserDataException() {
        String email = "";
        assertThrows(InvalidUserDataException.class, () -> emailValidator.checkEmail(email));
    }

    @Test
    public void givenInvalidEmailWhenCheckEmailThrowInvalidUserDataException() {
        String email = "@gmail.com";
        assertThrows(InvalidUserDataException.class, () -> emailValidator.checkEmail(email));
    }

    @Test
    public void given_valid_email_when_checkEmail_OK() {
        String email = "testEmail@gmail.com";
        emailValidator.checkEmail(email);
    }

}
