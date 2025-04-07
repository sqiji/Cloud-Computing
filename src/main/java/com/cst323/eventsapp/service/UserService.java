package com.cst323.eventsapp.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

/**
 * This service class provides business logic for user management, including saving users,
 * loading user details for authentication, and verifying passwords.
 * It implements the UserDetailsService interface for Spring Security integration.
 */
@Service
public class UserService implements UserDetailsService {

    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    /**
     * Constructor for the UserService.
     * It receives the UserRepository and PasswordEncoder dependencies through constructor injection.
     * @param userRepository    The repository for accessing user data.
     * @param passwordEncoder The encoder used to hash and verify passwords.
     */
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Saves a new user to the database after encoding their password.
     * @param userEntity The UserEntity object to save.
     * @return The saved UserModel object.
     */
    public UserModel save(UserEntity userEntity) { 
        logger.trace("****** handling request from save()");
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userRepository.save(userEntity);
        return convertToModel(userEntity);
    }

    /**
     * Implements the UserDetailsService interface to load user details by username for authentication.
     * @param username The username of the user to load.
     * @return A UserDetails object representing the loaded user.
     * @throws UsernameNotFoundException If the user with the given username is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.trace("****** handling request from loadUserByUsername()");
        UserEntity userEntity = userRepository.findByLoginName(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // Return user without roles/authorities
        return new User(userEntity.getUserName(), userEntity.getPassword(), new ArrayList<>());
    }

    /**
     * Finds a user by their login name and converts the result to a UserModel.
     * @param loginName The login name of the user to find.
     * @return The UserModel object if found, otherwise null.
     */
    public UserModel findByLoginName(String loginName) {
        logger.trace("****** handling request from findByLoginName()");
        UserEntity userEntity = userRepository.findByLoginName(loginName);
        if (userEntity != null) {
            return new UserModel(userEntity.getId().toString(), userEntity.getUserName(), userEntity.getPassword());
        }
        return null;
    }

    /**
     * Verifies if the provided password matches the encoded password of the given user.
     * @param user The UserModel object containing the plain text password to verify.
     * @return true if the password matches, false otherwise.
     */
    public boolean verifyPassword(UserModel user) {
        logger.trace("****** handling request from verifyPassword()");
        UserEntity userEntity = userRepository.findByLoginName(user.getUserName());
        if (userEntity == null) {
            return false;
        }
        return passwordEncoder.matches(user.getPassword(), userEntity.getPassword());
    }

    /**
     * Converts a list of UserEntity objects to a list of UserModel objects.
     * @param userEntities The List of UserEntity objects to convert.
     * @return A List of UserModel objects.
     */
    private List<UserModel> convertToModels(List<UserEntity> userEntities) {
        logger.trace("****** handling request from convertToModels()");
        List<UserModel> userModels = new ArrayList<>();
        for (UserEntity userEntity : userEntities) {
            userModels.add(convertToModel(userEntity));
        }
        return userModels;
    }

    /**
     * Converts a single UserEntity object to a UserModel object.
     * @param userEntity The UserEntity object to convert.
     * @return The UserModel object.
     */
    private UserModel convertToModel(UserEntity userEntity) {
        logger.trace("****** handling request from convertToModel()");
        UserModel userModel = new UserModel();
        userModel.setId(userEntity.getId().toString());
        userModel.setUserName(userEntity.getUserName());
        userModel.setPassword(userEntity.getPassword());
        return userModel;
    }

    /**
     * Converts a single UserModel object to a UserEntity object.
     * @param userModel The UserModel object to convert.
     * @return The UserEntity object.
     */
    private UserEntity convertToEntity(UserModel userModel) {
        logger.trace("****** handling request from convertToEntity()");
        UserEntity userEntity = new UserEntity();
        if (userModel.getId() != null) {
            userEntity.setId(Long.parseLong(userModel.getId()));
        }
        userEntity.setUserName(userModel.getUserName());
        userEntity.setPassword(userModel.getPassword());
        return userEntity;
    }
}
