package com.ilyass.admin.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/*
This service can be used to lock user accounts after a certain number of failed login attempts, thus improving the security of the application.
 */
@Service
public class LoginAttemptService {

    //Number of failed attempt to try to Login
    private static final int MAXIMUM_NUMBER_OF_LOGIN_ATTEMPTS = 5;
    private static final int ATTEMPT_INCREMENT = 1;
    private LoadingCache<String, Integer> loginAttemptCache;

    //Create the Memory cache to represent the users and number of attempts that they-ve try to login , as a block-notes or journal
    // .maximumSize(100) , is the buffer of the memory
    public LoginAttemptService(){
        super();
        loginAttemptCache = CacheBuilder.newBuilder().expireAfterWrite(15, TimeUnit.MINUTES)
                .maximumSize(100).build(new CacheLoader<String, Integer>() {
                    @Override
                    public Integer load(String key){
                        return 0;
                    }
                });
    }

    //Removes a user from the cache, resetting their failed login attempts.
    public void evictUserFromLoginAtemptCache(String username){
        loginAttemptCache.invalidate(username);
    }

    //Adds a failed login attempt for a user. Gets the current number of attempts and increments it.
    public void addUserToLoginAttemptCache(String username)  {
        int attempts = 0;
        try {
            attempts = ATTEMPT_INCREMENT + loginAttemptCache.get(username);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        loginAttemptCache.put(username , attempts);
    }

    //Checks if a user has exceeded the maximum number of failed login attempts allowed.
    public boolean hasExceededMaxAttempts(String username)  {
        try {
            return loginAttemptCache.get(username) >= MAXIMUM_NUMBER_OF_LOGIN_ATTEMPTS;
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

}

