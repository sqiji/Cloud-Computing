package com.cst323.eventsapp.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.cst323.eventsapp.models.UserEntity;

/**
 * This repository class implements the UserRepositoryInterface and handles database interactions
 * for the UserEntity using Spring's JdbcTemplate.
 */
@Repository
public class UserRepository implements UserRepositoryInterface {

    private final JdbcTemplate jdbcTemplate;

    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);

    /**
     * Constructor for the UserRepository.
     * It receives the JdbcTemplate dependency through constructor injection.
     * @param jdbcTemplate The Spring JdbcTemplate for executing SQL queries.
     */
    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

     /**
     * Retrieves a UserEntity from the 'users' table based on the provided login name.
     * @param loginName The login name of the user to find.
     * @return The UserEntity object if found, otherwise null.
     */
    @Override
    public UserEntity findByLoginName(String loginName) {

        logger.trace("******* handling request from UserRepository.findByLoginName()");

        String sql = "SELECT * FROM users WHERE login_name = ?";
        try {
            List<UserEntity> users = jdbcTemplate.query(sql, new UserExtractor(), loginName);
            return users.isEmpty() ? null : users.get(0); // Ensure a single result
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    
    /**
     * Saves a new UserEntity to the 'users' table or updates an existing one if its ID is present.
     * @param userEntity The UserEntity object to save or update.
     * @return The saved UserEntity object (with its generated ID if it was a new entity).
     */
    @Override
    public UserEntity save(UserEntity userEntity) {

        logger.trace("******* handling request from UserRepository.save()");
        if (userEntity.getId() == null) {  // New user
            String sql = "INSERT INTO users (login_name, password) VALUES (?, ?)";
            jdbcTemplate.update(sql, userEntity.getUserName(), userEntity.getPassword());
            Long id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
            userEntity.setId(id);
        } else {  // Existing user
            String sql = "UPDATE users SET login_name = ?, password = ? WHERE id = ?";
            jdbcTemplate.update(sql, userEntity.getUserName(), userEntity.getPassword(), userEntity.getId());
        }
        return userEntity;
    }

    /**
     * Inner static class implementing the ResultSetExtractor interface to map a ResultSet
     * to a List of UserEntity objects. This is used when the query might return multiple rows,
     * although in the findByLoginName method, we expect at most one.
     */
    private static class UserExtractor implements ResultSetExtractor<List<UserEntity>> {
        @Override
        public List<UserEntity> extractData(ResultSet rs) throws SQLException, DataAccessException {
            logger.trace("******* handling request from UserRepository.extracData()");
            List<UserEntity> users = new ArrayList<>();
            while (rs.next()) {
                UserEntity user = new UserEntity();
                user.setId(rs.getLong("id"));
                user.setUserName(rs.getString("login_name"));
                user.setPassword(rs.getString("password"));
                users.add(user);
            }
            return users;
        }
    }
}
