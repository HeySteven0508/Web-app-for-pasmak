package com.pasmakms.demo.domain;


import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class RegistrationForm {

    private String username;
    private String password;
    private String fullName;
    private String roles;

    public UserAccount toUserAccount(PasswordEncoder passwordEncoder){
        return new UserAccount(username,passwordEncoder.encode(password),fullName,roles);
    }
}
