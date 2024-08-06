package com.ilyass.admin.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

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

}
