package com.ews.parkswift.config;

/**
 * Application constants.
 */
public final class Constants {

    private Constants() {
    }

    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
    public static final String SPRING_PROFILE_PRODUCTION = "prod";
    public static final String SPRING_PROFILE_FAST = "fast";
    public static final String SPRING_PROFILE_CLOUD = "cloud";
    public static final String SYSTEM_ACCOUNT = "system";
    
    // - start for Constants used for PayPal service to get User Profile
    public static final String CLIENT_ID = "clientId";
    public static final String CLIENT_SECRET = "clientSecret";
    public static final String ENDPOINT = "endPoint";
    
    public static final String USER_EMAIL = "userEmail";
    public static final String USER_FAMILY_NAME = "userFamilyName";
    public static final String USER_GIVEN_NAME = "userFivenName";
    public static final String USER_GENDER = "userGender";
    public static final String ACCESSTOKEN = "accessToken";
    // -end for Constants used for PayPal service to get User Profile
    
    

}
