package com.ews.parkswift.domain;

import java.math.BigDecimal;

public class PriceHit {
	
	private BigDecimal rate;
	private BigDecimal cost;
	private PricePlan pricePlan;
	
	
	public PriceHit(BigDecimal rate, BigDecimal cost, PricePlan pricePlan) {
		super();
		this.rate = rate;
		this.cost = cost;
		this.pricePlan = pricePlan;
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
	
}
