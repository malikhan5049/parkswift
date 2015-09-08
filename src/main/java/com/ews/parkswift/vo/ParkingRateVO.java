package com.ews.parkswift.vo;

import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import com.ews.parkswift.domain.util.CustomLocalDateTimeDeserializer;
import com.ews.parkswift.domain.util.CustomLocalDateTimeSerializer;
import com.ews.parkswift.domain.util.CustomLocalTimeDeserializer;
import com.ews.parkswift.domain.util.CustomLocalTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class ParkingRateVO {

	public ParkingRateVO(String pricePlan, double pricePlanRate,
			double totalCost, LocalTime pricePlanStartTime,
			LocalTime pricePlanEndTime, LocalDateTime rateAppliedFrom,
			LocalDateTime rateAppliedTill) {
		super();
		this.pricePlan = pricePlan;
		this.pricePlanRate = pricePlanRate;
		this.totalCost = totalCost;
		this.pricePlanStartTime = pricePlanStartTime;
		this.pricePlanEndTime = pricePlanEndTime;
		this.rateAppliedFrom = rateAppliedFrom;
		this.rateAppliedTill = rateAppliedTill;
	}

	private String pricePlan;
	private double pricePlanRate;
	private double totalCost;

	@JsonSerialize(using = CustomLocalTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalTimeDeserializer.class)
	private LocalTime pricePlanStartTime;

	@JsonSerialize(using = CustomLocalTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalTimeDeserializer.class)
	private LocalTime pricePlanEndTime;

	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime rateAppliedFrom;

	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime rateAppliedTill;

	public String getPricePlan() {
		return pricePlan;
	}

	public void setPricePlan(String pricePlan) {
		this.pricePlan = pricePlan;
	}

	public double getPricePlanRate() {
		return pricePlanRate;
	}

	public void setPricePlanRate(double pricePlanRate) {
		this.pricePlanRate = pricePlanRate;
	}

	public double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}

	public LocalTime getPricePlanStartTime() {
		return pricePlanStartTime;
	}

	public void setPricePlanStartTime(LocalTime pricePlanStartTime) {
		this.pricePlanStartTime = pricePlanStartTime;
	}

	public LocalTime getPricePlanEndTime() {
		return pricePlanEndTime;
	}

	public void setPricePlanEndTime(LocalTime pricePlanEndTime) {
		this.pricePlanEndTime = pricePlanEndTime;
	}

	public LocalDateTime getRateAppliedFrom() {
		return rateAppliedFrom;
	}

	public void setRateAppliedFrom(LocalDateTime rateAppliedFrom) {
		this.rateAppliedFrom = rateAppliedFrom;
	}

	public LocalDateTime getRateAppliedTill() {
		return rateAppliedTill;
	}

	public void setRateAppliedTill(LocalDateTime rateAppliedTill) {
		this.rateAppliedTill = rateAppliedTill;
	}
}
