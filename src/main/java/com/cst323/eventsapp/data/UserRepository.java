package com.cst323.eventsapp.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.cst323.eventsapp.models.UserEntity;


@Repository
public class UserRepository implements UserRepositoryInterface {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserEntity findByLoginName(String loginName) {
        String sql = "SELECT * FROM users WHERE login_name = ?";
        try {
            List<UserEntity> users = jdbcTemplate.query(sql, new UserExtractor(), loginName);
            return users.isEmpty() ? null : users.get(0); // Ensure a single result
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<UserEntity> findAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, new UserExtractor());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
    

    @Override
    public UserEntity save(UserEntity userEntity) {
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

    @Override
    public UserEntity findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
    try {
        List<UserEntity> users = jdbcTemplate.query(sql, new UserExtractor(), id);
        return users.isEmpty() ? null : users.get(0); // Return first user or null
    } catch (EmptyResultDataAccessException e) {
        return null;
    }
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM users";
        Long result = jdbcTemplate.queryForObject(sql, Long.class);
        return result != null ? result : 0;
    }

    @Override
    public void delete(UserEntity user) {
        deleteById(user.getId());
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM users";
        jdbcTemplate.update(sql);
    }

    @Override
    public void deleteAll(Iterable<? extends UserEntity> users) {
        for (UserEntity user : users) {
            delete(user);
        }
    }

    @Override
    public List<UserEntity> saveAll(Iterable<UserEntity> users) {
        for (UserEntity user : users) {
            save(user);
        }
        return (List<UserEntity>) users;
    }

    private static class UserExtractor implements ResultSetExtractor<List<UserEntity>> {
        @Override
        public List<UserEntity> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<UserEntity> users = new ArrayList<>();
            while (rs.next()) {
                UserEntity user = new UserEntity();
                user.setId(rs.getLong("id"));
                user.setUserName(rs.getString("login_name"));
                user.setPassword(rs.getString("password"));
                // user.setEnabled(rs.getBoolean("enabled"));
                // user.setAccountNonExpired(rs.getBoolean("account_non_expired"));
                // user.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
                // user.setAccountNonLocked(rs.getBoolean("account_non_locked"));
                users.add(user);
            }
            return users;
        }
    }
}
