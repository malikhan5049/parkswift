package com.ews.parkswift.service;

import java.util.UUID;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ews.parkswift.config.Constants;
import com.ews.parkswift.domain.BookedParkingSpace;
import com.ews.parkswift.domain.BookingSchedule;
import com.ews.parkswift.domain.CustomerBooking;
import com.ews.parkswift.domain.PaymentCharged;
import com.ews.parkswift.domain.User;
import com.ews.parkswift.repository.BookedParkingSpaceRepository;
import com.ews.parkswift.repository.BookingScheduleRepository;
import com.ews.parkswift.repository.CustomerBookingRepository;
import com.ews.parkswift.repository.PaymentChargedRepository;
import com.ews.parkswift.web.rest.dto.parking.BookingDTO;

@Service
public class BookingService {
	
	private final Logger log = LoggerFactory.getLogger(ParkingLocationService.class);
	@Inject
    private CustomerBookingRepository customerBookingRepository; 
	@Inject
	private PaymentChargedRepository paymentChargedRepository;
	@Inject
	private BookedParkingSpaceRepository bookedParkingSpaceRepository;
	@Inject
	private BookingScheduleRepository bookingScheduleRepository; 
	@Inject
    private UserService userService;
	
	public boolean save(BookingDTO bookingDTO){
		
		CustomerBooking booking  = new CustomerBooking();
		
		populateDBObjectFromDTO(booking, bookingDTO);
		
		customerBookingRepository.save(booking);
		
		return false;
	}
	
	
	@SuppressWarnings("static-access")
	private void populateDBObjectFromDTO(CustomerBooking booking, BookingDTO bookingDTO){
		UUID bookingReferenceNo = UUID.fromString(Constants.BOOKING_REFERENCE_NUMBER_RANDOM).randomUUID();
		
		if(bookingDTO.getBookingId()!=null && !bookingDTO.getBookingId().equals("")){
			booking.setId(bookingDTO.getBookingId());
		}
		for(BookedParkingSpace bookedParkingSpace : bookingDTO.getBookedParkingSpaces()){
			// TODO : populate
		}
		booking.setBookingDateTime(bookingDTO.getBookingDateTime());
		booking.setBookingReferenceNumber(bookingReferenceNo.randomUUID().toString());
		
		BookingSchedule bookingSchedule = bookingScheduleRepository.findOne(bookingDTO.getBookingScheduleId());
		if(bookingSchedule!=null){		
			booking.setBookingSchedule(bookingSchedule);
		}
		
		booking.setCancelationDateTime(bookingDTO.getCancelationDateTime());
		booking.setCancelledBy(bookingDTO.getCancelledBy());
		booking.setFirstPaymentAmount(bookingDTO.getFirstPaymentAmount());
		booking.setLastPaymentAmount(bookingDTO.getLastPaymentAmount());
		booking.setLicencePlateNumber(bookingDTO.getLicencePlateNumber());
//		booking.setModelYear(bookingDTO.getModelYear());
		booking.setNextPaymentDateTime(bookingDTO.getNextPaymentDateTime());
		booking.setNumberOfPayments(bookingDTO.getNumberOfPayments());
		booking.setNumberOfPaymentsMade(bookingDTO.getNumberOfPaymentsMade());
		booking.setNumberOfSpacesBooked(bookingDTO.getNumberOfSpacesBooked());
		booking.setPaymentFrequency(bookingDTO.getPaymentFrequency());
		booking.setPaymentRecursive(bookingDTO.getPaymentRecursive());
		
		for(PaymentCharged paymentCharged: bookingDTO.getPaymentsCharged()){
			// TODO : populate
		}
//		booking.setPayments(payments);
		booking.setRegularPaymentAmount(bookingDTO.getRegularPaymentAmount());
		booking.setStatus(bookingDTO.getStatus());
		booking.setTotalAmount(bookingDTO.getTotalAmount());
		User user = userService.getUser();
		if(user!=null){
			booking.setUser(user);
		}
		booking.setVehicleMake(bookingDTO.getVehicleMake());
		booking.setVehicleModel(bookingDTO.getVehicleModel());
	}
	
}
