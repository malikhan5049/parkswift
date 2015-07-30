package com.ews.parkswift.config;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Application constants.
 */
public final class Constants {

    private Constants() {
    }
    private static final Logger log = LoggerFactory.getLogger(Constants.class);
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
    public static final String LOCATION_IMAGES_FOLDER_NAME = "locationimages";
	public static final String LOCATION_IMAGES_PARENT_FOLDER_NAME = "assets";
	public static final String BUSINESS_TYPE_RESIDENTIAL = "Residential";
	public static final Integer BUSINESS_TYPE_RESIDENTIAL_MAX_SPACE_LIMIT = 3;
	public static final String REPEAT_END_BASIS_AFTER = "After";
	public static final String REPEAT_END_BASIS_ON = "On";
	public static final String REPEAT_BASIS_WEEKLY = "Weekly";
	public static final String REPEAT_BASIS_MONTHLY = "Monthly";
	public static final Double DEFAULT_SEARCH_DISTANCE = 25d;
	public enum SearchDistanceUnit{MILE,KILOMETER}
    public static String LOCATION_IMAGES_PARENT_FOLDER_PATH;
    public static String LOCATION_IMAGES_FOLDER_URL;
	public static String LOCATION_IMAGES_FOLDER_PATH;
	public static final DateTimeFormatter DATETIMEFORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
	public static final DateTimeFormatter LOCALDATEFORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd");
	public static final DateTimeFormatter LOCALTIMEFORMATTER = DateTimeFormat.forPattern("hh:mm a");
    
    // -end for Constants used for PayPal service to get User Profile
    
    

}
