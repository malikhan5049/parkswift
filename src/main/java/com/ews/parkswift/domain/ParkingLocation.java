package com.ews.parkswift.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotEmpty;

import com.ews.parkswift.startup.ApplicationStartup.LookupHeaderCode;
import com.ews.parkswift.validation.InLookupHeader;
import com.ews.parkswift.web.rest.dto.ParkingSpaceDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * A ParkingLocation.
 */
@Entity
@Table(name = "PARKING_LOCATION")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ParkingLocation implements Serializable {
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty
    @Column(name = "bussiness_type", nullable = false)
    @InLookupHeader(code = LookupHeaderCode.LOC_TYPE)
    private String bussinessType;

    @NotEmpty
    @Column(name = "address_line1", nullable = false)
    private String addressLine1;

    @Column(name = "address_line2")
    private String addressLine2;

    @NotEmpty
    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "state")
    private String state;

    @NotEmpty
    @Column(name = "country", nullable = false)
    private String country;

    @NotEmpty
    @Column(name = "zip_code", nullable = false)
    private String zipCode;

    @NotNull
    @Digits(integer=10, fraction = 7)
    @Column(name = "longitude", precision=10, scale=7, nullable = false)
    private BigDecimal longitude;

    @NotNull
    @Digits(integer=10, fraction = 7)
    @Column(name = "lattitude", precision=10, scale=7, nullable = false)
    private BigDecimal lattitude;
    
    @Column(name = "active")
    private Boolean active = true;
    
    @Valid
    @OneToOne(cascade=CascadeType.ALL, orphanRemoval=true)
    private ParkingLocationContactInfo parkingLocationContactInfo;
    
    @Valid
    @NotNull
    @ManyToOne(fetch=FetchType.EAGER)
    private PaypallAccount paypallAccount;

    @ManyToOne
    @JsonIgnore
    private User user;
    
    @Valid
    @OneToMany(mappedBy = "parkingLocation",fetch=FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval=true)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ParkingLocationFacility> parkingLocationFacilitys = new HashSet<>();
    
    @OneToMany(mappedBy = "parkingLocation", fetch=FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Feedback> feedbacks = new HashSet<>();
    
    
    @Valid
    @OneToMany(mappedBy = "parkingLocation", fetch=FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval=true)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ParkingLocationImage> parkingLocationImages = new HashSet<>();
    
    @Transient
    @JsonSerialize
    private Set<ParkingSpaceDTO> parkingSpaces = new HashSet<>();
    
    @JsonIgnore
    @OneToMany(mappedBy = "parkingLocation",fetch=FetchType.LAZY)
    private Set<ParkingSpace>  parkingSpaceEntitys = new HashSet<>(); // in case you need full entity somewhere in code
    
    
    
    
    

    public Set<ParkingLocationImage> getParkingLocationImages() {
		return parkingLocationImages;
	}

	public void setParkingLocationImages(
			Set<ParkingLocationImage> parkingLocationImages) {
		this.parkingLocationImages = parkingLocationImages;
		this.parkingLocationImages.forEach((e)->{
			e.setParkingLocation(this);
		});
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
        parkingLocationFacilitys.forEach((e)->{
        	e.setParkingLocation(this);
        });
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

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ParkingLocation parkingLocation = (ParkingLocation) o;

        if ( ! Objects.equals(id, parkingLocation.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ParkingLocation{" +
                "id=" + id +
                ", bussinessType='" + bussinessType + "'" +
                ", addressLine1='" + addressLine1 + "'" +
                ", addressLine2='" + addressLine2 + "'" +
                ", city='" + city + "'" +
                ", state='" + state + "'" +
                ", country='" + country + "'" +
                ", zipCode='" + zipCode + "'" +
                ", longitude='" + longitude + "'" +
                ", lattitude='" + lattitude + "'" +
                '}';
    }
}
