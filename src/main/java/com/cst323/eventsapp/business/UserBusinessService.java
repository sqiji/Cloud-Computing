package com.cst323.eventsapp.business;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.cst323.eventsapp.data.UserRepository;
import com.cst323.eventsapp.models.UserEntity;

public class UserBusinessService implements  UserDetailsService{

    @Autowired
    private UserRepository service;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
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
