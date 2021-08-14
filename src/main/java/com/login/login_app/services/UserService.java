package com.login.login_app.services;

import com.login.login_app.exception.exceptions.NotFoundException;
import com.login.login_app.exception.exceptions.ValidEmailException;
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
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        boolean userHere = userRepository.findByEmail(email).isPresent();
        if(!userHere){
            throw new UsernameNotFoundException(String.format("User with email %s not found", email) );
        } return userRepository.findByEmail(email).get();
    }
    public String signUp(User user){

       boolean weFoundUser = userRepository.findByEmail(user.getEmail()).isPresent();
        if (weFoundUser)
            throw new ValidEmailException("EMAIL already taken");

       var encodePassword = bCryptPasswordEncoder.encode(user.getPassword());

       user.setPassword(encodePassword);

       userRepository.save(user);


        return makeToken(user);

    }

    private String makeToken(User user) {
        String token = UUID.randomUUID().toString();
        var Confirmation = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(10),
                user
        );

        confirmationTokenService.saveConfirmationToken(Confirmation);
        return token;
    }

    public int enableAppUser(String email) {
        return userRepository.enableAppUser(email);
    }

    public List<User> getAllUsers() {
        var users = userRepository.findAll();
        return users;
    }

    public String deleteUsers(Long userId) {
        boolean userExist = userRepository.existsById(userId);
        if (userExist) {
            userRepository.deleteById(userId);
            return "has removed";

        }
        throw new NotFoundException("there is no user like that");

    }

    public String blockUser(Long userId, boolean block) {

        boolean userExist = userRepository.existsById(userId);
        if (userExist) {
           var  user = userRepository.findById(userId).get();
           user.setLocked(block);
            userRepository.save(user);
            return "has blocked";

        }
        throw new NotFoundException("there is no user like that");
    }

    public User getUser(Long userId) {
        var userExist =userRepository.existsById(userId);
        if(userExist)
           return userRepository.getById(userId);

        throw new NotFoundException("there is no user like that");
    }
}
