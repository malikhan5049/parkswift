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
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";

    private static final DateTime DEFAULT_CREATED_AT = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_CREATED_AT = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_CREATED_AT_STR = dateTimeFormatter.print(DEFAULT_CREATED_AT);

    private static final DateTime DEFAULT_MODIFIED_AT = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_MODIFIED_AT = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_MODIFIED_AT_STR = dateTimeFormatter.print(DEFAULT_MODIFIED_AT);

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
        availableParking.setDateStart(DEFAULT_DATE_START);
        availableParking.setDateEnd(DEFAULT_DATE_END);
        availableParking.setTimeStart(DEFAULT_TIME_START);
        availableParking.setTimeEnd(DEFAULT_TIME_END);
        availableParking.setRepeatBasis(DEFAULT_REPEAT_BASIS);
        availableParking.setRepeatOccurrences(DEFAULT_REPEAT_OCCURRENCES);
        availableParking.setDescription(DEFAULT_DESCRIPTION);
        availableParking.setCreatedAt(DEFAULT_CREATED_AT);
        availableParking.setModifiedAt(DEFAULT_MODIFIED_AT);
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
        assertThat(testAvailableParking.getDateStart()).isEqualTo(DEFAULT_DATE_START);
        assertThat(testAvailableParking.getDateEnd()).isEqualTo(DEFAULT_DATE_END);
        assertThat(testAvailableParking.getTimeStart().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_TIME_START);
        assertThat(testAvailableParking.getTimeEnd().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_TIME_END);
        assertThat(testAvailableParking.getRepeatBasis()).isEqualTo(DEFAULT_REPEAT_BASIS);
        assertThat(testAvailableParking.getRepeatOccurrences()).isEqualTo(DEFAULT_REPEAT_OCCURRENCES);
        assertThat(testAvailableParking.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAvailableParking.getCreatedAt().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testAvailableParking.getModifiedAt().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_MODIFIED_AT);
    }

    @Test
    @Transactional
    public void checkDateStartIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(availableParkingRepository.findAll()).hasSize(0);
        // set the field null
        availableParking.setDateStart(null);

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
    public void checkDateEndIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(availableParkingRepository.findAll()).hasSize(0);
        // set the field null
        availableParking.setDateEnd(null);

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
    public void checkTimeStartIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(availableParkingRepository.findAll()).hasSize(0);
        // set the field null
        availableParking.setTimeStart(null);

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
    public void checkTimeEndIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(availableParkingRepository.findAll()).hasSize(0);
        // set the field null
        availableParking.setTimeEnd(null);

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
                .andExpect(jsonPath("$.[*].dateStart").value(hasItem(DEFAULT_DATE_START.toString())))
                .andExpect(jsonPath("$.[*].dateEnd").value(hasItem(DEFAULT_DATE_END.toString())))
                .andExpect(jsonPath("$.[*].timeStart").value(hasItem(DEFAULT_TIME_START_STR)))
                .andExpect(jsonPath("$.[*].timeEnd").value(hasItem(DEFAULT_TIME_END_STR)))
                .andExpect(jsonPath("$.[*].repeatBasis").value(hasItem(DEFAULT_REPEAT_BASIS.toString())))
                .andExpect(jsonPath("$.[*].repeatOccurrences").value(hasItem(DEFAULT_REPEAT_OCCURRENCES)))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT_STR)))
                .andExpect(jsonPath("$.[*].modifiedAt").value(hasItem(DEFAULT_MODIFIED_AT_STR)));
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
            .andExpect(jsonPath("$.dateStart").value(DEFAULT_DATE_START.toString()))
            .andExpect(jsonPath("$.dateEnd").value(DEFAULT_DATE_END.toString()))
            .andExpect(jsonPath("$.timeStart").value(DEFAULT_TIME_START_STR))
            .andExpect(jsonPath("$.timeEnd").value(DEFAULT_TIME_END_STR))
            .andExpect(jsonPath("$.repeatBasis").value(DEFAULT_REPEAT_BASIS.toString()))
            .andExpect(jsonPath("$.repeatOccurrences").value(DEFAULT_REPEAT_OCCURRENCES))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT_STR))
            .andExpect(jsonPath("$.modifiedAt").value(DEFAULT_MODIFIED_AT_STR));
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
        availableParking.setDateStart(UPDATED_DATE_START);
        availableParking.setDateEnd(UPDATED_DATE_END);
        availableParking.setTimeStart(UPDATED_TIME_START);
        availableParking.setTimeEnd(UPDATED_TIME_END);
        availableParking.setRepeatBasis(UPDATED_REPEAT_BASIS);
        availableParking.setRepeatOccurrences(UPDATED_REPEAT_OCCURRENCES);
        availableParking.setDescription(UPDATED_DESCRIPTION);
        availableParking.setCreatedAt(UPDATED_CREATED_AT);
        availableParking.setModifiedAt(UPDATED_MODIFIED_AT);
        restAvailableParkingMockMvc.perform(put("/api/availableParkings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(availableParking)))
                .andExpect(status().isOk());

        // Validate the AvailableParking in the database
        List<AvailableParking> availableParkings = availableParkingRepository.findAll();
        assertThat(availableParkings).hasSize(databaseSizeBeforeUpdate);
        AvailableParking testAvailableParking = availableParkings.get(availableParkings.size() - 1);
        assertThat(testAvailableParking.getDateStart()).isEqualTo(UPDATED_DATE_START);
        assertThat(testAvailableParking.getDateEnd()).isEqualTo(UPDATED_DATE_END);
        assertThat(testAvailableParking.getTimeStart().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_TIME_START);
        assertThat(testAvailableParking.getTimeEnd().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_TIME_END);
        assertThat(testAvailableParking.getRepeatBasis()).isEqualTo(UPDATED_REPEAT_BASIS);
        assertThat(testAvailableParking.getRepeatOccurrences()).isEqualTo(UPDATED_REPEAT_OCCURRENCES);
        assertThat(testAvailableParking.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAvailableParking.getCreatedAt().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAvailableParking.getModifiedAt().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_MODIFIED_AT);
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
