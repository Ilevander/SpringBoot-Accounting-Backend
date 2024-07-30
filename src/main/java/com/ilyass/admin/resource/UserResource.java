package com.ilyass.admin.resource;

import com.ilyass.admin.domain.User;
import com.ilyass.admin.exception.ExceptionHandling;
import com.ilyass.admin.exception.domain.EmailExistException;
import com.ilyass.admin.exception.domain.UserNotFoundException;
import com.ilyass.admin.exception.domain.UsernameExistException;
import com.ilyass.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path={"/","/user"})
public class UserResource extends ExceptionHandling {

    private UserService userService;

    @Autowired
    public UserResource(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/register")
    public User register(@RequestBody User user) throws UserNotFoundException, UsernameExistException {
        User newUser = userService.register(user.getFirstName() , user.getLastName() , user.getUsername() , user.getEmail());
        return new ResponseEntity<>(newUser , HttpStatus.OK).getBody();
    }
}
