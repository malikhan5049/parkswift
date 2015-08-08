package com.ews.parkswift.domain;

import java.math.BigDecimal;

import org.joda.time.LocalTime;

public class PriceHit {
	
	private BigDecimal rate;
	private BigDecimal cost;
	private PricePlan pricePlan;
	private LocalTime startTime;
	private LocalTime endTime;
	
	
	public PriceHit(BigDecimal rate, BigDecimal cost, PricePlan pricePlan, LocalTime startTime,
			LocalTime endTime) {
		super();
		this.rate = rate;
		this.cost = cost;
		this.pricePlan = pricePlan;
		this.startTime = startTime;
		this.endTime = endTime;
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



	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public PricePlan getPricePlan() {
		return pricePlan;
	}

	public void setPricePlan(PricePlan pricePlan) {
		this.pricePlan = pricePlan;
	}









	@Override
	public String toString() {
		return "PriceHit [rate=" + rate + ", cost=" + cost + ", pricePlan="
				+ pricePlan + ", startTime=" + startTime + ", endTime="
				+ endTime + "]";
	}
	
	
	
}
