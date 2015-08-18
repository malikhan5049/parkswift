package com.ews.parkswift.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Period;
import org.springframework.stereotype.Service;

import com.ews.parkswift.domain.CostingInputVO;
import com.ews.parkswift.domain.PriceHit;
import com.ews.parkswift.domain.PricePlan;
import com.ews.parkswift.domain.TimeInterval;

@Service
public class CostingService {
	
	
	public List<PriceHit> performCosting(CostingInputVO costingInputVO){
		List<PriceHit> priceHits = new ArrayList<>();
		
		Period dayPeriod = new Period(costingInputVO.getStartDate(), costingInputVO.getEndDate());
		
		Integer noOfDaysToCost = dayPeriod.getDays() + 1; // +1 for endDate inclus
		Integer noOfHoursToCost = getNoOfHoursBwStartTimeNEndTime(costingInputVO.getStartTime(), costingInputVO.getEndTime());
		performCostingRecursively(costingInputVO, costingInputVO.getStartTime(),costingInputVO.getEndTime(),noOfHoursToCost, noOfDaysToCost, priceHits);
		
			
		return priceHits;
	}



	private int getNoOfHoursBwStartTimeNEndTime(LocalTime startTime, LocalTime endTime) {
		DateTime dt_endTime =  endTime.toDateTimeToday() ;
		if(isFrom12PMTo11_59AM(startTime)  && 
				isFrom12AMto11_59PM(endTime))
			dt_endTime  = endTime.toDateTimeToday().plusDays(1);
		
		
		return new Period(startTime.toDateTimeToday(),dt_endTime).getHours();
	}
	
	private int getNoOfMinutesBwStartTimeNEndTime(LocalTime startTime, LocalTime endTime) {
		return new Period(startTime,endTime).getMinutes();
	}



	private boolean isFrom12PMTo11_59AM(LocalTime... times) {
		for(LocalTime e:times)
			if(e.getHourOfDay() < 12)
				return false;
		return true;
	}
	private boolean isFrom12AMto11_59PM(LocalTime... times) {
		for(LocalTime e:times)
			if(e.getHourOfDay() >= 12)
				return false;
		return true;
	}
	

	private void performCostingRecursively(CostingInputVO costingInputVO,
			LocalTime startTime, LocalTime endTime, Integer noOfHoursToCost,
			Integer noOfDaysToCost, List<PriceHit> priceHits) {
		if(noOfHoursToCost == PricePlan.FULLMONTH.getTimePeriod() && 
				costingInputVO.getStartDate().equals(getMonthStartDate(costingInputVO.getStartDate())) && costingInputVO.getEndDate().equals(getMonthEndDate(costingInputVO.getEndDate())) &&
				startTime.equals(TimeInterval.FULLDAY.getStartTime()) && endTime.equals(TimeInterval.FULLDAY.getEndTime())){
			BigDecimal monthlyRate = costingInputVO.getRatesMap().get(PricePlan.FULLMONTH.name());
			priceHits.add(new PriceHit(monthlyRate, monthlyRate.multiply(BigDecimal.valueOf(noOfDaysToCost/PricePlan.FULLMONTH.getDaysPeriod())),
					PricePlan.FULLMONTH, startTime, endTime));
		}else if(noOfHoursToCost == PricePlan.FULLDAY.getTimePeriod() &&
				startTime.equals(TimeInterval.FULLDAY.getStartTime()) && endTime.equals(TimeInterval.FULLDAY.getEndTime())){
			BigDecimal fullDayRate = costingInputVO.getRatesMap().get(PricePlan.FULLDAY.name());
			priceHits.add(new PriceHit(fullDayRate, fullDayRate.multiply(BigDecimal.valueOf(noOfDaysToCost)),
					PricePlan.FULLDAY, startTime, endTime));
		}else if(noOfHoursToCost == PricePlan._12HOURSDAY.getTimePeriod() &&
				startTime.equals(TimeInterval.DAY.getStartTime()) && endTime.equals(TimeInterval.DAY.getEndTime())){
			BigDecimal _12HrsDayRate = costingInputVO.getRatesMap().get(PricePlan._12HOURSDAY.name());
			priceHits.add(new PriceHit(_12HrsDayRate, _12HrsDayRate.multiply(BigDecimal.valueOf(noOfDaysToCost)),
					PricePlan._12HOURSDAY, startTime, endTime));
		}else if(noOfHoursToCost == PricePlan._12HOURSNIGHT.getTimePeriod() && 
				startTime.equals(TimeInterval.NIGHT.getStartTime()) && endTime.equals(TimeInterval.NIGHT.getEndTime())){
			BigDecimal _12HrsNightRate = costingInputVO.getRatesMap().get(PricePlan._12HOURSNIGHT.name());
			priceHits.add(new PriceHit(_12HrsNightRate, _12HrsNightRate.multiply(BigDecimal.valueOf(noOfDaysToCost)),
					PricePlan._12HOURSNIGHT, startTime, endTime));
		}else if(noOfHoursToCost <12 && 
				TimeInterval.DAY.contains(startTime) && TimeInterval.DAY.contains(endTime)){
			BigDecimal dayHourRate = costingInputVO.getRatesMap().get(PricePlan.DAYHOUR.name());
			
			if(startTime.getMinuteOfHour() == 30 && endTime.getMinuteOfHour() != 30){
				priceHits.add(new PriceHit(dayHourRate, dayHourRate.divide(BigDecimal.valueOf(2)).multiply(BigDecimal.valueOf(noOfDaysToCost)),
						PricePlan.DAYHOUR, startTime, startTime.plusMinutes(29)));
				startTime = startTime.plusMinutes(30);
			}
			if(endTime.getMinuteOfHour() == 30 && startTime.getMinuteOfHour() != 30){
				priceHits.add(new PriceHit(dayHourRate, dayHourRate.divide(BigDecimal.valueOf(2)).multiply(BigDecimal.valueOf(noOfDaysToCost)),
						PricePlan.DAYHOUR, endTime.minusMinutes(29), endTime));
				endTime = endTime.minusMinutes(30);
			}
			
			if(getNoOfHoursBwStartTimeNEndTime(startTime, endTime) > 0 || getNoOfMinutesBwStartTimeNEndTime(startTime, endTime) == 59)
				priceHits.add(new PriceHit(dayHourRate, dayHourRate.multiply(BigDecimal.valueOf(noOfHoursToCost * noOfDaysToCost)),
						PricePlan.DAYHOUR, startTime, endTime));
			
		}else if(noOfHoursToCost <12 && 
				TimeInterval.NIGHT.contains(startTime) && TimeInterval.NIGHT.contains(endTime)){
			BigDecimal nightHourRate = costingInputVO.getRatesMap().get(PricePlan.NIGHTHOUR.name());
			
			if(startTime.getMinuteOfHour() == 30 && endTime.getMinuteOfHour() != 30){
				priceHits.add(new PriceHit(nightHourRate, nightHourRate.divide(BigDecimal.valueOf(2)).multiply(BigDecimal.valueOf(noOfDaysToCost)),
						PricePlan.NIGHTHOUR, startTime, startTime.plusMinutes(29)));
				startTime = startTime.plusMinutes(30);
			}
			if(endTime.getMinuteOfHour() == 30 && startTime.getMinuteOfHour() != 30){
				priceHits.add(new PriceHit(nightHourRate, nightHourRate.divide(BigDecimal.valueOf(2)).multiply(BigDecimal.valueOf(noOfDaysToCost)),
						PricePlan.NIGHTHOUR, endTime.minusMinutes(29), endTime));
				endTime = endTime.minusMinutes(30);
			}
			
			if(getNoOfHoursBwStartTimeNEndTime(startTime, endTime) > 0 || getNoOfMinutesBwStartTimeNEndTime(startTime, endTime) == 59)
				priceHits.add(new PriceHit(nightHourRate, nightHourRate.multiply(BigDecimal.valueOf(noOfHoursToCost * noOfDaysToCost)),
						PricePlan.NIGHTHOUR, startTime, endTime));
			
		}else{
			LocalTime nextStartTime = null;
			if(TimeInterval.DAY.contains(startTime)){
				Integer noOfHoursBwStartTimeNDayEndTime = new Period(startTime, TimeInterval.DAY.getEndTime()).getHours();
				performCostingRecursively(costingInputVO, startTime, TimeInterval.DAY.getEndTime().minusMinutes(1), 
						noOfHoursBwStartTimeNDayEndTime, noOfDaysToCost, priceHits);
				noOfHoursToCost -= noOfHoursBwStartTimeNDayEndTime;
				nextStartTime = TimeInterval.NIGHT.getStartTime();
			}else if(TimeInterval.NIGHT.contains(startTime)){
				Integer noOfHoursBwStartTimeNNightEndTime = new Period(startTime, TimeInterval.NIGHT.getEndTime()).getHours();
				performCostingRecursively(costingInputVO, startTime, TimeInterval.NIGHT.getEndTime().minusMinutes(1), 
						noOfHoursBwStartTimeNNightEndTime, noOfDaysToCost, priceHits);
				noOfHoursToCost -= noOfHoursBwStartTimeNNightEndTime;
				nextStartTime = TimeInterval.DAY.getStartTime();
			}
			performCostingRecursively(costingInputVO, nextStartTime, endTime, noOfHoursToCost, noOfDaysToCost, priceHits);
		}
		
	}

	private void costMinutesPart(LocalTime startTime, LocalTime endTime,
			Integer noOfDaysToCost, List<PriceHit> priceHits,
			BigDecimal hourlyRate, PricePlan pricePlan) {
		if(startTime.getMinuteOfHour() == 30)
			priceHits.add(new PriceHit(hourlyRate, hourlyRate.divide(BigDecimal.valueOf(2)).multiply(BigDecimal.valueOf(noOfDaysToCost)),
					pricePlan, startTime, startTime.plusMinutes(30)));
		if(endTime.getMinuteOfHour() == 30)
			priceHits.add(new PriceHit(hourlyRate, hourlyRate.divide(BigDecimal.valueOf(2)).multiply(BigDecimal.valueOf(noOfDaysToCost)),
					pricePlan, endTime.minusMinutes(30), endTime));
		
	}

	private LocalDate getMonthEndDate(LocalDate date) {
		return date.dayOfMonth().withMaximumValue();
	}

	private LocalDate getMonthStartDate(LocalDate date) {
		return date.dayOfMonth().withMinimumValue();
	}
	
	
}
