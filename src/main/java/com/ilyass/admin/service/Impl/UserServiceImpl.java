package com.ilyass.admin.service.Impl;

import com.ilyass.admin.domain.User;
import com.ilyass.admin.domain.UserPrincipal;
import com.ilyass.admin.enumeration.Role;
import com.ilyass.admin.exception.domain.UsernameExistException;
import com.ilyass.admin.repository.UserRepository;
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
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static com.ilyass.admin.enumeration.Role.ROLE_USER;

@Service
@Transactional
@Qualifier("userDetailsService")
public class UserServiceImpl implements UserService , UserDetailsService {

    private UserRepository userRepository;
    private Logger LOGGER = LoggerFactory.getLogger(getClass());
    private BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            LOGGER.error("User not found by username"+ username);
            throw new UsernameNotFoundException("User not found by username" + username);
        }
         else{
             user.setLastLoginDate(user.getLastLoginDate());
             user.setLastLoginDate(new Date());
             userRepository.save(user);
            UserPrincipal userPrincipal = new UserPrincipal(user);
            return userPrincipal;
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
        return null;
    }

    private String getTemporaryProfileImageUrl() {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/image/profile/temp").toUriString();
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
        if(StringUtils.isNotEmpty(currentUsername)){
            User currentUser = findUserByUsername(currentUsername);
            if(currentUser == null){
                throw new UsernameNotFoundException("No user found by username" + currentUsername);
            }
            User userByUsername = findUserByUsername(newUsername);
            if(userByUsername != null && !currentUser.getId().equals(userByUsername.getId())){
                 throw new UsernameExistException("Username already exists");
            }
            User userByEmail = findUserByEmail(newEmail);
            if(userByEmail != null && !currentUser.getId().equals(userByEmail.getId())){
                throw new UsernameNotFoundException("Email already exists");
            }
            return currentUser;
        }
         else{
             User userByUsername = findUserByUsername(newUsername);
             if (userByUsername != null){
                 throw new UsernameExistException("Username already exists");
             }
            User userByEmail = findUserByEmail(newEmail);
            if (userByEmail != null){
                throw new UsernameExistException("Email already exists");
            }
             return null;
        }
    }

    @Override
    public List<User> getUsers() {
        return List.of();
    }

    @Override
    public User findUserByUsername(String username) {
        return null;
    }

    @Override
    public User findUserByEmail(String email) {
        return null;
    }
}
