/**
 * 
 */
package com.ews.parkswift.domain;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

/**
 * @author ali.khan
 *
 */
public class CostingInputVO {
	
	private LocalDate startDate;
	private LocalDate endDate;
	private LocalTime  startTime;
	private LocalTime endTime;
	private Map<String, BigDecimal> parkingSpacePriceEntrys = new HashMap<>();
	
	
	
	public CostingInputVO(LocalDate startDate, LocalDate endDate,
			LocalTime startTime, LocalTime endTime,
			Map<String, BigDecimal> parkingSpacePriceEntrys) {
		super();
		this.startDate = startDate;
		this.endDate = endDate;
		this.startTime = startTime;
		this.endTime = endTime;
		this.parkingSpacePriceEntrys = parkingSpacePriceEntrys;
	}
	public Map<String, BigDecimal> getParkingSpacePriceEntrys() {
		return parkingSpacePriceEntrys;
	}
	public void setParkingSpacePriceEntrys(
			Map<String, BigDecimal> parkingSpacePriceEntrys) {
		this.parkingSpacePriceEntrys = parkingSpacePriceEntrys;
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
	
	
}
