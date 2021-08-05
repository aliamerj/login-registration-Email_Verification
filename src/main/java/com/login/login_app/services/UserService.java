package com.login.login_app.services;

import com.login.login_app.models.ConfirmationToken;
import com.login.login_app.models.userModel.User;
import com.login.login_app.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException(String.format("User with email %s not found", email) ) );
    }
    public String signUp(User user){

       boolean weFoundUser = userRepository.findByEmail(user.getEmail()).isPresent();
        if (weFoundUser)
            throw new IllegalStateException("EMAIL already taken");

       String encodePassword = bCryptPasswordEncoder.encode(user.getPassword());

       user.setPassword(encodePassword);

       userRepository.save(user);

       String token = UUID.randomUUID().toString();
       var confir = new ConfirmationToken(
               token,
               LocalDateTime.now(),
               LocalDateTime.now().plusMinutes(10),
               user
       );
       confirmationTokenService.saveConfirmationToken(confir);
        return token;

    }
    public int enableAppUser(String email) {
        return userRepository.enableAppUser(email);
    }
}
