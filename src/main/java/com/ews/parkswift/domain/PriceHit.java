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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cost == null) ? 0 : cost.hashCode());
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result
				+ ((pricePlan == null) ? 0 : pricePlan.hashCode());
		result = prime * result + ((rate == null) ? 0 : rate.hashCode());
		result = prime * result
				+ ((startTime == null) ? 0 : startTime.hashCode());
		return result;
	}









	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PriceHit other = (PriceHit) obj;
		if (cost == null) {
			if (other.cost != null)
				return false;
		} else if (!cost.equals(other.cost))
			return false;
		if (endTime == null) {
			if (other.endTime != null)
				return false;
		} else if (!endTime.equals(other.endTime))
			return false;
		if (pricePlan != other.pricePlan)
			return false;
		if (rate == null) {
			if (other.rate != null)
				return false;
		} else if (!rate.equals(other.rate))
			return false;
		if (startTime == null) {
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
			return false;
		return true;
	}









	@Override
	public String toString() {
		return "PriceHit [rate=" + rate + ", cost=" + cost + ", pricePlan="
				+ pricePlan + ", startTime=" + startTime + ", endTime="
				+ endTime + "]";
	}
	
	
	
}
