package com.ews.parkswift.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.ews.parkswift.domain.util.CustomLocalDateTimeSerializer;
import com.ews.parkswift.domain.util.CustomLocalTimeDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * A ReservedParking.
 */
@Entity
@Table(name = "BOOKING_SCHEDULE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BookingSchedule implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalTimeDeserializer.class)
    @Column(name = "start_date_time", nullable = false)
    private DateTime startDateTime;

    @NotNull
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalTimeDeserializer.class)
    @Column(name = "end_date_time", nullable = false)
    private DateTime endDateTime;
 
    @Column(name = "repeat_basis")
    private String repeatBasis;

    @Column(name = "repeat_occurrences")
    private Integer repeatOccurrences;

    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "bookingSchedule")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<BookingScheduleRepeatOn> bookingScheduleRepeatOns = new HashSet<>();


    public void setStartDateTime(DateTime startDateTime) {
		this.startDateTime = startDateTime;
	}

	public void setEndDateTime(DateTime endDateTime) {
		this.endDateTime = endDateTime;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRepeatBasis() {
        return repeatBasis;
    }

    public void setRepeatBasis(String repeatOn) {
        this.repeatBasis = repeatOn;
    }

    public Integer getRepeatOccurrences() {
        return repeatOccurrences;
    }

    public void setRepeatOccurrences(Integer repeatOccurrences) {
        this.repeatOccurrences = repeatOccurrences;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<BookingScheduleRepeatOn> getBookingScheduleRepeatOns() {
        return bookingScheduleRepeatOns;
    }

    public void setBookingScheduleRepeatOns(Set<BookingScheduleRepeatOn> bookingScheduleRepeatOns) {
        this.bookingScheduleRepeatOns = bookingScheduleRepeatOns;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BookingSchedule bookingSchedule = (BookingSchedule) o;

        if ( ! Objects.equals(id, bookingSchedule.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BookingSchedule{" +
                "id=" + id +
                ", startDate='" + startDateTime + "'" +
                ", endDate='" + endDateTime + "'" +
                ", repeatOn='" + repeatBasis + "'" +
                ", repeatOccurrences='" + repeatOccurrences + "'" +
                ", status='" + status + "'" +
                '}';
    }

	public void constructStartDateTime() {
		startDateTime = new DateTime(startDateTime.getYear(),
				startDateTime.getMonthOfYear(), startDateTime.getDayOfMonth(),
				startDateTime.getHourOfDay(), startDateTime.getMinuteOfHour());
	}

	public void constructEndDateTime() {
		endDateTime = new DateTime(endDateTime.getYear(),
				endDateTime.getMonthOfYear(), endDateTime.getDayOfMonth(),
				endDateTime.getHourOfDay(), endDateTime.getMinuteOfHour());
	}
	
	public DateTime getStartDateTime() {
		if (startDateTime != null)
			return startDateTime;
		else {
			constructStartDateTime();
			return startDateTime;
		}
	}

	public DateTime getEndDateTime() {
		if (endDateTime != null)
			return endDateTime;
		else {
			constructEndDateTime();
			return endDateTime;
		}
	}
}
