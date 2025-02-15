package com.cst323.eventsapp.controllers;

import java.util.List;

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

@Controller
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public String getAllEvents(Model model) {
        List<EventModel> events = eventService.findAll();
        model.addAttribute("events", events);
        model.addAttribute("message", "Showing all events");
        model.addAttribute("pageTitle", "Events");
        return "events";
    }

    @GetMapping("/create")
    public String showCreateEventForm(Model model) {
        model.addAttribute("event", new EventModel());
        model.addAttribute("pageTitle", "Create Event");
        return "create-event";
    }

    @PostMapping("/create")
    public String createEvent(@ModelAttribute @Valid EventModel event, BindingResult result, Model model) {

        /*
        // Encode fields to prevent XSS
        event.setName(Encode.forHtml(event.getName()));
        event.setLocation(Encode.forHtml(event.getLocation()));
        event.setOrganizerid(Encode.forHtml(event.getOrganizerid()));
        event.setDescription(Encode.forHtml(event.getDescription()));
        */

        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Create Event");
            return "create-event";
        }
        eventService.save(event);
        return "redirect:/events";
    }

    @GetMapping("/edit/{id}")
    public String showEditEventForm(@PathVariable String id, Model model) {
        EventModel event = eventService.findById(id);
        model.addAttribute("event", event);
        return "edit-event";
    }

    @PostMapping("/edit/{id}")
    public String updateEvent(@PathVariable String id, @ModelAttribute EventModel event, Model model) {
        
        /*
        // Encode fields to prevent XSS
        event.setName(Encode.forHtml(event.getName()));
        event.setLocation(Encode.forHtml(event.getLocation()));
        event.setOrganizerid(Encode.forHtml(event.getOrganizerid()));
        event.setDescription(Encode.forHtml(event.getDescription()));
        */

        EventModel updatedEvent = eventService.updateEvent(id, event);
        model.addAttribute("event", updatedEvent);
        return "redirect:/events";
    }

    @GetMapping("/delete/{id}")
    public String deleteEvent(@PathVariable String id) {
        eventService.delete(id);
        return "redirect:/events";
    }

    // EventController.java (part of the existing file)
    @GetMapping("/search")
    public String searchForm(Model model) {
        model.addAttribute("eventSearch", new EventSearch());
        return "searchForm";
    }

    @PostMapping("/search")
    public String search(@ModelAttribute @Valid EventSearch eventSearch, 
            BindingResult result, Model model, @RequestParam("searchString") String searchTerm) {
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


    
    //Method to filter out potentially dangerous input
    public String sanitizeInput(String input){
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
