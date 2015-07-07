package com.ews.parkswift.domain;

import com.ews.parkswift.validation.ValidMonthDay;
import com.ews.parkswift.validation.ValidWeekDay;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A AvailableParkingRepeatOn.
 */
@Entity
@Table(name = "PARKING_SPACE_AVAILABILITY_REPEAT_ON")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AvailableParkingRepeatOn implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "day_of_month")
    @ValidMonthDay
    private Integer dayOfMonth;
    
    @Column(name = "date_of_week")
    @ValidWeekDay
    private String dayOfWeek;

    @ManyToOne
    @JsonIgnore
    private AvailableParking availableParking;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(Integer dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public AvailableParking getAvailableParking() {
        return availableParking;
    }

    public void setAvailableParking(AvailableParking availableParking) {
        this.availableParking = availableParking;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AvailableParkingRepeatOn availableParkingRepeatOn = (AvailableParkingRepeatOn) o;

        if ( (dayOfWeek!=null && ! Objects.equals(dayOfWeek, availableParkingRepeatOn.dayOfWeek)) ||
        		(dayOfMonth!=null && ! Objects.equals(dayOfMonth, availableParkingRepeatOn.dayOfMonth))) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return dayOfWeek == null?Objects.hashCode(dayOfMonth):Objects.hashCode(dayOfWeek);
    }

    @Override
    public String toString() {
        return "AvailableParkingRepeatOn{" +
                "id=" + id +
                ", dayOfMonth='" + dayOfMonth + "'" +
                ", dateOfWeek='" + dayOfWeek + "'" +
                '}';
    }
}
