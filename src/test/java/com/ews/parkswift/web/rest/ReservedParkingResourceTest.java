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

    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";

    private static final LocalDate DEFAULT_DATE_START = new LocalDate(0L);
    private static final LocalDate UPDATED_DATE_START = new LocalDate();

    private static final LocalDate DEFAULT_DATE_END = new LocalDate(0L);
    private static final LocalDate UPDATED_DATE_END = new LocalDate();

    private static final DateTime DEFAULT_TIME_START = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_TIME_START = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_TIME_START_STR = dateTimeFormatter.print(DEFAULT_TIME_START);

    private static final DateTime DEFAULT_TIME_END = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_TIME_END = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_TIME_END_STR = dateTimeFormatter.print(DEFAULT_TIME_END);
    private static final String DEFAULT_REPEAT_BASIS = "SAMPLE_TEXT";
    private static final String UPDATED_REPEAT_BASIS = "UPDATED_TEXT";

    private static final Integer DEFAULT_REPEAT_OCCURRENCES = 0;
    private static final Integer UPDATED_REPEAT_OCCURRENCES = 1;
    private static final String DEFAULT_STATUS = "SAMPLE_TEXT";
    private static final String UPDATED_STATUS = "UPDATED_TEXT";

    private static final DateTime DEFAULT_RESERVED_ON = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_RESERVED_ON = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_RESERVED_ON_STR = dateTimeFormatter.print(DEFAULT_RESERVED_ON);

    private static final Integer DEFAULT_PARENT_ID = 0;
    private static final Integer UPDATED_PARENT_ID = 1;

    private static final DateTime DEFAULT_CREATED_AT = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_CREATED_AT = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_CREATED_AT_STR = dateTimeFormatter.print(DEFAULT_CREATED_AT);

    private static final DateTime DEFAULT_MODIFIED_AT = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_MODIFIED_AT = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_MODIFIED_AT_STR = dateTimeFormatter.print(DEFAULT_MODIFIED_AT);

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
        reservedParking.setDescription(DEFAULT_DESCRIPTION);
        reservedParking.setDateStart(DEFAULT_DATE_START);
        reservedParking.setDateEnd(DEFAULT_DATE_END);
        reservedParking.setTimeStart(DEFAULT_TIME_START);
        reservedParking.setTimeEnd(DEFAULT_TIME_END);
        reservedParking.setRepeatBasis(DEFAULT_REPEAT_BASIS);
        reservedParking.setRepeatOccurrences(DEFAULT_REPEAT_OCCURRENCES);
        reservedParking.setStatus(DEFAULT_STATUS);
        reservedParking.setReservedOn(DEFAULT_RESERVED_ON);
        reservedParking.setParentId(DEFAULT_PARENT_ID);
        reservedParking.setCreatedAt(DEFAULT_CREATED_AT);
        reservedParking.setModifiedAt(DEFAULT_MODIFIED_AT);
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
        assertThat(testReservedParking.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testReservedParking.getDateStart()).isEqualTo(DEFAULT_DATE_START);
        assertThat(testReservedParking.getDateEnd()).isEqualTo(DEFAULT_DATE_END);
        assertThat(testReservedParking.getTimeStart().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_TIME_START);
        assertThat(testReservedParking.getTimeEnd().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_TIME_END);
        assertThat(testReservedParking.getRepeatBasis()).isEqualTo(DEFAULT_REPEAT_BASIS);
        assertThat(testReservedParking.getRepeatOccurrences()).isEqualTo(DEFAULT_REPEAT_OCCURRENCES);
        assertThat(testReservedParking.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testReservedParking.getReservedOn().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_RESERVED_ON);
        assertThat(testReservedParking.getParentId()).isEqualTo(DEFAULT_PARENT_ID);
        assertThat(testReservedParking.getCreatedAt().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testReservedParking.getModifiedAt().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_MODIFIED_AT);
    }

    @Test
    @Transactional
    public void checkDateStartIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(reservedParkingRepository.findAll()).hasSize(0);
        // set the field null
        reservedParking.setDateStart(null);

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
    public void checkDateEndIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(reservedParkingRepository.findAll()).hasSize(0);
        // set the field null
        reservedParking.setDateEnd(null);

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
    public void checkTimeStartIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(reservedParkingRepository.findAll()).hasSize(0);
        // set the field null
        reservedParking.setTimeStart(null);

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
    public void checkTimeEndIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(reservedParkingRepository.findAll()).hasSize(0);
        // set the field null
        reservedParking.setTimeEnd(null);

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
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].dateStart").value(hasItem(DEFAULT_DATE_START.toString())))
                .andExpect(jsonPath("$.[*].dateEnd").value(hasItem(DEFAULT_DATE_END.toString())))
                .andExpect(jsonPath("$.[*].timeStart").value(hasItem(DEFAULT_TIME_START_STR)))
                .andExpect(jsonPath("$.[*].timeEnd").value(hasItem(DEFAULT_TIME_END_STR)))
                .andExpect(jsonPath("$.[*].repeatBasis").value(hasItem(DEFAULT_REPEAT_BASIS.toString())))
                .andExpect(jsonPath("$.[*].repeatOccurrences").value(hasItem(DEFAULT_REPEAT_OCCURRENCES)))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].reservedOn").value(hasItem(DEFAULT_RESERVED_ON_STR)))
                .andExpect(jsonPath("$.[*].parentId").value(hasItem(DEFAULT_PARENT_ID)))
                .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT_STR)))
                .andExpect(jsonPath("$.[*].modifiedAt").value(hasItem(DEFAULT_MODIFIED_AT_STR)));
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
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.dateStart").value(DEFAULT_DATE_START.toString()))
            .andExpect(jsonPath("$.dateEnd").value(DEFAULT_DATE_END.toString()))
            .andExpect(jsonPath("$.timeStart").value(DEFAULT_TIME_START_STR))
            .andExpect(jsonPath("$.timeEnd").value(DEFAULT_TIME_END_STR))
            .andExpect(jsonPath("$.repeatBasis").value(DEFAULT_REPEAT_BASIS.toString()))
            .andExpect(jsonPath("$.repeatOccurrences").value(DEFAULT_REPEAT_OCCURRENCES))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.reservedOn").value(DEFAULT_RESERVED_ON_STR))
            .andExpect(jsonPath("$.parentId").value(DEFAULT_PARENT_ID))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT_STR))
            .andExpect(jsonPath("$.modifiedAt").value(DEFAULT_MODIFIED_AT_STR));
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
        reservedParking.setDescription(UPDATED_DESCRIPTION);
        reservedParking.setDateStart(UPDATED_DATE_START);
        reservedParking.setDateEnd(UPDATED_DATE_END);
        reservedParking.setTimeStart(UPDATED_TIME_START);
        reservedParking.setTimeEnd(UPDATED_TIME_END);
        reservedParking.setRepeatBasis(UPDATED_REPEAT_BASIS);
        reservedParking.setRepeatOccurrences(UPDATED_REPEAT_OCCURRENCES);
        reservedParking.setStatus(UPDATED_STATUS);
        reservedParking.setReservedOn(UPDATED_RESERVED_ON);
        reservedParking.setParentId(UPDATED_PARENT_ID);
        reservedParking.setCreatedAt(UPDATED_CREATED_AT);
        reservedParking.setModifiedAt(UPDATED_MODIFIED_AT);
        restReservedParkingMockMvc.perform(put("/api/reservedParkings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(reservedParking)))
                .andExpect(status().isOk());

        // Validate the ReservedParking in the database
        List<ReservedParking> reservedParkings = reservedParkingRepository.findAll();
        assertThat(reservedParkings).hasSize(databaseSizeBeforeUpdate);
        ReservedParking testReservedParking = reservedParkings.get(reservedParkings.size() - 1);
        assertThat(testReservedParking.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testReservedParking.getDateStart()).isEqualTo(UPDATED_DATE_START);
        assertThat(testReservedParking.getDateEnd()).isEqualTo(UPDATED_DATE_END);
        assertThat(testReservedParking.getTimeStart().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_TIME_START);
        assertThat(testReservedParking.getTimeEnd().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_TIME_END);
        assertThat(testReservedParking.getRepeatBasis()).isEqualTo(UPDATED_REPEAT_BASIS);
        assertThat(testReservedParking.getRepeatOccurrences()).isEqualTo(UPDATED_REPEAT_OCCURRENCES);
        assertThat(testReservedParking.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testReservedParking.getReservedOn().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_RESERVED_ON);
        assertThat(testReservedParking.getParentId()).isEqualTo(UPDATED_PARENT_ID);
        assertThat(testReservedParking.getCreatedAt().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testReservedParking.getModifiedAt().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_MODIFIED_AT);
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
