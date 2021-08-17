package com.login.login_app.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.login.login_app.models.Registration;
import com.login.login_app.models.userModel.User;
import com.login.login_app.models.userModel.UserRole;
import com.login.login_app.services.RegistrationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = RegistrationController.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@WithUserDetails(value = "aliamer")
class RegistrationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegistrationController underTest;

    @MockBean
    private RegistrationService registrationService;

    private Registration newRegister() {
        var user = new Registration("ali", "amer", "aliamer19ali@gmail.com", "ali123");
        return user;
    }
    private User getUser() {
        return new User("ali","amer","aliamer19ali@gmaili.com","ali123", UserRole.USER);
    }

    private User makeUserFromReg(Registration registration){
        var user = new User(registration.getFirstName(),registration.getLastName(),registration.getEmail(),registration.getPassword(), UserRole.USER);
        return user;
    }

    private User getAdmin() {
        return new User("ali","amer","aliamer19ali@gmaili.com","ali123", UserRole.ADMIN);
    }



    @Test
    @WithMockUser
    void registerNewUser() throws Exception {
        given(registrationService.register(newRegister())).willReturn("token");
        mockMvc.perform(post("/api/v1/registration").contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(objectToString(any()))))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser
    void UserCanNotMakeAccount()throws Exception{
        User user = getUser();
        given(registrationService.register(newRegister())).willReturn("token");
        mockMvc.perform(post("/api/v1/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(objectToString(user))))
                        .andExpect(status().isAccepted());
    }



    @Test
    @WithMockUser
    void adminCanNotMakeAccount()throws Exception{
        User admin = getAdmin();
        mockMvc.perform(post("/api/v1/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(objectToString(admin))))
                        .andExpect(status().isAccepted());
    }



    @Test
    @WithMockUser
    void confirm() throws Exception {
        mockMvc.perform(get("/api/v1/registration/confirm")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectToString(newRegister()))))
                .andExpect(status().isOk());

    }

    private String objectToString(Object object) {

        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}