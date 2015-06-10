package com.ews.parkswift.web.rest;

import com.ews.parkswift.Application;
import com.ews.parkswift.domain.ParkingSpace;
import com.ews.parkswift.repository.ParkingSpaceRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ParkingSpaceResource REST controller.
 *
 * @see ParkingSpaceResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ParkingSpaceResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";

    private static final Boolean DEFAULT_PART_OF_BATCH = false;
    private static final Boolean UPDATED_PART_OF_BATCH = true;

    private static final Integer DEFAULT_BATCH_NUMBER = 0;
    private static final Integer UPDATED_BATCH_NUMBER = 1;

    private static final Boolean DEFAULT_FULL_RESERVED = false;
    private static final Boolean UPDATED_FULL_RESERVED = true;

    private static final DateTime DEFAULT_CREATED_AT = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_CREATED_AT = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_CREATED_AT_STR = dateTimeFormatter.print(DEFAULT_CREATED_AT);

    private static final DateTime DEFAULT_MODIFIED_AT = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_MODIFIED_AT = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_MODIFIED_AT_STR = dateTimeFormatter.print(DEFAULT_MODIFIED_AT);

    @Inject
    private ParkingSpaceRepository parkingSpaceRepository;

    private MockMvc restParkingSpaceMockMvc;

    private ParkingSpace parkingSpace;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ParkingSpaceResource parkingSpaceResource = new ParkingSpaceResource();
        ReflectionTestUtils.setField(parkingSpaceResource, "parkingSpaceRepository", parkingSpaceRepository);
        this.restParkingSpaceMockMvc = MockMvcBuilders.standaloneSetup(parkingSpaceResource).build();
    }

    @Before
    public void initTest() {
        parkingSpace = new ParkingSpace();
        parkingSpace.setDescription(DEFAULT_DESCRIPTION);
        parkingSpace.setPartOfBatch(DEFAULT_PART_OF_BATCH);
        parkingSpace.setBatchNumber(DEFAULT_BATCH_NUMBER);
        parkingSpace.setFullReserved(DEFAULT_FULL_RESERVED);
        parkingSpace.setCreatedAt(DEFAULT_CREATED_AT);
        parkingSpace.setModifiedAt(DEFAULT_MODIFIED_AT);
    }

    @Test
    @Transactional
    public void createParkingSpace() throws Exception {
        int databaseSizeBeforeCreate = parkingSpaceRepository.findAll().size();

        // Create the ParkingSpace
        restParkingSpaceMockMvc.perform(post("/api/parkingSpaces")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(parkingSpace)))
                .andExpect(status().isCreated());

        // Validate the ParkingSpace in the database
        List<ParkingSpace> parkingSpaces = parkingSpaceRepository.findAll();
        assertThat(parkingSpaces).hasSize(databaseSizeBeforeCreate + 1);
        ParkingSpace testParkingSpace = parkingSpaces.get(parkingSpaces.size() - 1);
        assertThat(testParkingSpace.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testParkingSpace.getPartOfBatch()).isEqualTo(DEFAULT_PART_OF_BATCH);
        assertThat(testParkingSpace.getBatchNumber()).isEqualTo(DEFAULT_BATCH_NUMBER);
        assertThat(testParkingSpace.getFullReserved()).isEqualTo(DEFAULT_FULL_RESERVED);
        assertThat(testParkingSpace.getCreatedAt().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testParkingSpace.getModifiedAt().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_MODIFIED_AT);
    }

    @Test
    @Transactional
    public void getAllParkingSpaces() throws Exception {
        // Initialize the database
        parkingSpaceRepository.saveAndFlush(parkingSpace);

        // Get all the parkingSpaces
        restParkingSpaceMockMvc.perform(get("/api/parkingSpaces"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(parkingSpace.getId().intValue())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].partOfBatch").value(hasItem(DEFAULT_PART_OF_BATCH.booleanValue())))
                .andExpect(jsonPath("$.[*].batchNumber").value(hasItem(DEFAULT_BATCH_NUMBER)))
                .andExpect(jsonPath("$.[*].fullReserved").value(hasItem(DEFAULT_FULL_RESERVED.booleanValue())))
                .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT_STR)))
                .andExpect(jsonPath("$.[*].modifiedAt").value(hasItem(DEFAULT_MODIFIED_AT_STR)));
    }

    @Test
    @Transactional
    public void getParkingSpace() throws Exception {
        // Initialize the database
        parkingSpaceRepository.saveAndFlush(parkingSpace);

        // Get the parkingSpace
        restParkingSpaceMockMvc.perform(get("/api/parkingSpaces/{id}", parkingSpace.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(parkingSpace.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.partOfBatch").value(DEFAULT_PART_OF_BATCH.booleanValue()))
            .andExpect(jsonPath("$.batchNumber").value(DEFAULT_BATCH_NUMBER))
            .andExpect(jsonPath("$.fullReserved").value(DEFAULT_FULL_RESERVED.booleanValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT_STR))
            .andExpect(jsonPath("$.modifiedAt").value(DEFAULT_MODIFIED_AT_STR));
    }

    @Test
    @Transactional
    public void getNonExistingParkingSpace() throws Exception {
        // Get the parkingSpace
        restParkingSpaceMockMvc.perform(get("/api/parkingSpaces/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateParkingSpace() throws Exception {
        // Initialize the database
        parkingSpaceRepository.saveAndFlush(parkingSpace);

		int databaseSizeBeforeUpdate = parkingSpaceRepository.findAll().size();

        // Update the parkingSpace
        parkingSpace.setDescription(UPDATED_DESCRIPTION);
        parkingSpace.setPartOfBatch(UPDATED_PART_OF_BATCH);
        parkingSpace.setBatchNumber(UPDATED_BATCH_NUMBER);
        parkingSpace.setFullReserved(UPDATED_FULL_RESERVED);
        parkingSpace.setCreatedAt(UPDATED_CREATED_AT);
        parkingSpace.setModifiedAt(UPDATED_MODIFIED_AT);
        restParkingSpaceMockMvc.perform(put("/api/parkingSpaces")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(parkingSpace)))
                .andExpect(status().isOk());

        // Validate the ParkingSpace in the database
        List<ParkingSpace> parkingSpaces = parkingSpaceRepository.findAll();
        assertThat(parkingSpaces).hasSize(databaseSizeBeforeUpdate);
        ParkingSpace testParkingSpace = parkingSpaces.get(parkingSpaces.size() - 1);
        assertThat(testParkingSpace.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testParkingSpace.getPartOfBatch()).isEqualTo(UPDATED_PART_OF_BATCH);
        assertThat(testParkingSpace.getBatchNumber()).isEqualTo(UPDATED_BATCH_NUMBER);
        assertThat(testParkingSpace.getFullReserved()).isEqualTo(UPDATED_FULL_RESERVED);
        assertThat(testParkingSpace.getCreatedAt().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testParkingSpace.getModifiedAt().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_MODIFIED_AT);
    }

    @Test
    @Transactional
    public void deleteParkingSpace() throws Exception {
        // Initialize the database
        parkingSpaceRepository.saveAndFlush(parkingSpace);

		int databaseSizeBeforeDelete = parkingSpaceRepository.findAll().size();

        // Get the parkingSpace
        restParkingSpaceMockMvc.perform(delete("/api/parkingSpaces/{id}", parkingSpace.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ParkingSpace> parkingSpaces = parkingSpaceRepository.findAll();
        assertThat(parkingSpaces).hasSize(databaseSizeBeforeDelete - 1);
    }
}
