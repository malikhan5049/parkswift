package com.ews.parkswift.web.rest.dto.parking;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import com.ews.parkswift.config.Constants;
import com.ews.parkswift.domain.AvailabilitySchedule;
import com.ews.parkswift.domain.ParkingLocationFacility;
import com.ews.parkswift.domain.ParkingSpacePriceEntry;
import com.ews.parkswift.domain.ParkingSpaceVehicleType;
import com.ews.parkswift.startup.ApplicationStartup.LookupHeaderCode;
import com.ews.parkswift.vo.InLookupHeader;

public class FindParkingsDTO {
	
    @InLookupHeader(code = LookupHeaderCode.LOC_TYPE)
    private String bussinessType;
	
	@NotNull
	@Digits(integer=10, fraction = 7)
    private BigDecimal longitude;

    @NotNull
    @Digits(integer=10, fraction = 7)
    private BigDecimal lattitude;
    
    private Double distance =Constants.DEFAULT_SEARCH_DISTANCE;
    
    private String distanceUnit = Constants.SearchDistanceUnit.MILE.toString().toLowerCase();
    
    private Integer numberOfSpaces = 1;
    
    @Valid
    @NotNull
	private AvailabilitySchedule availabilitySchedule;
    
    
    @Valid
    private Set<ParkingLocationFacility> parkingLocationFacilitys = new HashSet<>();
    
    
    @Valid
	private Set<ParkingSpaceVehicleType> parkingSpaceVehicleTypes = new HashSet<>();
    @Valid
	private Set<ParkingSpacePriceEntry> parkingSpacePriceEntrys = new HashSet<>();
	
    private Set<String> repeatOnWeekDays = new HashSet<>();
    
    public Set<String> getRepeatOnWeekDays() {
     return repeatOnWeekDays;
    }
    public void setRepeatOnWeekDays(Set<String> repeatOnWeekDays) {
     this.repeatOnWeekDays = repeatOnWeekDays;
    }
	
	public Integer getNumberOfSpaces() {
		return numberOfSpaces;
	}
	public void setNumberOfSpaces(Integer numberOfSpaces) {
		this.numberOfSpaces = numberOfSpaces;
	}
	public Set<ParkingSpaceVehicleType> getParkingSpaceVehicleTypes() {
		return parkingSpaceVehicleTypes;
	}
	public void setParkingSpaceVehicleTypes(
			Set<ParkingSpaceVehicleType> parkingSpaceVehicleTypes) {
		this.parkingSpaceVehicleTypes = parkingSpaceVehicleTypes;
	}
	public Set<ParkingSpacePriceEntry> getParkingSpacePriceEntrys() {
		return parkingSpacePriceEntrys;
	}
	public void setParkingSpacePriceEntrys(
			Set<ParkingSpacePriceEntry> parkingSpacePriceEntrys) {
		this.parkingSpacePriceEntrys = parkingSpacePriceEntrys;
	}
	
    
	public AvailabilitySchedule getAvailabilitySchedule() {
		return availabilitySchedule;
	}
	public void setAvailabilitySchedule(AvailabilitySchedule availabilitySchedule) {
		this.availabilitySchedule = availabilitySchedule;
	}
	public String getBussinessType() {
		return bussinessType;
	}
	public void setBussinessType(String bussinessType) {
		this.bussinessType = bussinessType;
	}
	public BigDecimal getLongitude() {
		return longitude;
	}
	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}
	public BigDecimal getLattitude() {
		return lattitude;
	}
	public void setLattitude(BigDecimal lattitude) {
		this.lattitude = lattitude;
	}
	public Double getDistance() {
		return distance;
	}
	public void setDistance(Double distance) {
		this.distance = distance;
	}
	public String getDistanceUnit() {
		return distanceUnit;
	}
	public void setDistanceUnit(String distanceUnit) {
		this.distanceUnit = distanceUnit;
	}
	
	
	public Set<ParkingLocationFacility> getParkingLocationFacilitys() {
		return parkingLocationFacilitys;
	}
	public void setParkingLocationFacilitys(
			Set<ParkingLocationFacility> parkingLocationFacilitys) {
		this.parkingLocationFacilitys = parkingLocationFacilitys;
	}
	
	@Override
	public String toString() {
		return "ParkingLocationDTO [bussinessType=" + bussinessType
				+ ", longitude=" + longitude + ", lattitude=" + lattitude
				+ ", distance=" + distance + ", distanceUnit=" + distanceUnit
				+ "]";
	}
    
    
    
    
}
