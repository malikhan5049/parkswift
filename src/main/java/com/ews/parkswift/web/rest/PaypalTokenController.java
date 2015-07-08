package com.ews.parkswift.web.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import sun.misc.*;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.ews.parkswift.config.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.api.openidconnect.Tokeninfo;
import com.paypal.api.openidconnect.Userinfo;
import com.paypal.api.openidconnect.UserinfoParameters;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;

@RestController
@RequestMapping("/api")
public class PaypalTokenController {

	  @RequestMapping(value = "/paypalAccessToken",
	            method = RequestMethod.POST)
	    @Timed
	  private Map<String, Object> getPayPalUserInfo(String authorizationCode) {
		  StringBuilder tokenUrl = new StringBuilder(
		  "https://api.paypal.com/v1/identity/openidconnect/tokenservice");
		  tokenUrl.append("?grant_type=authorization_code");
		  // code should be obtained manually and pasted here.
		  tokenUrl.append("&code=" + authorizationCode);
		  Map<String, Object> responseMap = getResponse(tokenUrl.toString(), "POST", "Basic " + getAuthorizationHeader());
		  //return (String) responseMap.get("access_token");
		  String accessToken = (String) responseMap.get("access_token");
		  StringBuilder userInfoUrl = new StringBuilder(
					"https://www.paypal.com/webapps/auth/protocol/openidconnect/v1/userinfo");
			userInfoUrl.append("?schema=openid");
			return getResponse(userInfoUrl.toString(), "GET",
					("Bearer " + accessToken));
		 }
	  private static Map<String, Object> getResponse(String urlStr,
				String method, String authHeader) {
			Map<String, Object> responseMap = null;
			BufferedReader br = null;
			HttpURLConnection conn = null;
			try {
				URL url = new URL(urlStr);
				conn = (HttpURLConnection) url.openConnection();

				// Set Timeouts Appropriately as per the App needs
				conn.setConnectTimeout(60000);
				conn.setReadTimeout(60000);

				conn.setRequestMethod(method);

				if (authHeader != null) {
					conn.setRequestProperty("Authorization", authHeader);
				}
				conn.setRequestProperty("Accept", "application/json");

				if (conn.getResponseCode() != 200) {
					throw new RuntimeException("Failed : HTTP error code : "
							+ conn.getResponseCode());
				}

				br = new BufferedReader(new InputStreamReader(
						(conn.getInputStream())));

				String output;
				StringBuilder builder = new StringBuilder();
				while ((output = br.readLine()) != null) {
					builder.append(output);
				}
				// Jackson object mapper to unmarshall json response. More info at:
				// http://wiki.fasterxml.com/JacksonInFiveMinutes
				ObjectMapper mapper = new ObjectMapper();
				responseMap = mapper.readValue(builder.toString(), Map.class);

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (conn != null) {
					conn.disconnect();
				}

				br = null;
			}
			return responseMap;
		}

	  private static String getAuthorizationHeader() {
			// client id and secret has been left empty for user to fill in.
			String clientId = "AVABqtTXMd3JWa219nb0NTFrmwpSNn2SdGHUPMHZGRcykjSAga_-q35rZs3t_4ueY0z8d0fnw-B8Wox7";
			String clientSecret = "EBcCief4MzwNZnM0mWp-mRMXiH6qIQsE_EbNJ5CSE8xbxBLh8TtBwfHnA9laQ_qiUPKpIHV9NJmyL7GB";

			String authString = clientId + ":" + clientSecret;
			BASE64Encoder encoder = new BASE64Encoder();
			String header = encoder.encode(authString.getBytes());
			header = header.replace("\n", "");
			header = header.replace("\r", "");
			return header;
		}

	/*  @RequestMapping(value = "/paypalAccessToken",
	            method = RequestMethod.POST)
	    @Timed
	    public String paypal(@RequestParam String accessToken ) throws PayPalRESTException {
		 
	    Map<String, String> configurationMap = new HashMap<String, String>();
	    configurationMap.put(Constants.CLIENT_ID, "...");
	    configurationMap.put(Constants.CLIENT_SECRET, "...");
	    configurationMap.put(Constants.ENDPOINT, "https://api.paypal.com/");
	    APIContext apiContext = new APIContext();
	    apiContext.setConfigurationMap(configurationMap);
	    
	    Tokeninfo info = new Tokeninfo();
	    info.setRefreshToken("refreshToken");
	    UserinfoParameters param = new UserinfoParameters();
	    param.setAccessToken(info.getAccessToken());
	    Userinfo userInfo = Userinfo.getUserinfo(apiContext);//.userinfo(apiContext, param);
	    userInfo.getEmail();
	    userInfo.getFamilyName();
	    userInfo.getGivenName();
	    userInfo.getGender();
	    return accessToken;
	  }*/
}
