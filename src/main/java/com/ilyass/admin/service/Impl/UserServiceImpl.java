package com.ilyass.admin.service.Impl;

import com.ilyass.admin.domain.User;
import com.ilyass.admin.domain.UserPrincipal;
import com.ilyass.admin.repository.UserRepository;
import com.ilyass.admin.service.UserService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
@Qualifier("userDetailsService")
public class UserServiceImpl implements UserService , UserDetailsService {

    private UserRepository userRepository;
    private Logger LOGGER = LoggerFactory.getLogger(getClass());

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
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
    public User register(String firstname, String lastname, String username, String email) {
        validateNewUsernameAndEmail();
        return null;
    }

    private User validateNewUsernameAndEmail(String currentUsername , String newUsername , String newEmail) {
        if(StringUtils.isNotEmpty(currentUsername)){
            User currentUser = findUserByUsername(currentUsername);
            if(currentUser == null){
                throw new UsernameNotFoundException("No user found by username" + currentUsername);
            }
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
