package com.mbarca.vete.repository.impl;

import com.mbarca.vete.domain.User;
import com.mbarca.vete.repository.UserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final String CREATE_USER = "INSERT INTO Users (name, password, role) VALUES (?, ?, ?)";
    private final String DELETE_USER = "DELETE FROM Users WHERE name = ?";

    private final JdbcTemplate jdbcTemplate;

    public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Integer createUser(User user) {
        return jdbcTemplate.update(CREATE_USER,
                user.getName(),
                user.getPassword(),
                user.getRole());
    }

    @Override
    public Integer deleteUser(String name){
        return jdbcTemplate.update(DELETE_USER, name);
    }
}
