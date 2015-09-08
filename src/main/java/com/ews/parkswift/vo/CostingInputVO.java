/**
 * 
 */
package com.ews.parkswift.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import com.ews.parkswift.domain.util.CustomLocalDateSerializer;
import com.ews.parkswift.domain.util.CustomLocalTimeDeserializer;
import com.ews.parkswift.domain.util.CustomLocalTimeSerializer;
import com.ews.parkswift.domain.util.ISO8601LocalDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author ali.khan
 *
 */
public class CostingInputVO implements Serializable {

	private static final long serialVersionUID = -2266121139201519054L;

	@JsonSerialize(using = CustomLocalDateSerializer.class)
	@JsonDeserialize(using = ISO8601LocalDateDeserializer.class)
	private LocalDate startDate;

	@JsonSerialize(using = CustomLocalDateSerializer.class)
	@JsonDeserialize(using = ISO8601LocalDateDeserializer.class)
	private LocalDate endDate;

	@JsonSerialize(using = CustomLocalTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalTimeDeserializer.class)
	private LocalTime startTime;
	
	@JsonSerialize(using = CustomLocalTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalTimeDeserializer.class)
	private LocalTime endTime;
	
	private LocalDateTime startDateTime;

	private LocalDateTime endDateTime;
	
	private Map<String, BigDecimal> ratesMap = new HashMap<>();

	public CostingInputVO() {
		super();
	}
	
	public CostingInputVO(LocalDate startDate, LocalDate endDate,
			LocalTime startTime, LocalTime endTime,
			Map<String, BigDecimal> ratesMap) {
		super();
		this.startDate = startDate;
		this.endDate = endDate;
		this.startTime = startTime;
		this.endTime = endTime;
		this.ratesMap = ratesMap;
	}
	
	public void constructStartDateTime() {
		startDateTime = new LocalDateTime(startDate.getYear(),
				startDate.getMonthOfYear(), startDate.getDayOfMonth(),
				startTime.getHourOfDay(), startTime.getMinuteOfHour());
	}

	public void constructEndDateTime() {
		endDateTime = new LocalDateTime(endDate.getYear(),
				endDate.getMonthOfYear(), endDate.getDayOfMonth(),
				endTime.getHourOfDay(), endTime.getMinuteOfHour());
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
	
	public Map<String, BigDecimal> getRatesMap() {
		return ratesMap;
	}

	public void setRatesMap(Map<String, BigDecimal> parkingSpacePriceEntrys) {
		this.ratesMap = parkingSpacePriceEntrys;
	}
	
	public LocalDateTime getStartDateTime() {
		return startDateTime;
	}

	public LocalDateTime getEndDateTime() {
		return endDateTime;
	}

	public void setStartDateTime(LocalDateTime startDateTime) {
		this.startDateTime = startDateTime;
		if (startDateTime != null) {
			this.startDate = this.startDateTime.toLocalDate();
			this.startTime = this.startDateTime.toLocalTime();
		}
	}

	public void setEndDateTime(LocalDateTime endDateTime) {
		this.endDateTime = endDateTime;
		if (endDateTime != null) {
			this.endDate = this.startDateTime.toLocalDate();
			this.endTime = this.startDateTime.toLocalTime();
		}
	}
	
	
}
