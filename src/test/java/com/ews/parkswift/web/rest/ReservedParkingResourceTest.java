package com.ews.parkswift.web.rest;

import com.ews.parkswift.Application;
import com.ews.parkswift.domain.ReservedParking;
import com.ews.parkswift.repository.ReservedParkingRepository;

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
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ReservedParkingResource REST controller.
 *
 * @see ReservedParkingResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ReservedParkingResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");


    private static final LocalDate DEFAULT_START_DATE = new LocalDate(0L);
    private static final LocalDate UPDATED_START_DATE = new LocalDate();

    private static final LocalDate DEFAULT_END_DATE = new LocalDate(0L);
    private static final LocalDate UPDATED_END_DATE = new LocalDate();

    private static final DateTime DEFAULT_START_TIME = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_START_TIME = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_START_TIME_STR = dateTimeFormatter.print(DEFAULT_START_TIME);

    private static final DateTime DEFAULT_END_TIME = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_END_TIME = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_END_TIME_STR = dateTimeFormatter.print(DEFAULT_END_TIME);
    private static final String DEFAULT_REPEAT_ON = "SAMPLE_TEXT";
    private static final String UPDATED_REPEAT_ON = "UPDATED_TEXT";

    private static final Integer DEFAULT_REPEAT_OCCURRENCES = 0;
    private static final Integer UPDATED_REPEAT_OCCURRENCES = 1;
    private static final String DEFAULT_STATUS = "SAMPLE_TEXT";
    private static final String UPDATED_STATUS = "UPDATED_TEXT";

    private static final DateTime DEFAULT_RESERVED_ON = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_RESERVED_ON = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_RESERVED_ON_STR = dateTimeFormatter.print(DEFAULT_RESERVED_ON);

    @Inject
    private ReservedParkingRepository reservedParkingRepository;

    private MockMvc restReservedParkingMockMvc;

    private ReservedParking reservedParking;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReservedParkingResource reservedParkingResource = new ReservedParkingResource();
        ReflectionTestUtils.setField(reservedParkingResource, "reservedParkingRepository", reservedParkingRepository);
        this.restReservedParkingMockMvc = MockMvcBuilders.standaloneSetup(reservedParkingResource).build();
    }

    @Before
    public void initTest() {
        reservedParking = new ReservedParking();
        reservedParking.setStartDate(DEFAULT_START_DATE);
        reservedParking.setEndDate(DEFAULT_END_DATE);
        reservedParking.setStartTime(DEFAULT_START_TIME);
        reservedParking.setEndTime(DEFAULT_END_TIME);
        reservedParking.setRepeatOn(DEFAULT_REPEAT_ON);
        reservedParking.setRepeatOccurrences(DEFAULT_REPEAT_OCCURRENCES);
        reservedParking.setStatus(DEFAULT_STATUS);
        reservedParking.setReservedOn(DEFAULT_RESERVED_ON);
    }

    @Test
    @Transactional
    public void createReservedParking() throws Exception {
        int databaseSizeBeforeCreate = reservedParkingRepository.findAll().size();

        // Create the ReservedParking
        restReservedParkingMockMvc.perform(post("/api/reservedParkings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(reservedParking)))
                .andExpect(status().isCreated());

        // Validate the ReservedParking in the database
        List<ReservedParking> reservedParkings = reservedParkingRepository.findAll();
        assertThat(reservedParkings).hasSize(databaseSizeBeforeCreate + 1);
        ReservedParking testReservedParking = reservedParkings.get(reservedParkings.size() - 1);
        assertThat(testReservedParking.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testReservedParking.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testReservedParking.getStartTime().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_START_TIME);
        assertThat(testReservedParking.getEndTime().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_END_TIME);
        assertThat(testReservedParking.getRepeatOn()).isEqualTo(DEFAULT_REPEAT_ON);
        assertThat(testReservedParking.getRepeatOccurrences()).isEqualTo(DEFAULT_REPEAT_OCCURRENCES);
        assertThat(testReservedParking.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testReservedParking.getReservedOn().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_RESERVED_ON);
    }

    @Test
    @Transactional
    public void checkStartDateIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(reservedParkingRepository.findAll()).hasSize(0);
        // set the field null
        reservedParking.setStartDate(null);

        // Create the ReservedParking, which fails.
        restReservedParkingMockMvc.perform(post("/api/reservedParkings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(reservedParking)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<ReservedParking> reservedParkings = reservedParkingRepository.findAll();
        assertThat(reservedParkings).hasSize(0);
    }

    @Test
    @Transactional
    public void checkEndDateIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(reservedParkingRepository.findAll()).hasSize(0);
        // set the field null
        reservedParking.setEndDate(null);

        // Create the ReservedParking, which fails.
        restReservedParkingMockMvc.perform(post("/api/reservedParkings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(reservedParking)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<ReservedParking> reservedParkings = reservedParkingRepository.findAll();
        assertThat(reservedParkings).hasSize(0);
    }

    @Test
    @Transactional
    public void checkStartTimeIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(reservedParkingRepository.findAll()).hasSize(0);
        // set the field null
        reservedParking.setStartTime(null);

        // Create the ReservedParking, which fails.
        restReservedParkingMockMvc.perform(post("/api/reservedParkings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(reservedParking)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<ReservedParking> reservedParkings = reservedParkingRepository.findAll();
        assertThat(reservedParkings).hasSize(0);
    }

    @Test
    @Transactional
    public void checkEndTimeIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(reservedParkingRepository.findAll()).hasSize(0);
        // set the field null
        reservedParking.setEndTime(null);

        // Create the ReservedParking, which fails.
        restReservedParkingMockMvc.perform(post("/api/reservedParkings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(reservedParking)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<ReservedParking> reservedParkings = reservedParkingRepository.findAll();
        assertThat(reservedParkings).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllReservedParkings() throws Exception {
        // Initialize the database
        reservedParkingRepository.saveAndFlush(reservedParking);

        // Get all the reservedParkings
        restReservedParkingMockMvc.perform(get("/api/reservedParkings"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(reservedParking.getId().intValue())))
                .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
                .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
                .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME_STR)))
                .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME_STR)))
                .andExpect(jsonPath("$.[*].repeatOn").value(hasItem(DEFAULT_REPEAT_ON.toString())))
                .andExpect(jsonPath("$.[*].repeatOccurrences").value(hasItem(DEFAULT_REPEAT_OCCURRENCES)))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].reservedOn").value(hasItem(DEFAULT_RESERVED_ON_STR)));
    }

    @Test
    @Transactional
    public void getReservedParking() throws Exception {
        // Initialize the database
        reservedParkingRepository.saveAndFlush(reservedParking);

        // Get the reservedParking
        restReservedParkingMockMvc.perform(get("/api/reservedParkings/{id}", reservedParking.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(reservedParking.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME_STR))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME_STR))
            .andExpect(jsonPath("$.repeatOn").value(DEFAULT_REPEAT_ON.toString()))
            .andExpect(jsonPath("$.repeatOccurrences").value(DEFAULT_REPEAT_OCCURRENCES))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.reservedOn").value(DEFAULT_RESERVED_ON_STR));
    }

    @Test
    @Transactional
    public void getNonExistingReservedParking() throws Exception {
        // Get the reservedParking
        restReservedParkingMockMvc.perform(get("/api/reservedParkings/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReservedParking() throws Exception {
        // Initialize the database
        reservedParkingRepository.saveAndFlush(reservedParking);

		int databaseSizeBeforeUpdate = reservedParkingRepository.findAll().size();

        // Update the reservedParking
        reservedParking.setStartDate(UPDATED_START_DATE);
        reservedParking.setEndDate(UPDATED_END_DATE);
        reservedParking.setStartTime(UPDATED_START_TIME);
        reservedParking.setEndTime(UPDATED_END_TIME);
        reservedParking.setRepeatOn(UPDATED_REPEAT_ON);
        reservedParking.setRepeatOccurrences(UPDATED_REPEAT_OCCURRENCES);
        reservedParking.setStatus(UPDATED_STATUS);
        reservedParking.setReservedOn(UPDATED_RESERVED_ON);
        restReservedParkingMockMvc.perform(put("/api/reservedParkings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(reservedParking)))
                .andExpect(status().isOk());

        // Validate the ReservedParking in the database
        List<ReservedParking> reservedParkings = reservedParkingRepository.findAll();
        assertThat(reservedParkings).hasSize(databaseSizeBeforeUpdate);
        ReservedParking testReservedParking = reservedParkings.get(reservedParkings.size() - 1);
        assertThat(testReservedParking.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testReservedParking.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testReservedParking.getStartTime().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_START_TIME);
        assertThat(testReservedParking.getEndTime().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_END_TIME);
        assertThat(testReservedParking.getRepeatOn()).isEqualTo(UPDATED_REPEAT_ON);
        assertThat(testReservedParking.getRepeatOccurrences()).isEqualTo(UPDATED_REPEAT_OCCURRENCES);
        assertThat(testReservedParking.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testReservedParking.getReservedOn().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_RESERVED_ON);
    }

    @Test
    @Transactional
    public void deleteReservedParking() throws Exception {
        // Initialize the database
        reservedParkingRepository.saveAndFlush(reservedParking);

		int databaseSizeBeforeDelete = reservedParkingRepository.findAll().size();

        // Get the reservedParking
        restReservedParkingMockMvc.perform(delete("/api/reservedParkings/{id}", reservedParking.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ReservedParking> reservedParkings = reservedParkingRepository.findAll();
        assertThat(reservedParkings).hasSize(databaseSizeBeforeDelete - 1);
    }
}
