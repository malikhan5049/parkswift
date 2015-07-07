package com.ews.parkswift.web.rest;

import com.ews.parkswift.Application;
import com.ews.parkswift.domain.AvailableParking;
import com.ews.parkswift.repository.AvailableParkingRepository;

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

import org.joda.time.LocalDate;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AvailableParkingResource REST controller.
 *
 * @see AvailableParkingResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AvailableParkingResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");


    private static final LocalDate DEFAULT_START_DATE = new LocalDate(0L);
    private static final LocalDate UPDATED_START_DATE = new LocalDate();

    private static final LocalDate DEFAULT_END_DATE = new LocalDate(0L);
    private static final LocalDate UPDATED_END_DATE = new LocalDate();

    private static final LocalTime DEFAULT_START_TIME = new LocalTime(0L, DateTimeZone.UTC);
    private static final LocalTime UPDATED_START_TIME = new LocalTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_START_TIME_STR = dateTimeFormatter.print(DEFAULT_START_TIME);

    private static final LocalTime DEFAULT_END_TIME = new LocalTime(0L, DateTimeZone.UTC);
    private static final LocalTime UPDATED_END_TIME = new LocalTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_END_TIME_STR = dateTimeFormatter.print(DEFAULT_END_TIME);
    private static final String DEFAULT_REPEAT_ON = "SAMPLE_TEXT";
    private static final String UPDATED_REPEAT_ON = "UPDATED_TEXT";

    private static final Integer DEFAULT_REPEAT_OCCURRENCES = 0;
    private static final Integer UPDATED_REPEAT_OCCURRENCES = 1;

    @Inject
    private AvailableParkingRepository availableParkingRepository;

    private MockMvc restAvailableParkingMockMvc;

    private AvailableParking availableParking;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AvailableParkingResource availableParkingResource = new AvailableParkingResource();
        ReflectionTestUtils.setField(availableParkingResource, "availableParkingRepository", availableParkingRepository);
        this.restAvailableParkingMockMvc = MockMvcBuilders.standaloneSetup(availableParkingResource).build();
    }

    @Before
    public void initTest() {
        availableParking = new AvailableParking();
        availableParking.setStartDate(DEFAULT_START_DATE);
        availableParking.setEndDate(DEFAULT_END_DATE);
        availableParking.setStartTime(DEFAULT_START_TIME);
        availableParking.setEndTime(DEFAULT_END_TIME);
        availableParking.setRepeatOn(DEFAULT_REPEAT_ON);
        availableParking.setRepeatOccurrences(DEFAULT_REPEAT_OCCURRENCES);
    }

    @Test
    @Transactional
    public void createAvailableParking() throws Exception {
        int databaseSizeBeforeCreate = availableParkingRepository.findAll().size();

        // Create the AvailableParking
        restAvailableParkingMockMvc.perform(post("/api/availableParkings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(availableParking)))
                .andExpect(status().isCreated());

        // Validate the AvailableParking in the database
        List<AvailableParking> availableParkings = availableParkingRepository.findAll();
        assertThat(availableParkings).hasSize(databaseSizeBeforeCreate + 1);
        AvailableParking testAvailableParking = availableParkings.get(availableParkings.size() - 1);
        assertThat(testAvailableParking.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testAvailableParking.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testAvailableParking.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testAvailableParking.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testAvailableParking.getRepeatOn()).isEqualTo(DEFAULT_REPEAT_ON);
        assertThat(testAvailableParking.getRepeatOccurrences()).isEqualTo(DEFAULT_REPEAT_OCCURRENCES);
    }

    @Test
    @Transactional
    public void checkStartDateIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(availableParkingRepository.findAll()).hasSize(0);
        // set the field null
        availableParking.setStartDate(null);

        // Create the AvailableParking, which fails.
        restAvailableParkingMockMvc.perform(post("/api/availableParkings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(availableParking)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<AvailableParking> availableParkings = availableParkingRepository.findAll();
        assertThat(availableParkings).hasSize(0);
    }

    @Test
    @Transactional
    public void checkEndDateIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(availableParkingRepository.findAll()).hasSize(0);
        // set the field null
        availableParking.setEndDate(null);

        // Create the AvailableParking, which fails.
        restAvailableParkingMockMvc.perform(post("/api/availableParkings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(availableParking)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<AvailableParking> availableParkings = availableParkingRepository.findAll();
        assertThat(availableParkings).hasSize(0);
    }

    @Test
    @Transactional
    public void checkStartTimeIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(availableParkingRepository.findAll()).hasSize(0);
        // set the field null
        availableParking.setStartTime(null);

        // Create the AvailableParking, which fails.
        restAvailableParkingMockMvc.perform(post("/api/availableParkings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(availableParking)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<AvailableParking> availableParkings = availableParkingRepository.findAll();
        assertThat(availableParkings).hasSize(0);
    }

    @Test
    @Transactional
    public void checkEndTimeIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(availableParkingRepository.findAll()).hasSize(0);
        // set the field null
        availableParking.setEndTime(null);

        // Create the AvailableParking, which fails.
        restAvailableParkingMockMvc.perform(post("/api/availableParkings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(availableParking)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<AvailableParking> availableParkings = availableParkingRepository.findAll();
        assertThat(availableParkings).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllAvailableParkings() throws Exception {
        // Initialize the database
        availableParkingRepository.saveAndFlush(availableParking);

        // Get all the availableParkings
        restAvailableParkingMockMvc.perform(get("/api/availableParkings"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(availableParking.getId().intValue())))
                .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
                .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
                .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME_STR)))
                .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME_STR)))
                .andExpect(jsonPath("$.[*].repeatOn").value(hasItem(DEFAULT_REPEAT_ON.toString())))
                .andExpect(jsonPath("$.[*].repeatOccurrences").value(hasItem(DEFAULT_REPEAT_OCCURRENCES)));
    }

    @Test
    @Transactional
    public void getAvailableParking() throws Exception {
        // Initialize the database
        availableParkingRepository.saveAndFlush(availableParking);

        // Get the availableParking
        restAvailableParkingMockMvc.perform(get("/api/availableParkings/{id}", availableParking.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(availableParking.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME_STR))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME_STR))
            .andExpect(jsonPath("$.repeatOn").value(DEFAULT_REPEAT_ON.toString()))
            .andExpect(jsonPath("$.repeatOccurrences").value(DEFAULT_REPEAT_OCCURRENCES));
    }

    @Test
    @Transactional
    public void getNonExistingAvailableParking() throws Exception {
        // Get the availableParking
        restAvailableParkingMockMvc.perform(get("/api/availableParkings/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAvailableParking() throws Exception {
        // Initialize the database
        availableParkingRepository.saveAndFlush(availableParking);

		int databaseSizeBeforeUpdate = availableParkingRepository.findAll().size();

        // Update the availableParking
        availableParking.setStartDate(UPDATED_START_DATE);
        availableParking.setEndDate(UPDATED_END_DATE);
        availableParking.setStartTime(UPDATED_START_TIME);
        availableParking.setEndTime(UPDATED_END_TIME);
        availableParking.setRepeatOn(UPDATED_REPEAT_ON);
        availableParking.setRepeatOccurrences(UPDATED_REPEAT_OCCURRENCES);
        restAvailableParkingMockMvc.perform(put("/api/availableParkings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(availableParking)))
                .andExpect(status().isOk());

        // Validate the AvailableParking in the database
        List<AvailableParking> availableParkings = availableParkingRepository.findAll();
        assertThat(availableParkings).hasSize(databaseSizeBeforeUpdate);
        AvailableParking testAvailableParking = availableParkings.get(availableParkings.size() - 1);
        assertThat(testAvailableParking.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testAvailableParking.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testAvailableParking.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testAvailableParking.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testAvailableParking.getRepeatOn()).isEqualTo(UPDATED_REPEAT_ON);
        assertThat(testAvailableParking.getRepeatOccurrences()).isEqualTo(UPDATED_REPEAT_OCCURRENCES);
    }

    @Test
    @Transactional
    public void deleteAvailableParking() throws Exception {
        // Initialize the database
        availableParkingRepository.saveAndFlush(availableParking);

		int databaseSizeBeforeDelete = availableParkingRepository.findAll().size();

        // Get the availableParking
        restAvailableParkingMockMvc.perform(delete("/api/availableParkings/{id}", availableParking.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<AvailableParking> availableParkings = availableParkingRepository.findAll();
        assertThat(availableParkings).hasSize(databaseSizeBeforeDelete - 1);
    }
}
