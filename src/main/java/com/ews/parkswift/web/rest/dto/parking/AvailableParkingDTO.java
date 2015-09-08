package com.ews.parkswift.web.rest.dto.parking;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ews.parkswift.config.Constants;
import com.ews.parkswift.domain.ParkingSpacePriceEntry;
import com.ews.parkswift.domain.PriceHit;
import com.ews.parkswift.vo.ParkingRateVO;
import com.ews.parkswift.web.rest.dto.ParkingSpaceDTO;

public class AvailableParkingDTO {

	private Long locId;

	private Long parkingSpaceId;

	private Long availabilityScheduleId;

	private String bussinessType;

	private String addressLine1;

	private String addressLine2;

	private String city;

	private String state;

	private String country;

	private String zipCode;

	private BigDecimal longitude;

	private BigDecimal lattitude;
	
	private boolean isFavourite=false;

	private Double distance = Constants.DEFAULT_SEARCH_DISTANCE;

	private BigDecimal cost;

	private Set<ParkingSpacePriceEntry> parkingSpacePriceEntrys = new HashSet<>();

	private Set<String> parkingLocationFacilitys = new HashSet<String>();

	private List<String> parkingLocationImages = new ArrayList<String>();

	private Set<String> parkingSpaceVehicleTypes = new HashSet<String>();

    private Set<ParkingSpaceDTO> parkingSpaces = new HashSet<>();
    
    private Set<ParkingRateVO> parkingRates = new HashSet<ParkingRateVO>();

    public Set<ParkingRateVO> getParkingRates() {
     return parkingRates;
    }

    public void setParkingRates(Set<ParkingRateVO> parkingRates) {
     this.parkingRates = parkingRates;
    }
    
	public Set<ParkingSpaceDTO> getParkingSpaces() {
		return parkingSpaces;
	}

	public void setParkingSpaces(Set<ParkingSpaceDTO> parkingSpaces) {
		this.parkingSpaces = parkingSpaces;
	}

	public Set<String> getParkingSpaceVehicleTypes() {
		return parkingSpaceVehicleTypes;
	}

	public void setParkingSpaceVehicleTypes(
			Set<String> parkingSpaceVehicleTypes) {
		this.parkingSpaceVehicleTypes = parkingSpaceVehicleTypes;
	}

	public Set<String> getParkingLocationFacilitys() {
		return parkingLocationFacilitys;
	}

	public void setParkingLocationFacilitys(
			Set<String> parkingLocationFacilitys) {
		this.parkingLocationFacilitys = parkingLocationFacilitys;
	}

	public List<String> getParkingLocationImages() {
		return parkingLocationImages;
	}

	public void setParkingLocationImages(List<String> parkingLocationImages) {
		this.parkingLocationImages = parkingLocationImages;
	}

	private String distanceUnit;

	private String nick;

	private Set<PriceHit> priceHits = new HashSet<PriceHit>();

	public Set<PriceHit> getPriceHits() {
		return priceHits;
	}

	public void setPriceHits(Set<PriceHit> priceHits) {
		this.priceHits = priceHits;
	}

	public Long getParkingSpaceId() {
		return parkingSpaceId;
	}

	public void setParkingSpaceId(Long parkingSpaceId) {
		this.parkingSpaceId = parkingSpaceId;
	}

	public Long getAvailabilityScheduleId() {
		return availabilityScheduleId;
	}

	public void setAvailabilityScheduleId(Long availabilityScheduleId) {
		this.availabilityScheduleId = availabilityScheduleId;
	}

	public String getBussinessType() {
		return bussinessType;
	}

	public void setBussinessType(String bussinessType) {
		this.bussinessType = bussinessType;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
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

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public Set<ParkingSpacePriceEntry> getParkingSpacePriceEntrys() {
		return parkingSpacePriceEntrys;
	}

	public void setParkingSpacePriceEntrys(
			Set<ParkingSpacePriceEntry> parkingSpacePriceEntrys) {
		this.parkingSpacePriceEntrys = parkingSpacePriceEntrys;
	}

	public void setDistanceUnit(String distanceUnit) {
		this.distanceUnit = distanceUnit;

	}

	public String getDistanceUnit() {
		return distanceUnit;
	}

	public void setNick(String nick) {
		this.nick = nick;

	}

	public String getNick() {
		return nick;
	}

	public Long getLocId() {
		return locId;
	}

	public void setLocId(Long locId) {
		this.locId = locId;
	}
	
	public boolean isFavourite() {
		return isFavourite;
	}

	public void setFavourite(boolean isFavourite) {
		this.isFavourite = isFavourite;
	}


}
