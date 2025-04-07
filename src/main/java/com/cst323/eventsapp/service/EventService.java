package com.cst323.eventsapp.service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cst323.eventsapp.data.EventRepositoryInterface;
import com.cst323.eventsapp.models.EventEntity;
import com.cst323.eventsapp.models.EventModel;

/**
 * This service class provides the business logic for managing Event entities.
 * It interacts with the EventRepositoryInterface to perform database operations
 * and converts between EventEntity and EventModel objects.
 */
@Service
public class EventService  {

    private final EventRepositoryInterface eventRepository;

    private static final Logger logger = LoggerFactory.getLogger(EventService.class);

    /**
     * Constructor for the EventService.
     * It receives the EventRepositoryInterface dependency through constructor injection.
     * @param eventRepository The repository interface for Event entities.
     */
    @Autowired
    public EventService(EventRepositoryInterface eventRepository) {
        this.eventRepository = eventRepository;
    }

    /**
     * Retrieves all events from the database and converts them to a list of EventModel objects.
     * @return A List of EventModel objects representing all events.
     */
    public List<EventModel> findAll() {

        logger.trace("****** handling request from findAll()");
        List<EventEntity> eventEntities = eventRepository.findAll();
        return convertToModels(eventEntities);
    }


    /**
     * Saves a new EventModel to the database by converting it to an EventEntity.
     * If the EventModel has an ID, it updates the existing event.
     * @param event The EventModel object to save.
     * @return The saved EventModel object (with its generated ID if it was a new event).
     */
    public EventModel save(EventModel event) {
        logger.trace("****** handling request from save()");
        EventEntity eventEntity = convertToEntity(event);
        EventEntity savedEvent = eventRepository.save(eventEntity);
        return convertToModel(savedEvent);
    }

    /**
     * Deletes an event from the database based on its ID.
     * @param id The String representation of the ID of the event to delete.
     */
    public void delete(String id) {
        logger.trace("****** handling request from delete()");
        eventRepository.deleteById(Long.valueOf(id));
    }

    /**
     * Updates an existing event in the database.
     * @param id    The String representation of the ID of the event to update.
     * @param event The EventModel object containing the updated event information.
     * @return The updated EventModel object.
     */
    public EventModel updateEvent(String id, EventModel event) {
        logger.trace("****** handling request from updateEvent()");
        event.setId(id);
        return save(event);
    }

    /**
     * Retrieves an event from the database based on its ID and converts it to an EventModel.
     * @param id The String representation of the ID of the event to retrieve.
     * @return The EventModel object representing the retrieved event.
     */
    public EventModel findById(String id) {
        logger.trace("****** handling request from findById()");
        EventEntity eventEntity = eventRepository.findById(Long.valueOf(id));
        return convertToModel(eventEntity);
    }

     /**
     * Converts a list of EventEntity objects to a list of EventModel objects.
     * @param eventEntities The List of EventEntity objects to convert.
     * @return A List of EventModel objects.
     */
    private List<EventModel> convertToModels(List<EventEntity> eventEntities) {
        logger.trace("****** handling request from convertToModels()");
        List<EventModel> eventModels = new ArrayList<>();
        for (EventEntity eventEntity : eventEntities) {
            eventModels.add(convertToModel(eventEntity));
        }
        return eventModels;
    }

    /**
     * Converts a single EventEntity object to an EventModel object.
     * @param eventEntity The EventEntity object to convert.
     * @return The EventModel object.
     */
    private EventModel convertToModel(EventEntity eventEntity) {
        logger.trace("****** handling request from convertToModel()");
        return new EventModel(
                eventEntity.getId().toString(),
                eventEntity.getName(),
                eventEntity.getDate(),
                eventEntity.getLocation(),
                //eventEntity.getOrganizerid()
                eventEntity.getDescription()
        );
    }

     /**
     * Converts a single EventModel object to an EventEntity object.
     * @param eventModel The EventModel object to convert.
     * @return The EventEntity object.
     */
    private EventEntity convertToEntity(EventModel eventModel) {
        logger.trace("****** handling request from convertToEntity()");
        EventEntity eventEntity = new EventEntity();
        if (eventModel.getId() != null) {
            eventEntity.setId(Long.parseLong(eventModel.getId()));
        }
        eventEntity.setName(eventModel.getName());
        
        // Format the date to 'YYYY-MM-DD'
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(eventModel.getDate());
        eventEntity.setDate(Date.valueOf(formattedDate)); // Use java.sql.Date

        eventEntity.setLocation(eventModel.getLocation());
        //eventEntity.setOrganizerid(eventModel.getOrganizerid());
        eventEntity.setDescription(eventModel.getDescription());
        return eventEntity;
    }

    /**
     * Finds events in the database whose description contains the given search string.
     * @param searchString The string to search for in the event descriptions.
     * @return A List of EventModel objects whose description contains the search string.
     */
    public List<EventModel> findByDescription(String searchString) {
        logger.trace("****** handling request from findByDescription()");
        List<EventEntity> eventEntities = eventRepository.findByDescription(searchString);
        return convertToModels(eventEntities);  
    }
}
