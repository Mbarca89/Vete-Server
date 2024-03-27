package com.mbarca.vete.repository;

import com.mbarca.vete.domain.User;
import com.mbarca.vete.exceptions.UserNotFoundException;

import java.util.List;

public interface UserRepository {
    Integer createUser(User user);
    Integer deleteUser(String userName);
    User findUserByName(String userName);
    List<User> getUsers();
    Integer editUser(User user) throws UserNotFoundException;

}
