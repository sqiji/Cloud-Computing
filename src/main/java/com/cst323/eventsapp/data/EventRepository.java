package com.cst323.eventsapp.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.cst323.eventsapp.models.EventEntity;

@Repository
public class EventRepository implements EventRepositoryInterface {

    private final JdbcTemplate jdbcTemplate;

    private final String url = "jdbc:mysql://localhost:3306/eventsapp";
    private final String username = "root";
    private final String password = "root";

    @Autowired
    public EventRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<EventEntity> findByOrganizerid(Long organizerid) {
        String sql = "SELECT * FROM events WHERE organizerid = ?";
        return jdbcTemplate.query(sql, new EventModelRowMapper(), organizerid);
    }

    @Override
    public List<EventEntity> findAll() {
        String sql = "SELECT * FROM events";
        return jdbcTemplate.query(sql, new EventModelRowMapper());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM events WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public EventEntity save(EventEntity event) {

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

        // String sql = "INSERT INTO events (name, date, location, organizerid, description) VALUES (?, ?, ?, ?, ?)";
        // jdbcTemplate.update(sql, event.getName(), event.getDate(), event.getLocation(), event.getOrganizerid(), event.getDescription());

    }

   
    public void update(EventEntity event) {

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
        // String sql = "UPDATE events SET name = ?, date = ?, location = ?, organizerid = ?, description = ? WHERE id = ?";
        // jdbcTemplate.update(sql, event.getName(), event.getDate(), event.getLocation(), event.getOrganizerid(), event.getDescription(), event.getId());
    }

    @Override
    public EventEntity findById(Long id) {
        String sql = "SELECT * FROM events WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new EventModelRowMapper(), id);
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM events WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public List<EventEntity> findByDescription(String description) {
        String sql = "SELECT * FROM events WHERE description LIKE ?";
        return jdbcTemplate.query(sql, new EventModelRowMapper(), "%" + description + "%");
    }

    private static class EventModelRowMapper implements RowMapper<EventEntity> {
        @Override
        public EventEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
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




    // @Override
    // public List<EventEntity> findByOrganizerid(Long organizerid) {
    //     List<EventEntity> events = new ArrayList<>();
    //     String sql = "SELECT * FROM events WHERE organizerid = ?"; //+ organizerid;
    //     //events = jdbcTemplate.query(sql, new EventModelRowMapper());

    //     try(Connection conn = DriverManager.getConnection(url, username, password);
    //         PreparedStatement pstmt = conn.prepareStatement(sql)){
                
    //             pstmt.setLong(1, organizerid);
    //             try (ResultSet rs = pstmt.executeQuery()){
    //                 while(rs.next()){
    //                     EventEntity event = new EventEntity();
    //                     event.setId(rs.getLong("id"));
    //                     event.setName(rs.getString("name"));
    //                     event.setDate(rs.getDate("date"));
    //                     event.setLocation(rs.getString("location"));
    //                     event.setOrganizerid(rs.getString("organizerid"));
    //                     event.setDescription(rs.getString("description"));
    //                     events.add(event);
    //                 }
    //             }
    //         }catch (SQLException e){
    //             e.printStackTrace();
    //         }
    //     return events;
    // }

    // @Override
    // public List<EventEntity> findAll() {
    //     //Declare List 
    //     List<EventEntity> events = new ArrayList<>();

    //     String sql = "SELECT * FROM events";
    //     //events = jdbcTemplate.query(sql, new EventModelRowMapper());


    //     try (Connection conn = DriverManager.getConnection(url, username, password);
    //         Statement stmt = conn.createStatement();
    //         ResultSet rs = stmt.executeQuery(sql)) {

    //         while(rs.next()){
    //             EventEntity event = new EventEntity();
    //             event.setId(rs.getLong("id"));
    //             event.setName(rs.getString("name"));
    //             event.setDate(rs.getDate("date"));
    //             event.setLocation(rs.getString("location"));
    //             event.setOrganizerid(rs.getString("organizerid"));
    //             event.setDescription(rs.getString("description"));
    //             events.add(event);
    //         }
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }

    //     return events;
    // }

    // @Override
    // public void deleteById(Long id) {
    //     String sql = "DELETE FROM events WHERE id = ?"; //+ id;
    //     //jdbcTemplate.update(sql);

    //     try (Connection conn = DriverManager.getConnection(url, username, password);
    //         PreparedStatement pstmt = conn.prepareStatement(sql)){
    //             pstmt.setLong(1, id);
    //             pstmt.executeQuery();
    //         }catch (SQLException e){
    //             e.printStackTrace();
    //         }
    // }

    // @Override
    // public EventEntity save(EventEntity event) {
    //     if (event.getId() == null) {
    //         // String sql = "INSERT INTO events (name, date, location, organizerid, description) " +
    //         //              "VALUES ('" + event.getName() + "', '" + event.getDate() + "', '" + event.getLocation() + "', '" + event.getOrganizerid() + "', '" + event.getDescription() + "')";
    //         // jdbcTemplate.update(sql);
    //         // Long id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
    //         // event.setId(id);

    //         //Protect from SQL injection
    //         String sql = "INSERT INTO events (name, date, location, organizerid, description) VALUES (?, ?, ?, ?, ?)";
            
    //         try (Connection conn = DriverManager.getConnection(url, username, password);
    //             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {  

    //                 pstmt.setString(1, event.getName());
    //                 pstmt.setDate(2, new java.sql.Date(event.getDate().getTime()));
    //                 pstmt.setString(3, event.getLocation());
    //                 pstmt.setString(4, event.getOrganizerid());
    //                 pstmt.setString(5, event.getDescription());

    //                 int result = pstmt.executeUpdate();
    //                 if(result == 1){
    //                     ResultSet rs = pstmt.getGeneratedKeys();
    //                     if(rs.next()){
    //                         event.setId(rs.getLong(1));
    //                     }
    //                     return event;
    //                 }
    //             }catch(SQLException e){
    //                 e.printStackTrace();
    //             }

    //     } 
    //     return null;
    // }

    // public void update(EventEntity event){
    //     // String sql = "UPDATE events SET name = '" + event.getName() + "', date = '" + event.getDate() + "', location = '" + event.getLocation() + "', organizerid = '" + event.getOrganizerid() + "', description = '" + event.getDescription() + 
    //     //              "' WHERE id = " + event.getId();
    //     // jdbcTemplate.update(sql);

    //     //Protect from SQL injection
    //      String sql = "UPDATE events SET id = ?, name = ?, date = ?, location = ?, organizerid = ?, description = ? WHERE id = ?";
        
    //     try (Connection conn = DriverManager.getConnection(url, username, password);
    //         PreparedStatement pstmt = conn.prepareStatement(sql)) {  

    //             //pstmt.setLong(1, event.getId());
    //             pstmt.setString(1, event.getName());
    //             pstmt.setDate(2, new java.sql.Date(event.getDate().getTime()));
    //             pstmt.setString(3, event.getLocation());
    //             pstmt.setString(4, event.getOrganizerid());
    //             pstmt.setString(5, event.getDescription());

    //             pstmt.executeUpdate();
                
                   
    //         }catch(SQLException e){
    //             e.printStackTrace();
    //         }
    // }

    // @Override
    // public EventEntity findById(Long id) {
    //     EventEntity event = null;
    //     // String sql = "SELECT * FROM events WHERE id = " + id;
    //     // event = jdbcTemplate.queryForObject(sql, new EventModelRowMapper());

    //     String sql = "SELECT * FROM events WHERE id = ?";

    //     try (Connection conn = DriverManager.getConnection(url, username, password);
    //     PreparedStatement pstmt = conn.prepareStatement(sql)) {   //Using PreparedStatement

    //         pstmt.setLong(1, id);
    
    //         try (ResultSet rs = pstmt.executeQuery()){
        
    //             if (rs.next()) {
    //                 event = new EventEntity();
    //                 event.setId(rs.getLong("id"));
    //                 event.setName(rs.getString("name"));
    //                 event.setDate(rs.getDate("date"));
    //                 event.setLocation(rs.getString("location"));
    //                 event.setOrganizerid(rs.getString("organizerid"));
    //                 event.setDescription(rs.getString("description"));
    //             }
    //         }
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    //     return event;
    // }

    // @Override
    // public boolean existsById(Long id) {
    //     String sql = "SELECT COUNT(*) FROM events WHERE id = " + id;
    //     Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
    //     return count != null && count > 0;
    // }

    // private static class EventModelRowMapper implements RowMapper<EventEntity> {
    //     @Override
    //     public EventEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    //         EventEntity event = new EventEntity();
    //         event.setId(rs.getLong("id"));
    //         event.setName(rs.getString("name"));
    //         event.setDate(rs.getDate("date"));
    //         event.setLocation(rs.getString("location"));
    //         event.setOrganizerid(rs.getString("organizerid"));
    //         event.setDescription(rs.getString("description"));
    //         return event;
    //     }
    // }

    // @Override
    // public List<EventEntity> findByDescription(String description) { 
    //     List<EventEntity> events = new ArrayList<>();
    //     String sql = "SELECT * FROM events WHERE description LIKE ?";
    //     //events = jdbcTemplate.query(sql, new EventModelRowMapper());

    //     try(Connection conn = DriverManager.getConnection(url, username, password);
    //         PreparedStatement pstmt = conn.prepareStatement(sql)){
    //             pstmt.setString(1, "%" + description + "%");
    //             try(ResultSet rs = pstmt.executeQuery()){
    //                 while(rs.next()){
    //                     EventEntity event = new EventEntity();
    //                     event.setId(rs.getLong("id"));
    //                     event.setName(rs.getString("name"));
    //                     event.setDate(rs.getDate("date"));
    //                     event.setLocation(rs.getString("location"));
    //                     event.setOrganizerid(rs.getString("organizerid"));
    //                     event.setDescription(rs.getString("description"));
    //                     events.add(event);
    //                 }
    //             }    
    //         } catch (SQLException e) {
    //             e.printStackTrace();
    //         }
    //     return events;
    // }
}
