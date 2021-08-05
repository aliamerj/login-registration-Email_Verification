package com.login.login_app.services;

import com.login.login_app.exception.exceptions.ExpiredException;
import com.login.login_app.exception.exceptions.HasConfirmed;
import com.login.login_app.exception.exceptions.NotFoundException;
import com.login.login_app.exception.exceptions.ValidEmailException;
import com.login.login_app.models.Registration;
import com.login.login_app.models.userModel.User;
import com.login.login_app.models.userModel.UserRole;
import com.login.login_app.repositories.UserRepository;
import com.login.login_app.services.emailService.EmailValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;


@Service
@AllArgsConstructor
public class RegistrationService {

    private final UserService appUserService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailValidator emailValidator;


    public String register(Registration request) {
        var isEmailValid =emailValidator.test(request.getEmail());
        if (!isEmailValid)
            throw new ValidEmailException("email is valid");

        String token = appUserService.signUp(
                new User(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getPassword(),
                        UserRole.USER

                )
        );



        return  "http://localhost:8080/api/v1/registration/confirm?token=" + token;
    }

    @Transactional
    public String confirmToken(String token) {
        var  confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new NotFoundException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new HasConfirmed("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new ExpiredException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        appUserService.enableAppUser(
                confirmationToken.getUser().getEmail());
        return "confirmed";
    }


}

