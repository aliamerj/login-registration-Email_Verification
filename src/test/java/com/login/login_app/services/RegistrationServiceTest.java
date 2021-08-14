package com.login.login_app.services;


import com.login.login_app.exception.exceptions.ExpiredException;
import com.login.login_app.exception.exceptions.HasConfirmed;
import com.login.login_app.exception.exceptions.NotFoundException;
import com.login.login_app.exception.exceptions.ValidEmailException;
import com.login.login_app.models.ConfirmationToken;
import com.login.login_app.models.Registration;
import com.login.login_app.models.userModel.User;
import com.login.login_app.models.userModel.UserRole;
import com.login.login_app.services.emailService.EmailValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {
    @Mock
    private UserService appUserService;
    @Mock
    private  ConfirmationTokenService confirmationTokenService;
    @Mock
    private  EmailValidator emailValidator;



    @Autowired
    private RegistrationService underTest;

    @BeforeEach
    void setUp() {
        underTest = new RegistrationService(appUserService,confirmationTokenService,emailValidator);
    }

    private Registration userTest(){
        var user = new Registration("ali","amer","aliamer19ali@gmail.com","ali123");
        return user;
    }
    private User makeUserFromReg(Registration registration){
        var user = new User(registration.getFirstName(),registration.getLastName(),registration.getEmail(),registration.getPassword(),UserRole.USER);
    return user;
    }








    @Test
    void registerWithNotValidEmail() {
        var ali = userTest();
        var link =  "http://localhost:8080/api/v1/registration/confirm?token=";

        given(emailValidator.test(makeUserFromReg(ali).getEmail())).willReturn(true);

       var re = underTest.register(ali);

       verify(appUserService).signUp(makeUserFromReg(ali));
       assertThat(re).startsWith(link);



    }
    @Test
    void registerWithValidEmail(){
        var ali = userTest();

      var exception =  assertThrows(ValidEmailException.class, ()-> underTest.register(ali));
        assertEquals("sorry, email is not valid" , exception.getMessage() );
        verify(appUserService, never()).signUp(any());


    }

    @Test
    void confirmToken() {

        var token = "http://localhost:8080/api/v1/registration/confirm?token=584rjk948r4";
        var cratedAt =  LocalDateTime.of(2021,8,14,12,0);
        var expiredAt =LocalDateTime.of(2021,8,14,12,30);
        var confirmToken = new ConfirmationToken(token,cratedAt,expiredAt,makeUserFromReg(userTest()));
        var now = LocalDateTime.of(2021,8,14,12,2);
        Mockito.mockStatic(LocalDateTime.class);
        // token exist
        given(confirmationTokenService.getToken(token)).willReturn(Optional.of(confirmToken));
        // token not expire
        when(LocalDateTime.now()).thenReturn(now);



      var result =  underTest.confirmToken(token);

        verify(confirmationTokenService).setConfirmedAt(any());
        verify(appUserService).enableAppUser(any());
        assertEquals("confirmed" , result );
    }
    @Test
    void confirmTokenTokenNotFound(){
        var token = "http://localhost:8080/api/v1/registration/confirm?token=584rjk948r4";


        var exception =  assertThrows(NotFoundException.class, ()-> underTest.confirmToken(token));
        assertEquals("token not found" , exception.getMessage() );

        verify(confirmationTokenService, never()).saveConfirmationToken(any());
        verify(appUserService, never()).enableAppUser(any());
    }
    @Test
    void confirmTokenTokenExpire(){
        var token = "http://localhost:8080/api/v1/registration/confirm?token=584rjk948r4";
        var cratedAt =  LocalDateTime.of(2021,8,14,12,0);
        var expiredAt =LocalDateTime.of(2021,8,14,12,30);
        var confirmToken = new ConfirmationToken(token,cratedAt,expiredAt,makeUserFromReg(userTest()));

        // token exist
        given(confirmationTokenService.getToken(token)).willReturn(Optional.of(confirmToken));

        var exception =  assertThrows(ExpiredException.class, ()-> underTest.confirmToken(token));
        assertEquals("token expired" , exception.getMessage() );

        verify(confirmationTokenService, never()).saveConfirmationToken(any());
        verify(appUserService, never()).enableAppUser(any());
    }
    @Test
    void confirmTokenAlreadyConfirm(){
        var token = "http://localhost:8080/api/v1/registration/confirm?token=584rjk948r4";
        var cratedAt =  LocalDateTime.of(2021,8,14,12,0);
        var expiredAt =LocalDateTime.of(2021,8,14,12,30);
        var now = LocalDateTime.of(2021,8,14,12,2);
        var confirmToken = new ConfirmationToken(token,cratedAt,expiredAt,makeUserFromReg(userTest()));
        given(confirmationTokenService.getToken(token)).willReturn(Optional.of(confirmToken));

        confirmToken.setConfirmedAt(now);


        var exception =  assertThrows(HasConfirmed.class, ()-> underTest.confirmToken(token));
        assertEquals("email already confirmed" , exception.getMessage() );





    }

}