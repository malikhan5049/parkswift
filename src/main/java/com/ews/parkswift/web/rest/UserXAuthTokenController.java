package com.ews.parkswift.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ews.parkswift.domain.Authority;
import com.ews.parkswift.domain.User;
import com.ews.parkswift.repository.UserRepository;
import com.ews.parkswift.security.xauth.Token;
import com.ews.parkswift.security.xauth.TokenProvider;
import com.ews.parkswift.service.UserService;
import com.ews.parkswift.web.rest.dto.UserDTO;
import com.ews.parkswift.web.rest.dto.UserDTOMobile;
import com.jayway.jsonpath.internal.JsonReader;
import com.restfb.json.JsonObject;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.GoogleApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.gatling.core.json.JSON;
import io.gatling.recorder.util.Json;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.json.JSONException;
import org.json.JSONObject;

@RestController
@RequestMapping("/api")
public class UserXAuthTokenController {

	// start for login with facebook
	public static final String FB_APP_ID = "502018279946405";
	public static final String FB_APP_SECRET = "b2ad575154ef4bde345c2f23d3b1cde5";
	public static final String REDIRECT_URI = "http://localhost:8080/Facebook_Login/fbhome";
	static String accessToken = "";
	// end for login with facebook
    @Inject
    private TokenProvider tokenProvider;

    @Inject
    private AuthenticationManager authenticationManager;

    @Inject
    private UserDetailsService userDetailsService;
    
    @Inject
    private UserService userService;

    @RequestMapping(value = "/authenticate",
            method = RequestMethod.POST)
    @Timed
    public Token authorize(@RequestParam String username, @RequestParam String password) {

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = this.authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails details = this.userDetailsService.loadUserByUsername(username);
        return tokenProvider.createToken(details);
    }
    
    
    // appId = "502018279946405";
    // redirectUrl = "http://localhost:8080/index.sec";
    // faceAppSecret = "b2ad575154ef4bde345c2f23d3b1cde5";
    @RequestMapping(value = "/authenticatefb",
            method = RequestMethod.POST)
    @Timed
    public Map<String, String> authorizewithfb(@RequestParam String token) throws MalformedURLException, JSONException, IOException {
    	
		FBGraph fbGraph = new FBGraph(token);
		String graph = fbGraph.getFBGraph();
		@SuppressWarnings("unchecked")
		Map<String, String> fbProfileData = fbGraph.getGraphData(graph);
		return fbProfileData;
        }      
    
    // CLIENT_ID = "872550699005-o80it1noveuijc5ar4q9vs23qdldjf2o.apps.googleusercontent.com";
    // CLIENT_SECRET = "MenVMqSS5-OkbgpYv13umPXZ";
    //  API key =    AIzaSyDRBUmklUXhmtzcqg_LLB1nboyiIvJtcnQ
    @RequestMapping(value = "/authenticategoogleplus",
            method = RequestMethod.POST)
    @Timed
    public Map<String, String> authorizewithGooglePlus(@RequestParam String token) throws MalformedURLException, JSONException, IOException {
    	
    	OAuthService service = new ServiceBuilder().provider(GoogleApi.class).apiKey("AIzaSyDRBUmklUXhmtzcqg_LLB1nboyiIvJtcnQ")
        .apiSecret("MenVMqSS5-OkbgpYv13umPXZ").build();
    	
    	org.scribe.model.Token requestToken = service.getRequestToken();
    	//String authUrl = service.getAuthorizationUrl(requestToken);
    	//String authUrl = service.getAccessToken(requestToken);
    	Verifier v = new Verifier("verifier you got from the user");
    	org.scribe.model.Token accessToken = service.getAccessToken(requestToken, v); // the requestToken you had from step 2
    	
    	OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.twitter.com/1/account/verify_credentials.xml");
    	service.signRequest(accessToken, request); // the access token from step 4
    	Response response = request.send();
    	System.out.println(response.getBody());
		return null;
        }      
   
   
    @RequestMapping(value = "/authenticatemobile",
            method = RequestMethod.POST)
    @Timed
    public ResponseEntity<UserDTOMobile> authorizeMobile(@Valid @RequestBody UserDTO userDTO, HttpServletResponse response, Device device) {
    	
    	if(device.isNormal())
    		return null;
    	
    	String username = userDTO.getEmail();
    	String password = userDTO.getPassword();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = this.authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails details = this.userDetailsService.loadUserByUsername(username);
        
        Token tokenMobile = tokenProvider.createTokenMobile(details);
        response.addHeader("x-auth-token", tokenMobile.getToken());
        
        User user = userService.getUserWithAuthorities();
        
        return new ResponseEntity<UserDTOMobile>(new UserDTOMobile(
        		user.getFirstName(), user.getLastName(), user.getEmail(),user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toCollection(LinkedList::new)))
        , HttpStatus.OK);
    }
    
    @RequestMapping(value = "/authenticatemobilefb",
            method = RequestMethod.POST)
    @Timed
    public ResponseEntity<UserDTOMobile> authorizeMobilefb(@Valid @RequestBody UserDTO userDTO, HttpServletResponse response, Device device) {
    	
    	if(device.isNormal())
    		return null;
    	
    	String username = userDTO.getEmail();
    	String password = userDTO.getPassword();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = this.authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails details = this.userDetailsService.loadUserByUsername(username);
        
        Token tokenMobile = tokenProvider.createTokenMobile(details);
        response.addHeader("x-auth-token", tokenMobile.getToken());
        
        User user = userService.getUserWithAuthorities();
        
        return new ResponseEntity<UserDTOMobile>(new UserDTOMobile(
        		user.getFirstName(), user.getLastName(), user.getEmail(),user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toCollection(LinkedList::new)))
        , HttpStatus.OK);
    }
}
