package com.ews.parkswift.domain;

import org.joda.time.Interval;
import org.joda.time.LocalTime;

import com.ews.parkswift.config.Constants;

public enum TimeInterval {
	FULLDAY(LocalTime.parse("8:00 AM", Constants.LOCALTIMEFORMATTER), LocalTime.parse("8:00 AM", Constants.LOCALTIMEFORMATTER)) {
		@Override
		public boolean contains(LocalTime time) {
			if(getInterval() == null)
				setInterval(new Interval(getStartTime().toDateTimeToday(), getEndTime().toDateTimeToday().plusDays(1)));
			return getInterval().contains(time.getHourOfDay()<12?time.toDateTimeToday().plusDays(1):time.toDateTimeToday());
		}
	},
	DAY(LocalTime.parse("8:00 AM", Constants.LOCALTIMEFORMATTER), LocalTime.parse("8:00 PM", Constants.LOCALTIMEFORMATTER)) {
		@Override
		public boolean contains(LocalTime time) {
			if(getInterval() == null)
				setInterval(new Interval(getStartTime().toDateTimeToday(), getEndTime().toDateTimeToday()));
			return getInterval().contains(time.toDateTimeToday()) || time.equals(getEndTime());
		}
	},
	NIGHT(LocalTime.parse("8:00 PM", Constants.LOCALTIMEFORMATTER), LocalTime.parse("8:00 AM", Constants.LOCALTIMEFORMATTER)) {
		@Override
		public boolean contains(LocalTime time) {
			if(getInterval() == null)
				setInterval(new Interval(getStartTime().toDateTimeToday(), getEndTime().toDateTimeToday().plusDays(1)));
			return getInterval().contains(time.getHourOfDay()<12?time.toDateTimeToday().plusDays(1):time.toDateTimeToday()) || time.equals(getEndTime());
		}
	};
	
	public abstract boolean contains(LocalTime time);
	private LocalTime startTime;
	private LocalTime endTime;
	private Interval interval;
	private TimeInterval(LocalTime startTime, LocalTime endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
	}
	public LocalTime getStartTime() {
		return startTime;
	}
	public LocalTime getEndTime() {
		return endTime;
	}
	
	public Interval getInterval(){
		return interval;
	}
	public void setInterval(Interval interval) {
		this.interval = interval;
	}
	
	
}
