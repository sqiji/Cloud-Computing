package com.cst323.eventsapp.business;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.cst323.eventsapp.controllers.EventController;
import com.cst323.eventsapp.data.UserRepository;
import com.cst323.eventsapp.models.UserEntity;


/**
 * This class implements the Spring Security's UserDetailsService interface.
 * It is responsible for loading user details during the authentication process.
 * It interacts with the UserRepository to retrieve user information from the data source.
 */
public class UserBusinessService implements  UserDetailsService{

    
    //Auto-wired dependency on the UserRepository.
    //This repository is used to access user data from the database.
     
    @Autowired
    private UserRepository service;

    //Logger instance for logging events and debugging.
    private static final Logger logger = LoggerFactory.getLogger(UserBusinessService.class);

    /**
     * This method is the core of the UserDetailsService interface.
     * It loads user details based on the provided username.
     * @param username The username of the user trying to authenticate.
     * @return A UserDetails object representing the loaded user.
     * @throws UsernameNotFoundException If the user with the given username is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{

        logger.trace("******** handle request from loadUserByUsername()" + username);

        UserEntity user = service.findByLoginName(username);
        if(user != null){
            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            authorities.add(new SimpleGrantedAuthority("USER"));
            return new User(user.getUserName(), user.getPassword(), authorities);
        }
        else{
            throw new UsernameNotFoundException("username not found");
        }
    }
    
}
