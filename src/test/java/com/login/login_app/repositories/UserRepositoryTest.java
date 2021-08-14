package com.login.login_app.repositories;


import com.login.login_app.models.userModel.User;
import com.login.login_app.models.userModel.UserRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository underTest;

    @AfterEach
    void down(){
        underTest.deleteAll();

    }

    private User userTest() {
        return new User(
                "ali", "amer", "aliamer19ali@gmail.com", "1234", UserRole.USER);
    }

    @Test
    void findByEmailExists(){
        User user = userTest();
        underTest.save(user);
        boolean userHere = underTest.findByEmail(user.getEmail()).isPresent();
        assertThat(userHere).isTrue();
    }

    @Test
    void findByEmailNotExists(){
        User user = userTest();

        boolean userHere = underTest.findByEmail(user.getEmail()).isPresent();
        assertThat(userHere).isFalse();
    }
    @Test
    void enableAppUser(){

  //todo : check
    }


}