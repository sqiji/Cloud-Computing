package com.cst323.eventsapp.config;

import  org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
public class PasswordEncodingConfig {
    
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(); //BCryte is a secure password hashing algorithm. 
                                            //It is the recommended password hashing algorithm by OWASP.

        //Could also use these options:
        //return NoOpPasswordEncoder().getInstance(); //not recommender for production since it does not encrypt password

        //or
        //return new Pbkdf2PasswordEncoder(); // PBKDF2 with HMAC SHA-1, 1024 interations, and 128-bit salt

        //or:
        //return SCryptPasswordEncoder(); //SCrypt is more secure than PBKDF2.

        //0r:
        //return new BCryptPasswordEncoder(10); // BCrypt with strength of 10 means 2^10 rounds of hashing

    }
    
}

