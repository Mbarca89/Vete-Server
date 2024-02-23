package com.mbarca.vete.repository;

import com.mbarca.vete.domain.User;

import java.util.List;

public interface UserRepository {
    Integer createUser(User user);
    Integer deleteUser(String userName);
    User findUserByName(String userName);
    List<User> getUsers();

}
