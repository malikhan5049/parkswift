package com.ews.parkswift.web.rest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.codahale.metrics.annotation.Timed;
import com.ews.parkswift.domain.ParkingLocation;
import com.ews.parkswift.domain.ParkingLocationImage;
import com.ews.parkswift.repository.ParkingLocationRepository;
import com.ews.parkswift.service.ParkingLocationService;
import com.ews.parkswift.web.rest.util.PaginationUtil;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * REST controller for managing ParkingLocation.
 */
@RestController
@RequestMapping("/api")
public class ParkingLocationResource {

    private final Logger log = LoggerFactory.getLogger(ParkingLocationResource.class);
    private final ObjectMapper mapper =  new ObjectMapper();
    
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    @Inject
    private ParkingLocationRepository parkingLocationRepository;
    @Inject
    private ParkingLocationService parkingLocationService;

    /**
     * POST  /parkingLocations -> Create a new parkingLocation.
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonParseException 
     */
    @RequestMapping(value = "/parkingLocations",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestPart(value = "json") String parkignLocationStr,
    		@RequestParam(value = "images", required = false) List<MultipartFile> images) throws URISyntaxException, JsonParseException, JsonMappingException, IOException {
        log.debug("REST request to save ParkingLocation : {}", parkignLocationStr);
        ParkingLocation parkingLocation = mapper.readValue(parkignLocationStr, ParkingLocation.class);
        if(!validator.validate(parkingLocation).isEmpty())
        	return ResponseEntity.badRequest().header("Failure", "Please enter required information").build();
    	Set<ParkingLocationImage> parkingLocationImages = new HashSet<>();
    	ParkingLocationImage parkingLocationImage;
    	for(MultipartFile e : images){
    		parkingLocationImage = new ParkingLocationImage();
    		parkingLocationImage.setImage(e.getBytes());
    		parkingLocationImages.add(parkingLocationImage);
    	}
    	parkingLocation.setParkingLocationImages(parkingLocationImages);
        if (parkingLocation.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new parkingLocation cannot already have an ID").build();
        }
        
        parkingLocationService.save(parkingLocation);
        return ResponseEntity.created(new URI("/api/parkingLocations/" + parkingLocation.getId())).build();
    }

    /**
     * PUT  /parkingLocations -> Updates an existing parkingLocation.
     */
    @RequestMapping(value = "/parkingLocations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody ParkingLocation parkingLocation) throws URISyntaxException {
        log.debug("REST request to update ParkingLocation : {}", parkingLocation);
        if (parkingLocation.getId() == null) {
//            return create(parkingLocation);
        }
        parkingLocationService.save(parkingLocation);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /parkingLocations -> get all the parkingLocations.
     */
    @RequestMapping(value = "/parkingLocations",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ParkingLocation>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<ParkingLocation> page = parkingLocationRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/parkingLocations", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /parkingLocations/:id -> get the "id" parkingLocation.
     */
    @RequestMapping(value = "/parkingLocations/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ParkingLocation> get(@PathVariable Long id) {
        log.debug("REST request to get ParkingLocation : {}", id);
        return Optional.ofNullable(parkingLocationRepository.findOne(id))
            .map(parkingLocation -> new ResponseEntity<>(
                parkingLocation,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /parkingLocations/:id -> delete the "id" parkingLocation.
     */
    @RequestMapping(value = "/parkingLocations/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete ParkingLocation : {}", id);
        parkingLocationService.delete(id);
    }
}
