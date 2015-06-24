package com.ews.parkswift.web.rest;

import com.ews.parkswift.Application;
import com.ews.parkswift.domain.ParkingLocation;
import com.ews.parkswift.repository.ParkingLocationRepository;
import com.ews.parkswift.service.ParkingLocationService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.hasItem;

import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ParkingLocationResource REST controller.
 *
 * @see ParkingLocationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ParkingLocationResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static final String DEFAULT_PROPERTY_TYPE = "SAMPLE_TEXT";
    private static final String UPDATED_PROPERTY_TYPE = "UPDATED_TEXT";

    private static final Integer DEFAULT_NUMBER_OF_SPACES = 0;
    private static final Integer UPDATED_NUMBER_OF_SPACES = 1;
    private static final String DEFAULT_ADDRESS_LINE1 = "SAMPLE_TEXT";
    private static final String UPDATED_ADDRESS_LINE1 = "UPDATED_TEXT";
    private static final String DEFAULT_ADDRESS_LINE2 = "SAMPLE_TEXT";
    private static final String UPDATED_ADDRESS_LINE2 = "UPDATED_TEXT";
    private static final String DEFAULT_CITY = "SAMPLE_TEXT";
    private static final String UPDATED_CITY = "UPDATED_TEXT";
    private static final String DEFAULT_STATE = "SAMPLE_TEXT";
    private static final String UPDATED_STATE = "UPDATED_TEXT";
    private static final String DEFAULT_COUNTRY = "SAMPLE_TEXT";
    private static final String UPDATED_COUNTRY = "UPDATED_TEXT";
    private static final String DEFAULT_ZIP_CODE = "SAMPLE_TEXT";
    private static final String UPDATED_ZIP_CODE = "UPDATED_TEXT";

    private static final BigDecimal DEFAULT_LONGITUDE = new BigDecimal(0);
    private static final BigDecimal UPDATED_LONGITUDE = new BigDecimal(1);

    private static final BigDecimal DEFAULT_LATTITUDE = new BigDecimal(0);
    private static final BigDecimal UPDATED_LATTITUDE = new BigDecimal(1);

    private static final DateTime DEFAULT_CREATED_AT = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_CREATED_AT = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_CREATED_AT_STR = dateTimeFormatter.print(DEFAULT_CREATED_AT);

    private static final DateTime DEFAULT_MODIFIED_AT = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_MODIFIED_AT = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_MODIFIED_AT_STR = dateTimeFormatter.print(DEFAULT_MODIFIED_AT);

    @Inject
    private ParkingLocationRepository parkingLocationRepository;
    @Inject
    private ParkingLocationService parkingLocationService;

    private MockMvc restParkingLocationMockMvc;

    private ParkingLocation parkingLocation;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ParkingLocationResource parkingLocationResource = new ParkingLocationResource();
        ReflectionTestUtils.setField(parkingLocationResource, "parkingLocationRepository", parkingLocationRepository);
        ReflectionTestUtils.setField(parkingLocationResource, "parkingLocationService", parkingLocationService);
        this.restParkingLocationMockMvc = MockMvcBuilders.standaloneSetup(parkingLocationResource).build();
    }

    @Before
    public void initTest() {
        parkingLocation = new ParkingLocation();
        parkingLocation.setPropertyType(DEFAULT_PROPERTY_TYPE);
        parkingLocation.setNumberOfSpaces(DEFAULT_NUMBER_OF_SPACES);
        parkingLocation.setAddressLine1(DEFAULT_ADDRESS_LINE1);
        parkingLocation.setAddressLine2(DEFAULT_ADDRESS_LINE2);
        parkingLocation.setCity(DEFAULT_CITY);
        parkingLocation.setState(DEFAULT_STATE);
        parkingLocation.setCountry(DEFAULT_COUNTRY);
        parkingLocation.setZipCode(DEFAULT_ZIP_CODE);
        parkingLocation.setLongitude(DEFAULT_LONGITUDE);
        parkingLocation.setLattitude(DEFAULT_LATTITUDE);
        parkingLocation.setCreatedAt(DEFAULT_CREATED_AT);
        parkingLocation.setModifiedAt(DEFAULT_MODIFIED_AT);
    }

    @Test
    @Transactional
    public void createParkingLocation() throws Exception {
        int databaseSizeBeforeCreate = parkingLocationRepository.findAll().size();

        // Create the ParkingLocation
        restParkingLocationMockMvc.perform(post("/api/parkingLocations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(parkingLocation)))
                .andExpect(status().isCreated());

        // Validate the ParkingLocation in the database
        List<ParkingLocation> parkingLocations = parkingLocationRepository.findAll();
        assertThat(parkingLocations).hasSize(databaseSizeBeforeCreate + 1);
        ParkingLocation testParkingLocation = parkingLocations.get(parkingLocations.size() - 1);
        assertThat(testParkingLocation.getPropertyType()).isEqualTo(DEFAULT_PROPERTY_TYPE);
        assertThat(testParkingLocation.getNumberOfSpaces()).isEqualTo(DEFAULT_NUMBER_OF_SPACES);
        assertThat(testParkingLocation.getAddressLine1()).isEqualTo(DEFAULT_ADDRESS_LINE1);
        assertThat(testParkingLocation.getAddressLine2()).isEqualTo(DEFAULT_ADDRESS_LINE2);
        assertThat(testParkingLocation.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testParkingLocation.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testParkingLocation.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testParkingLocation.getZipCode()).isEqualTo(DEFAULT_ZIP_CODE);
        assertThat(testParkingLocation.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testParkingLocation.getLattitude()).isEqualTo(DEFAULT_LATTITUDE);
        assertThat(testParkingLocation.getCreatedAt().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testParkingLocation.getModifiedAt().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_MODIFIED_AT);
    }

    @Test
    @Transactional
    public void checkPropertyTypeIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(parkingLocationRepository.findAll()).hasSize(0);
        // set the field null
        parkingLocation.setPropertyType(null);

        // Create the ParkingLocation, which fails.
        restParkingLocationMockMvc.perform(post("/api/parkingLocations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(parkingLocation)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<ParkingLocation> parkingLocations = parkingLocationRepository.findAll();
        assertThat(parkingLocations).hasSize(0);
    }

    @Test
    @Transactional
    public void checkNumberOfSpacesIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(parkingLocationRepository.findAll()).hasSize(0);
        // set the field null
        parkingLocation.setNumberOfSpaces(null);

        // Create the ParkingLocation, which fails.
        restParkingLocationMockMvc.perform(post("/api/parkingLocations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(parkingLocation)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<ParkingLocation> parkingLocations = parkingLocationRepository.findAll();
        assertThat(parkingLocations).hasSize(0);
    }

    @Test
    @Transactional
    public void checkAddressLine1IsRequired() throws Exception {
        // Validate the database is empty
        assertThat(parkingLocationRepository.findAll()).hasSize(0);
        // set the field null
        parkingLocation.setAddressLine1(null);

        // Create the ParkingLocation, which fails.
        restParkingLocationMockMvc.perform(post("/api/parkingLocations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(parkingLocation)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<ParkingLocation> parkingLocations = parkingLocationRepository.findAll();
        assertThat(parkingLocations).hasSize(0);
    }

    @Test
    @Transactional
    public void checkCityIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(parkingLocationRepository.findAll()).hasSize(0);
        // set the field null
        parkingLocation.setCity(null);

        // Create the ParkingLocation, which fails.
        restParkingLocationMockMvc.perform(post("/api/parkingLocations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(parkingLocation)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<ParkingLocation> parkingLocations = parkingLocationRepository.findAll();
        assertThat(parkingLocations).hasSize(0);
    }

    @Test
    @Transactional
    public void checkCountryIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(parkingLocationRepository.findAll()).hasSize(0);
        // set the field null
        parkingLocation.setCountry(null);

        // Create the ParkingLocation, which fails.
        restParkingLocationMockMvc.perform(post("/api/parkingLocations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(parkingLocation)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<ParkingLocation> parkingLocations = parkingLocationRepository.findAll();
        assertThat(parkingLocations).hasSize(0);
    }

    @Test
    @Transactional
    public void checkZipCodeIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(parkingLocationRepository.findAll()).hasSize(0);
        // set the field null
        parkingLocation.setZipCode(null);

        // Create the ParkingLocation, which fails.
        restParkingLocationMockMvc.perform(post("/api/parkingLocations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(parkingLocation)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<ParkingLocation> parkingLocations = parkingLocationRepository.findAll();
        assertThat(parkingLocations).hasSize(0);
    }

    @Test
    @Transactional
    public void checkLongitudeIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(parkingLocationRepository.findAll()).hasSize(0);
        // set the field null
        parkingLocation.setLongitude(null);

        // Create the ParkingLocation, which fails.
        restParkingLocationMockMvc.perform(post("/api/parkingLocations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(parkingLocation)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<ParkingLocation> parkingLocations = parkingLocationRepository.findAll();
        assertThat(parkingLocations).hasSize(0);
    }

    @Test
    @Transactional
    public void checkLattitudeIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(parkingLocationRepository.findAll()).hasSize(0);
        // set the field null
        parkingLocation.setLattitude(null);

        // Create the ParkingLocation, which fails.
        restParkingLocationMockMvc.perform(post("/api/parkingLocations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(parkingLocation)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<ParkingLocation> parkingLocations = parkingLocationRepository.findAll();
        assertThat(parkingLocations).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllParkingLocations() throws Exception {
        // Initialize the database
        parkingLocationRepository.saveAndFlush(parkingLocation);

        // Get all the parkingLocations
        restParkingLocationMockMvc.perform(get("/api/parkingLocations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(parkingLocation.getId().intValue())))
                .andExpect(jsonPath("$.[*].propertyType").value(hasItem(DEFAULT_PROPERTY_TYPE.toString())))
                .andExpect(jsonPath("$.[*].numberOfSpaces").value(hasItem(DEFAULT_NUMBER_OF_SPACES)))
                .andExpect(jsonPath("$.[*].addressLine1").value(hasItem(DEFAULT_ADDRESS_LINE1.toString())))
                .andExpect(jsonPath("$.[*].addressLine2").value(hasItem(DEFAULT_ADDRESS_LINE2.toString())))
                .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
                .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
                .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
                .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE.toString())))
                .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.intValue())))
                .andExpect(jsonPath("$.[*].lattitude").value(hasItem(DEFAULT_LATTITUDE.intValue())))
                .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT_STR)))
                .andExpect(jsonPath("$.[*].modifiedAt").value(hasItem(DEFAULT_MODIFIED_AT_STR)));
    }

    @Test
    @Transactional
    public void getParkingLocation() throws Exception {
        // Initialize the database
        parkingLocationRepository.saveAndFlush(parkingLocation);

        // Get the parkingLocation
        restParkingLocationMockMvc.perform(get("/api/parkingLocations/{id}", parkingLocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(parkingLocation.getId().intValue()))
            .andExpect(jsonPath("$.propertyType").value(DEFAULT_PROPERTY_TYPE.toString()))
            .andExpect(jsonPath("$.numberOfSpaces").value(DEFAULT_NUMBER_OF_SPACES))
            .andExpect(jsonPath("$.addressLine1").value(DEFAULT_ADDRESS_LINE1.toString()))
            .andExpect(jsonPath("$.addressLine2").value(DEFAULT_ADDRESS_LINE2.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()))
            .andExpect(jsonPath("$.zipCode").value(DEFAULT_ZIP_CODE.toString()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.intValue()))
            .andExpect(jsonPath("$.lattitude").value(DEFAULT_LATTITUDE.intValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT_STR))
            .andExpect(jsonPath("$.modifiedAt").value(DEFAULT_MODIFIED_AT_STR));
    }

    @Test
    @Transactional
    public void getNonExistingParkingLocation() throws Exception {
        // Get the parkingLocation
        restParkingLocationMockMvc.perform(get("/api/parkingLocations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateParkingLocation() throws Exception {
        // Initialize the database
        parkingLocationRepository.saveAndFlush(parkingLocation);

		int databaseSizeBeforeUpdate = parkingLocationRepository.findAll().size();

        // Update the parkingLocation
        parkingLocation.setPropertyType(UPDATED_PROPERTY_TYPE);
        parkingLocation.setNumberOfSpaces(UPDATED_NUMBER_OF_SPACES);
        parkingLocation.setAddressLine1(UPDATED_ADDRESS_LINE1);
        parkingLocation.setAddressLine2(UPDATED_ADDRESS_LINE2);
        parkingLocation.setCity(UPDATED_CITY);
        parkingLocation.setState(UPDATED_STATE);
        parkingLocation.setCountry(UPDATED_COUNTRY);
        parkingLocation.setZipCode(UPDATED_ZIP_CODE);
        parkingLocation.setLongitude(UPDATED_LONGITUDE);
        parkingLocation.setLattitude(UPDATED_LATTITUDE);
        parkingLocation.setCreatedAt(UPDATED_CREATED_AT);
        parkingLocation.setModifiedAt(UPDATED_MODIFIED_AT);
        restParkingLocationMockMvc.perform(put("/api/parkingLocations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(parkingLocation)))
                .andExpect(status().isOk());

        // Validate the ParkingLocation in the database
        List<ParkingLocation> parkingLocations = parkingLocationRepository.findAll();
        assertThat(parkingLocations).hasSize(databaseSizeBeforeUpdate);
        ParkingLocation testParkingLocation = parkingLocations.get(parkingLocations.size() - 1);
        assertThat(testParkingLocation.getPropertyType()).isEqualTo(UPDATED_PROPERTY_TYPE);
        assertThat(testParkingLocation.getNumberOfSpaces()).isEqualTo(UPDATED_NUMBER_OF_SPACES);
        assertThat(testParkingLocation.getAddressLine1()).isEqualTo(UPDATED_ADDRESS_LINE1);
        assertThat(testParkingLocation.getAddressLine2()).isEqualTo(UPDATED_ADDRESS_LINE2);
        assertThat(testParkingLocation.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testParkingLocation.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testParkingLocation.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testParkingLocation.getZipCode()).isEqualTo(UPDATED_ZIP_CODE);
        assertThat(testParkingLocation.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testParkingLocation.getLattitude()).isEqualTo(UPDATED_LATTITUDE);
        assertThat(testParkingLocation.getCreatedAt().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testParkingLocation.getModifiedAt().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_MODIFIED_AT);
    }

    @Test
    @Transactional
    public void deleteParkingLocation() throws Exception {
        // Initialize the database
        parkingLocationRepository.saveAndFlush(parkingLocation);

		int databaseSizeBeforeDelete = parkingLocationRepository.findAll().size();

        // Get the parkingLocation
        restParkingLocationMockMvc.perform(delete("/api/parkingLocations/{id}", parkingLocation.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ParkingLocation> parkingLocations = parkingLocationRepository.findAll();
        assertThat(parkingLocations).hasSize(databaseSizeBeforeDelete - 1);
    }
}
