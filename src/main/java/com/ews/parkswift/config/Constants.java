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
	public static final DateTimeFormatter LOCALDATETIMEFORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm a");
    
	public static final String PAYPALL_CLIENT_ID = "AX6jOoqINjUAUHhslHZcjM0_3TlMIrPBF3sqVzLrDAeQV4B5fygos2429qYK0pEjUs_xuZJpZMxIheD4";
	public static final String PAYPALL_CLIENT_SECRET = "ED7r_WCA_jlY7T7HQcJF7DjHfB6GgAZUBnb9Uku9UUMKN8LGi4D-bersMAIdGizFohXcURomIpFbEXqb";
	
//	public static final String PAYPALL_CLIENT_ID = "AVABqtTXMd3JWa219nb0NTFrmwpSNn2SdGHUPMHZGRcykjSAga_-q35rZs3t_4ueY0z8d0fnw-B8Wox7";
//	public static final String PAYPALL_CLIENT_SECRET = "EBcCief4MzwNZnM0mWp-mRMXiH6qIQsE_EbNJ5CSE8xbxBLh8TtBwfHnA9laQ_qiUPKpIHV9NJmyL7GB";
	public static final String PAYPALL_REDIRECT_URI = "http://www.parkswift.com/";
	
    // -end for Constants used for PayPal service to get User Profile
	
	public static final String BOOKING_REFERENCE_NUMBER_RANDOM = "38400000-8cf0-11bd-b23e-10b96e4ef00d";
	public static final String PAYMENT_REFERENCE_NUMBER_RANDOM = "38400000-8cf0-11bd-b23e-10b96e4ef00d";
	public static final String MODE = "mode";
	public static final String MODE_SANDBOX = "sandbox";
	public static final String EMAIL = "email";
	public static final String OPENID = "openid";
	
	public static final String CURRENCY_USD = "USD";
	public static final String LOCALE_US = "en_US";
	public static final String PAYPALL_ACTION_TYPE_PAY = "PAY";
	public static final String PAYPALL_ACTION_TYPE_CREATE = "CREATE";
	public static final String PAYPALL_PREAPPROVAL_CANCEL_URL = "https://devtools-paypal.com/guide/ap_preapprove_payment?cancel=true";
	public static final String PAYPALL_PREAPPROVAL_RETURN_URL = "https://devtools-paypal.com/guide/ap_preapprove_payment?success=true";
	
	public static final String PAYPALL_CHAINED_CANCEL_URL = "https://devtools-paypal.com/guide/ap_chained_payment/php?cancel=true";
	public static final String PAYPALL_CHAINED_RETURN_URL = "https://devtools-paypal.com/guide/ap_chained_payment/php?success=true";
	
	public static final String PAYPALL_PAYMENT_APPROVAL_URL = "https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_ap-payment&paykey=";
	
	public static final String PAYPALL_IPN_NOTIFICATION_URL = "http://replaceIpnUrl.com";
	
//	public static final String PAYPALL_USER_NAME = "saad.sial77_api1.Yahoo.com";
//	public static final String PAYPALL_PASSWORD = "QJ4XSC82ZPVLU54E";
//	public static final String PAYPALL_SIGNATURE = "AFcWxV21C7fd0v3bYYYRCpSSRl31AEv2GIvRBVF2OlipVtiYjZzUe-4k";
//	public static final String PAYPALL_APP_ID = "APP-80W284485P519543T";
	
	
	public static final String PAYPALL_USER_NAME = "saad.sial77_api1.Yahoo.com";
	public static final String PAYPALL_PASSWORD = "KQFMMLFW3UFS52ZE";
	public static final String PAYPALL_SIGNATURE = "ADyVKgzotwVjMNudZaW4Uw4OeWpxAF2uHg38efzaTgTiCKUeWwT2e6Qo";
	public static final String PAYPALL_APP_ID = "APP-80W284485P519543T";
	
	public static final String STATUS_SUCCESS= "Success";
	public static final String PAYPAL_PAYMENT_RESPONSE_URL ="http://10.10.10.118:8080/api/paymentResponse"; 
	public static final String PAYPAL_CONFIG_PROPERTIES_FILE = "src/main/resources/sdk_config.properties";
}
