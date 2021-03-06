package com.ews.parkswift.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ews.parkswift.domain.PaypallAccount;
import com.ews.parkswift.repository.PaypallAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

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
}
