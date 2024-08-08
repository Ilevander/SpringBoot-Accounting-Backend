package com.ilyass.admin.resource;

import com.ilyass.admin.domain.User;
import com.ilyass.admin.domain.UserPrincipal;
import com.ilyass.admin.exception.ExceptionHandling;
import com.ilyass.admin.exception.domain.EmailExistException;
import com.ilyass.admin.exception.domain.UserNotFoundException;
import com.ilyass.admin.exception.domain.UsernameExistException;
import com.ilyass.admin.service.UserService;
import com.ilyass.admin.utility.JWTTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;

import java.io.IOException;

import static com.ilyass.admin.constant.SecurityConstant.JWT_TOKEN_HEADER;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path={"/","/user"})
public class UserResource extends ExceptionHandling {

    private UserService userService;
    private AuthenticationManager authenticationManager;
    private JWTTokenProvider jwtTokenProvider;

    @Autowired
    public UserResource(UserService userService, AuthenticationManager authenticationManager, JWTTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) throws UserNotFoundException, UsernameExistException, EmailExistException, MessagingException {
        User newUser = userService.register(user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail());
        return new ResponseEntity<>(newUser, OK);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user){
              authenticate(user.getUsername() , user.getPassword());
              User loginUser = userService.findUserByUsername(user.getUsername());
              UserPrincipal userPrincipal = new UserPrincipal(loginUser);
              HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
              return new ResponseEntity<>(loginUser , jwtHeader, OK);

    }

    @PostMapping("/add")
    public ResponseEntity<User> addnNewUser(@RequestParam("firstName") String firstName,
                                            @RequestParam("lastName") String lastName,
                                            @RequestParam("username") String username,
                                            @RequestParam("email") String email,
                                            @RequestParam("role") String role,
                                            @RequestParam("isActive") String isActive,
                                            @RequestParam("isNonLocked") String isNonLocked,
                                            @RequestParam(value = "profileImage" , required = false) MultipartFile profileImage) throws IOException, UsernameExistException {
        User newUser = userService.addNewUser(firstName , lastName , username , email , role
                , Boolean.parseBoolean(isNonLocked) , Boolean.parseBoolean(isActive) , profileImage);
        return new ResponseEntity<>(newUser , OK);
    }

    private HttpHeaders getJwtHeader(UserPrincipal user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER , jwtTokenProvider.generateJwtToken(user));
        return headers;
    }

    private void authenticate(String username , String password){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
}
