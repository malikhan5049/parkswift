package com.ews.parkswift.domain;

public enum PricePlan {
	
	FULLMONTH(30,24,TimeInterval.FULLDAY), FULLWEEK(7, 24, TimeInterval.FULLDAY), FULLDAY(null, 24, TimeInterval.FULLDAY),
	_12HOURSDAY(null, 12, TimeInterval.DAY), _12HOURSNIGHT(null, 12, TimeInterval.NIGHT),
	 DAYHOUR(null, 1, TimeInterval.DAY), NIGHTHOUR(null, 1, TimeInterval.NIGHT);
	
	private Integer daysPeriod;
	private Integer timePeriod;
	private TimeInterval timeInterval;
	private PricePlan(Integer daysPeriod, Integer timePeriod, TimeInterval interval) {
		this.daysPeriod = daysPeriod;
		this.timePeriod = timePeriod;
		this.timeInterval = interval;
	}
	public int getDaysPeriod() {
		return daysPeriod;
	}
	public int getTimePeriod() {
		return timePeriod;
	}
	public TimeInterval getTimeInterval() {
		return timeInterval;
	}
	
	
}
