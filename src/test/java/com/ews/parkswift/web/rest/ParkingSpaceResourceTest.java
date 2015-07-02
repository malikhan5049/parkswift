package com.ews.parkswift.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.ews.parkswift.Application;
import com.ews.parkswift.domain.ParkingLocation;
import com.ews.parkswift.domain.ParkingSpace;
import com.ews.parkswift.domain.ParkingSpacePriceEntry;
import com.ews.parkswift.domain.ParkingSpaceVehicleType;
import com.ews.parkswift.repository.ParkingSpaceRepository;
import com.ews.parkswift.service.ParkingSpaceService;

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

    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";
    private static final Integer DEFAULT_NUMBER_OF_SPACES = 1;
    private static final Integer UPDATED_NUMBER_OF_SPACES = 2;

    private static final Boolean DEFAULT_GROUP_RECORD = false;
    private static final Boolean UPDATED_GROUP_RECORD = true;

    private static final Integer DEFAULT_GROUP_NUMBER = 0;
    private static final Integer UPDATED_GROUP_NUMBER = 1;

    private static final Boolean DEFAULT_FULL_RESERVED = false;
    private static final Boolean UPDATED_FULL_RESERVED = true;

    @Inject
    private ParkingSpaceRepository parkingSpaceRepository;
    @Inject
    private ParkingSpaceService parkingSpaceService;

    private MockMvc restParkingSpaceMockMvc;

    private ParkingSpace parkingSpace;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ParkingSpaceResource parkingSpaceResource = new ParkingSpaceResource();
        ReflectionTestUtils.setField(parkingSpaceResource, "parkingSpaceRepository", parkingSpaceRepository);
        ReflectionTestUtils.setField(parkingSpaceResource, "parkingSpaceService", parkingSpaceService);
        this.restParkingSpaceMockMvc = MockMvcBuilders.standaloneSetup(parkingSpaceResource).build();
    }

    @Before
    public void initTest() {
        parkingSpace = new ParkingSpace();
        parkingSpace.setDescription(DEFAULT_DESCRIPTION);
        parkingSpace.setNumberOfSpaces(DEFAULT_NUMBER_OF_SPACES);
        parkingSpace.setGroupRecord(DEFAULT_GROUP_RECORD);
        parkingSpace.setGroupNumber(DEFAULT_GROUP_NUMBER);
        parkingSpace.setFullReserved(DEFAULT_FULL_RESERVED);
        parkingSpace.setParkingLocation(new ParkingLocation(){{setId(1L);}});
        parkingSpace.setParkingSpacePriceEntrys(new HashSet<ParkingSpacePriceEntry>(){{add(new ParkingSpacePriceEntry(){{setType("Day Hour");setPrice(BigDecimal.valueOf(5));}});}});
        parkingSpace.setParkingSpaceVehicleTypes(new HashSet<ParkingSpaceVehicleType>(){{add(new ParkingSpaceVehicleType(){{setType("car");}});}});
    }

    @Test
    @Transactional
    public void createParkingSpace() throws Exception {
        int databaseSizeBeforeCreate = parkingSpaceRepository.findAll().size();
        InputStream development_ribbon_image_is = new FileInputStream(new File("src/main/webapp/assets/images/development_ribbon.png"));
        InputStream hipster_image_is = new FileInputStream(new File("src/main/webapp/assets/images/hipster.png"));
        MockMultipartFile firstFile = new MockMultipartFile("images", "development_ribbon.png", "image/png", development_ribbon_image_is);
        MockMultipartFile secondFile = new MockMultipartFile("images", "hipster.png", "image/png", hipster_image_is);
        MockMultipartFile jsonFile = new MockMultipartFile("json", "", "application/json", "{\"description\": \"asdf\",\"parkingLocation\":{\"id\":\"1\"}}".getBytes());
        // Create the ParkingSpace
        restParkingSpaceMockMvc.perform(MockMvcRequestBuilders.fileUpload("/api/parkingSpaces")
                .file(firstFile).file(secondFile).file(jsonFile))
                .andExpect(status().isCreated());

        // Validate the ParkingSpace in the database
        List<ParkingSpace> parkingSpaces = parkingSpaceRepository.findAll();
        assertThat(parkingSpaces).hasSize(databaseSizeBeforeCreate + 1);
        ParkingSpace testParkingSpace = parkingSpaces.get(parkingSpaces.size() - 1);
        assertThat(testParkingSpace.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testParkingSpace.getNumberOfSpaces()).isEqualTo(DEFAULT_NUMBER_OF_SPACES);
        assertThat(testParkingSpace.getGroupRecord()).isEqualTo(DEFAULT_GROUP_RECORD);
        assertThat(testParkingSpace.getGroupNumber()).isEqualTo(DEFAULT_GROUP_NUMBER);
        assertThat(testParkingSpace.getFullReserved()).isEqualTo(DEFAULT_FULL_RESERVED);
    }

    @Test
    @Transactional
    public void checkNumberOfSpacesIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(parkingSpaceRepository.findAll()).hasSize(0);
        // set the field null
        parkingSpace.setNumberOfSpaces(null);

        // Create the ParkingSpace, which fails.
        restParkingSpaceMockMvc.perform(post("/api/parkingSpaces")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(parkingSpace)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<ParkingSpace> parkingSpaces = parkingSpaceRepository.findAll();
        assertThat(parkingSpaces).hasSize(0);
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
                .andExpect(jsonPath("$.[*].numberOfSpaces").value(hasItem(DEFAULT_NUMBER_OF_SPACES.toString())))
                .andExpect(jsonPath("$.[*].groupRecord").value(hasItem(DEFAULT_GROUP_RECORD.booleanValue())))
                .andExpect(jsonPath("$.[*].groupNumber").value(hasItem(DEFAULT_GROUP_NUMBER)))
                .andExpect(jsonPath("$.[*].fullReserved").value(hasItem(DEFAULT_FULL_RESERVED.booleanValue())));
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
            .andExpect(jsonPath("$.numberOfSpaces").value(DEFAULT_NUMBER_OF_SPACES.toString()))
            .andExpect(jsonPath("$.groupRecord").value(DEFAULT_GROUP_RECORD.booleanValue()))
            .andExpect(jsonPath("$.groupNumber").value(DEFAULT_GROUP_NUMBER))
            .andExpect(jsonPath("$.fullReserved").value(DEFAULT_FULL_RESERVED.booleanValue()));
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
        parkingSpace.setNumberOfSpaces(UPDATED_NUMBER_OF_SPACES);
        parkingSpace.setGroupRecord(UPDATED_GROUP_RECORD);
        parkingSpace.setGroupNumber(UPDATED_GROUP_NUMBER);
        parkingSpace.setFullReserved(UPDATED_FULL_RESERVED);
        restParkingSpaceMockMvc.perform(put("/api/parkingSpaces")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(parkingSpace)))
                .andExpect(status().isOk());

        // Validate the ParkingSpace in the database
        List<ParkingSpace> parkingSpaces = parkingSpaceRepository.findAll();
        assertThat(parkingSpaces).hasSize(databaseSizeBeforeUpdate);
        ParkingSpace testParkingSpace = parkingSpaces.get(parkingSpaces.size() - 1);
        assertThat(testParkingSpace.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testParkingSpace.getNumberOfSpaces()).isEqualTo(UPDATED_NUMBER_OF_SPACES);
        assertThat(testParkingSpace.getGroupRecord()).isEqualTo(UPDATED_GROUP_RECORD);
        assertThat(testParkingSpace.getGroupNumber()).isEqualTo(UPDATED_GROUP_NUMBER);
        assertThat(testParkingSpace.getFullReserved()).isEqualTo(UPDATED_FULL_RESERVED);
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
