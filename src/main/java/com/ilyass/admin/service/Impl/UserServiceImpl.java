package com.ilyass.admin.service.Impl;

import com.ilyass.admin.domain.User;
import com.ilyass.admin.domain.UserPrincipal;
import com.ilyass.admin.exception.domain.UsernameExistException;
import com.ilyass.admin.repository.UserRepository;
import com.ilyass.admin.service.LoginAttemptService;
import com.ilyass.admin.service.UserService;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static com.ilyass.admin.constant.UserImplConstant.*;
import static com.ilyass.admin.enumeration.Role.ROLE_USER;

@Service
@Transactional
@Qualifier("userDetailsService")
public class UserServiceImpl implements UserService , UserDetailsService {

    private UserRepository userRepository;
    private Logger LOGGER = LoggerFactory.getLogger(getClass());
    private BCryptPasswordEncoder passwordEncoder;
    private LoginAttemptService loginAttemptService;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, LoginAttemptService loginAttemptService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            LOGGER.error(NO_USER_FOUND_BY_USERNAME+ username);
            throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + username);
        }
         else{
             validateLoginAttempt(user);
             user.setLastLoginDate(user.getLastLoginDate());
             user.setLastLoginDate(new Date());
             userRepository.save(user);
            UserPrincipal userPrincipal = new UserPrincipal(user);
            LOGGER.info(FOUND_USER_BY_USERNAME + username);
            return userPrincipal;
        }
    }

    private void validateLoginAttempt(User user) {
        if(user.isNotLocked()){
            if(loginAttemptService.hasExceededMaxAttempts(user.getUsername())){
                user.setNotLocked(false);
            }
             else{
                 user.setNotLocked(true);
            }
        }
         else{
             loginAttemptService.evictUserFromLoginAtemptCache(user.getUsername());
        }
    }

    @Override
    public User register(String firstname, String lastname, String username, String email) throws UsernameExistException {
        validateNewUsernameAndEmail(StringUtils.EMPTY , username , email);
        User user = new User();
        user.setUserId(generateUserId());
        String password = generatePssword();
        String encodedPassword = encodedPassword(password);
        user.setFirstName(firstname);
        user.setLastName(lastname);
        user.setUsername(username);
        user.setEmail(email);
        user.setJoinDate(new Date());
        user.setPassword(encodedPassword);
        user.setActive(true);
        user.setNotLocked(true);
        user.setRole(ROLE_USER.name());
        user.setAuthorities(ROLE_USER.getAuthorities());
        user.setProfileImageUrl(getTemporaryProfileImageUrl());
        userRepository.save(user);
        LOGGER.info("New user password: " + password);
        return user;
    }

    private String getTemporaryProfileImageUrl() {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(DEFAULT_USER_IMAGE_PATH).toUriString();
    }

    private String encodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    private String generatePssword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    private String generateUserId() {
        return RandomStringUtils.randomNumeric(10);
    }

    private User validateNewUsernameAndEmail(String currentUsername , String newUsername , String newEmail) throws UsernameExistException {
        User userByNewUsername = findUserByUsername(newUsername);
        User userByNewEmail = findUserByEmail(newEmail);
        if(StringUtils.isNotEmpty(currentUsername)){
            User currentUser = findUserByUsername(currentUsername);
            if(currentUser == null){
                throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + currentUsername);
            }

            if(userByNewUsername != null && !currentUser.getId().equals(userByNewUsername.getId())){
                 throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
            }

            if(userByNewEmail != null && !currentUser.getId().equals(userByNewEmail.getId())){
                throw new UsernameNotFoundException(EMAIL_ALREADY_EXISTS);
            }
            return currentUser;
        }
         else{
             if (userByNewUsername != null){
                 throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
             }
            if (userByNewEmail != null){
                throw new UsernameExistException(EMAIL_ALREADY_EXISTS);
            }
             return null;
        }
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
