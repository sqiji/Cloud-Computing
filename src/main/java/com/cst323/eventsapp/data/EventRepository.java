package com.cst323.eventsapp.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.cst323.eventsapp.models.EventEntity;

/**
 * This repository class implements the EventRepositoryInterface and handles database interactions
 * for the Event entity using Spring's JdbcTemplate.
 */
@Repository
public class EventRepository implements EventRepositoryInterface {

    private final JdbcTemplate jdbcTemplate;

   

    private static final Logger logger = LoggerFactory.getLogger(EventRepository.class);

    /**
     * Constructor for the EventRepository.
     * It receives the JdbcTemplate dependency through constructor injection.
     * @param jdbcTemplate The Spring JdbcTemplate for executing SQL queries.
     */
    @Autowired
    public EventRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Retrieves all EventEntity records from the 'events' table.
     * @return A List of all EventEntity objects.
     */
    @Override
    public List<EventEntity> findAll() {

        logger.trace("****** handle request from findAll()");

        String sql = "SELECT * FROM events";
        return jdbcTemplate.query(sql, new EventModelRowMapper());
    }

    /**
     * Deletes an EventEntity record from the 'events' table based on its ID.
     * @param id The ID of the EventEntity to delete.
     */
    @Override
    public void deleteById(Long id) {

        logger.trace("****** handle request from deleteById()");
        String sql = "DELETE FROM events WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    /**
     * Saves a new EventEntity to the 'events' table or updates an existing one if its ID is present.
     * @param event The EventEntity object to save.
     * @return The saved EventEntity object (with its generated ID if it was a new entity).
     */
    @Override
    public EventEntity save(EventEntity event) {

        logger.trace("****** handle request from save()");

        if (event.getId() != null && existsById(event.getId())) {
            // Update the existing event
            update(event);
            return event;
        }
    
        // Insert new event
        String sql = "INSERT INTO events (name, date, location, description) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, event.getName(), event.getDate(), event.getLocation(), event.getDescription());
    
        // Retrieve last inserted ID
        Long id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
        event.setId(id);
        return event;

    }

    /**
     * Updates an existing EventEntity record in the 'events' table.
     * @param event The EventEntity object with updated information.
     * @throws RuntimeException if the event with the given ID is not found.
     */
    public void update(EventEntity event) {

        logger.trace("****** handle request from update()");

        String sql = "UPDATE events SET name = ?, date = ?, location = ?, description = ? WHERE id = ?";
    
    int rowsUpdated = jdbcTemplate.update(sql, 
        event.getName(),
        event.getDate(),
        event.getLocation(),
        //event.getOrganizerid(),
        event.getDescription(),
        event.getId());

    if (rowsUpdated == 0) {
        throw new RuntimeException("Event with ID " + event.getId() + " not found for update.");
    }
    }

    /**
     * Retrieves an EventEntity record from the 'events' table based on its ID.
     * @param id The ID of the EventEntity to retrieve.
     * @return The EventEntity object with the given ID, or null if not found (though queryForObject will throw an exception).
     */
    @Override
    public EventEntity findById(Long id) {

        logger.trace("****** handle request from findById()");

        String sql = "SELECT * FROM events WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new EventModelRowMapper(), id);
    }

    /**
     * Checks if an EventEntity record exists in the 'events' table with the given ID.
     * @param id The ID to check for.
     * @return true if an event with the given ID exists, false otherwise.
     */
    @Override
    public boolean existsById(Long id) {

        logger.trace("****** handle request from existById()");

        String sql = "SELECT COUNT(*) FROM events WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    /**
     * Retrieves a List of EventEntity records from the 'events' table whose description contains the given string (case-insensitive).
     * @param description The description to search for.
     * @return A List of EventEntity objects whose description contains the search term.
     */
    @Override
    public List<EventEntity> findByDescription(String description) {

        logger.trace("****** handle request from findByDescription()");

        String sql = "SELECT * FROM events WHERE description LIKE ?";
        return jdbcTemplate.query(sql, new EventModelRowMapper(), "%" + description + "%");
    }

    /**
     * Inner static class implementing the RowMapper interface to map a ResultSet row to an EventEntity object.
     */
    private static class EventModelRowMapper implements RowMapper<EventEntity> {
        @Override
        public EventEntity mapRow(ResultSet rs, int rowNum) throws SQLException {

            logger.trace("****** handle request from mapRow()"); 
            EventEntity event = new EventEntity();
            event.setId(rs.getLong("id"));
            event.setName(rs.getString("name"));
            event.setDate(rs.getDate("date"));
            event.setLocation(rs.getString("location"));
            //event.setOrganizerid(rs.getString("organizerid")); // Fix: Use setLong
            event.setDescription(rs.getString("description"));
            return event;
        }
    }
}
