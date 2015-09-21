package com.ews.parkswift.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import org.joda.time.DateTime;
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
import com.ews.parkswift.config.Constants;
import com.ews.parkswift.domain.CustomerBooking;
import com.ews.parkswift.repository.CustomerBookingRepository;
import com.ews.parkswift.service.BookingService;
import com.ews.parkswift.service.util.paypal.PaypalUtils;
import com.ews.parkswift.web.rest.dto.parking.BookingDTO;

/**
 * REST controller for managing CustomerBooking.
 */
@RestController
@RequestMapping("/api")
public class CustomerBookingResource {

    private final Logger log = LoggerFactory.getLogger(CustomerBookingResource.class);

    @Inject
    private CustomerBookingRepository customerBookingRepository;
    @Inject
    private BookingService bookingService; 

    /**
     * POST  /customerBookings -> Create a new customerBooking.
     */
    @RequestMapping(value = "/customerBookings",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody BookingDTO bookingDTO) throws URISyntaxException {
        log.debug("REST request to save CustomerBooking : {}", bookingDTO);
//        if (b.getId() != null) {
//            return ResponseEntity.badRequest().header("Failure", "A new customerBooking cannot already have an ID").build();
//        }
        Map<String, String> mapPaymentDetails = PaypalUtils.getPaymentDetails(bookingDTO.getPaymentDTO().getPayKey());
        String paymentStatus="";
        if(null!=mapPaymentDetails){
        	if(mapPaymentDetails.get("status")!=null){
        		paymentStatus = mapPaymentDetails.get("status");
        	}
        }
        if(paymentStatus.equalsIgnoreCase(Constants.PAYPAL_PAYMENT_STATUS_COMPLETED)){
        	
        	if(mapPaymentDetails.get("expiryKey")!=null){
        		bookingDTO.getPaymentDTO().setPayKeyExpiryDate(new DateTime(mapPaymentDetails.get("expiryKey")));
        	}
        	if(mapPaymentDetails.get("senderEmail")!=null){
        		bookingDTO.getPaymentDTO().setSenderPaypalEmail(mapPaymentDetails.get("senderEmail"));
        	}
        	bookingService.save(bookingDTO);
        }
//        customerBookingRepository.save(customerBooking);
        return ResponseEntity.created(new URI("/api/customerBookings/" + bookingDTO.getBookingId())).build();
    }

    /**
     * PUT  /customerBookings -> Updates an existing customerBooking.
     */
    @RequestMapping(value = "/customerBookings",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody BookingDTO bookingDTO) throws URISyntaxException {
        log.debug("REST request to update CustomerBooking : {}", bookingDTO);
        if (bookingDTO.getBookingId() == null) {
            return create(bookingDTO);
        }
        bookingService.save(bookingDTO);
//        customerBookingRepository.save(customerBooking);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /customerBookings -> get all the customerBookings.
     */
    @RequestMapping(value = "/customerBookings/cancel/{id}",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public boolean cancelBooking(@PathVariable Long id){
    	return bookingService.cancelBooking(id);
    }
    
    
    /**
     * GET  /customerBookings -> get all the customerBookings.
     */
    @RequestMapping(value = "/customerBookings",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<BookingDTO> getAll(){
    	List<BookingDTO> listBookings = bookingService.getAllBookings();
    	return listBookings;
    }

    /**
     * GET  /customerBookings/:id -> get the "id" customerBooking.
     */
    @RequestMapping(value = "/customerBookings/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CustomerBooking> get(@PathVariable Long id) {
        log.debug("REST request to get CustomerBooking : {}", id);
        return Optional.ofNullable(customerBookingRepository.findOne(id))
            .map(customerBooking -> new ResponseEntity<>(
                customerBooking,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /customerBookings/:id -> delete the "id" customerBooking.
     */
    @RequestMapping(value = "/customerBookings/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete CustomerBooking : {}", id);
        customerBookingRepository.delete(id);
    }
}
