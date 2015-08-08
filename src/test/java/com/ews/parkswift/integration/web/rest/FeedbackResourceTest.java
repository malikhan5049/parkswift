package com.ews.parkswift.integration.web.rest;

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

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
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
import com.ews.parkswift.config.Constants;
import com.ews.parkswift.domain.Feedback;
import com.ews.parkswift.repository.FeedbackRepository;
import com.ews.parkswift.web.rest.FeedbackResource;

/**
 * Test class for the FeedbackResource REST controller.
 *
 * @see FeedbackResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class FeedbackResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = Constants.DATETIMEFORMATTER;


    private static final Integer DEFAULT_RATING = 0;
    private static final Integer UPDATED_RATING = 1;
    private static final String DEFAULT_COMMENTS = "SAMPLE_TEXT";
    private static final String UPDATED_COMMENTS = "UPDATED_TEXT";

    private static final DateTime DEFAULT_POSTED_ON = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_POSTED_ON = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_POSTED_ON_STR = dateTimeFormatter.print(DEFAULT_POSTED_ON);

    private static final Long DEFAULT_FEEDBACK_BY = 0L;
    private static final Long UPDATED_FEEDBACK_BY = 1L;

    @Inject
    private FeedbackRepository feedbackRepository;

    private MockMvc restFeedbackMockMvc;

    private Feedback feedback;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FeedbackResource feedbackResource = new FeedbackResource();
        ReflectionTestUtils.setField(feedbackResource, "feedbackRepository", feedbackRepository);
        this.restFeedbackMockMvc = MockMvcBuilders.standaloneSetup(feedbackResource).build();
    }

    @Before
    public void initTest() {
        feedback = new Feedback();
        feedback.setRating(DEFAULT_RATING);
        feedback.setComments(DEFAULT_COMMENTS);
        feedback.setPosted_on(DEFAULT_POSTED_ON);
        feedback.setFeedback_by(DEFAULT_FEEDBACK_BY);
    }

    @Test
    @Transactional
    public void createFeedback() throws Exception {
        int databaseSizeBeforeCreate = feedbackRepository.findAll().size();

        // Create the Feedback
        restFeedbackMockMvc.perform(post("/api/feedbacks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(feedback)))
                .andExpect(status().isCreated());

        // Validate the Feedback in the database
        List<Feedback> feedbacks = feedbackRepository.findAll();
        assertThat(feedbacks).hasSize(databaseSizeBeforeCreate + 1);
        Feedback testFeedback = feedbacks.get(feedbacks.size() - 1);
        assertThat(testFeedback.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testFeedback.getComments()).isEqualTo(DEFAULT_COMMENTS);
        assertThat(testFeedback.getPosted_on().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_POSTED_ON);
        assertThat(testFeedback.getFeedback_by()).isEqualTo(DEFAULT_FEEDBACK_BY);
    }

    @Test
    @Transactional
    public void checkRatingIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(feedbackRepository.findAll()).hasSize(0);
        // set the field null
        feedback.setRating(null);

        // Create the Feedback, which fails.
        restFeedbackMockMvc.perform(post("/api/feedbacks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(feedback)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Feedback> feedbacks = feedbackRepository.findAll();
        assertThat(feedbacks).hasSize(0);
    }

    @Test
    @Transactional
    public void checkCommentsIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(feedbackRepository.findAll()).hasSize(0);
        // set the field null
        feedback.setComments(null);

        // Create the Feedback, which fails.
        restFeedbackMockMvc.perform(post("/api/feedbacks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(feedback)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Feedback> feedbacks = feedbackRepository.findAll();
        assertThat(feedbacks).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllFeedbacks() throws Exception {
        // Initialize the database
        feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbacks
        restFeedbackMockMvc.perform(get("/api/feedbacks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(feedback.getId().intValue())))
                .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
                .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS.toString())))
                .andExpect(jsonPath("$.[*].posted_on").value(hasItem(DEFAULT_POSTED_ON_STR)))
                .andExpect(jsonPath("$.[*].feedback_by").value(hasItem(DEFAULT_FEEDBACK_BY.intValue())));
    }

    @Test
    @Transactional
    public void getFeedback() throws Exception {
        // Initialize the database
        feedbackRepository.saveAndFlush(feedback);

        // Get the feedback
        restFeedbackMockMvc.perform(get("/api/feedbacks/{id}", feedback.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(feedback.getId().intValue()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS.toString()))
            .andExpect(jsonPath("$.posted_on").value(DEFAULT_POSTED_ON_STR))
            .andExpect(jsonPath("$.feedback_by").value(DEFAULT_FEEDBACK_BY.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingFeedback() throws Exception {
        // Get the feedback
        restFeedbackMockMvc.perform(get("/api/feedbacks/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFeedback() throws Exception {
        // Initialize the database
        feedbackRepository.saveAndFlush(feedback);

		int databaseSizeBeforeUpdate = feedbackRepository.findAll().size();

        // Update the feedback
        feedback.setRating(UPDATED_RATING);
        feedback.setComments(UPDATED_COMMENTS);
        feedback.setPosted_on(UPDATED_POSTED_ON);
        feedback.setFeedback_by(UPDATED_FEEDBACK_BY);
        restFeedbackMockMvc.perform(put("/api/feedbacks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(feedback)))
                .andExpect(status().isOk());

        // Validate the Feedback in the database
        List<Feedback> feedbacks = feedbackRepository.findAll();
        assertThat(feedbacks).hasSize(databaseSizeBeforeUpdate);
        Feedback testFeedback = feedbacks.get(feedbacks.size() - 1);
        assertThat(testFeedback.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testFeedback.getComments()).isEqualTo(UPDATED_COMMENTS);
        assertThat(testFeedback.getPosted_on().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_POSTED_ON);
        assertThat(testFeedback.getFeedback_by()).isEqualTo(UPDATED_FEEDBACK_BY);
    }

    @Test
    @Transactional
    public void deleteFeedback() throws Exception {
        // Initialize the database
        feedbackRepository.saveAndFlush(feedback);

		int databaseSizeBeforeDelete = feedbackRepository.findAll().size();

        // Get the feedback
        restFeedbackMockMvc.perform(delete("/api/feedbacks/{id}", feedback.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Feedback> feedbacks = feedbackRepository.findAll();
        assertThat(feedbacks).hasSize(databaseSizeBeforeDelete - 1);
    }
}
