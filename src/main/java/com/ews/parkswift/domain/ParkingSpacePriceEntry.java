package com.ews.parkswift.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotEmpty;

import com.ews.parkswift.startup.ApplicationStartup.LookupHeaderCode;
import com.ews.parkswift.vo.InLookupHeader;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A ParkingSpacePriceEntry.
 */
@Entity
@Table(name = "PARKING_SPACE_PRICE_ENTRY")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ParkingSpacePriceEntry implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty
    @Column(name = "type", nullable = false)
    @InLookupHeader(code=LookupHeaderCode.PS_PP)
    private String type;

    @NotNull
    @Column(name = "price", precision=10, scale=2, nullable = false)
    private BigDecimal price;

    @ManyToOne
    @JsonIgnore
    private ParkingSpace parkingSpace;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public ParkingSpace getParkingSpace() {
        return parkingSpace;
    }

    public void setParkingSpace(ParkingSpace parkingSpace) {
        this.parkingSpace = parkingSpace;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ParkingSpacePriceEntry parkingSpacePriceEntry = (ParkingSpacePriceEntry) o;

        if ( ! Objects.equals(type, parkingSpacePriceEntry.type)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type);
    }

    @Override
    public String toString() {
        return "ParkingSpacePriceEntry{" +
                "id=" + id +
                ", type='" + type + "'" +
                ", price='" + price + "'" +
                '}';
    }
}
