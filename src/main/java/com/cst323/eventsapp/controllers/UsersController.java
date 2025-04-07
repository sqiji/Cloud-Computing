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


/**
 * This controller handles HTTP requests related to user registration, login, and logout.
 */
@Controller
@RequestMapping("/users")
public class UsersController {

    // userservice allows the controller to interact with the database and users table
    @Autowired
    private UserService userService;

   
    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

    /**
     * Handles GET requests to "/users/".
     * This method is likely the home page for logged-in users.
     * @return The name of the view to render (in this case, "home").
     */
    @GetMapping("/")
    public String home() {
        logger.trace("******* User logged in");
        return "home";
    }

    /**
     * Handles GET requests to "/users/register".
     * Displays the user registration form.
     * @param model The Spring Model object used to pass data to the view.
     * @return The name of the view to render (in this case, "register").
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        logger.trace("******* Handling request from register form");
        model.addAttribute("user", new UserModel());
        return "register";
    }

    /**
     * Handles POST requests to "/users/register".
     * Processes the submitted registration form, creates a new user, and saves it to the database.
     * @param user  The UserModel object populated with the data from the registration form.
     * @param model The Spring Model object used to pass data to the view (e.g., for error messages).
     * @return Redirects to the login form page ("/users/loginForm") upon successful registration,
     * otherwise returns to the "register" form with an error message if the user already exists.
     */
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

    logger.trace("******* new user has been registred" + user); 

    return "redirect:/users/loginForm";  // Redirect to the login page after successful registration   
}

    /**
     * Handles GET requests to "/users/loginForm".
     * Displays the user login form.
     * @param model The Spring Model object used to pass data to the view.
     * @return The name of the view to render (in this case, "login").
     */
    @GetMapping("/loginForm")
    public String showLoginForm(Model model) {
        
        logger.trace("******* handling request from login form");

        model.addAttribute("user", new UserModel());
        model.addAttribute("pageTitle", "Login");
        
        return "login";
    }
 
    /**
     * Handles GET requests to "/users/logout".
     * Invalidates the current user's session, effectively logging them out.
     * @param session The HttpSession object representing the user's session.
     * @return Redirects the user back to the login form page ("/users/loginForm").
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {

        logger.trace("******* handling request form logout");

        session.invalidate();
        return "redirect:/users/loginForm";
    }
}
