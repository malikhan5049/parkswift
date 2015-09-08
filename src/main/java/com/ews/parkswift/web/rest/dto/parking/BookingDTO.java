package com.ews.parkswift.web.rest.dto.parking;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import com.ews.parkswift.domain.BookedParkingSpace;
import com.ews.parkswift.domain.PaymentCharged;
import com.ews.parkswift.domain.util.CustomLocalDateSerializer;
import com.ews.parkswift.domain.util.CustomLocalTimeDeserializer;
import com.ews.parkswift.domain.util.CustomLocalTimeSerializer;
import com.ews.parkswift.domain.util.ISO8601LocalDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class BookingDTO {
	
	private Long bookingId;
	private Long userId;
	private Long bookingScheduleId;
	private Long availabilityScheduleId;
	private Long parkingSpaceId;
	private Long locationId;

	private String bookingReferenceNumber;
	private Date bookingDateTime;
	private Integer numberOfSpacesBooked;
	private BigDecimal totalAmount;
	private Boolean paymentRecursive;
	private String paymentFrequency;
	private Integer numberOfPayments;
	private BigDecimal firstPaymentAmount;
	private BigDecimal regularPaymentAmount;
	private BigDecimal lastPaymentAmount;
	private Integer numberOfPaymentsMade;
	private DateTime nextPaymentDateTime;
	private String vehicleMake;
	private String vehicleModel;
	private String modelYear;
	private String licencePlateNumber;
	private String status;
	private DateTime cancelationDateTime;
	private Long cancelledBy;
	
	@JsonSerialize(using = CustomLocalDateSerializer.class)
	@JsonDeserialize(using = ISO8601LocalDateDeserializer.class)
	private LocalDate startDate;
	
	@JsonSerialize(using = CustomLocalDateSerializer.class)
	@JsonDeserialize(using = ISO8601LocalDateDeserializer.class)
	private LocalDate endDate;
	
	@JsonSerialize(using = CustomLocalTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalTimeDeserializer.class)
	private LocalTime startTime;
	
	@JsonSerialize(using = CustomLocalTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalTimeDeserializer.class)
	private LocalTime endTime;
	private String repeatBasis;
	private Integer repeatOccurrences;
	
	private Set<BookedParkingSpace> bookedParkingSpaces = new HashSet<>();
	private Set<PaymentCharged> paymentsCharged = new HashSet<>();
	private AvailableParkingDTO availableParkingDTO = new AvailableParkingDTO();
	
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	public LocalTime getStartTime() {
		return startTime;
	}
	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}
	public LocalTime getEndTime() {
		return endTime;
	}
	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}
	public String getRepeatBasis() {
		return repeatBasis;
	}
	public void setRepeatBasis(String repeatBasis) {
		this.repeatBasis = repeatBasis;
	}
	public Integer getRepeatOccurrences() {
		return repeatOccurrences;
	}
	public void setRepeatOccurrences(Integer repeatOccurrences) {
		this.repeatOccurrences = repeatOccurrences;
	}
	public Long getAvailabilityScheduleId() {
		return availabilityScheduleId;
	}
	public void setAvailabilityScheduleId(Long availabilityScheduleId) {
		this.availabilityScheduleId = availabilityScheduleId;
	}
	public Long getParkingSpaceId() {
		return parkingSpaceId;
	}
	public void setParkingSpaceId(Long parkingSpaceId) {
		this.parkingSpaceId = parkingSpaceId;
	}
	public Long getLocationId() {
		return locationId;
	}
	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}
	public Long getBookingId() {
		return bookingId;
	}
	public void setBookingId(Long bookingId) {
		this.bookingId = bookingId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getBookingScheduleId() {
		return bookingScheduleId;
	}
	public void setBookingScheduleId(Long bookingScheduleId) {
		this.bookingScheduleId = bookingScheduleId;
	}
	public String getBookingReferenceNumber() {
		return bookingReferenceNumber;
	}
	public void setBookingReferenceNumber(String bookingReferenceNumber) {
		this.bookingReferenceNumber = bookingReferenceNumber;
	}
	public Date getBookingDateTime() {
		return bookingDateTime;
	}
	public void setBookingDateTime(Date bookingDateTime) {
		this.bookingDateTime = bookingDateTime;
	}
	public Integer getNumberOfSpacesBooked() {
		return numberOfSpacesBooked;
	}
	public void setNumberOfSpacesBooked(Integer numberOfSpacesBooked) {
		this.numberOfSpacesBooked = numberOfSpacesBooked;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public Boolean getPaymentRecursive() {
		return paymentRecursive;
	}
	public void setPaymentRecursive(Boolean paymentRecursive) {
		this.paymentRecursive = paymentRecursive;
	}
	public String getPaymentFrequency() {
		return paymentFrequency;
	}
	public void setPaymentFrequency(String paymentFrequency) {
		this.paymentFrequency = paymentFrequency;
	}
	public Integer getNumberOfPayments() {
		return numberOfPayments;
	}
	public void setNumberOfPayments(Integer numberOfPayments) {
		this.numberOfPayments = numberOfPayments;
	}
	public BigDecimal getFirstPaymentAmount() {
		return firstPaymentAmount;
	}
	public void setFirstPaymentAmount(BigDecimal firstPaymentAmount) {
		this.firstPaymentAmount = firstPaymentAmount;
	}
	public BigDecimal getRegularPaymentAmount() {
		return regularPaymentAmount;
	}
	public void setRegularPaymentAmount(BigDecimal regularPaymentAmount) {
		this.regularPaymentAmount = regularPaymentAmount;
	}
	public BigDecimal getLastPaymentAmount() {
		return lastPaymentAmount;
	}
	public void setLastPaymentAmount(BigDecimal lastPaymentAmount) {
		this.lastPaymentAmount = lastPaymentAmount;
	}
	public Integer getNumberOfPaymentsMade() {
		return numberOfPaymentsMade;
	}
	public void setNumberOfPaymentsMade(Integer numberOfPaymentsMade) {
		this.numberOfPaymentsMade = numberOfPaymentsMade;
	}
	public DateTime getNextPaymentDateTime() {
		return nextPaymentDateTime;
	}
	public void setNextPaymentDateTime(DateTime nextPaymentDateTime) {
		this.nextPaymentDateTime = nextPaymentDateTime;
	}
	public String getVehicleMake() {
		return vehicleMake;
	}
	public void setVehicleMake(String vehicleMake) {
		this.vehicleMake = vehicleMake;
	}
	public String getVehicleModel() {
		return vehicleModel;
	}
	public void setVehicleModel(String vehicleModel) {
		this.vehicleModel = vehicleModel;
	}
	public String getModelYear() {
		return modelYear;
	}
	public void setModelYear(String modelYear) {
		this.modelYear = modelYear;
	}
	public String getLicencePlateNumber() {
		return licencePlateNumber;
	}
	public void setLicencePlateNumber(String licencePlateNumber) {
		this.licencePlateNumber = licencePlateNumber;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public DateTime getCancelationDateTime() {
		return cancelationDateTime;
	}
	public void setCancelationDateTime(DateTime cancelationDateTime) {
		this.cancelationDateTime = cancelationDateTime;
	}
	public Long getCancelledBy() {
		return cancelledBy;
	}
	public void setCancelledBy(Long cancelledBy) {
		this.cancelledBy = cancelledBy;
	}
	public Set<BookedParkingSpace> getBookedParkingSpaces() {
		return bookedParkingSpaces;
	}
	public void setBookedParkingSpaces(Set<BookedParkingSpace> bookedParkingSpaces) {
		this.bookedParkingSpaces = bookedParkingSpaces;
	}
	public Set<PaymentCharged> getPaymentsCharged() {
		return paymentsCharged;
	}
	public void setPaymentsCharged(Set<PaymentCharged> paymentsCharged) {
		this.paymentsCharged = paymentsCharged;
	}
	public AvailableParkingDTO getAvailableParkingDTO() {
		return availableParkingDTO;
	}
	public void setAvailableParkingDTO(AvailableParkingDTO availableParkingDTO) {
		this.availableParkingDTO = availableParkingDTO;
	}
	
}
