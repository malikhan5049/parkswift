package com.ews.parkswift.service;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.LocalDateTime;
import org.joda.time.Minutes;
import org.springframework.stereotype.Service;

import com.ews.parkswift.domain.PricePlan;
import com.ews.parkswift.vo.CostingInputVO;
import com.ews.parkswift.vo.ParkingRateVO;

@Service
public class CostingService {
	
	private CostingInputVO costingVO;
	
	private List<ParkingRateVO> applicableParkingRates;
	
	private double dayHourRate, nightHourRate, _12HrsDayRate, _12HrsNightRate, fullDayRate, weeklyRate, monthlyRate;
	
	public List<ParkingRateVO> performCosting(CostingInputVO costingInputVO) {
		
		this.costingVO = costingInputVO;
		
		ParkingRateVO parkingRateVO=null;
		costingVO.constructStartDateTime();
		costingVO.constructEndDateTime();
		applicableParkingRates = new ArrayList<ParkingRateVO>();
		
		dayHourRate = costingVO.getRatesMap().get(PricePlan.DAYHOUR.name()).doubleValue();
		nightHourRate = costingVO.getRatesMap().get(PricePlan.NIGHTHOUR.name()).doubleValue();
		_12HrsDayRate = costingVO.getRatesMap().get(PricePlan._12HOURSDAY.name()).doubleValue();
		_12HrsNightRate = costingVO.getRatesMap().get(PricePlan._12HOURSNIGHT.name()).doubleValue();
		fullDayRate = costingVO.getRatesMap().get(PricePlan.FULLDAY.name()).doubleValue();
		weeklyRate = costingVO.getRatesMap().get(PricePlan.FULLWEEK.name()).doubleValue();
		monthlyRate = costingVO.getRatesMap().get(PricePlan.FULLMONTH.name()).doubleValue();
		
		do {
			try{
				parkingRateVO = calculateParkingCost();
			}catch(Exception e){
				e.printStackTrace();
				e.getMessage();
			}
			applicableParkingRates.add(parkingRateVO);
		}while(costingVO.getStartDateTime().compareTo(costingVO.getEndDateTime()) < 0);
		
		return applicableParkingRates;
	}
	
	private ParkingRateVO calculateParkingCost() throws Exception{

		double totalCost = 0;
		int numberOfMinutes = 0;
		LocalDateTime applyRateFrom, applyRateTill;
		double _1by4DayHourRate = dayHourRate / 4;
		double _1by4NightHourRate = nightHourRate / 4;
		int numberOfDaysToCost = getNumberOfDaysBetweenDates(costingVO.getStartDateTime(), costingVO.getEndDateTime()); 
		
		if (costingVO.getStartTime().compareTo(PricePlan._12HOURSDAY.getTimeInterval().getStartTime()) == 0 &&
				Hours.hoursBetween(costingVO.getStartDateTime(), costingVO.getEndDateTime()).getHours() >= 24) {
			
			applyRateFrom = costingVO.getStartDateTime();
			applyRateTill = costingVO.getStartDateTime().plusDays(numberOfDaysToCost);
			totalCost = fullDayRate * numberOfDaysToCost;
			costingVO.setStartDateTime(applyRateTill);
			return new ParkingRateVO(PricePlan.FULLDAY.name(), fullDayRate, totalCost, PricePlan.FULLDAY.getTimeInterval().getStartTime(), PricePlan.FULLDAY.getTimeInterval().getEndTime(), applyRateFrom, applyRateTill);
		}
		
		if (costingVO.getStartTime().compareTo(PricePlan._12HOURSDAY.getTimeInterval().getStartTime()) == 0 &&
				Hours.hoursBetween(costingVO.getStartDateTime(), costingVO.getEndDateTime()).getHours() >= 12) {
			
			applyRateFrom = costingVO.getStartDateTime();
			applyRateTill = costingVO.getStartDateTime().plusHours(12);
			costingVO.setStartDateTime(applyRateTill);
			return new ParkingRateVO(PricePlan._12HOURSDAY.name(), _12HrsDayRate, _12HrsDayRate, PricePlan._12HOURSDAY.getTimeInterval().getStartTime(), PricePlan._12HOURSDAY.getTimeInterval().getEndTime(), applyRateFrom, applyRateTill);
		}
		
		if (costingVO.getStartTime().compareTo(PricePlan._12HOURSNIGHT.getTimeInterval().getStartTime()) == 0 &&
				Hours.hoursBetween(costingVO.getStartDateTime(), costingVO.getEndDateTime()).getHours() >= 12) {
			
			applyRateFrom = costingVO.getStartDateTime();
			applyRateTill = costingVO.getStartDateTime().plusHours(12);
			costingVO.setStartDateTime(applyRateTill);
			return new ParkingRateVO(PricePlan._12HOURSNIGHT.name(), _12HrsNightRate, _12HrsNightRate, PricePlan._12HOURSNIGHT.getTimeInterval().getStartTime(), PricePlan._12HOURSNIGHT.getTimeInterval().getEndTime(), applyRateFrom, applyRateTill);
		}
		
		if (costingVO.getStartTime().compareTo(PricePlan._12HOURSDAY.getTimeInterval().getStartTime()) >= 0 &&
				costingVO.getStartTime().compareTo(PricePlan._12HOURSDAY.getTimeInterval().getEndTime()) < 0) {
						
			if (costingVO.getEndTime().compareTo(PricePlan._12HOURSDAY.getTimeInterval().getEndTime()) < 0 &&
					costingVO.getStartDate().compareTo(costingVO.getEndDate()) == 0)
				numberOfMinutes = Minutes.minutesBetween(costingVO.getStartTime(), costingVO.getEndTime()).getMinutes();
			else
				numberOfMinutes = Minutes.minutesBetween(costingVO.getStartTime(), PricePlan._12HOURSDAY.getTimeInterval().getEndTime()).getMinutes();
			
			applyRateFrom = costingVO.getStartDateTime();
			applyRateTill = costingVO.getStartDateTime().plusMinutes(numberOfMinutes);
			
			totalCost = _1by4DayHourRate * (numberOfMinutes/15);
			costingVO.setStartDateTime(applyRateTill);
			return new ParkingRateVO(PricePlan.DAYHOUR.name(), dayHourRate, totalCost, PricePlan.DAYHOUR.getTimeInterval().getStartTime(), PricePlan.DAYHOUR.getTimeInterval().getEndTime(), applyRateFrom, applyRateTill);
		}

		int hrsToNextDay = PricePlan._12HOURSNIGHT.getTimeInterval().getEndTime().getHourOfDay() + 1;
		if (costingVO.getStartTime().minusHours(hrsToNextDay).compareTo(PricePlan._12HOURSNIGHT.getTimeInterval().getStartTime().minusHours(hrsToNextDay)) >= 0 &&
				costingVO.getStartTime().minusHours(hrsToNextDay).compareTo(PricePlan._12HOURSNIGHT.getTimeInterval().getEndTime().minusHours(hrsToNextDay)) < 0) {
						
			LocalDateTime _12HourNightEnd = new LocalDateTime(costingVO.getStartDateTime().getYear(), costingVO.getStartDateTime().getMonthOfYear(), 
					costingVO.getStartDateTime().getDayOfMonth(), costingVO.getStartDateTime().getHourOfDay(), costingVO.getStartDateTime().getMinuteOfHour());
			int hrOfDay = _12HourNightEnd.getHourOfDay();
			int _12HourNightEndHour = PricePlan._12HOURSNIGHT.getTimeInterval().getEndTime().getHourOfDay();
			int hrsToAdd = hrOfDay>=20&&hrOfDay<24?24-hrOfDay+_12HourNightEndHour:_12HourNightEndHour-hrOfDay;
			_12HourNightEnd = _12HourNightEnd.plusHours(hrsToAdd);
			
			if (costingVO.getEndDateTime().compareTo(_12HourNightEnd) >= 0)
				numberOfMinutes = Minutes.minutesBetween(costingVO.getStartTime().minusHours(hrsToNextDay), PricePlan._12HOURSNIGHT.getTimeInterval().getEndTime().minusHours(hrsToNextDay)).getMinutes();
			else
				numberOfMinutes = Minutes.minutesBetween(costingVO.getStartTime().minusHours(hrsToNextDay), costingVO.getEndTime().minusHours(hrsToNextDay)).getMinutes();
			
			applyRateFrom = costingVO.getStartDateTime();
			applyRateTill = costingVO.getStartDateTime().plusMinutes(numberOfMinutes);
			
			totalCost = _1by4NightHourRate * (numberOfMinutes/15);
			costingVO.setStartDateTime(applyRateTill);
			return new ParkingRateVO(PricePlan.NIGHTHOUR.name(), nightHourRate, totalCost, PricePlan.NIGHTHOUR.getTimeInterval().getStartTime(), PricePlan.NIGHTHOUR.getTimeInterval().getEndTime(), applyRateFrom, applyRateTill);
		}
		return null;
	}
	
	private int getNumberOfDaysBetweenDates(LocalDateTime firstDateTime, LocalDateTime secondDateTime) {
		
		return Days.daysBetween(firstDateTime, secondDateTime).getDays();
	}
}
