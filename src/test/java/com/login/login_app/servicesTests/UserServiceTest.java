package com.login.login_app.servicesTests;

import com.login.login_app.exception.exceptions.NotFoundException;
import com.login.login_app.exception.exceptions.ValidEmailException;
import com.login.login_app.models.userModel.User;
import com.login.login_app.models.userModel.UserRole;
import com.login.login_app.repositoriesTests.UserRepository;
import com.login.login_app.services.ConfirmationTokenService;
import com.login.login_app.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {


    @Mock
    private  UserRepository userRepository;

    @Mock
    private  BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private  ConfirmationTokenService confirmationTokenService;


    @Autowired
    private UserService underTest;

    @BeforeEach
    void setUp(){

        underTest =new UserService(userRepository,bCryptPasswordEncoder,confirmationTokenService);

    }



    private User userTest() {
        var userJeff = new User();
        userJeff.setFirstName("ali");
        userJeff.setLastName("amer");
        userJeff.setUsername("ali@gmail.com");
        userJeff.setPassword("ali123");
        userJeff.setUserRole(UserRole.USER);

        return userJeff;

    }

    @Test
    void loadUserByUsernameNotFound() {
       var ex = assertThrows(UsernameNotFoundException.class , ()-> underTest.loadUserByUsername("aliamer19") );
        assertEquals("User with email aliamer19 not found", ex.getMessage());
    }
    @Test
    void loadUserByUsernameFound() {
        given(userRepository.findByEmail(userTest().getEmail())).willReturn(Optional.of(userTest()));

     var us =  underTest.loadUserByUsername(userTest().getEmail());
       assertThat(userTest()).isEqualTo(us);


    }

    @Test
    void signUpEmailToken() {
        given(userRepository.findByEmail(userTest().getEmail())).willReturn(Optional.of(userTest()));

     var ex =   assertThrows(ValidEmailException.class , ()-> underTest.signUp(userTest()));
     assertEquals("EMAIL already taken" , ex.getMessage() );
     verify(userRepository, never()).save(any());


    }
    @Test
    void signUpEmailNotToken() {
     var jeff = userTest();

       underTest.signUp(jeff);

       verify(userRepository).save(jeff);


    }


    @Test
    void enableAppUser() {

       given(userRepository.enableAppUser(userTest().getEmail())).willReturn(1);

       var re = underTest.enableAppUser(userTest().getEmail());

       assertThat(re).isEqualTo(1);


    }

    @Test
    void getAllUsers() {
        var user = userTest();
        List<User> usersList = new ArrayList<>();
        usersList.add(user);
        given(userRepository.findAll()).willReturn(usersList);

        var us = underTest.getAllUsers();

        assertThat(us).isEqualTo(usersList);
    }

    @Test
    void deleteUsersFound() {
        var userId = userTest().getId();
        given(userRepository.existsById(userId)).willReturn(true);

        var test =underTest.deleteUsers(userId);
        verify(userRepository, never()).findByEmail(userTest().getEmail());
        assertEquals("has removed",test);
    }
    @Test
    void deleteUsersNotFound() {
        var userId = userTest().getId();
        given(userRepository.existsById(userId)).willReturn(false);

        var ex =   assertThrows(NotFoundException.class , ()-> underTest.deleteUsers(userId));

        assertEquals("there is no user like that" , ex.getMessage() );
        verify(userRepository, never()).delete(any());

    }

    @Test
    void blockUserFound() {
        var userId = userTest().getId();
        given(userRepository.existsById(userId)).willReturn(true);
        given(userRepository.findById(userId)).willReturn(Optional.of(userTest()));

      var test =  underTest.blockUser(userId,true);

      verify(userRepository).save(any());

      assertEquals("has blocked", test);

    }
    @Test
    void blockUserNotFound() {
        var userId = userTest().getId();
        var ex =   assertThrows(NotFoundException.class , ()-> underTest.deleteUsers(userId));

        assertEquals("there is no user like that" , ex.getMessage() );
        verify(userRepository, never()).delete(any());
    }

    @Test
    void getUserIfFound() {
        var userId = userTest().getId();
        given(userRepository.existsById(userId)).willReturn(true);
        given(userRepository.getById(userId)).willReturn(userTest());

        var test = underTest.getUser(userId);

        assertThat(test).isEqualTo(userTest());

    }
    @Test
    void getUserIfNotFound() {
        var userId = userTest().getId();
        var ex =   assertThrows(NotFoundException.class , ()-> underTest.deleteUsers(userId));

        assertEquals("there is no user like that" , ex.getMessage() );


    }
}