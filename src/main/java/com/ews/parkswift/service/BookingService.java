package com.ews.parkswift.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.threeten.bp.LocalDateTime;

import com.ews.parkswift.config.Constants;
import com.ews.parkswift.domain.AvailabilitySchedule;
import com.ews.parkswift.domain.BookedParkingSpace;
import com.ews.parkswift.domain.BookingSchedule;
import com.ews.parkswift.domain.CustomerBooking;
import com.ews.parkswift.domain.ParkingLocation;
import com.ews.parkswift.domain.ParkingLocationFacility;
import com.ews.parkswift.domain.ParkingLocationImage;
import com.ews.parkswift.domain.ParkingSpace;
import com.ews.parkswift.domain.PaymentCharged;
import com.ews.parkswift.domain.User;
import com.ews.parkswift.repository.AvailabilityScheduleRepository;
import com.ews.parkswift.repository.BookedParkingSpaceRepository;
import com.ews.parkswift.repository.BookingScheduleRepository;
import com.ews.parkswift.repository.CustomerBookingRepository;
import com.ews.parkswift.repository.ParkingSpaceRepository;
import com.ews.parkswift.repository.PaymentChargedRepository;
import com.ews.parkswift.web.rest.dto.parking.AvailableParkingDTO;
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
	@Inject
	private ParkingSpaceRepository parkingSpaceRepository;
	@Inject 
	private AvailabilityScheduleRepository availabilityScheduleRepository;
	
	public boolean save(BookingDTO bookingDTO){
		
		CustomerBooking booking  = new CustomerBooking();
		
		populateDBObjectFromDTO(booking, bookingDTO);
		
		BookingSchedule bookingSchedule = new BookingSchedule();
		bookingSchedule.setStartDate(bookingDTO.getStartDate());
		bookingSchedule.setEndDate(bookingDTO.getEndDate());
		bookingSchedule.setStartTime(bookingDTO.getStartTime());
		bookingSchedule.setEndTime(bookingDTO.getEndTime());
		bookingSchedule.setRepeatBasis(bookingDTO.getRepeatBasis());
		bookingSchedule.setStatus(bookingDTO.getStatus());
		bookingScheduleRepository.save(bookingSchedule);
		
		booking.setBookingSchedule(bookingSchedule);
		
		customerBookingRepository.save(booking);
		
		BookedParkingSpace bookedParkingSpace = new BookedParkingSpace();
		if(booking.getParkingSpace()!=null){
			bookedParkingSpace.setParkingSpace(booking.getParkingSpace());
		}
		bookedParkingSpace.setCustomerBooking(booking);
		bookedParkingSpaceRepository.save(bookedParkingSpace);
		
		return true;
	}
	
	
	@SuppressWarnings("static-access")
	private void populateDBObjectFromDTO(CustomerBooking booking, BookingDTO bookingDTO){
		UUID bookingReferenceNo = UUID.fromString(Constants.BOOKING_REFERENCE_NUMBER_RANDOM).randomUUID();
		
		if(bookingDTO.getBookingId()!=null && !bookingDTO.getBookingId().equals("")){
			booking.setId(bookingDTO.getBookingId());
		}
		
//		if(bookingDTO.getAvailabilityScheduleId()!=null && !bookingDTO.getAvailabilityScheduleId().equals("")){
//			AvailabilitySchedule availabilitySchedule = availabilityScheduleRepository.findOne(bookingDTO.getAvailabilityScheduleId());
//			if(availabilitySchedule!=null){
//				booking.set
//			}
//		}
		
		if(bookingDTO.getBookingScheduleId()!=null){
			BookingSchedule bookingSchedule = bookingScheduleRepository.findOne(bookingDTO.getBookingScheduleId());
			if(bookingSchedule!=null){		
				booking.setBookingSchedule(bookingSchedule);
			}
		}
		User user = userService.getUser();
		if(user!=null){
			booking.setUser(user);
		}
		ParkingSpace parkingSpace = null;
		if(bookingDTO.getParkingSpaceId() !=null){
			parkingSpace = parkingSpaceRepository.findOne(bookingDTO.getParkingSpaceId());
			if(parkingSpace !=null){
				if(bookingDTO.getAvailabilityScheduleId()!=null && !bookingDTO.getAvailabilityScheduleId().equals("")){
					AvailabilitySchedule availabilitySchedule = availabilityScheduleRepository.findOne(bookingDTO.getAvailabilityScheduleId());
					if(availabilitySchedule!=null){
						parkingSpace.getAvailabilitySchedules().add(availabilitySchedule);
					}
				}
				booking.setParkingSpace(parkingSpace);
			}
		}
		booking.setBookingReferenceNumber(bookingReferenceNo.randomUUID().toString());
		booking.setBookingDateTime(DateTime.now());
		booking.setCancelationDateTime(bookingDTO.getCancelationDateTime());
		booking.setCancelledBy(bookingDTO.getCancelledBy());
//		booking.setFirstPaymentAmount(bookingDTO.getFirstPaymentAmount());
//		booking.setLastPaymentAmount(bookingDTO.getLastPaymentAmount());
//		booking.setLicencePlateNumber(bookingDTO.getLicencePlateNumber());
//		booking.setModelYear(bookingDTO.getModelYear());
		booking.setNextPaymentDateTime(bookingDTO.getNextPaymentDateTime());
//		booking.setNumberOfPayments(bookingDTO.getNumberOfPayments());
//		booking.setNumberOfPaymentsMade(bookingDTO.getNumberOfPaymentsMade());
		booking.setNumberOfSpacesBooked(bookingDTO.getNumberOfSpacesBooked());
		booking.setPaymentFrequency(bookingDTO.getPaymentFrequency());
		booking.setPaymentRecursive(bookingDTO.getPaymentRecursive());
		
		for(PaymentCharged paymentCharged: bookingDTO.getPaymentsCharged()){
			// TODO : populate
		}
//		booking.setPayments(payments);
//		booking.setRegularPaymentAmount(bookingDTO.getRegularPaymentAmount());
		booking.setStatus(bookingDTO.getStatus());
		booking.setTotalAmount(bookingDTO.getTotalAmount());
		
//		booking.setVehicleMake(bookingDTO.getVehicleMake());
//		booking.setVehicleModel(bookingDTO.getVehicleModel());
		
	}
	
	public List<BookingDTO> getAllBookings(){
		List<BookingDTO> listReturningBookings = new ArrayList<>();
		
		List<CustomerBooking> listBookings = customerBookingRepository.findAllForCurrentUser();
		
		for(CustomerBooking booking: listBookings){
			BookingDTO bookingDTO = new BookingDTO();
			listReturningBookings.add(populateDTOFromDB(bookingDTO, booking));
		}
		return listReturningBookings;
	}
	
	
	private BookingDTO populateDTOFromDB(BookingDTO bookingDTO, CustomerBooking booking){
		bookingDTO.setBookingId(booking.getId());
//		for(BookedParkingSpace bookedParkingSpace : booking.getBookedParkingSpaces()){
//			bookingDTO.setBookedParkingSpaces(booking.getBookedParkingSpaces());
//		}
		bookingDTO.setBookedParkingSpaces(booking.getBookedParkingSpaces());
		bookingDTO.setBookingDateTime(booking.getBookingDateTime().toDate());
		bookingDTO.setBookingReferenceNumber(booking.getBookingReferenceNumber());
		
		if(bookingDTO.getBookingScheduleId()!=null){
			BookingSchedule bookingSchedule = bookingScheduleRepository.findOne(bookingDTO.getBookingScheduleId());
			if(bookingSchedule!=null){		
				booking.setBookingSchedule(bookingSchedule);
			}
		}
		bookingDTO.setParkingSpaceId(booking.getParkingSpace().getId());
		
		AvailableParkingDTO availableParkingDTO = new AvailableParkingDTO();
		bookingDTO.setAvailableParkingDTO(populateAvailableLocationDTO(availableParkingDTO, booking.getParkingSpace().getParkingLocation()));
		
		bookingDTO.setCancelationDateTime(booking.getCancelationDateTime());
		bookingDTO.setCancelledBy(booking.getCancelledBy());
//		booking.setFirstPaymentAmount(bookingDTO.getFirstPaymentAmount());
//		booking.setLastPaymentAmount(bookingDTO.getLastPaymentAmount());
//		booking.setLicencePlateNumber(bookingDTO.getLicencePlateNumber());
//		booking.setModelYear(bookingDTO.getModelYear());
		bookingDTO.setNextPaymentDateTime(booking.getNextPaymentDateTime());
//		booking.setNumberOfPayments(bookingDTO.getNumberOfPayments());
//		booking.setNumberOfPaymentsMade(bookingDTO.getNumberOfPaymentsMade());
		bookingDTO.setNumberOfSpacesBooked(booking.getNumberOfSpacesBooked());
		bookingDTO.setPaymentFrequency(booking.getPaymentFrequency());
		bookingDTO.setPaymentRecursive(booking.getPaymentRecursive());
		
		for(PaymentCharged paymentCharged: booking.getPayments()){
			// TODO : populate
		}
//		booking.setPayments(payments);
//		booking.setRegularPaymentAmount(bookingDTO.getRegularPaymentAmount());
		bookingDTO.setStatus(booking.getStatus());
		bookingDTO.setTotalAmount(booking.getTotalAmount());
		User user = userService.getUser();
		if(user!=null){
			bookingDTO.setUserId(user.getId());
		}
//		booking.setVehicleMake(bookingDTO.getVehicleMake());
//		booking.setVehicleModel(bookingDTO.getVehicleModel());
		
		if(booking.getBookingSchedule()!=null){
			bookingDTO.setStartDate(booking.getBookingSchedule().getStartDate());
			bookingDTO.setEndDate(booking.getBookingSchedule().getEndDate());
			bookingDTO.setStartTime(booking.getBookingSchedule().getStartTime());
			bookingDTO.setEndTime(booking.getBookingSchedule().getEndTime());
			bookingDTO.setRepeatBasis(booking.getBookingSchedule().getRepeatBasis());
			bookingDTO.setStatus(booking.getBookingSchedule().getStatus());
		}
		
		
		return bookingDTO;
	} 
	
	
	private AvailableParkingDTO populateAvailableLocationDTO(AvailableParkingDTO availableParkingDTO, ParkingLocation parkingLocation){
		availableParkingDTO.setLocId(parkingLocation.getId());
    	availableParkingDTO.setBussinessType(parkingLocation.getBussinessType());
    	availableParkingDTO.setAddressLine1(parkingLocation.getAddressLine1());
    	availableParkingDTO.setAddressLine2(parkingLocation.getAddressLine2());
    	availableParkingDTO.setCity(parkingLocation.getCity());
    	availableParkingDTO.setState(parkingLocation.getState());
    	availableParkingDTO.setZipCode(parkingLocation.getZipCode());
    	availableParkingDTO.setCountry(parkingLocation.getCountry());
    	availableParkingDTO.setLongitude(parkingLocation.getLongitude());
    	availableParkingDTO.setLattitude(parkingLocation.getLattitude());
    	
    	for (ParkingLocationImage plImage : parkingLocation.getParkingLocationImages())
    		availableParkingDTO.getParkingLocationImages().add(plImage.getURL());
    	
    	for (ParkingLocationFacility plFacility : parkingLocation.getParkingLocationFacilitys())
    		availableParkingDTO.getParkingLocationFacilitys().add(plFacility.getFacility());
    	
//		List<ParkingSpace> listParkingSpace = parkingSpaceRepository.findAllByParkingLocationId(parkingLocation.getId());
//		for(ParkingSpace parkingSpace: listParkingSpace){
//			for(ParkingSpaceVehicleType parkingSpaceVehicleType: parkingSpace.getParkingSpaceVehicleTypes()){
//				availableParkingDTO.getParkingSpaceVehicleTypes().add(parkingSpaceVehicleType.getType());
//				availableParkingDTO.setParkingSpacePriceEntrys(parkingSpace.getParkingSpacePriceEntrys());
//			}
//		}
    	return availableParkingDTO;
	}
	
}
