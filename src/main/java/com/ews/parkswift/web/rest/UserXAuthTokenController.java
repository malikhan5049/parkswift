package com.ews.parkswift.web.rest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.json.JSONException;
import org.json.JSONObject;
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

import com.codahale.metrics.annotation.Timed;
import com.ews.parkswift.domain.Authority;
import com.ews.parkswift.domain.User;
import com.ews.parkswift.repository.UserRepository;
import com.ews.parkswift.security.xauth.Token;
import com.ews.parkswift.security.xauth.TokenProvider;
import com.ews.parkswift.service.UserService;
import com.ews.parkswift.web.rest.dto.UserDTO;
import com.ews.parkswift.web.rest.dto.UserDTOMobile;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfoplus;
//import com.google.api.client.json.jackson2.JacksonFactory;

@RestController
@RequestMapping("/api")
public class UserXAuthTokenController {

	
    @Inject
    private TokenProvider tokenProvider;

    @Inject
    private UserRepository userRepository;
    
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
    	
    	boolean insertUserActivated = false;
		FBGraph fbGraph = new FBGraph(token);
		String graph = fbGraph.getFBGraph();
		@SuppressWarnings("unchecked")
		Map<String, String> fbProfileData = fbGraph.getGraphData(graph);
		if(userRepository.findOneByEmail(fbProfileData.get("email")) == null)
		{
			User user = userService.createUserInformationfromSocialMediaProfile(fbProfileData.get("email"), fbProfileData.get("first_name"),
			fbProfileData.get("last_name"), fbProfileData.get("email").toLowerCase(), "en", insertUserActivated);
		}
		return fbProfileData;
        }      
    
    // CLIENT_ID = "872550699005-o80it1noveuijc5ar4q9vs23qdldjf2o.apps.googleusercontent.com";
    // CLIENT_SECRET = "MenVMqSS5-OkbgpYv13umPXZ";
    //  API key =    AIzaSyDRBUmklUXhmtzcqg_LLB1nboyiIvJtcnQ
    //@SuppressWarnings("unchecked")
	@RequestMapping(value = "/authenticategoogleplus",
            method = RequestMethod.POST)
    @Timed
    public  Map<String, String> authorizewithGooglePlus(@RequestParam String token) throws MalformedURLException, JSONException, IOException {
    	
    	boolean insertUserActivated = false;
    	GoogleCredential credential = new GoogleCredential().setAccessToken(token);   
    	 Oauth2 oauth2 = new Oauth2.Builder(new NetHttpTransport(), new JacksonFactory(), credential).setApplicationName(
    	          "Oauth2").build();
    	 Userinfoplus userinfo = oauth2.userinfo().get().execute();
    	 @SuppressWarnings("rawtypes")
		Map<String, String> googleplusProfile = new HashMap<String, String>();
 		try {
 			JSONObject json = new JSONObject(userinfo);
 			googleplusProfile.put("id", json.getString("id"));
 			googleplusProfile.put("first_name", json.getString("name"));
 			if (json.has("email"))
 				googleplusProfile.put("email", json.getString("email"));
 			if (json.has("gender"))
 				googleplusProfile.put("gender", json.getString("gender"));
 		} catch (JSONException e) {
 			e.printStackTrace();
 			throw new RuntimeException("ERROR in parsing google plus graph data. " + e);
 		}
 		if(userRepository.findOneByEmail(googleplusProfile.get("email")) == null)
		{
			User user = userService.createUserInformationfromSocialMediaProfile(googleplusProfile.get("email"), googleplusProfile.get("first_name"),
					googleplusProfile.get("last_name"), googleplusProfile.get("email"), "en", insertUserActivated);
		}
 		return googleplusProfile;
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
        		user.getFirstName(), user.getLastName(), user.getEmail(), user.getMobilePhone(), user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toCollection(LinkedList::new)))
        , HttpStatus.OK);
    }
    
}
