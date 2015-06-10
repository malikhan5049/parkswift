package com.ews.parkswift.web.rest;

import com.ews.parkswift.Application;
import com.ews.parkswift.domain.ParkingSpaceImage;
import com.ews.parkswift.repository.ParkingSpaceImageRepository;

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
 * Test class for the ParkingSpaceImageResource REST controller.
 *
 * @see ParkingSpaceImageResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ParkingSpaceImageResourceTest {

    private static final String DEFAULT_IMAGE = "SAMPLE_TEXT";
    private static final String UPDATED_IMAGE = "UPDATED_TEXT";

    @Inject
    private ParkingSpaceImageRepository parkingSpaceImageRepository;

    private MockMvc restParkingSpaceImageMockMvc;

    private ParkingSpaceImage parkingSpaceImage;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ParkingSpaceImageResource parkingSpaceImageResource = new ParkingSpaceImageResource();
        ReflectionTestUtils.setField(parkingSpaceImageResource, "parkingSpaceImageRepository", parkingSpaceImageRepository);
        this.restParkingSpaceImageMockMvc = MockMvcBuilders.standaloneSetup(parkingSpaceImageResource).build();
    }

    @Before
    public void initTest() {
        parkingSpaceImage = new ParkingSpaceImage();
        parkingSpaceImage.setImage(DEFAULT_IMAGE);
    }

    @Test
    @Transactional
    public void createParkingSpaceImage() throws Exception {
        int databaseSizeBeforeCreate = parkingSpaceImageRepository.findAll().size();

        // Create the ParkingSpaceImage
        restParkingSpaceImageMockMvc.perform(post("/api/parkingSpaceImages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(parkingSpaceImage)))
                .andExpect(status().isCreated());

        // Validate the ParkingSpaceImage in the database
        List<ParkingSpaceImage> parkingSpaceImages = parkingSpaceImageRepository.findAll();
        assertThat(parkingSpaceImages).hasSize(databaseSizeBeforeCreate + 1);
        ParkingSpaceImage testParkingSpaceImage = parkingSpaceImages.get(parkingSpaceImages.size() - 1);
        assertThat(testParkingSpaceImage.getImage()).isEqualTo(DEFAULT_IMAGE);
    }

    @Test
    @Transactional
    public void checkImageIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(parkingSpaceImageRepository.findAll()).hasSize(0);
        // set the field null
        parkingSpaceImage.setImage(null);

        // Create the ParkingSpaceImage, which fails.
        restParkingSpaceImageMockMvc.perform(post("/api/parkingSpaceImages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(parkingSpaceImage)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<ParkingSpaceImage> parkingSpaceImages = parkingSpaceImageRepository.findAll();
        assertThat(parkingSpaceImages).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllParkingSpaceImages() throws Exception {
        // Initialize the database
        parkingSpaceImageRepository.saveAndFlush(parkingSpaceImage);

        // Get all the parkingSpaceImages
        restParkingSpaceImageMockMvc.perform(get("/api/parkingSpaceImages"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(parkingSpaceImage.getId().intValue())))
                .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE.toString())));
    }

    @Test
    @Transactional
    public void getParkingSpaceImage() throws Exception {
        // Initialize the database
        parkingSpaceImageRepository.saveAndFlush(parkingSpaceImage);

        // Get the parkingSpaceImage
        restParkingSpaceImageMockMvc.perform(get("/api/parkingSpaceImages/{id}", parkingSpaceImage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(parkingSpaceImage.getId().intValue()))
            .andExpect(jsonPath("$.image").value(DEFAULT_IMAGE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingParkingSpaceImage() throws Exception {
        // Get the parkingSpaceImage
        restParkingSpaceImageMockMvc.perform(get("/api/parkingSpaceImages/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateParkingSpaceImage() throws Exception {
        // Initialize the database
        parkingSpaceImageRepository.saveAndFlush(parkingSpaceImage);

		int databaseSizeBeforeUpdate = parkingSpaceImageRepository.findAll().size();

        // Update the parkingSpaceImage
        parkingSpaceImage.setImage(UPDATED_IMAGE);
        restParkingSpaceImageMockMvc.perform(put("/api/parkingSpaceImages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(parkingSpaceImage)))
                .andExpect(status().isOk());

        // Validate the ParkingSpaceImage in the database
        List<ParkingSpaceImage> parkingSpaceImages = parkingSpaceImageRepository.findAll();
        assertThat(parkingSpaceImages).hasSize(databaseSizeBeforeUpdate);
        ParkingSpaceImage testParkingSpaceImage = parkingSpaceImages.get(parkingSpaceImages.size() - 1);
        assertThat(testParkingSpaceImage.getImage()).isEqualTo(UPDATED_IMAGE);
    }

    @Test
    @Transactional
    public void deleteParkingSpaceImage() throws Exception {
        // Initialize the database
        parkingSpaceImageRepository.saveAndFlush(parkingSpaceImage);

		int databaseSizeBeforeDelete = parkingSpaceImageRepository.findAll().size();

        // Get the parkingSpaceImage
        restParkingSpaceImageMockMvc.perform(delete("/api/parkingSpaceImages/{id}", parkingSpaceImage.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ParkingSpaceImage> parkingSpaceImages = parkingSpaceImageRepository.findAll();
        assertThat(parkingSpaceImages).hasSize(databaseSizeBeforeDelete - 1);
    }
}
