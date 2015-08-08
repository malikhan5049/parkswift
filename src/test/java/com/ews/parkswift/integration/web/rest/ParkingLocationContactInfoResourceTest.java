package com.ews.parkswift.integration.web.rest;

import com.ews.parkswift.Application;
import com.ews.parkswift.domain.ParkingLocationContactInfo;
import com.ews.parkswift.repository.ParkingLocationContactInfoRepository;
import com.ews.parkswift.web.rest.ParkingLocationContactInfoResource;

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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ParkingLocationContactInfoResource REST controller.
 *
 * @see ParkingLocationContactInfoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ParkingLocationContactInfoResourceTest {

    private static final String DEFAULT_FIRST_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_FIRST_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_LAST_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_LAST_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_PHONE1 = "SAMPLE_TEXT";
    private static final String UPDATED_PHONE1 = "UPDATED_TEXT";
    private static final String DEFAULT_PHONE2 = "SAMPLE_TEXT";
    private static final String UPDATED_PHONE2 = "UPDATED_TEXT";
    private static final String DEFAULT_EMAIL1 = "SAMPLE_TEXT";
    private static final String UPDATED_EMAIL1 = "UPDATED_TEXT";
    private static final String DEFAULT_EMAIL2 = "SAMPLE_TEXT";
    private static final String UPDATED_EMAIL2 = "UPDATED_TEXT";

    @Inject
    private ParkingLocationContactInfoRepository parkingLocationContactInfoRepository;

    private MockMvc restParkingLocationContactInfoMockMvc;

    private ParkingLocationContactInfo parkingLocationContactInfo;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ParkingLocationContactInfoResource parkingLocationContactInfoResource = new ParkingLocationContactInfoResource();
        ReflectionTestUtils.setField(parkingLocationContactInfoResource, "parkingLocationContactInfoRepository", parkingLocationContactInfoRepository);
        this.restParkingLocationContactInfoMockMvc = MockMvcBuilders.standaloneSetup(parkingLocationContactInfoResource).build();
    }

    @Before
    public void initTest() {
        parkingLocationContactInfo = new ParkingLocationContactInfo();
        parkingLocationContactInfo.setFirstName(DEFAULT_FIRST_NAME);
        parkingLocationContactInfo.setLastName(DEFAULT_LAST_NAME);
        parkingLocationContactInfo.setPhone1(DEFAULT_PHONE1);
        parkingLocationContactInfo.setPhone2(DEFAULT_PHONE2);
        parkingLocationContactInfo.setEmail1(DEFAULT_EMAIL1);
        parkingLocationContactInfo.setEmail2(DEFAULT_EMAIL2);
    }

    @Test
    @Transactional
    public void createParkingLocationContactInfo() throws Exception {
        int databaseSizeBeforeCreate = parkingLocationContactInfoRepository.findAll().size();

        // Create the ParkingLocationContactInfo
        restParkingLocationContactInfoMockMvc.perform(post("/api/parkingLocationContactInfos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(parkingLocationContactInfo)))
                .andExpect(status().isCreated());

        // Validate the ParkingLocationContactInfo in the database
        List<ParkingLocationContactInfo> parkingLocationContactInfos = parkingLocationContactInfoRepository.findAll();
        assertThat(parkingLocationContactInfos).hasSize(databaseSizeBeforeCreate + 1);
        ParkingLocationContactInfo testParkingLocationContactInfo = parkingLocationContactInfos.get(parkingLocationContactInfos.size() - 1);
        assertThat(testParkingLocationContactInfo.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testParkingLocationContactInfo.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testParkingLocationContactInfo.getPhone1()).isEqualTo(DEFAULT_PHONE1);
        assertThat(testParkingLocationContactInfo.getPhone2()).isEqualTo(DEFAULT_PHONE2);
        assertThat(testParkingLocationContactInfo.getEmail1()).isEqualTo(DEFAULT_EMAIL1);
        assertThat(testParkingLocationContactInfo.getEmail2()).isEqualTo(DEFAULT_EMAIL2);
    }

    @Test
    @Transactional
    public void getAllParkingLocationContactInfos() throws Exception {
        // Initialize the database
        parkingLocationContactInfoRepository.saveAndFlush(parkingLocationContactInfo);

        // Get all the parkingLocationContactInfos
        restParkingLocationContactInfoMockMvc.perform(get("/api/parkingLocationContactInfos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(parkingLocationContactInfo.getId().intValue())))
                .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
                .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
                .andExpect(jsonPath("$.[*].phone1").value(hasItem(DEFAULT_PHONE1.toString())))
                .andExpect(jsonPath("$.[*].phone2").value(hasItem(DEFAULT_PHONE2.toString())))
                .andExpect(jsonPath("$.[*].email1").value(hasItem(DEFAULT_EMAIL1.toString())))
                .andExpect(jsonPath("$.[*].email2").value(hasItem(DEFAULT_EMAIL2.toString())));
    }

    @Test
    @Transactional
    public void getParkingLocationContactInfo() throws Exception {
        // Initialize the database
        parkingLocationContactInfoRepository.saveAndFlush(parkingLocationContactInfo);

        // Get the parkingLocationContactInfo
        restParkingLocationContactInfoMockMvc.perform(get("/api/parkingLocationContactInfos/{id}", parkingLocationContactInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(parkingLocationContactInfo.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.phone1").value(DEFAULT_PHONE1.toString()))
            .andExpect(jsonPath("$.phone2").value(DEFAULT_PHONE2.toString()))
            .andExpect(jsonPath("$.email1").value(DEFAULT_EMAIL1.toString()))
            .andExpect(jsonPath("$.email2").value(DEFAULT_EMAIL2.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingParkingLocationContactInfo() throws Exception {
        // Get the parkingLocationContactInfo
        restParkingLocationContactInfoMockMvc.perform(get("/api/parkingLocationContactInfos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateParkingLocationContactInfo() throws Exception {
        // Initialize the database
        parkingLocationContactInfoRepository.saveAndFlush(parkingLocationContactInfo);

		int databaseSizeBeforeUpdate = parkingLocationContactInfoRepository.findAll().size();

        // Update the parkingLocationContactInfo
        parkingLocationContactInfo.setFirstName(UPDATED_FIRST_NAME);
        parkingLocationContactInfo.setLastName(UPDATED_LAST_NAME);
        parkingLocationContactInfo.setPhone1(UPDATED_PHONE1);
        parkingLocationContactInfo.setPhone2(UPDATED_PHONE2);
        parkingLocationContactInfo.setEmail1(UPDATED_EMAIL1);
        parkingLocationContactInfo.setEmail2(UPDATED_EMAIL2);
        restParkingLocationContactInfoMockMvc.perform(put("/api/parkingLocationContactInfos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(parkingLocationContactInfo)))
                .andExpect(status().isOk());

        // Validate the ParkingLocationContactInfo in the database
        List<ParkingLocationContactInfo> parkingLocationContactInfos = parkingLocationContactInfoRepository.findAll();
        assertThat(parkingLocationContactInfos).hasSize(databaseSizeBeforeUpdate);
        ParkingLocationContactInfo testParkingLocationContactInfo = parkingLocationContactInfos.get(parkingLocationContactInfos.size() - 1);
        assertThat(testParkingLocationContactInfo.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testParkingLocationContactInfo.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testParkingLocationContactInfo.getPhone1()).isEqualTo(UPDATED_PHONE1);
        assertThat(testParkingLocationContactInfo.getPhone2()).isEqualTo(UPDATED_PHONE2);
        assertThat(testParkingLocationContactInfo.getEmail1()).isEqualTo(UPDATED_EMAIL1);
        assertThat(testParkingLocationContactInfo.getEmail2()).isEqualTo(UPDATED_EMAIL2);
    }

    @Test
    @Transactional
    public void deleteParkingLocationContactInfo() throws Exception {
        // Initialize the database
        parkingLocationContactInfoRepository.saveAndFlush(parkingLocationContactInfo);

		int databaseSizeBeforeDelete = parkingLocationContactInfoRepository.findAll().size();

        // Get the parkingLocationContactInfo
        restParkingLocationContactInfoMockMvc.perform(delete("/api/parkingLocationContactInfos/{id}", parkingLocationContactInfo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ParkingLocationContactInfo> parkingLocationContactInfos = parkingLocationContactInfoRepository.findAll();
        assertThat(parkingLocationContactInfos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
