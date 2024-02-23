package com.mbarca.vete.repository.impl;

import com.mbarca.vete.domain.User;
import com.mbarca.vete.repository.UserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final String CREATE_USER = "INSERT INTO Users (name, password, role) VALUES (?, ?, ?)";
    private final String DELETE_USER = "DELETE FROM Users WHERE name = ?";
    final String FIND_USER_BY_NAME= "SELECT * FROM Users WHERE name = ?";
    private final String GET_ALL_USERS = "SELECT * FROM Users";

    private final JdbcTemplate jdbcTemplate;

    public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Integer createUser(User user) {
        return jdbcTemplate.update(CREATE_USER,
                user.getUserName(),
                user.getPassword(),
                user.getRole());
    }

    @Override
    public Integer deleteUser(String userName){
        return jdbcTemplate.update(DELETE_USER, userName);
    }

    public User findUserByName(String userName) {
        Object[] params = {userName};
        int[] types = {1};
        return jdbcTemplate.queryForObject(FIND_USER_BY_NAME, params, types, new UserRowMapper());
    }
    @Override
    public List<User> getUsers() {
        return jdbcTemplate.query(GET_ALL_USERS,new UserRowMapper());
    }

    static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setUserName(rs.getString("name").toLowerCase());
            user.setRole(rs.getString("role"));
            user.setPassword(rs.getString("password"));
            return user;
        }
    }
}
