package com.login.login_app.repositories;

import com.login.login_app.models.ConfirmationToken;
import com.login.login_app.models.userModel.User;
import com.login.login_app.models.userModel.UserRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.BDDAssumptions.given;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class ConfirmationTokenRepositoryTest {
    @Autowired
    private ConfirmationTokenRepository underTest;
    @Mock
    private ConfirmationTokenRepository confirmationTokenRepository;




    @AfterEach
    void down(){
        underTest.deleteAll();

    }
    private ConfirmationToken get(){
        var cratedAt =  LocalDateTime.of(2021,8,14,12,0);
        var expiredAt =LocalDateTime.of(2021,8,14,12,30);
        var con = new ConfirmationToken("token",cratedAt,expiredAt,new User("alig","amerg", "aliagmer19@gmail.com","alig1234", UserRole.USER));
        return con;
    }

    @Test
    void findByTokenNotExists() {

       boolean userHere = underTest.findByToken(get().getToken()).isPresent();
       assertThat(userHere).isFalse();

    }
    @Test
    void findByTokenExists() {

        confirmationTokenRepository.save(get());

        boolean userHere = underTest.findByToken(get().getToken()).isPresent();
        assertThat(userHere).isFalse();

    }

    @Test
    void updateConfirmedAt() {
    }
}