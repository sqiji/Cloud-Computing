package com.cst323.eventsapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // This annotation indicates that this class is a configuration class
@EnableWebSecurity
public class SecurityConfig {

// inject the UserDetailsService and PasswordEncoder beans from the Spring context. 
//These classes are part of the Spring Security framework
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

// Constructor injection of the UserDetailsService and PasswordEncoder beans
    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

// The securityFilterChain method is used to define the security filter chain.
// This method is annotated with @Bean, which means that the method returns a bean that is managed by the Spring container.
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(authorizeHttpRequests ->
            authorizeHttpRequests
                // allow anyone to access the registration and login pages
                .requestMatchers("/users/register", "/users/loginForm", "/", "/index", "/css/**", "/js/**").permitAll()
                // only admin users can access the edit and delete pages
                .requestMatchers("/events", "/events/edit/**", "/events/delete/**").authenticated()
                // all other requests must be authenticated. Home, Create.
                .anyRequest().authenticated()
        )
        //define where the login and logout pages are located
        .formLogin(loginForm ->
            loginForm
                .loginPage("/users/loginForm")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/users/", true)
                .failureUrl("/users/loginForm?error=true")
                .permitAll()
        )
        .logout(logout ->
            logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/index")
        );
    return http.build();
}

//Necessary because we are using a custom UserDetailsService and PsswordEncoder
@Autowired
public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
}



}

