package com.cst323.eventsapp.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cst323.eventsapp.models.EventModel;
import com.cst323.eventsapp.models.EventSearch;
import com.cst323.eventsapp.service.EventService;

import jakarta.validation.Valid;


/**
 * This controller handles HTTP requests related to events.
 * It provides functionalities for displaying, creating, editing, deleting, and searching events.
 */
@Controller
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    private static final Logger logger = LoggerFactory.getLogger(EventController.class);

    /**
     * Constructor for the EventController.
     * It receives the EventService dependency through constructor injection.
     * @param eventService The service responsible for event-related business logic.
     */
    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * Handles GET requests to the base path "/events".
     * Retrieves all events from the EventService and displays them on the "events" view.
     * @param model The Spring Model object used to pass data to the view.
     * @return The name of the view to render (in this case, "events").
     */
    @GetMapping
    public String getAllEvents(Model model) {
        logger.trace("******* handling request to get all events");
        List<EventModel> events = eventService.findAll();
        model.addAttribute("events", events);
        model.addAttribute("message", "Showing all events");
        model.addAttribute("pageTitle", "Events");
        return "events";
    }

    /**
     * Handles GET requests to "/events/create".
     * Displays the form for creating a new event.
     * @param model The Spring Model object used to pass data to the view.
     * @return The name of the view to render (in this case, "create-event").
     */
    @GetMapping("/create")
    public String showCreateEventForm(Model model) {
        logger.trace("******* handling request form create form");
        model.addAttribute("event", new EventModel());
        model.addAttribute("pageTitle", "Create Event");
        return "create-event";
    }

    /**
     * Handles POST requests to "/events/create".
     * Creates a new event based on the submitted form data.
     * @param event   The EventModel object populated with the form data.
     * @param result  The BindingResult object that holds the validation results.
     * @param model   The Spring Model object used to pass data to the view (in case of validation errors).
     * @return Redirects to the "/events" page upon successful creation, otherwise returns to the "create-event" form with error messages.
     */
    @PostMapping("/create")
    public String createEvent(@ModelAttribute @Valid EventModel event, BindingResult result, Model model) {
        logger.trace("******* new event was created");
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Create Event");
            return "create-event";
        }
        eventService.save(event);
        return "redirect:/events";
    }


     /**
     * Handles GET requests to "/events/edit/{id}".
     * Displays the form for editing an existing event.
     * @param id    The ID of the event to be edited, extracted from the URL path.
     * @param model The Spring Model object used to pass data to the view.
     * @return The name of the view to render (in this case, "edit-event").
     */
    @GetMapping("/edit/{id}")
    public String showEditEventForm(@PathVariable String id, Model model) {
        logger.trace("******* handling request from edit form");
        EventModel event = eventService.findById(id);
        model.addAttribute("event", event);
        return "edit-event";
    }

    /**
     * Handles POST requests to "/events/edit/{id}".
     * Updates an existing event based on the submitted form data.
     * @param id    The ID of the event to be updated, extracted from the URL path.
     * @param event The EventModel object populated with the updated form data.
     * @param model The Spring Model object used to pass data to the view.
     * @return Redirects to the "/events" page after successful update.
     */
    @PostMapping("/edit/{id}")
    public String updateEvent(@PathVariable String id, @ModelAttribute EventModel event, Model model) {
    
        logger.trace("******* event was updated");
        EventModel updatedEvent = eventService.updateEvent(id, event);
        model.addAttribute("event", updatedEvent);
        return "redirect:/events";
    }

     /**
     * Handles GET requests to "/events/delete/{id}".
     * Deletes an event with the specified ID.
     * @param id The ID of the event to be deleted, extracted from the URL path.
     * @return Redirects to the "/events" page after successful deletion.
     */
    @GetMapping("/delete/{id}")
    public String deleteEvent(@PathVariable String id) {
        logger.trace("******* event was deleted");
        eventService.delete(id);
        return "redirect:/events";
    }

    // EventController.java (part of the existing file)
    /**
     * Handles GET requests to "/events/search".
     * Displays the form for searching events.
     * @param model The Spring Model object used to pass data to the view.
     * @return The name of the view to render (in this case, "searchForm").
     */
    @GetMapping("/search")
    public String searchForm(Model model) {
        logger.trace("******* handling request from search form");
        model.addAttribute("eventSearch", new EventSearch());
        return "searchForm";
    }

    /**
     * Handles POST requests to "/events/search".
     * Searches for events based on the submitted search criteria.
     * @param eventSearch The EventSearch object populated with the search criteria from the form.
     * @param result      The BindingResult object that holds the validation results for the search form.
     * @param model       The Spring Model object used to pass data to the view.
     * @param searchTerm  The search term explicitly passed as a request parameter.
     * @return Returns the "searchForm" view with error messages if validation fails, otherwise returns the "events" view with the search results.
     */
    @PostMapping("/search")
    public String search(@ModelAttribute @Valid EventSearch eventSearch, 
            BindingResult result, Model model, @RequestParam("searchString") String searchTerm) {

        logger.trace("******* searched event found");

        if (result.hasErrors()) {
            return "searchForm";
        }
        //filter out potentially dangerous input
        searchTerm = sanitizeInput(searchTerm);
        List<EventModel> events = eventService.findByDescription(searchTerm);
        //List<EventModel> events = eventService.findByDescription(eventSearch.getSearchString());
        model.addAttribute("message", "Search results for " + eventSearch.getSearchString());
        model.addAttribute("events", events);
        return "events";
    }


    
    /**
     * Method to filter out potentially dangerous input to prevent security vulnerabilities like SQL injection.
     * @param input The string to be sanitized.
     * @return The sanitized string.
     */
    public String sanitizeInput(String input){

        logger.trace("******* In sanitizeInput()");

        if (input == null){
            return null;
        }
        //Restrict length
        if(input.length() > 100){
            input = input.substring(0, 100);
        }
        //Escape single quotes
        input = input.replaceAll("'", "''");
        //Remove potentially dangerous keywords
        String[] keywords = {"--", ";", "/\\*", "\\*/", "xp_", "exec", "select", "insert", "update", "delete", "drop", "alter"};
        for (String keyword : keywords){
            input = input.replaceAll("(?i)" + keyword, "");
        }
        //Allow only alphanumeric charachers and spaces
        return input.replaceAll("[^a-zA-Z0-9]", "");
    }
    

}
