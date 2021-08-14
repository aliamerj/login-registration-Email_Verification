package com.login.login_app.services.emailService;

import org.springframework.stereotype.Service;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.function.Predicate;

@Service
public class EmailValidator implements Predicate<String> {


    @Override
    public boolean test(String email) {
        if (email.contains("@") && email.endsWith(".com") && email.length() >= 7)
            return true;

        return false;
    }
}
