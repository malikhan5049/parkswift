package com.ews.parkswift.domain;

import java.io.Serializable;
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
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * A ParkingSpace.
 */
@Entity
@Table(name = "PARKING_SPACE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ParkingSpace implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "description")
    private String description;
    
    @Column(name = "number_of_spaces", nullable = false)
    private Integer numberOfSpaces;
    
    @Column(name = "nick")
    private String nick;

    @Column(name = "group_record")
    @JsonIgnore
    private Boolean groupRecord;

    @NotNull
    @ManyToOne
    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    private ParkingLocation parkingLocation;
    
    @Valid
    @NotEmpty
    @OneToMany(mappedBy="parkingSpace",fetch=FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval=true)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ParkingSpaceVehicleType> parkingSpaceVehicleTypes = new HashSet<>();

    @Valid
    @NotEmpty
    @OneToMany(mappedBy="parkingSpace",fetch=FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval=true)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ParkingSpacePriceEntry> parkingSpacePriceEntrys = new HashSet<>();
    
    @Valid
    @NotEmpty
    @OneToMany(mappedBy="parkingSpace",fetch=FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval=true)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AvailabilitySchedule> availabilitySchedules = new HashSet<>();
    
    @OneToMany(mappedBy = "parkingSpace",fetch=FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval=true)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<BookedParkingSpace> bookedParkingSpace = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNumberOfSpaces() {
        return numberOfSpaces;
    }

    public void setNumberOfSpaces(Integer numberOfSpaces) {
        this.numberOfSpaces = numberOfSpaces;
    }

    public Boolean getGroupRecord() {
        return groupRecord;
    }

    public void setGroupRecord(Boolean groupRecord) {
        this.groupRecord = groupRecord;
    }



    public ParkingLocation getParkingLocation() {
        return parkingLocation;
    }
    
    @SuppressWarnings("serial")
	@JsonSetter("parkingLocation")
    public void setParkingLocation(Long parkingLocationId) {
        this.parkingLocation = new ParkingLocation(){{setId(parkingLocationId);}};
    }

    public void setParkingLocation(ParkingLocation parkingLocation) {
        this.parkingLocation = parkingLocation;
    }

    public Set<ParkingSpaceVehicleType> getParkingSpaceVehicleTypes() {
        return parkingSpaceVehicleTypes;
    }

    public void setParkingSpaceVehicleTypes(Set<ParkingSpaceVehicleType> parkingSpaceVehicleTypes) {
        this.parkingSpaceVehicleTypes = parkingSpaceVehicleTypes;
        this.parkingSpaceVehicleTypes.forEach((e)->{e.setParkingSpace(this);});
    }

    public Set<ParkingSpacePriceEntry> getParkingSpacePriceEntrys() {
        return parkingSpacePriceEntrys;
    }

    public void setParkingSpacePriceEntrys(Set<ParkingSpacePriceEntry> parkingSpacePriceEntrys) {
        this.parkingSpacePriceEntrys = parkingSpacePriceEntrys;
        this.parkingSpacePriceEntrys.forEach((e)->{e.setParkingSpace(this);});
    }

    public Set<AvailabilitySchedule> getAvailabilitySchedules() {
        return availabilitySchedules;
    }

    public void setAvailabilitySchedules(Set<AvailabilitySchedule> availabilitySchedules) {
        this.availabilitySchedules = availabilitySchedules;
        this.availabilitySchedules.forEach((e)->{e.setParkingSpace(this);});
    }
    
    public Set<BookedParkingSpace> getBookedParkingSpace() {
        return bookedParkingSpace;
    }

    public void setBookedParkingSpace(Set<BookedParkingSpace> bookedParkingSpace) {
        this.bookedParkingSpace = bookedParkingSpace;
    }
    
	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ParkingSpace parkingSpace = (ParkingSpace) o;

        if ( ! Objects.equals(id, parkingSpace.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ParkingSpace{" +
                "id=" + id +
                ", description='" + description + "'" +
                ", numberOfSpaces='" + numberOfSpaces + "'" +
                ", groupRecord='" + groupRecord + "'" +
                '}';
    }
}
