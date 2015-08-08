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
	private Map<String, BigDecimal> ratesMap = new HashMap<>();
	
	
	
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
	public Map<String, BigDecimal> getRatesMap() {
		return ratesMap;
	}
	public void setRatesMap(
			Map<String, BigDecimal> parkingSpacePriceEntrys) {
		this.ratesMap = parkingSpacePriceEntrys;
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
