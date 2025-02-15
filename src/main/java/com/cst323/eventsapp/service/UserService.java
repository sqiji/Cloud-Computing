package com.cst323.eventsapp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cst323.eventsapp.data.UserRepository;
import com.cst323.eventsapp.models.UserEntity;
import com.cst323.eventsapp.models.UserModel;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserModel save(UserEntity userEntity) { 
        //userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
        // UserEntity userEntity = convertToEntity(userModel);
        // UserEntity savedUser = userRepository.save(userEntity);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userRepository.save(userEntity);
        return convertToModel(userEntity);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByLoginName(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // Return user without roles/authorities
        return new User(userEntity.getUserName(), userEntity.getPassword(), new ArrayList<>());
    }

    public UserModel findByLoginName(String loginName) {
        UserEntity userEntity = userRepository.findByLoginName(loginName);
        if (userEntity != null) {
            return new UserModel(userEntity.getId().toString(), userEntity.getUserName(), userEntity.getPassword());
        }
        return null;
    }

    public boolean verifyPassword(UserModel user) {
        UserEntity userEntity = userRepository.findByLoginName(user.getUserName());
        if (userEntity == null) {
            return false;
        }
        return passwordEncoder.matches(user.getPassword(), userEntity.getPassword());
    }

    public UserModel findById(String id) {
        UserEntity userEntity = userRepository.findById(Long.parseLong(id));
        return convertToModel(userEntity);
    }

    public void delete(String id) {
        userRepository.deleteById(Long.parseLong(id));
    }

    public List<UserModel> findAll() {
        List<UserEntity> userEntities = userRepository.findAll();
        return convertToModels(userEntities);
    }

    private List<UserModel> convertToModels(List<UserEntity> userEntities) {
        List<UserModel> userModels = new ArrayList<>();
        for (UserEntity userEntity : userEntities) {
            userModels.add(convertToModel(userEntity));
        }
        return userModels;
    }

    private UserModel convertToModel(UserEntity userEntity) {
        UserModel userModel = new UserModel();
        userModel.setId(userEntity.getId().toString());
        userModel.setUserName(userEntity.getUserName());
        userModel.setPassword(userEntity.getPassword());
        // userModel.setEnabled(userEntity.isEnabled());
        // userModel.setAccountNonExpired(userEntity.isAccountNonExpired());
        // userModel.setCredentialsNonExpired(userEntity.isCredentialsNonExpired());
        // userModel.setAccountNonLocked(userEntity.isAccountNonLocked());
        return userModel;
    }

    private UserEntity convertToEntity(UserModel userModel) {
        UserEntity userEntity = new UserEntity();
        if (userModel.getId() != null) {
            userEntity.setId(Long.parseLong(userModel.getId()));
        }
        userEntity.setUserName(userModel.getUserName());
        userEntity.setPassword(userModel.getPassword());
        // userEntity.setEnabled(userModel.isEnabled());
        // userEntity.setAccountNonExpired(userModel.isAccountNonExpired());
        // userEntity.setCredentialsNonExpired(userModel.isCredentialsNonExpired());
        // userEntity.setAccountNonLocked(userModel.isAccountNonLocked());
        return userEntity;
    }
}
