package com.ews.parkswift.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
		Period datePeriod = new Period(costingInputVO.getStartDate(), costingInputVO.getEndDate());
		Period timePeriod = new Period(costingInputVO.getStartTime(), costingInputVO.getEndTime());
		
		Integer noOfDaysToCost = datePeriod.getDays();
		Integer noOfHoursToCost = timePeriod.getHours();
		
		if(noOfDaysToCost >= PricePlan.FULLMONTH.getDaysPeriod() && noOfHoursToCost == PricePlan.FULLMONTH.getTimePeriod()){
			if(costingInputVO.getStartTime().equals(PricePlan.FULLMONTH.getTimeInterval().getStartTime()) &&
					costingInputVO.getEndTime().equals(PricePlan.FULLMONTH.getTimeInterval().getEndTime())){
				BigDecimal monthlyRate = costingInputVO.getParkingSpacePriceEntrys().get(PricePlan.FULLMONTH.name());
				int monthlyMultiple = noOfDaysToCost/PricePlan.FULLMONTH.getDaysPeriod();
				BigDecimal  monthlyCost =  monthlyRate.multiply(BigDecimal.valueOf(monthlyMultiple));
				priceHits.add(new PriceHit(monthlyRate, monthlyCost, PricePlan.FULLMONTH));
				noOfDaysToCost -= PricePlan.FULLMONTH.getDaysPeriod()*monthlyMultiple;
				
				BigDecimal dailyRate = costingInputVO.getParkingSpacePriceEntrys().get(PricePlan.FULLDAY.name());
				BigDecimal dailyCost = dailyRate.multiply(BigDecimal.valueOf(noOfDaysToCost));
				priceHits.add(new PriceHit(dailyRate, dailyCost, PricePlan.FULLDAY));
				
			}else{
				if(TimeInterval.DAY.contains(costingInputVO.getStartTime())){
					Period timePeriodInDay = new Period(costingInputVO.getStartTime(), TimeInterval.DAY.getEndTime());
					
					BigDecimal dailyRate = costingInputVO.getParkingSpacePriceEntrys().get(PricePlan.FULLDAY.name());
					BigDecimal dailyCost = dailyRate.multiply(BigDecimal.valueOf(noOfDaysToCost));
					priceHits.add(new PriceHit(dailyRate, dailyCost, PricePlan.FULLDAY));
				}
			}
		}
			
		return null;
	}
}
