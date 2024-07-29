package com.ilyass.admin.resource;

import com.ilyass.admin.domain.User;
import com.ilyass.admin.exception.ExceptionHandling;
import com.ilyass.admin.exception.domain.EmailExistException;
import com.ilyass.admin.exception.domain.UserNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path={"/","/user"})
public class UserResource extends ExceptionHandling {

    @GetMapping("/home")
    public String showUser() throws UserNotFoundException {
        //return "application works...!";
        throw new UserNotFoundException("User w was nt found !!");
    }
}
