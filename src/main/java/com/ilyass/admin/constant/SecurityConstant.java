package com.ilyass.admin.constant;

public class SecurityConstant {
    public static final long EXPIRATION_TIME = 432_000_000; //5 days expressed in milliseconds
    public static final String TOKEN_PREFIX = "Bearer"; //ownership of token without verifing , everyone can use the token to access to the application, since the token has bearer too
    public static final String JWT_TOKEN_HEADER = "Jwt-Token";
    public static final String TOKEN_CANNOT_BE_VERIFIED = "Token can not be verified";
    public static final String GET_ARRAYS_LLC = "Get Arrays, LLC";
    public static final String GET_ARRAYS_ADMINISTRATION = "User Management Portal";
    public static final String AUTHORITIES = "authorities";
    public static final String FORBIDDEN_MESSAGE = "You need to log in to access this page";
    public static final String ACCESS_DENIED_MESSAGE = "You do not have permission to access this page";
    public static final String OPTION_HTTP_METHOD = "OPTIONS";
    public static final String[] PUBLIC_URL = {"/user/login","/user/register","/user/resetpassword/**","/user/image/**"};
    //public static final String[] PUBLIC_URL = {"**"};
}
