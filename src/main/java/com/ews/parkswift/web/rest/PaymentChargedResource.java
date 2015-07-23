package com.ews.parkswift.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ews.parkswift.domain.PaymentCharged;
import com.ews.parkswift.repository.PaymentChargedRepository;
import com.ews.parkswift.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
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
 * REST controller for managing PaymentCharged.
 */
@RestController
@RequestMapping("/api")
public class PaymentChargedResource {

    private final Logger log = LoggerFactory.getLogger(PaymentChargedResource.class);

    @Inject
    private PaymentChargedRepository paymentChargedRepository;

    /**
     * POST  /paymentChargeds -> Create a new paymentCharged.
     */
    @RequestMapping(value = "/paymentChargeds",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody PaymentCharged paymentCharged) throws URISyntaxException {
        log.debug("REST request to save PaymentCharged : {}", paymentCharged);
        if (paymentCharged.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new paymentCharged cannot already have an ID").build();
        }
        paymentChargedRepository.save(paymentCharged);
        return ResponseEntity.created(new URI("/api/paymentChargeds/" + paymentCharged.getId())).build();
    }

    /**
     * PUT  /paymentChargeds -> Updates an existing paymentCharged.
     */
    @RequestMapping(value = "/paymentChargeds",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody PaymentCharged paymentCharged) throws URISyntaxException {
        log.debug("REST request to update PaymentCharged : {}", paymentCharged);
        if (paymentCharged.getId() == null) {
            return create(paymentCharged);
        }
        paymentChargedRepository.save(paymentCharged);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /paymentChargeds -> get all the paymentChargeds.
     */
    @RequestMapping(value = "/paymentChargeds",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PaymentCharged>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<PaymentCharged> page = paymentChargedRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/paymentChargeds", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /paymentChargeds/:id -> get the "id" paymentCharged.
     */
    @RequestMapping(value = "/paymentChargeds/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PaymentCharged> get(@PathVariable Long id) {
        log.debug("REST request to get PaymentCharged : {}", id);
        return Optional.ofNullable(paymentChargedRepository.findOne(id))
            .map(paymentCharged -> new ResponseEntity<>(
                paymentCharged,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /paymentChargeds/:id -> delete the "id" paymentCharged.
     */
    @RequestMapping(value = "/paymentChargeds/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete PaymentCharged : {}", id);
        paymentChargedRepository.delete(id);
    }
}
