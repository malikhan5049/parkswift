package com.ews.parkswift.web.rest;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.ews.parkswift.domain.Authority;
import com.ews.parkswift.domain.PaypallAccount;
import com.ews.parkswift.domain.User;
import com.ews.parkswift.repository.PaypallAccountRepository;
import com.ews.parkswift.repository.UserRepository;
import com.ews.parkswift.security.AuthoritiesConstants;
import com.ews.parkswift.security.SecurityUtils;
import com.ews.parkswift.security.xauth.Token;
import com.ews.parkswift.security.xauth.TokenProvider;
import com.ews.parkswift.service.MailService;
import com.ews.parkswift.service.UserService;
import com.ews.parkswift.web.rest.dto.UserDTO;
import com.ews.parkswift.web.rest.dto.UserDTOMobile;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountResource {

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserService userService;

    @Inject
    private MailService mailService;
    
    @Inject
    private TokenProvider tokenProvider;
    
    @Inject
    private UserDetailsService userDetailsService;
    
    @Inject
    private PaypallAccountRepository paypallAccountRepository;

    /**
     * POST  /register -> register the user.
     */
    @RequestMapping(value = "/register",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserDTOMobile> registerAccount(@Valid @RequestBody UserDTO userDTO, HttpServletRequest request, HttpServletResponse response) {
        
    	if (userRepository.findOneByLogin(userDTO.getLogin()).isPresent() || userRepository.findOneByEmail(userDTO.getEmail()).isPresent())
    		return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
    	else {
    		
    		boolean insertUserActivated = false;
        	if(DeviceUtils.isMobile(request))
        		insertUserActivated = true;
            User user = userService.createUserInformation(userDTO.getLogin(), userDTO.getPassword(),
            userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail().toLowerCase(),
            userDTO.getContactNumber(), userDTO.getLangKey(), insertUserActivated);
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();               

            if(!insertUserActivated)
            	mailService.sendActivationEmail(user, baseUrl);
            
            List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<SimpleGrantedAuthority>();
            grantedAuthorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.USER));
            UserDetails userDetails = new org.springframework.security.core.userdetails.User(userDTO.getLogin(), user.getPassword()!=null?user.getPassword():"", grantedAuthorities);
    		
    		Token tokenMobile = tokenProvider.createTokenMobile(userDetails);
            response.addHeader("x-auth-token", tokenMobile.getToken());
            
            if(userDTO.getPaypalEmail()!=null && !userDTO.getPaypalEmail().equals("")){
            	PaypallAccount paypallAccount = new PaypallAccount();
            	paypallAccount.setEmail(userDTO.getPaypalEmail());
            	paypallAccount.setIsDefault(true);
            	paypallAccount.setUser(user);
            	
            	paypallAccountRepository.save(paypallAccount);
            }
            
            return new ResponseEntity<UserDTOMobile>(new UserDTOMobile(
            		user.getFirstName(), user.getLastName(), user.getEmail(), user.getMobilePhone(), user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toCollection(LinkedList::new)))
            , HttpStatus.CREATED);
    	}

    }
    
    /**
     * POST  /registerfromSocialMedia -> register the user from Social Media.
     */
    @RequestMapping(value = "/registerSocialMedia",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> registerAccountfromSocialMedia(@Valid @RequestBody UserDTO userDTO, HttpServletRequest request) {
        return userRepository.findOneByLogin(userDTO.getLogin())
            .map(user -> new ResponseEntity<>("login already in use", HttpStatus.BAD_REQUEST))
            .orElseGet(() -> userRepository.findOneByEmail(userDTO.getEmail())
                .map(user -> new ResponseEntity<>("e-mail address already in use", HttpStatus.BAD_REQUEST))
                .orElseGet(() -> {
                	boolean insertUserActivated = false;
                	if(DeviceUtils.isMobile(request))
                		insertUserActivated = true;
                    User user = userService.createUserInformationfromSocialMediaProfile(userDTO.getLogin(), userDTO.getFirstName(),
                    		userDTO.getLastName(), userDTO.getEmail().toLowerCase(), userDTO.getLangKey(), insertUserActivated);
                    String baseUrl = request.getScheme() + // "http"
                    "://" +                                // "://"
                    request.getServerName() +              // "myhost"
                    ":" +                                  // ":"
                    request.getServerPort();               // "80"

                    if(!insertUserActivated)
                    	mailService.sendActivationEmail(user, baseUrl);
                    return new ResponseEntity<>(HttpStatus.CREATED);
                })
        );
    }
    
    @RequestMapping(value = "/loginWithSocialMedia",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserDTOMobile> loginWithSocialMediaAccount(@Valid @RequestBody UserDTO userDTO, HttpServletRequest request, HttpServletResponse response) {
        
    	if (userRepository.findOneByLogin(userDTO.getLogin()).isPresent()) {
    		
    		UserDetails details = this.userDetailsService.loadUserByUsername(userDTO.getLogin());
    		
    		Token tokenMobile = tokenProvider.createTokenMobile(details);
            response.addHeader("x-auth-token", tokenMobile.getToken());
            
//            User user = userRepository.findUserByLoginName();
            Optional<User> userOptional = userRepository.findOneByLogin(userDTO.getLogin());
            if (userOptional.isPresent()) {
            	User user = userOptional.get();
            	List<String> roles = new ArrayList<String>();
//            	if (user.getAuthorities().size() > 0) Throws Exception
//            		roles.addAll(user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toCollection(LinkedList::new)));
            	return new ResponseEntity<UserDTOMobile>(new UserDTOMobile(user.getFirstName(), user.getLastName(), user.getEmail(), user.getMobilePhone(), roles), HttpStatus.OK);
            }
    	} else if (userRepository.findOneByEmail(userDTO.getEmail()).isPresent()) {
    		
    		UserDetails details = this.userDetailsService.loadUserByUsername(userDTO.getEmail());
    		
    		Token tokenMobile = tokenProvider.createTokenMobile(details);
            response.addHeader("x-auth-token", tokenMobile.getToken());
            
            Optional<User> userOptional = userRepository.findOneByEmail(userDTO.getEmail());
            if (userOptional.isPresent()) {
            	User user = userOptional.get();
            	List<String> roles = new ArrayList<String>();
//            	if (user.getAuthorities().size() > 0) Throws Exception
//            		roles.addAll(user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toCollection(LinkedList::new)));
            	return new ResponseEntity<UserDTOMobile>(new UserDTOMobile(user.getFirstName(), user.getLastName(), user.getEmail(), user.getMobilePhone(), roles), HttpStatus.OK);
            }
    	} else {
    		
    		boolean insertUserActivated = false;
        	if(DeviceUtils.isMobile(request))
        		insertUserActivated = true;
            User user = userService.createUserInformationfromSocialMediaProfile(userDTO.getLogin(), userDTO.getFirstName(),
            		userDTO.getLastName(), userDTO.getEmail().toLowerCase(), userDTO.getLangKey(), insertUserActivated);
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();               

            if(!insertUserActivated)
            	mailService.sendActivationEmail(user, baseUrl);
            List<String> roles = new ArrayList<String>();
//        	if (user.getAuthorities().size() > 0) Throws Exception
//        		roles.addAll(user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toCollection(LinkedList::new)));
        	return new ResponseEntity<UserDTOMobile>(new UserDTOMobile(user.getFirstName(), user.getLastName(), user.getEmail(), user.getMobilePhone(), roles), HttpStatus.OK);
    	}
    	return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * GET  /activate -> activate the registered user.
     */
    @RequestMapping(value = "/activate",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<String> activateAccount(@RequestParam(value = "key") String key) {
        return userService.activateRegistration(key)
            .map(user -> new ResponseEntity<String>(HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * GET  /authenticate -> check if the user is authenticated, and return its login.
     */
    @RequestMapping(value = "/authenticate",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    /**
     * GET  /account -> get the current user.
     */
    @RequestMapping(value = "/account",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserDTO> getAccount() {
        return Optional.ofNullable(userService.getUserWithAuthorities())
            .map(user -> new ResponseEntity<>(
                new UserDTO(
                    user.getLogin(),
                    null,
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getLangKey(),
                    user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toCollection(LinkedList::new))),
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * POST  /account -> update the current user information.
     */
    @RequestMapping(value = "/account",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<String> saveAccount(@RequestBody UserDTO userDTO) {
        return userRepository
            .findOneByLogin(userDTO.getLogin())
            .filter(u -> u.getLogin().equals(SecurityUtils.getCurrentLogin()))
            .map(u -> {
                userService.updateUserInformation(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail(), userDTO.getLangKey());
                return new ResponseEntity<String>(HttpStatus.OK);
            })
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * POST  /change_password -> changes the current user's password
     */
    @RequestMapping(value = "/account/change_password",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> changePassword(@RequestBody String password) {
        if (StringUtils.isEmpty(password) || password.length() < 5 || password.length() > 50) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        userService.changePassword(password);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    /**
     * POST  /change_password -> changes the current user's password
     */
    @RequestMapping(value = "/account/changepassword",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> changePassword(@RequestBody UserDTO userDto) {
        if (StringUtils.isEmpty(userDto.getPassword()) || userDto.getPassword().length() < 5 || userDto.getPassword().length() > 50) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        userService.changePassword(userDto.getPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/account/reset_password/init",
        method = RequestMethod.POST,
        produces = MediaType.TEXT_PLAIN_VALUE)
    @Timed
    public ResponseEntity<?> requestPasswordReset(@RequestBody String mail, HttpServletRequest request) {
        
        return userService.requestPasswordReset(mail)
            .map(user -> {
                String baseUrl = request.getScheme() +
                    "://" +
                    request.getServerName() +
                    ":" +
                    request.getServerPort();
            mailService.sendPasswordResetMail(user, baseUrl);
            return new ResponseEntity<>("e-mail was sent", HttpStatus.OK);
            }).orElse(new ResponseEntity<>("e-mail address not registered", HttpStatus.BAD_REQUEST));
        
    }

    @RequestMapping(value = "/account/reset_password/finish",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<String> finishPasswordReset(@RequestParam(value = "key") String key, @RequestParam(value = "newPassword") String newPassword) {
        return userService.completePasswordReset(newPassword, key)
              .map(user -> new ResponseEntity<String>(HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
