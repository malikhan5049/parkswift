package com.ews.parkswift.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ReservedParkingRepeatOn.
 */
@Entity
@Table(name = "BOOKING_SCHEDULE_REPEAT_ON")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BookingScheduleRepeatOn implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @Column(name = "day_of_week")
    private String dayOfWeek;

    @ManyToOne
    private BookingSchedule bookingSchedule;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public BookingSchedule getBookingSchedule() {
        return bookingSchedule;
    }

    public void setBookingSchedule(BookingSchedule bookingSchedule) {
        this.bookingSchedule = bookingSchedule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BookingScheduleRepeatOn bookingScheduleRepeatOn = (BookingScheduleRepeatOn) o;

        if ( ! Objects.equals(id, bookingScheduleRepeatOn.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BookingScheduleRepeatOn{" +
                "id=" + id +
                ", dayOfWeek='" + dayOfWeek + "'" +
                '}';
    }
}
