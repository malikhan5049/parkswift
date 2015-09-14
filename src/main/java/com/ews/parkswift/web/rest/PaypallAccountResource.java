package com.ews.parkswift.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.ews.parkswift.domain.PaypallAccount;
import com.ews.parkswift.repository.PaypallAccountRepository;
import com.ews.parkswift.service.util.paypal.PaypalUtils;
import com.ews.parkswift.web.rest.dto.PaypallAccountDTO;
import com.ews.parkswift.web.rest.dto.parking.PaymentDTO;

/**
 * REST controller for managing PaypallAccount.
 */
@RestController
@RequestMapping("/api")
public class PaypallAccountResource {

    private final Logger log = LoggerFactory.getLogger(PaypallAccountResource.class);

    @Inject
    private PaypallAccountRepository paypallAccountRepository;

    /**
     * POST  /paypallAccounts -> Create a new paypallAccount.
     */
    @RequestMapping(value = "/paypallAccounts",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody PaypallAccount paypallAccount) throws URISyntaxException {
    	log.debug("REST request to save PaypallAccount : {}", paypallAccount);
        if (paypallAccount.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new paypallAccount cannot already have an ID").build();
        }
        paypallAccountRepository.save(paypallAccount);
        return ResponseEntity.created(new URI("/api/paypallAccounts/" + paypallAccount.getId())).build();
    }

    /**
     * PUT  /paypallAccounts -> Updates an existing paypallAccount.
     */
    @RequestMapping(value = "/paypallAccounts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody PaypallAccount paypallAccount) throws URISyntaxException {
        log.debug("REST request to update PaypallAccount : {}", paypallAccount);
        if (paypallAccount.getId() == null) {
            return create(paypallAccount);
        }
        paypallAccountRepository.save(paypallAccount);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /paypallAccounts -> get all the paypallAccounts.
     */
    @RequestMapping(value = "/paypallAccounts",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PaypallAccount> getAll() {
        log.debug("REST request to get all PaypallAccounts");
        return paypallAccountRepository.findAllForCurrentUser();
    }

    /**
     * GET  /paypallAccounts/:id -> get the "id" paypallAccount.
     */
    @RequestMapping(value = "/paypallAccounts/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PaypallAccount> get(@PathVariable Long id) {
        log.debug("REST request to get PaypallAccount : {}", id);
        return Optional.ofNullable(paypallAccountRepository.findOne(id))
            .map(paypallAccount -> new ResponseEntity<>(
                paypallAccount,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /paypallAccounts/:id -> delete the "id" paypallAccount.
     */
    @RequestMapping(value = "/paypallAccounts/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete PaypallAccount : {}", id);
        paypallAccountRepository.delete(id);
    }
    
    
    @RequestMapping(value = "/verifyPaypalAccount",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public PaypallAccountDTO get(@Valid @RequestBody PaypallAccountDTO accountDTO) {
	    return PaypalUtils.getPaypalUserDetails(accountDTO.getAuthToken());
	}
    
    
    @RequestMapping(value = "/executePayment",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public PaymentDTO executePayment(@Valid @RequestBody PaymentDTO paymentDTO) {
//					PaypalUtils.getResponse(accountDTO.getAuthorization_id(), accountDTO.getPayKey(), accountDTO.getAmount());
//				try {
//					PaypalUtils.captureAuthorizedPayment(accountDTO.getAuthorization_id(),accountDTO.getAmount());
//					try {
//						PaypalUtils.getResponseAgainstAuthorizationKey(accountDTO.getAuthorization_id(), accountDTO.getPayKey(), accountDTO.getAmount());
//					} catch (MalformedURLException | JSONException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				} catch (PayPalException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				PaypalUtils.captureAuthorizedPayment(accountDTO.getAuthorization_id(), accountDTO.getAmount());
			
    	paymentDTO.setPaypalURLWithPayKey(PaypalUtils.generatePayment(paymentDTO));
    	return paymentDTO;
    }
    
    @RequestMapping(value = "/paymentResponse",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void executePayment(@Valid @RequestBody Object paymentDTO) {
//    	paymentDTO.setPaypalURLWithPayKey(PaypalUtils.generatePayment(paymentDTO));
//    	return paymentDTO;
    	System.out.println(paymentDTO);
    }
    
}
