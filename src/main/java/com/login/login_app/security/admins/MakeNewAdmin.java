package com.login.login_app.security.admins;

import com.login.login_app.models.userModel.User;
import com.login.login_app.models.userModel.UserRole;
import com.login.login_app.repositoriesTests.UserRepository;
import com.login.login_app.security.PasswordEncoderConf;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class MakeNewAdmin {
    private final UserRepository userRepository;
    private final PasswordEncoderConf passwordEncoderConf;


    public void addNewAdmin(String firstName, String lastName,String username , String password ) {
        var admin = new User();
        admin.setFirstName(firstName);
        admin.setLastName(lastName);
        admin.setUsername(username);
        admin.setPassword(passwordEncoderConf.bCryptPasswordEncoder().encode(password));
        admin.setUserRole(UserRole.ADMIN);
        admin.getAuthorities();
        admin.setLocked(false);
        admin.setEnabled(true);
        userRepository.save(admin);


    }
}
