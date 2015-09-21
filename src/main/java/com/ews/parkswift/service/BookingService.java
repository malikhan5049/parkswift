package com.ews.parkswift.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.ews.parkswift.config.Constants;
import com.ews.parkswift.domain.BookedParkingSpace;
import com.ews.parkswift.domain.BookingSchedule;
import com.ews.parkswift.domain.BookingScheduleRepeatOn;
import com.ews.parkswift.domain.CustomerBooking;
import com.ews.parkswift.domain.ParkingLocation;
import com.ews.parkswift.domain.ParkingLocationFacility;
import com.ews.parkswift.domain.ParkingLocationImage;
import com.ews.parkswift.domain.ParkingSpace;
import com.ews.parkswift.domain.PaymentCharged;
import com.ews.parkswift.domain.User;
import com.ews.parkswift.repository.BookedParkingSpaceRepository;
import com.ews.parkswift.repository.BookingScheduleRepeatOnRepository;
import com.ews.parkswift.repository.BookingScheduleRepository;
import com.ews.parkswift.repository.CustomerBookingRepository;
import com.ews.parkswift.repository.ParkingSpaceRepository;
import com.ews.parkswift.repository.PaymentChargedRepository;
import com.ews.parkswift.web.rest.dto.parking.AvailableParkingDTO;
import com.ews.parkswift.web.rest.dto.parking.BookingDTO;
import com.ews.parkswift.web.rest.dto.parking.PaymentDTO;
import com.paypal.svcs.types.ap.Receiver;

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
	private BookingScheduleRepeatOnRepository bookingScheduleRepeatOnRepository;

	public boolean save(BookingDTO bookingDTO){

		CustomerBooking booking  = new CustomerBooking();
		
		populateBookingDBObjectFromDTO(booking, bookingDTO);
		
		BookingSchedule bookingSchedule = new BookingSchedule();
		bookingSchedule.setStartDateTime(new DateTime(bookingDTO.getStartDate().getYear(), bookingDTO.getStartDate().getMonthOfYear(), bookingDTO.getStartDate().getDayOfMonth(), bookingDTO.getStartTime().getHourOfDay(), bookingDTO.getStartTime().getMinuteOfHour()));
		bookingSchedule.setEndDateTime(new DateTime(bookingDTO.getEndDate().getYear(), bookingDTO.getEndDate().getMonthOfYear(), bookingDTO.getEndDate().getDayOfMonth(), bookingDTO.getEndTime().getHourOfDay(), bookingDTO.getEndTime().getMinuteOfHour()));
//		bookingSchedule.setStartTime(bookingDTO.getStartTime());
//		bookingSchedule.setEndTime(bookingDTO.getEndTime());
		bookingSchedule.setRepeatBasis(bookingDTO.getRepeatBasis());
		bookingSchedule.setStatus(bookingDTO.getStatus());
		bookingScheduleRepository.save(bookingSchedule);

		for(String dayOfWeek: bookingDTO.getRepeatOnWeekDays()){
			BookingScheduleRepeatOn bookingScheduleRepeatOn = new BookingScheduleRepeatOn();
			bookingScheduleRepeatOn.setDayOfWeek(dayOfWeek);
			bookingScheduleRepeatOn.setBookingSchedule(bookingSchedule);
			bookingScheduleRepeatOnRepository.save(bookingScheduleRepeatOn);
			
		}
		booking.setBookingSchedule(bookingSchedule);
		
		customerBookingRepository.save(booking);
		
		BookedParkingSpace bookedParkingSpace = new BookedParkingSpace();

		ParkingSpace parkingSpace = null;
		if(bookingDTO.getParkingSpaceId() !=null){
			parkingSpace = parkingSpaceRepository.findOne(bookingDTO.getParkingSpaceId());
			if(parkingSpace !=null){
				bookedParkingSpace.setParkingSpace(parkingSpace);
			}
		}
		bookedParkingSpace.setCustomerBooking(booking);
		bookedParkingSpaceRepository.save(bookedParkingSpace);
		
		PaymentCharged paymentCharged = populatePaymentChargedDBFromDTO(bookingDTO.getPaymentDTO());
		paymentCharged.setCustomerBooking(booking);
		paymentChargedRepository.save(paymentCharged);
		
		return true;
	}
	
	private PaymentDTO populatePaymentDTOFromDB(PaymentDTO paymentDTO, PaymentCharged paymentCharged){
		
		paymentDTO.setAmountCharged(paymentCharged.getAmountCharged());
		paymentDTO.setAmountToTransferOwnerAccount(paymentCharged.getAmountToTransferOwnerAccount());
		paymentDTO.setAmountToTransferParkSwiftAccount(paymentCharged.getAmountToTransferParkSwiftAccount());
		paymentDTO.setPaymentReferenceNumber(paymentCharged.getPaymentReferenceNumber());
		paymentDTO.setPaypallPaymentResponse(paymentCharged.getPaypallPaymentResponse());
		paymentDTO.setStatus(paymentCharged.getStatus());
		paymentDTO.setTransactionDateTime(paymentCharged.getTransactionDateTime());
		paymentDTO.setTransferToOwnerAccountDateTime(paymentCharged.getTransferToOwnerAccountDateTime());
		paymentDTO.setTransferToParkSwiftAccountDateTime(paymentCharged.getTransferToParkSwiftAccountDateTime());
		paymentDTO.setPayKey(paymentCharged.getPayKey());
		paymentDTO.setPayKeyExpiryDate(paymentCharged.getPaykeyExpiryDate());
		paymentDTO.setSenderPaypalEmail(paymentCharged.getSenderPaypalEmail());
		return paymentDTO;
		
	}
	private PaymentCharged populatePaymentChargedDBFromDTO(PaymentDTO paymentDTO){
		
		UUID paymentReferenceNo = UUID.fromString(Constants.PAYMENT_REFERENCE_NUMBER_RANDOM).randomUUID();
		DateTime now = new DateTime();
		
		BigDecimal amountToTransferOwnerAccount=null;
		BigDecimal amountToTransferParkSwiftAccount=null;
		
		for(Receiver receiver:paymentDTO.getListReceivers()){
			if(receiver.getPrimary()){
				amountToTransferOwnerAccount = new BigDecimal(receiver.getAmount());
			}else{
				amountToTransferParkSwiftAccount =  new BigDecimal(receiver.getAmount());
			}
		}

		PaymentCharged paymentCharged = new PaymentCharged();
		paymentCharged.setAmountCharged(paymentDTO.getAmountCharged());
		paymentCharged.setAmountToTransferOwnerAccount(amountToTransferOwnerAccount);
		paymentCharged.setAmountToTransferParkSwiftAccount(amountToTransferParkSwiftAccount);
		paymentCharged.setPaymentReferenceNumber(paymentReferenceNo.randomUUID().toString());
		paymentCharged.setPaypallPaymentResponse(Constants.PAYPAL_PAYMENT_STATUS_COMPLETED);
		paymentCharged.setStatus(Constants.PAYPAL_PAYMENT_STATUS_COMPLETED);
		paymentCharged.setTransactionDateTime(now);
		paymentCharged.setTransferToOwnerAccountDateTime(now);
		paymentCharged.setTransferToParkSwiftAccountDateTime(now);
		paymentCharged.setPayKey(paymentDTO.getPayKey());
		paymentCharged.setPaykeyExpiryDate(paymentDTO.getPayKeyExpiryDate());
		paymentCharged.setSenderPaypalEmail(paymentCharged.getSenderPaypalEmail());
		
		return paymentCharged;
	}
	
	
	@SuppressWarnings("static-access")
	private void populateBookingDBObjectFromDTO(CustomerBooking booking, BookingDTO bookingDTO){
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
//		ParkingSpace parkingSpace = null;
//		if(bookingDTO.getParkingSpaceId() !=null){
//			parkingSpace = parkingSpaceRepository.findOne(bookingDTO.getParkingSpaceId());
//			if(parkingSpace !=null){
//				if(bookingDTO.getAvailabilityScheduleId()!=null && !bookingDTO.getAvailabilityScheduleId().equals("")){
//					AvailabilitySchedule availabilitySchedule = availabilityScheduleRepository.findOne(bookingDTO.getAvailabilityScheduleId());
//					if(availabilitySchedule!=null){
//						parkingSpace.getAvailabilitySchedules().add(availabilitySchedule);
//					}
//				}
//				booking.setParkingSpace(parkingSpace);
//			}
//		}
		booking.setBookingReferenceNumber(bookingReferenceNo.randomUUID().toString());
		booking.setBookingDateTime(new DateTime());
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
		
//		booking.setRegularPaymentAmount(bookingDTO.getRegularPaymentAmount());
		booking.setStatus(Constants.BOOKING_STATUS_BOOKED);
		booking.setTotalAmount(bookingDTO.getTotalAmount());
		
//		booking.setVehicleMake(bookingDTO.getVehicleMake());
//		booking.setVehicleModel(bookingDTO.getVehicleModel());
		
	}
	
	public boolean cancelBooking(Long id){
		CustomerBooking customerBooking = customerBookingRepository.findOne(id);
		if(null!=customerBooking){
			customerBooking.setStatus(Constants.BOOKING_STATUS_CANCELLED);
			customerBookingRepository.save(customerBooking);
			return true;
		}
		return false;
	}
	
	public List<BookingDTO> getAllBookings(){
		List<BookingDTO> listReturningBookings = new ArrayList<>();
		
		List<CustomerBooking> listBookings = customerBookingRepository.findAllForCurrentUser();
		
		for(CustomerBooking booking: listBookings){
			BookingDTO bookingDTO = new BookingDTO();
			listReturningBookings.add(populateBookingDTOFromDB(bookingDTO, booking));
		}
		return listReturningBookings;
	}
	
	
	private BookingDTO populateBookingDTOFromDB(BookingDTO bookingDTO, CustomerBooking booking){
		bookingDTO.setBookingId(booking.getId());
//		for(BookedParkingSpace bookedParkingSpace : booking.getBookedParkingSpaces()){
//			bookingDTO.setBookedParkingSpaces(booking.getBookedParkingSpaces());
//		}
//		bookingDTO.setBookedParkingSpaces(booking.getBookedParkingSpaces());
		bookingDTO.setBookingDateTime(booking.getBookingDateTime());
		bookingDTO.setBookingReferenceNumber(booking.getBookingReferenceNumber());
		
//		if(bookingDTO.getBookingScheduleId()!=null){
//			BookingSchedule bookingSchedule = bookingScheduleRepository.findOne(bookingDTO.getBookingScheduleId());
//			if(bookingSchedule!=null){		
//				booking.setBookingSchedule(bookingSchedule);
//			}
//		}
		bookingDTO.setBookingScheduleId(booking.getBookingSchedule().getId());
//		bookingDTO.setParkingSpaceId(booking.getBookedParkingSpaces().getId());
		
		AvailableParkingDTO availableParkingDTO = new AvailableParkingDTO();
		ParkingLocation parkingLocation=null;
		for(BookedParkingSpace bookedParkingSpace:booking.getBookedParkingSpaces()){
			parkingLocation = bookedParkingSpace.getParkingSpace().getParkingLocation(); 
			break;
		}
		bookingDTO.setAvailableParkingDTO(populateAvailableLocationDTO(availableParkingDTO, parkingLocation));
		
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
		bookingDTO.setCancelationDateTime(booking.getCancelationDateTime());
		booking.setNextPaymentDateTime(booking.getNextPaymentDateTime());
		
		for(PaymentCharged paymentCharged: booking.getPaymentsCharged()){
			PaymentDTO paymentDTO = new PaymentDTO();
			bookingDTO.setPaymentDTO(populatePaymentDTOFromDB(paymentDTO, paymentCharged));
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
			bookingDTO.setStartDate(booking.getBookingSchedule().getStartDateTime().toLocalDate());
			bookingDTO.setEndDate(booking.getBookingSchedule().getEndDateTime().toLocalDate());
			bookingDTO.setStartTime(booking.getBookingSchedule().getStartDateTime().toLocalTime());
			bookingDTO.setEndTime(booking.getBookingSchedule().getEndDateTime().toLocalTime());
			bookingDTO.setRepeatBasis(booking.getBookingSchedule().getRepeatBasis());
			bookingDTO.setStatus(booking.getStatus());
			bookingDTO.setStartDateTime(booking.getBookingSchedule().getStartDateTime());
			bookingDTO.setEndDateTime(booking.getBookingSchedule().getEndDateTime());
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
