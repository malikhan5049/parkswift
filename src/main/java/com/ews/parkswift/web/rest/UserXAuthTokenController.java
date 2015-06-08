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

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UserXAuthTokenController {

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
}
