package com.mbarca.vete.repository;

import com.mbarca.vete.domain.User;

public interface AuthRepository {
    User findUserByName(String userName);
}
