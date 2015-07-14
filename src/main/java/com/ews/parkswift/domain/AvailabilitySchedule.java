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
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import com.ews.parkswift.domain.util.CustomLocalDateSerializer;
import com.ews.parkswift.domain.util.CustomLocalTimeDeserializer;
import com.ews.parkswift.domain.util.CustomLocalTimeSerializer;
import com.ews.parkswift.domain.util.ISO8601LocalDateDeserializer;
import com.ews.parkswift.startup.ApplicationStartup.LookupHeaderCode;
import com.ews.parkswift.validation.InLookupHeader;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * A availabilitySchedule.
 */
@Entity
@Table(name = "AVAILABILITY_SCHEDULE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AvailabilitySchedule implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Future
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = ISO8601LocalDateDeserializer.class)
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Future
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = ISO8601LocalDateDeserializer.class)
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @NotNull
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalTime")
    @JsonSerialize(using = CustomLocalTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalTimeDeserializer.class)
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @NotNull
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalTime")
    @JsonSerialize(using = CustomLocalTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalTimeDeserializer.class)
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "repeat_basis")
    @InLookupHeader(code=LookupHeaderCode.REP_BASIS)
    private String repeatBasis;

    
    @Column(name = "repeat_after_every")
    @InLookupHeader(code=LookupHeaderCode.REP_AFTR_EVRY)
    private Integer repeatAfterEvery;
    
    @Size(max=10)
    @Column(name = "repeat_end_basis", length=10)
    @InLookupHeader(code=LookupHeaderCode.REP_END_BASIS)
    private String repeatEndBasis;
    
    @Column(name = "repeat_end_occurrences")
    private Integer repeatEndOccurrences;
    
    
    @Future
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = ISO8601LocalDateDeserializer.class)
    @Column(name = "repeat_end_date")
    private LocalDate repeatEndDate;
    
    
    @Size(max=20)
    @Column(name = "repeat_by", length=20)
    @InLookupHeader(code=LookupHeaderCode.REP_BY)
    private String repeatBy;
    

    @ManyToOne
    @JsonIgnore
    private ParkingSpace parkingSpace;
    
    @Valid
    @OneToMany(mappedBy = "availabilitySchedule",fetch=FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval=true)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AvailabilityScheduleRepeatOn> availabilityScheduleRepeatOns = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }


    public Integer getRepeatEndOccurrences() {
        return repeatEndOccurrences;
    }
    
    

    public void setRepeatEndOccurrences(Integer repeatOccurrences) {
        this.repeatEndOccurrences = repeatOccurrences;
    }

    public ParkingSpace getParkingSpace() {
        return parkingSpace;
    }

    public void setParkingSpace(ParkingSpace parkingSpace) {
        this.parkingSpace = parkingSpace;
    }

    public Set<AvailabilityScheduleRepeatOn> getAvailabilityScheduleRepeatOns() {
        return availabilityScheduleRepeatOns;
    }

    public void setAvailabilityScheduleRepeatOns(Set<AvailabilityScheduleRepeatOn> availabilityScheduleRepeatOns) {
        this.availabilityScheduleRepeatOns = availabilityScheduleRepeatOns;
        this.availabilityScheduleRepeatOns.forEach((e)->{e.setAvailabilitySchedule(this);});
    }
    
    

    public Integer getRepeatAfterEvery() {
		return repeatAfterEvery;
	}

	public void setRepeatAfterEvery(Integer repeatAfterEvery) {
		this.repeatAfterEvery = repeatAfterEvery;
	}

	public String getRepeatEndBasis() {
		return repeatEndBasis;
	}

	public void setRepeatEndBasis(String repeatEndBasis) {
		this.repeatEndBasis = repeatEndBasis;
	}

	public String getRepeatBy() {
		return repeatBy;
	}

	public void setRepeatBy(String repeatBy) {
		this.repeatBy = repeatBy;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AvailabilitySchedule availabilitySchedule = (AvailabilitySchedule) o;

        if ( ! Objects.equals(startDate, availabilitySchedule.startDate) && Objects.equals(endDate, availabilitySchedule.endDate) &&
        		Objects.equals(startTime, availabilitySchedule.startTime) && Objects.equals(endTime, availabilitySchedule.endTime)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(startDate)+Objects.hashCode(endDate)+Objects.hashCode(startTime)+Objects.hashCode(endTime);
    }
    
    

    public String getRepeatBasis() {
		return repeatBasis;
	}

	public void setRepeatBasis(String repeatBasis) {
		this.repeatBasis = repeatBasis;
	}

	public LocalDate getRepeatEndDate() {
		return repeatEndDate;
	}

	public void setRepeatEndDate(LocalDate repeatEndDate) {
		this.repeatEndDate = repeatEndDate;
	}

	@Override
    public String toString() {
        return "AvailableSchedule{" +
                "id=" + id +
                ", startDate='" + startDate + "'" +
                ", endDate='" + endDate + "'" +
                ", startTime='" + startTime + "'" +
                ", endTime='" + endTime + "'" +
                ", repeatOn='" + repeatBasis + "'" +
                ", repeatOccurrences='" + repeatEndOccurrences + "'" +
                '}';
    }
}
