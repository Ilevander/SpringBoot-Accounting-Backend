package com.ilyass.admin.service;

import com.ilyass.admin.domain.User;
import com.ilyass.admin.exception.domain.UsernameExistException;

import java.util.List;

public interface UserService {

    User register(String firstname, String lastname, String username, String email) throws UsernameExistException;

    List<User> getUsers();

    User findUserByUsername(String username);

    User findUserByEmail(String email);

}
