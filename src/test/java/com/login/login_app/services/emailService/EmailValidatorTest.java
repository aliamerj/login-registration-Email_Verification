package com.login.login_app.services.emailService;

import com.login.login_app.services.ConfirmationTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EmailValidatorTest {

    @Autowired
    private EmailValidator underTest;

   @BeforeEach
    void setUp() {
        underTest = new EmailValidator();
    }




    @ParameterizedTest
    @CsvSource({
            "ali , false",
            "dhjsfkshd , false",
            "aliamer@gmail.com , true",
            "aliamer@ali.com , true",
            "ali@8u45rh , false"

    })
    void test1(String email , boolean expected) {

      var is =  underTest.test(email);

        assertEquals(is,expected);


    }
}