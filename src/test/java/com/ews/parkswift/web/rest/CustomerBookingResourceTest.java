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

import java.math.BigDecimal;
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
import com.ews.parkswift.domain.CustomerBooking;
import com.ews.parkswift.repository.CustomerBookingRepository;

/**
 * Test class for the CustomerBookingResource REST controller.
 *
 * @see CustomerBookingResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CustomerBookingResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = Constants.DATETIMEFORMATTER;

    private static final String DEFAULT_BOOKING_REFERENCE_NUMBER = "SAMPLE_TEXT";
    private static final String UPDATED_BOOKING_REFERENCE_NUMBER = "UPDATED_TEXT";

    private static final DateTime DEFAULT_BOOKING_DATE_TIME = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_BOOKING_DATE_TIME = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_BOOKING_DATE_TIME_STR = dateTimeFormatter.print(DEFAULT_BOOKING_DATE_TIME);

    private static final Integer DEFAULT_NUMBER_OF_SPACES_BOOKED = 0;
    private static final Integer UPDATED_NUMBER_OF_SPACES_BOOKED = 1;

    private static final BigDecimal DEFAULT_TOTAL_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_TOTAL_AMOUNT = new BigDecimal(1);

    private static final Boolean DEFAULT_PAYMENT_RECURSIVE = false;
    private static final Boolean UPDATED_PAYMENT_RECURSIVE = true;
    private static final String DEFAULT_PAYMENT_FREQUENCY = "SAMPLE_TEXT";
    private static final String UPDATED_PAYMENT_FREQUENCY = "UPDATED_TEXT";

    private static final Integer DEFAULT_NUMBER_OF_PAYMENTS = 0;
    private static final Integer UPDATED_NUMBER_OF_PAYMENTS = 1;

    private static final BigDecimal DEFAULT_FIRST_PAYMENT_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_FIRST_PAYMENT_AMOUNT = new BigDecimal(1);

    private static final BigDecimal DEFAULT_REGULAR_PAYMENT_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_REGULAR_PAYMENT_AMOUNT = new BigDecimal(1);

    private static final BigDecimal DEFAULT_LAST_PAYMENT_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_LAST_PAYMENT_AMOUNT = new BigDecimal(1);

    private static final Integer DEFAULT_NUMBER_OF_PAYMENTS_MADE = 0;
    private static final Integer UPDATED_NUMBER_OF_PAYMENTS_MADE = 1;

    private static final DateTime DEFAULT_NEXT_PAYMENT_DATE_TIME = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_NEXT_PAYMENT_DATE_TIME = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_NEXT_PAYMENT_DATE_TIME_STR = dateTimeFormatter.print(DEFAULT_NEXT_PAYMENT_DATE_TIME);
    private static final String DEFAULT_VEHICLE_MAKE = "SAMPLE_TEXT";
    private static final String UPDATED_VEHICLE_MAKE = "UPDATED_TEXT";
    private static final String DEFAULT_VEHICLE_MODEL = "SAMPLE_TEXT";
    private static final String UPDATED_VEHICLE_MODEL = "UPDATED_TEXT";
    private static final String DEFAULT_MODEL_YEAR = "SAMPLE_TEXT";
    private static final String UPDATED_MODEL_YEAR = "UPDATED_TEXT";
    private static final String DEFAULT_LICENCE_PLATE_NUMBER = "SAMPLE_TEXT";
    private static final String UPDATED_LICENCE_PLATE_NUMBER = "UPDATED_TEXT";
    private static final String DEFAULT_STATUS = "SAMPLE_TEXT";
    private static final String UPDATED_STATUS = "UPDATED_TEXT";

    private static final DateTime DEFAULT_CANCELATION_DATE_TIME = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_CANCELATION_DATE_TIME = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_CANCELATION_DATE_TIME_STR = dateTimeFormatter.print(DEFAULT_CANCELATION_DATE_TIME);

    private static final Long DEFAULT_CANCELLED_BY = 0L;
    private static final Long UPDATED_CANCELLED_BY = 1L;

    @Inject
    private CustomerBookingRepository customerBookingRepository;

    private MockMvc restCustomerBookingMockMvc;

    private CustomerBooking customerBooking;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CustomerBookingResource customerBookingResource = new CustomerBookingResource();
        ReflectionTestUtils.setField(customerBookingResource, "customerBookingRepository", customerBookingRepository);
        this.restCustomerBookingMockMvc = MockMvcBuilders.standaloneSetup(customerBookingResource).build();
    }

    @Before
    public void initTest() {
        customerBooking = new CustomerBooking();
        customerBooking.setBookingReferenceNumber(DEFAULT_BOOKING_REFERENCE_NUMBER);
        customerBooking.setBookingDateTime(DEFAULT_BOOKING_DATE_TIME);
        customerBooking.setNumberOfSpacesBooked(DEFAULT_NUMBER_OF_SPACES_BOOKED);
        customerBooking.setTotalAmount(DEFAULT_TOTAL_AMOUNT);
        customerBooking.setPaymentRecursive(DEFAULT_PAYMENT_RECURSIVE);
        customerBooking.setPaymentFrequency(DEFAULT_PAYMENT_FREQUENCY);
        customerBooking.setNumberOfPayments(DEFAULT_NUMBER_OF_PAYMENTS);
        customerBooking.setFirstPaymentAmount(DEFAULT_FIRST_PAYMENT_AMOUNT);
        customerBooking.setRegularPaymentAmount(DEFAULT_REGULAR_PAYMENT_AMOUNT);
        customerBooking.setLastPaymentAmount(DEFAULT_LAST_PAYMENT_AMOUNT);
        customerBooking.setNumberOfPaymentsMade(DEFAULT_NUMBER_OF_PAYMENTS_MADE);
        customerBooking.setNextPaymentDateTime(DEFAULT_NEXT_PAYMENT_DATE_TIME);
        customerBooking.setVehicleMake(DEFAULT_VEHICLE_MAKE);
        customerBooking.setVehicleModel(DEFAULT_VEHICLE_MODEL);
        customerBooking.setModelYear(DEFAULT_MODEL_YEAR);
        customerBooking.setLicencePlateNumber(DEFAULT_LICENCE_PLATE_NUMBER);
        customerBooking.setStatus(DEFAULT_STATUS);
        customerBooking.setCancelationDateTime(DEFAULT_CANCELATION_DATE_TIME);
        customerBooking.setCancelledBy(DEFAULT_CANCELLED_BY);
    }

    @Test
    @Transactional
    public void createCustomerBooking() throws Exception {
        int databaseSizeBeforeCreate = customerBookingRepository.findAll().size();

        // Create the CustomerBooking
        restCustomerBookingMockMvc.perform(post("/api/customerBookings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerBooking)))
                .andExpect(status().isCreated());

        // Validate the CustomerBooking in the database
        List<CustomerBooking> customerBookings = customerBookingRepository.findAll();
        assertThat(customerBookings).hasSize(databaseSizeBeforeCreate + 1);
        CustomerBooking testCustomerBooking = customerBookings.get(customerBookings.size() - 1);
        assertThat(testCustomerBooking.getBookingReferenceNumber()).isEqualTo(DEFAULT_BOOKING_REFERENCE_NUMBER);
        assertThat(testCustomerBooking.getBookingDateTime().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_BOOKING_DATE_TIME);
        assertThat(testCustomerBooking.getNumberOfSpacesBooked()).isEqualTo(DEFAULT_NUMBER_OF_SPACES_BOOKED);
        assertThat(testCustomerBooking.getTotalAmount()).isEqualTo(DEFAULT_TOTAL_AMOUNT);
        assertThat(testCustomerBooking.getPaymentRecursive()).isEqualTo(DEFAULT_PAYMENT_RECURSIVE);
        assertThat(testCustomerBooking.getPaymentFrequency()).isEqualTo(DEFAULT_PAYMENT_FREQUENCY);
        assertThat(testCustomerBooking.getNumberOfPayments()).isEqualTo(DEFAULT_NUMBER_OF_PAYMENTS);
        assertThat(testCustomerBooking.getFirstPaymentAmount()).isEqualTo(DEFAULT_FIRST_PAYMENT_AMOUNT);
        assertThat(testCustomerBooking.getRegularPaymentAmount()).isEqualTo(DEFAULT_REGULAR_PAYMENT_AMOUNT);
        assertThat(testCustomerBooking.getLastPaymentAmount()).isEqualTo(DEFAULT_LAST_PAYMENT_AMOUNT);
        assertThat(testCustomerBooking.getNumberOfPaymentsMade()).isEqualTo(DEFAULT_NUMBER_OF_PAYMENTS_MADE);
        assertThat(testCustomerBooking.getNextPaymentDateTime().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_NEXT_PAYMENT_DATE_TIME);
        assertThat(testCustomerBooking.getVehicleMake()).isEqualTo(DEFAULT_VEHICLE_MAKE);
        assertThat(testCustomerBooking.getVehicleModel()).isEqualTo(DEFAULT_VEHICLE_MODEL);
        assertThat(testCustomerBooking.getModelYear()).isEqualTo(DEFAULT_MODEL_YEAR);
        assertThat(testCustomerBooking.getLicencePlateNumber()).isEqualTo(DEFAULT_LICENCE_PLATE_NUMBER);
        assertThat(testCustomerBooking.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testCustomerBooking.getCancelationDateTime().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_CANCELATION_DATE_TIME);
        assertThat(testCustomerBooking.getCancelledBy()).isEqualTo(DEFAULT_CANCELLED_BY);
    }

    @Test
    @Transactional
    public void checkBookingReferenceNumberIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(customerBookingRepository.findAll()).hasSize(0);
        // set the field null
        customerBooking.setBookingReferenceNumber(null);

        // Create the CustomerBooking, which fails.
        restCustomerBookingMockMvc.perform(post("/api/customerBookings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerBooking)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<CustomerBooking> customerBookings = customerBookingRepository.findAll();
        assertThat(customerBookings).hasSize(0);
    }

    @Test
    @Transactional
    public void checkBookingDateTimeIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(customerBookingRepository.findAll()).hasSize(0);
        // set the field null
        customerBooking.setBookingDateTime(null);

        // Create the CustomerBooking, which fails.
        restCustomerBookingMockMvc.perform(post("/api/customerBookings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerBooking)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<CustomerBooking> customerBookings = customerBookingRepository.findAll();
        assertThat(customerBookings).hasSize(0);
    }

    @Test
    @Transactional
    public void checkNumberOfSpacesBookedIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(customerBookingRepository.findAll()).hasSize(0);
        // set the field null
        customerBooking.setNumberOfSpacesBooked(null);

        // Create the CustomerBooking, which fails.
        restCustomerBookingMockMvc.perform(post("/api/customerBookings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerBooking)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<CustomerBooking> customerBookings = customerBookingRepository.findAll();
        assertThat(customerBookings).hasSize(0);
    }

    @Test
    @Transactional
    public void checkTotalAmountIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(customerBookingRepository.findAll()).hasSize(0);
        // set the field null
        customerBooking.setTotalAmount(null);

        // Create the CustomerBooking, which fails.
        restCustomerBookingMockMvc.perform(post("/api/customerBookings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerBooking)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<CustomerBooking> customerBookings = customerBookingRepository.findAll();
        assertThat(customerBookings).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllCustomerBookings() throws Exception {
        // Initialize the database
        customerBookingRepository.saveAndFlush(customerBooking);

        // Get all the customerBookings
        restCustomerBookingMockMvc.perform(get("/api/customerBookings"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(customerBooking.getId().intValue())))
                .andExpect(jsonPath("$.[*].bookingReferenceNumber").value(hasItem(DEFAULT_BOOKING_REFERENCE_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].bookingDateTime").value(hasItem(DEFAULT_BOOKING_DATE_TIME_STR)))
                .andExpect(jsonPath("$.[*].numberOfSpacesBooked").value(hasItem(DEFAULT_NUMBER_OF_SPACES_BOOKED)))
                .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(DEFAULT_TOTAL_AMOUNT.intValue())))
                .andExpect(jsonPath("$.[*].paymentRecursive").value(hasItem(DEFAULT_PAYMENT_RECURSIVE.booleanValue())))
                .andExpect(jsonPath("$.[*].paymentFrequency").value(hasItem(DEFAULT_PAYMENT_FREQUENCY.toString())))
                .andExpect(jsonPath("$.[*].numberOfPayments").value(hasItem(DEFAULT_NUMBER_OF_PAYMENTS)))
                .andExpect(jsonPath("$.[*].firstPaymentAmount").value(hasItem(DEFAULT_FIRST_PAYMENT_AMOUNT.intValue())))
                .andExpect(jsonPath("$.[*].regularPaymentAmount").value(hasItem(DEFAULT_REGULAR_PAYMENT_AMOUNT.intValue())))
                .andExpect(jsonPath("$.[*].lastPaymentAmount").value(hasItem(DEFAULT_LAST_PAYMENT_AMOUNT.intValue())))
                .andExpect(jsonPath("$.[*].numberOfPaymentsMade").value(hasItem(DEFAULT_NUMBER_OF_PAYMENTS_MADE)))
                .andExpect(jsonPath("$.[*].nextPaymentDateTime").value(hasItem(DEFAULT_NEXT_PAYMENT_DATE_TIME_STR)))
                .andExpect(jsonPath("$.[*].vehicleMake").value(hasItem(DEFAULT_VEHICLE_MAKE.toString())))
                .andExpect(jsonPath("$.[*].vehicleModel").value(hasItem(DEFAULT_VEHICLE_MODEL.toString())))
                .andExpect(jsonPath("$.[*].modelYear").value(hasItem(DEFAULT_MODEL_YEAR.toString())))
                .andExpect(jsonPath("$.[*].licencePlateNumber").value(hasItem(DEFAULT_LICENCE_PLATE_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].cancelationDateTime").value(hasItem(DEFAULT_CANCELATION_DATE_TIME_STR)))
                .andExpect(jsonPath("$.[*].cancelledBy").value(hasItem(DEFAULT_CANCELLED_BY.intValue())));
    }

    @Test
    @Transactional
    public void getCustomerBooking() throws Exception {
        // Initialize the database
        customerBookingRepository.saveAndFlush(customerBooking);

        // Get the customerBooking
        restCustomerBookingMockMvc.perform(get("/api/customerBookings/{id}", customerBooking.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(customerBooking.getId().intValue()))
            .andExpect(jsonPath("$.bookingReferenceNumber").value(DEFAULT_BOOKING_REFERENCE_NUMBER.toString()))
            .andExpect(jsonPath("$.bookingDateTime").value(DEFAULT_BOOKING_DATE_TIME_STR))
            .andExpect(jsonPath("$.numberOfSpacesBooked").value(DEFAULT_NUMBER_OF_SPACES_BOOKED))
            .andExpect(jsonPath("$.totalAmount").value(DEFAULT_TOTAL_AMOUNT.intValue()))
            .andExpect(jsonPath("$.paymentRecursive").value(DEFAULT_PAYMENT_RECURSIVE.booleanValue()))
            .andExpect(jsonPath("$.paymentFrequency").value(DEFAULT_PAYMENT_FREQUENCY.toString()))
            .andExpect(jsonPath("$.numberOfPayments").value(DEFAULT_NUMBER_OF_PAYMENTS))
            .andExpect(jsonPath("$.firstPaymentAmount").value(DEFAULT_FIRST_PAYMENT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.regularPaymentAmount").value(DEFAULT_REGULAR_PAYMENT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.lastPaymentAmount").value(DEFAULT_LAST_PAYMENT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.numberOfPaymentsMade").value(DEFAULT_NUMBER_OF_PAYMENTS_MADE))
            .andExpect(jsonPath("$.nextPaymentDateTime").value(DEFAULT_NEXT_PAYMENT_DATE_TIME_STR))
            .andExpect(jsonPath("$.vehicleMake").value(DEFAULT_VEHICLE_MAKE.toString()))
            .andExpect(jsonPath("$.vehicleModel").value(DEFAULT_VEHICLE_MODEL.toString()))
            .andExpect(jsonPath("$.modelYear").value(DEFAULT_MODEL_YEAR.toString()))
            .andExpect(jsonPath("$.licencePlateNumber").value(DEFAULT_LICENCE_PLATE_NUMBER.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.cancelationDateTime").value(DEFAULT_CANCELATION_DATE_TIME_STR))
            .andExpect(jsonPath("$.cancelledBy").value(DEFAULT_CANCELLED_BY.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCustomerBooking() throws Exception {
        // Get the customerBooking
        restCustomerBookingMockMvc.perform(get("/api/customerBookings/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCustomerBooking() throws Exception {
        // Initialize the database
        customerBookingRepository.saveAndFlush(customerBooking);

		int databaseSizeBeforeUpdate = customerBookingRepository.findAll().size();

        // Update the customerBooking
        customerBooking.setBookingReferenceNumber(UPDATED_BOOKING_REFERENCE_NUMBER);
        customerBooking.setBookingDateTime(UPDATED_BOOKING_DATE_TIME);
        customerBooking.setNumberOfSpacesBooked(UPDATED_NUMBER_OF_SPACES_BOOKED);
        customerBooking.setTotalAmount(UPDATED_TOTAL_AMOUNT);
        customerBooking.setPaymentRecursive(UPDATED_PAYMENT_RECURSIVE);
        customerBooking.setPaymentFrequency(UPDATED_PAYMENT_FREQUENCY);
        customerBooking.setNumberOfPayments(UPDATED_NUMBER_OF_PAYMENTS);
        customerBooking.setFirstPaymentAmount(UPDATED_FIRST_PAYMENT_AMOUNT);
        customerBooking.setRegularPaymentAmount(UPDATED_REGULAR_PAYMENT_AMOUNT);
        customerBooking.setLastPaymentAmount(UPDATED_LAST_PAYMENT_AMOUNT);
        customerBooking.setNumberOfPaymentsMade(UPDATED_NUMBER_OF_PAYMENTS_MADE);
        customerBooking.setNextPaymentDateTime(UPDATED_NEXT_PAYMENT_DATE_TIME);
        customerBooking.setVehicleMake(UPDATED_VEHICLE_MAKE);
        customerBooking.setVehicleModel(UPDATED_VEHICLE_MODEL);
        customerBooking.setModelYear(UPDATED_MODEL_YEAR);
        customerBooking.setLicencePlateNumber(UPDATED_LICENCE_PLATE_NUMBER);
        customerBooking.setStatus(UPDATED_STATUS);
        customerBooking.setCancelationDateTime(UPDATED_CANCELATION_DATE_TIME);
        customerBooking.setCancelledBy(UPDATED_CANCELLED_BY);
        restCustomerBookingMockMvc.perform(put("/api/customerBookings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerBooking)))
                .andExpect(status().isOk());

        // Validate the CustomerBooking in the database
        List<CustomerBooking> customerBookings = customerBookingRepository.findAll();
        assertThat(customerBookings).hasSize(databaseSizeBeforeUpdate);
        CustomerBooking testCustomerBooking = customerBookings.get(customerBookings.size() - 1);
        assertThat(testCustomerBooking.getBookingReferenceNumber()).isEqualTo(UPDATED_BOOKING_REFERENCE_NUMBER);
        assertThat(testCustomerBooking.getBookingDateTime().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_BOOKING_DATE_TIME);
        assertThat(testCustomerBooking.getNumberOfSpacesBooked()).isEqualTo(UPDATED_NUMBER_OF_SPACES_BOOKED);
        assertThat(testCustomerBooking.getTotalAmount()).isEqualTo(UPDATED_TOTAL_AMOUNT);
        assertThat(testCustomerBooking.getPaymentRecursive()).isEqualTo(UPDATED_PAYMENT_RECURSIVE);
        assertThat(testCustomerBooking.getPaymentFrequency()).isEqualTo(UPDATED_PAYMENT_FREQUENCY);
        assertThat(testCustomerBooking.getNumberOfPayments()).isEqualTo(UPDATED_NUMBER_OF_PAYMENTS);
        assertThat(testCustomerBooking.getFirstPaymentAmount()).isEqualTo(UPDATED_FIRST_PAYMENT_AMOUNT);
        assertThat(testCustomerBooking.getRegularPaymentAmount()).isEqualTo(UPDATED_REGULAR_PAYMENT_AMOUNT);
        assertThat(testCustomerBooking.getLastPaymentAmount()).isEqualTo(UPDATED_LAST_PAYMENT_AMOUNT);
        assertThat(testCustomerBooking.getNumberOfPaymentsMade()).isEqualTo(UPDATED_NUMBER_OF_PAYMENTS_MADE);
        assertThat(testCustomerBooking.getNextPaymentDateTime().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_NEXT_PAYMENT_DATE_TIME);
        assertThat(testCustomerBooking.getVehicleMake()).isEqualTo(UPDATED_VEHICLE_MAKE);
        assertThat(testCustomerBooking.getVehicleModel()).isEqualTo(UPDATED_VEHICLE_MODEL);
        assertThat(testCustomerBooking.getModelYear()).isEqualTo(UPDATED_MODEL_YEAR);
        assertThat(testCustomerBooking.getLicencePlateNumber()).isEqualTo(UPDATED_LICENCE_PLATE_NUMBER);
        assertThat(testCustomerBooking.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testCustomerBooking.getCancelationDateTime().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_CANCELATION_DATE_TIME);
        assertThat(testCustomerBooking.getCancelledBy()).isEqualTo(UPDATED_CANCELLED_BY);
    }

    @Test
    @Transactional
    public void deleteCustomerBooking() throws Exception {
        // Initialize the database
        customerBookingRepository.saveAndFlush(customerBooking);

		int databaseSizeBeforeDelete = customerBookingRepository.findAll().size();

        // Get the customerBooking
        restCustomerBookingMockMvc.perform(delete("/api/customerBookings/{id}", customerBooking.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<CustomerBooking> customerBookings = customerBookingRepository.findAll();
        assertThat(customerBookings).hasSize(databaseSizeBeforeDelete - 1);
    }
}
