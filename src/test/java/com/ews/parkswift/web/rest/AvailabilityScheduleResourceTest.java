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

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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

import com.ews.parkswift.Application;
import com.ews.parkswift.domain.AvailabilitySchedule;
import com.ews.parkswift.repository.AvailabilityScheduleRepository;

/**
 * Test class for the availabilityScheduleResource REST controller.
 *
 * @see AvailabilityScheduleResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AvailabilityScheduleResourceTest {

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
    private AvailabilityScheduleRepository availabilityScheduleRepository;

    private MockMvc restavailabilityScheduleMockMvc;

    private AvailabilitySchedule availabilitySchedule;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AvailabilityScheduleResource availabilityScheduleResource = new AvailabilityScheduleResource();
        ReflectionTestUtils.setField(availabilityScheduleResource, "availabilityScheduleRepository", availabilityScheduleRepository);
        this.restavailabilityScheduleMockMvc = MockMvcBuilders.standaloneSetup(availabilityScheduleResource).build();
    }

    @Before
    public void initTest() {
        availabilitySchedule = new AvailabilitySchedule();
        availabilitySchedule.setStartDate(DEFAULT_START_DATE);
        availabilitySchedule.setEndDate(DEFAULT_END_DATE);
        availabilitySchedule.setStartTime(DEFAULT_START_TIME);
        availabilitySchedule.setEndTime(DEFAULT_END_TIME);
        availabilitySchedule.setRepeatBasis(DEFAULT_REPEAT_ON);
        availabilitySchedule.setRepeatEndOccurrences(DEFAULT_REPEAT_OCCURRENCES);
    }

    @Test
    @Transactional
    public void createavailabilitySchedule() throws Exception {
        int databaseSizeBeforeCreate = availabilityScheduleRepository.findAll().size();

        // Create the availabilitySchedule
        restavailabilityScheduleMockMvc.perform(post("/api/availabilitySchedules")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(availabilitySchedule)))
                .andExpect(status().isCreated());

        // Validate the availabilitySchedule in the database
        List<AvailabilitySchedule> availabilitySchedules = availabilityScheduleRepository.findAll();
        assertThat(availabilitySchedules).hasSize(databaseSizeBeforeCreate + 1);
        AvailabilitySchedule testavailabilitySchedule = availabilitySchedules.get(availabilitySchedules.size() - 1);
        assertThat(testavailabilitySchedule.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testavailabilitySchedule.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testavailabilitySchedule.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testavailabilitySchedule.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testavailabilitySchedule.getRepeatBasis()).isEqualTo(DEFAULT_REPEAT_ON);
        assertThat(testavailabilitySchedule.getRepeatEndOccurrences()).isEqualTo(DEFAULT_REPEAT_OCCURRENCES);
    }

    @Test
    @Transactional
    public void checkStartDateIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(availabilityScheduleRepository.findAll()).hasSize(0);
        // set the field null
        availabilitySchedule.setStartDate(null);

        // Create the availabilitySchedule, which fails.
        restavailabilityScheduleMockMvc.perform(post("/api/availabilitySchedules")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(availabilitySchedule)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<AvailabilitySchedule> availabilitySchedules = availabilityScheduleRepository.findAll();
        assertThat(availabilitySchedules).hasSize(0);
    }

    @Test
    @Transactional
    public void checkEndDateIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(availabilityScheduleRepository.findAll()).hasSize(0);
        // set the field null
        availabilitySchedule.setEndDate(null);

        // Create the availabilitySchedule, which fails.
        restavailabilityScheduleMockMvc.perform(post("/api/availabilitySchedules")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(availabilitySchedule)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<AvailabilitySchedule> availabilitySchedules = availabilityScheduleRepository.findAll();
        assertThat(availabilitySchedules).hasSize(0);
    }

    @Test
    @Transactional
    public void checkStartTimeIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(availabilityScheduleRepository.findAll()).hasSize(0);
        // set the field null
        availabilitySchedule.setStartTime(null);

        // Create the availabilitySchedule, which fails.
        restavailabilityScheduleMockMvc.perform(post("/api/availabilitySchedules")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(availabilitySchedule)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<AvailabilitySchedule> availabilitySchedules = availabilityScheduleRepository.findAll();
        assertThat(availabilitySchedules).hasSize(0);
    }

    @Test
    @Transactional
    public void checkEndTimeIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(availabilityScheduleRepository.findAll()).hasSize(0);
        // set the field null
        availabilitySchedule.setEndTime(null);

        // Create the availabilitySchedule, which fails.
        restavailabilityScheduleMockMvc.perform(post("/api/availabilitySchedules")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(availabilitySchedule)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<AvailabilitySchedule> availabilitySchedules = availabilityScheduleRepository.findAll();
        assertThat(availabilitySchedules).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllavailabilitySchedules() throws Exception {
        // Initialize the database
        availabilityScheduleRepository.saveAndFlush(availabilitySchedule);

        // Get all the availabilitySchedules
        restavailabilityScheduleMockMvc.perform(get("/api/availabilitySchedules"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(availabilitySchedule.getId().intValue())))
                .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
                .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
                .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME_STR)))
                .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME_STR)))
                .andExpect(jsonPath("$.[*].repeatOn").value(hasItem(DEFAULT_REPEAT_ON.toString())))
                .andExpect(jsonPath("$.[*].repeatOccurrences").value(hasItem(DEFAULT_REPEAT_OCCURRENCES)));
    }

    @Test
    @Transactional
    public void getavailabilitySchedule() throws Exception {
        // Initialize the database
        availabilityScheduleRepository.saveAndFlush(availabilitySchedule);

        // Get the availabilitySchedule
        restavailabilityScheduleMockMvc.perform(get("/api/availabilitySchedules/{id}", availabilitySchedule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(availabilitySchedule.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME_STR))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME_STR))
            .andExpect(jsonPath("$.repeatOn").value(DEFAULT_REPEAT_ON.toString()))
            .andExpect(jsonPath("$.repeatOccurrences").value(DEFAULT_REPEAT_OCCURRENCES));
    }

    @Test
    @Transactional
    public void getNonExistingavailabilitySchedule() throws Exception {
        // Get the availabilitySchedule
        restavailabilityScheduleMockMvc.perform(get("/api/availabilitySchedules/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateavailabilitySchedule() throws Exception {
        // Initialize the database
        availabilityScheduleRepository.saveAndFlush(availabilitySchedule);

		int databaseSizeBeforeUpdate = availabilityScheduleRepository.findAll().size();

        // Update the availabilitySchedule
        availabilitySchedule.setStartDate(UPDATED_START_DATE);
        availabilitySchedule.setEndDate(UPDATED_END_DATE);
        availabilitySchedule.setStartTime(UPDATED_START_TIME);
        availabilitySchedule.setEndTime(UPDATED_END_TIME);
        availabilitySchedule.setRepeatBasis(UPDATED_REPEAT_ON);
        availabilitySchedule.setRepeatEndOccurrences(UPDATED_REPEAT_OCCURRENCES);
        restavailabilityScheduleMockMvc.perform(put("/api/availabilitySchedules")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(availabilitySchedule)))
                .andExpect(status().isOk());

        // Validate the availabilitySchedule in the database
        List<AvailabilitySchedule> availabilitySchedules = availabilityScheduleRepository.findAll();
        assertThat(availabilitySchedules).hasSize(databaseSizeBeforeUpdate);
        AvailabilitySchedule testavailabilitySchedule = availabilitySchedules.get(availabilitySchedules.size() - 1);
        assertThat(testavailabilitySchedule.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testavailabilitySchedule.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testavailabilitySchedule.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testavailabilitySchedule.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testavailabilitySchedule.getRepeatBasis()).isEqualTo(UPDATED_REPEAT_ON);
        assertThat(testavailabilitySchedule.getRepeatEndOccurrences()).isEqualTo(UPDATED_REPEAT_OCCURRENCES);
    }

    @Test
    @Transactional
    public void deleteavailabilitySchedule() throws Exception {
        // Initialize the database
        availabilityScheduleRepository.saveAndFlush(availabilitySchedule);

		int databaseSizeBeforeDelete = availabilityScheduleRepository.findAll().size();

        // Get the availabilitySchedule
        restavailabilityScheduleMockMvc.perform(delete("/api/availabilitySchedules/{id}", availabilitySchedule.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<AvailabilitySchedule> availabilitySchedules = availabilityScheduleRepository.findAll();
        assertThat(availabilitySchedules).hasSize(databaseSizeBeforeDelete - 1);
    }
}
