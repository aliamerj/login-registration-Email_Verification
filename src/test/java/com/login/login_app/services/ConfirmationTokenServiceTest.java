package com.login.login_app.services;

import com.login.login_app.models.ConfirmationToken;
import com.login.login_app.models.userModel.User;
import com.login.login_app.models.userModel.UserRole;
import com.login.login_app.repositories.ConfirmationTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.assertj.core.api.BDDAssumptions.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConfirmationTokenServiceTest {
    @Mock
    private  ConfirmationTokenRepository confirmationTokenRepository;
    @Autowired
    private ConfirmationTokenService underTest;

    @BeforeEach
    void setUp() {
        underTest = new ConfirmationTokenService(confirmationTokenRepository);
    }
    private ConfirmationToken get(){
        var cratedAt =  LocalDateTime.of(2021,8,14,12,0);
        var expiredAt =LocalDateTime.of(2021,8,14,12,30);
        var con = new ConfirmationToken("token",cratedAt,expiredAt,new User("ali","amer", "aliamer19","ali1234", UserRole.USER));
   return con;
    }

    @Test
    void saveConfirmationToken() {
        underTest.saveConfirmationToken(get());

        verify(confirmationTokenRepository).save(any());

    }

    @Test
    void getToken() {
        confirmationTokenRepository.save(get());

        underTest.getToken(get().getToken());

        verify(confirmationTokenRepository).findByToken(get().getToken());

    }

    @Test
    void setConfirmedAt() {
        var now = LocalDateTime.of(2021,8,14,12,2);
        Mockito.mockStatic(LocalDateTime.class);
        when(LocalDateTime.now()).thenReturn(now);
        underTest.setConfirmedAt(get().getToken());

        verify(confirmationTokenRepository).updateConfirmedAt(get().getToken(),LocalDateTime.now());

    }
}