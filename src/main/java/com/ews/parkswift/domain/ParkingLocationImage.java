package com.ews.parkswift.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A ParkingSpaceImage.
 */

@Entity
@Table(name = "PARKING_LOCATION_IMAGE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ParkingLocationImage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Transient
    @JsonProperty
    private byte[] image;
    
    @Column(name = "url", nullable=false)
    @JsonProperty
    private String url;
    
    @NotNull
    @Column(name = "type")
    private String type;


	@ManyToOne
    @JsonIgnore
    private ParkingLocation parkingLocation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public ParkingLocation getParkingLocation() {
        return parkingLocation;
    }

    public void setParkingLocation(ParkingLocation parkingLocation) {
        this.parkingLocation = parkingLocation;
    }
    
    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
    public String getURL() {
		return url;
	}

	public void setURL(String url) {
		this.url = url;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ParkingLocationImage parkingLocationImage = (ParkingLocationImage) o;

        if ( ! Objects.equals(image, parkingLocationImage.image)) return false;
        else if ( ! Objects.equals(url, parkingLocationImage.url)) return false;
        else if ( ! Objects.equals(type, parkingLocationImage.type)) return false;

        return true;
    }

    @Override
    public int hashCode() {
    	return url==null? Objects.hashCode(image):Objects.hashCode(url);
    }
    
	@Override
    public String toString() {
        return "parkingLocationImage{" +
                "id=" + id +
                ", image='" + image + "'" +
                ", url='" + url + "'" +
                ", type='" + type + "'" +
                '}';
    }
}
