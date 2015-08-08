package com.ews.parkswift.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
		
		Integer noOfDaysToCost = dayPeriod.getDays() == 0 ?1:dayPeriod.getDays();
		Integer noOfHoursToCost = new Period(costingInputVO.getStartTime(), costingInputVO.getEndTime()).getHours();
		performCostingRecursively(costingInputVO, costingInputVO.getStartTime(),costingInputVO.getEndTime(),noOfHoursToCost, noOfDaysToCost, priceHits);
		
			
		return priceHits;
	}

	private void performCostingRecursively(CostingInputVO costingInputVO,
			LocalTime startTime, LocalTime endTime, Integer noOfHoursToCost,
			Integer noOfDaysToCost, List<PriceHit> priceHits) {
		if(noOfHoursToCost == PricePlan.FULLMONTH.getTimePeriod() && 
				costingInputVO.getStartDate().equals(getMonthStartDate(costingInputVO.getStartDate())) && costingInputVO.getEndDate().equals(getMonthEndDate(costingInputVO.getEndDate())) &&
				costingInputVO.getStartTime().equals(TimeInterval.FULLDAY.getStartTime()) && costingInputVO.getEndTime().equals(TimeInterval.FULLDAY.getEndTime())){
			BigDecimal monthlyRate = costingInputVO.getRatesMap().get(PricePlan.FULLMONTH.name());
			priceHits.add(new PriceHit(monthlyRate, monthlyRate.multiply(BigDecimal.valueOf(noOfDaysToCost/PricePlan.FULLMONTH.getDaysPeriod())),
					PricePlan.FULLMONTH, startTime, endTime));
		}else if(noOfHoursToCost == PricePlan.FULLDAY.getTimePeriod() &&
				costingInputVO.getStartTime().equals(TimeInterval.FULLDAY.getStartTime()) && costingInputVO.getEndTime().equals(TimeInterval.FULLDAY.getEndTime())){
			BigDecimal fullDayRate = costingInputVO.getRatesMap().get(PricePlan.FULLDAY.name());
			priceHits.add(new PriceHit(fullDayRate, fullDayRate.multiply(BigDecimal.valueOf(noOfDaysToCost)),
					PricePlan.FULLDAY, startTime, endTime));
		}else if(noOfHoursToCost == PricePlan._12HOURSDAY.getTimePeriod() &&
				costingInputVO.getStartTime().equals(TimeInterval.DAY.getStartTime()) && costingInputVO.getEndTime().equals(TimeInterval.DAY.getEndTime())){
			BigDecimal _12HrsDayRate = costingInputVO.getRatesMap().get(PricePlan._12HOURSDAY.name());
			priceHits.add(new PriceHit(_12HrsDayRate, _12HrsDayRate.multiply(BigDecimal.valueOf(noOfDaysToCost)),
					PricePlan._12HOURSDAY, startTime, endTime));
		}else if(noOfHoursToCost == PricePlan._12HOURSNIGHT.getTimePeriod() && 
				costingInputVO.getStartTime().equals(TimeInterval.NIGHT.getStartTime()) && costingInputVO.getEndTime().equals(TimeInterval.NIGHT.getEndTime())){
			BigDecimal _12HrsNightRate = costingInputVO.getRatesMap().get(PricePlan._12HOURSNIGHT.name());
			priceHits.add(new PriceHit(_12HrsNightRate, _12HrsNightRate.multiply(BigDecimal.valueOf(noOfDaysToCost)),
					PricePlan._12HOURSNIGHT, startTime, endTime));
		}else if(noOfHoursToCost <12 && 
				TimeInterval.DAY.contains(costingInputVO.getStartTime()) && TimeInterval.DAY.contains(costingInputVO.getEndTime())){
			BigDecimal dayHourRate = costingInputVO.getRatesMap().get(PricePlan.DAYHOUR.name());
			priceHits.add(new PriceHit(dayHourRate, dayHourRate.multiply(BigDecimal.valueOf(noOfHoursToCost * noOfDaysToCost)),
					PricePlan.DAYHOUR, startTime, endTime));
		}else if(noOfHoursToCost <12 && 
				TimeInterval.NIGHT.contains(costingInputVO.getStartTime()) && TimeInterval.NIGHT.contains(costingInputVO.getEndTime())){
			BigDecimal nightHourRate = costingInputVO.getRatesMap().get(PricePlan.DAYHOUR.name());
			priceHits.add(new PriceHit(nightHourRate, nightHourRate.multiply(BigDecimal.valueOf(noOfHoursToCost * noOfDaysToCost)),
					PricePlan.DAYHOUR, startTime, endTime));
		}else{
			LocalTime nextStartTime = null;
			if(TimeInterval.DAY.contains(costingInputVO.getStartTime())){
				Integer noOfHoursBwStartTimeNDayEndTime = new Period(costingInputVO.getStartTime(), TimeInterval.DAY.getEndTime()).getHours();
				performCostingRecursively(costingInputVO, costingInputVO.getStartTime(), TimeInterval.DAY.getEndTime(), 
						noOfHoursBwStartTimeNDayEndTime, noOfDaysToCost, priceHits);
				noOfHoursToCost -= noOfHoursBwStartTimeNDayEndTime;
				nextStartTime = TimeInterval.NIGHT.getStartTime();
			}else if(TimeInterval.NIGHT.contains(costingInputVO.getStartTime())){
				Integer noOfHoursBwStartTimeNNightEndTime = new Period(costingInputVO.getStartTime(), TimeInterval.NIGHT.getEndTime()).getHours();
				performCostingRecursively(costingInputVO, costingInputVO.getStartTime(), TimeInterval.NIGHT.getEndTime(), 
						noOfHoursBwStartTimeNNightEndTime, noOfDaysToCost, priceHits);
				noOfHoursToCost -= noOfHoursBwStartTimeNNightEndTime;
				nextStartTime = TimeInterval.DAY.getStartTime();
			}
			performCostingRecursively(costingInputVO, nextStartTime, endTime, noOfHoursToCost, noOfDaysToCost, priceHits);
		}
		
	}

	private LocalDate getMonthEndDate(LocalDate date) {
		return date.dayOfMonth().withMaximumValue();
	}

	private LocalDate getMonthStartDate(LocalDate date) {
		return date.dayOfMonth().withMinimumValue();
	}
	
	
}
