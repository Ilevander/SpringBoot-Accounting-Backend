package com.ilyass.admin.service;

import com.ilyass.admin.domain.User;

import java.util.List;

public interface UserService {

    User register(String firstname, String lastname, String username, String email);

    List<User> getUsers();

    User findUserByUsername(String username);

    User findUserByEmail(String email);

}
