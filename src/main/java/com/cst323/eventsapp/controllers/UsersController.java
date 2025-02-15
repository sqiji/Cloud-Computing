package com.cst323.eventsapp.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cst323.eventsapp.models.UserEntity;
import com.cst323.eventsapp.models.UserModel;
import com.cst323.eventsapp.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/users")
public class UsersController {

    // userservice allows the controller to interact with the database and users table
    @Autowired
    private UserService userService;

   
    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserModel());
        return "register";
    }

    // response to the form submission. create a new user and save it to the database
    // @PostMapping("/register")
    // public String registerUser(@ModelAttribute UserModel user, Model model) {
    //     UserModel existingUser = userService.findByLoginName(user.getUserName());
    //     if (existingUser != null) {
    //         model.addAttribute("error", "User already exists!");
    //         model.addAttribute("user", user);
    //         return "register";
    //     }
      
    //     //setDefaultValues(user);
    //     // save the user to the database

    //     userService.save(user);
    //     logger.info("User registered: {}", user.getUserName());
    //     model.addAttribute("user", user);
    //     return "redirect:/users/loginForm";
    // }

    //response to the form submission. create a new user and save it to the database
    @PostMapping("/register")
    public String registerUser(@ModelAttribute UserModel user, Model model) {
    // Check if the user already exists in the database
    UserModel existingUser = userService.findByLoginName(user.getUserName());
    if (existingUser != null) {
        model.addAttribute("error", "User already exists!");
        model.addAttribute("user", user);
        return "register";
    }

    // Convert UserModel to UserEntity
    UserEntity userEntity = new UserEntity();
    userEntity.setUserName(user.getUserName());
    userEntity.setPassword(user.getPassword());  // Ensure this password is encoded before saving

    // Save the user to the database
    userService.save(userEntity);

    logger.info("User registered: {}", user.getUserName());
    model.addAttribute("user", user);
    return "redirect:/users/loginForm";  // Redirect to the login page after successful registration
}

    // if register form does not have these values, set default values here
    // private void setDefaultValues(UserModel user) {
    //     // set default values for the user
    //     user.setEnabled(true);
    //     user.setAccountNonExpired(true);
    //     user.setCredentialsNonExpired(true);
    //     user.setAccountNonLocked(true);
    // }


    // show the login form.
    @GetMapping("/loginForm")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new UserModel());
        model.addAttribute("pageTitle", "Login");
        return "login";
    }
 
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/users/loginForm";
    }
}
