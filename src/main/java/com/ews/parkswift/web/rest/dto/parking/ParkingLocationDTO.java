package com.ews.parkswift.web.rest.dto.parking;


import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import com.ews.parkswift.domain.Feedback;
import com.ews.parkswift.domain.ParkingLocationContactInfo;
import com.ews.parkswift.domain.ParkingLocationFacility;
import com.ews.parkswift.domain.ParkingLocationImage;
import com.ews.parkswift.domain.ParkingSpace;
import com.ews.parkswift.domain.PaypallAccount;
import com.ews.parkswift.domain.User;
import com.ews.parkswift.web.rest.dto.ParkingSpaceDTO;

/**
 * A ParkingLocation.
 */
public class ParkingLocationDTO {
	
    private Long id;
    private String bussinessType;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String country;
    private String zipCode;
    private BigDecimal longitude;
    private BigDecimal lattitude;
    private Boolean active = true;
    private ParkingLocationContactInfo parkingLocationContactInfo;
    private PaypallAccount paypallAccount;
    private User user;
    private Set<ParkingLocationFacility> parkingLocationFacilitys = new HashSet<>();
    private Set<Feedback> feedbacks = new HashSet<>();
    private Set<ParkingLocationImage> parkingLocationImages = new HashSet<>();
    private Set<ParkingSpaceDTO> parkingSpaces = new HashSet<>();
    private Set<ParkingSpace>  parkingSpaceEntitys = new HashSet<>(); // in case you need full entity somewhere in code
    

    public Set<ParkingLocationImage> getParkingLocationImages() {
		return parkingLocationImages;
	}

	public void setParkingLocationImages(
			Set<ParkingLocationImage> parkingLocationImages) {
		this.parkingLocationImages = parkingLocationImages;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public ParkingLocationContactInfo getParkingLocationContactInfo() {
        return parkingLocationContactInfo;
    }

    public void setParkingLocationContactInfo(ParkingLocationContactInfo parkingLocationContactInfo) {
        this.parkingLocationContactInfo = parkingLocationContactInfo;
    }

    public PaypallAccount getPaypallAccount() {
        return paypallAccount;
    }

    public void setPaypallAccount(PaypallAccount paypallAccount) {
        this.paypallAccount = paypallAccount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<ParkingLocationFacility> getParkingLocationFacilitys() {
        return parkingLocationFacilitys;
    }

    public void setParkingLocationFacilitys(Set<ParkingLocationFacility> parkingLocationFacilitys) {
        this.parkingLocationFacilitys = parkingLocationFacilitys;
    }

    public Set<ParkingSpaceDTO> getParkingSpaces() {
        return parkingSpaces;
    }

    public void setParkingSpaces(Set<ParkingSpaceDTO> parkingSpaces) {
        this.parkingSpaces = parkingSpaces;
    }
    
    public Set<ParkingSpace> getParkingSpaceEntitys() {
		return parkingSpaceEntitys;
	}

	public void setParkingSpaceEntitys(Set<ParkingSpace> parkingSpaceEntitys) {
		this.parkingSpaceEntitys = parkingSpaceEntitys;
	}

	public Set<Feedback> getFeedbacks() {
		return feedbacks;
	}

	public void setFeedbacks(Set<Feedback> feedbacks) {
		this.feedbacks = feedbacks;
	}
	
	public Boolean isActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

}
