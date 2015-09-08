package com.ews.parkswift.web.rest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.ews.parkswift.service.FindParkingSpaceService;
import com.ews.parkswift.vo.FindParkingsDTOValidator;
import com.ews.parkswift.web.rest.dto.parking.AvailableParkingDTO;
import com.ews.parkswift.web.rest.dto.parking.FindParkingsDTO;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * REST controller for managing ParkingSpace.
 */
@RestController
@RequestMapping("/api")
public class FindParkingSpaceResource {

    private final Logger log = LoggerFactory.getLogger(FindParkingSpaceResource.class);
    
    @Inject
    private FindParkingSpaceService findParkingSpaceService;
    
    
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(new FindParkingsDTOValidator());
    }
    
    
    /**
     * POST  /find available parkingSpaces 
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonParseException 
     */
	@RequestMapping(value = "/findAvailableParkings",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AvailableParkingDTO> findAvailableParkings(@Valid @RequestBody FindParkingsDTO findAvailableParkingsDTO)throws URISyntaxException, JsonParseException, JsonMappingException, IOException {//@Valid @RequestBody ParkingSpace parkingSpace) throws URISyntaxException {
    	
        log.debug("REST request to find ParkingSpace : {}", findAvailableParkingsDTO);
        
        List<AvailableParkingDTO> availableParkings = findParkingSpaceService.findParkingSpaces(findAvailableParkingsDTO);
        
        return availableParkings;
    }
}
