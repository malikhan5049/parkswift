package com.ews.parkswift.unit.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import com.ews.parkswift.config.Constants;
import com.ews.parkswift.domain.CostingInputVO;
import com.ews.parkswift.domain.PriceHit;
import com.ews.parkswift.domain.PricePlan;
import com.ews.parkswift.service.CostingService;

@RunWith(Theories.class)
public class CostingServiceTest {
	
	CostingService costingService;
	
	private static  Map<String, BigDecimal> ratesMap = new HashMap<String, BigDecimal>(){{
		put(PricePlan.DAYHOUR.name(), new BigDecimal(3));
		put(PricePlan.NIGHTHOUR.name(), new BigDecimal(2));
		put(PricePlan._12HOURSDAY.name(), new BigDecimal(10));
		put(PricePlan._12HOURSNIGHT.name(), new BigDecimal(8));
		put(PricePlan.FULLDAY.name(), new BigDecimal(18));
		put(PricePlan.FULLMONTH.name(), new BigDecimal(500));
	}};
	@DataPoints public static CostingServiceUseCase[] costerServiceUseCases = CostingServiceUseCase.values();
	
	@Before
	public void init() {
		costingService = new CostingService();
	}
	@Theory
	public void testPerformCosting(CostingServiceUseCase costingServiceUseCase){
		List<PriceHit> priceHits = costingService.performCosting(costingServiceUseCase.getCostingInputVO());
		Assert.assertEquals(costingServiceUseCase.getPriceHits(), priceHits);
	}
	@SuppressWarnings("serial")
	private enum CostingServiceUseCase{
		
		TODAY_DAYHOUR_8AMTO7PM(new CostingInputVO(LocalDate.parse(LocalDate.now().toString(), Constants.LOCALDATEFORMATTER), LocalDate.parse(LocalDate.now().toString(), Constants.LOCALDATEFORMATTER),
				LocalTime.parse("08:00 AM", Constants.LOCALTIMEFORMATTER), LocalTime.parse("07:00 PM", Constants.LOCALTIMEFORMATTER), ratesMap),
				new ArrayList<PriceHit>(){{
					add(new PriceHit(ratesMap.get(PricePlan.DAYHOUR.name()), ratesMap.get(PricePlan.DAYHOUR.name()).multiply(BigDecimal.valueOf(11)), 
							PricePlan.DAYHOUR, LocalTime.parse("08:00 AM", Constants.LOCALTIMEFORMATTER), LocalTime.parse("07:00 PM", Constants.LOCALTIMEFORMATTER)));
				}}),
		TODAY_DAYHOUR_8AMTO7PM_MULTIPLEDAYS(new CostingInputVO(LocalDate.parse(LocalDate.now().toString(), Constants.LOCALDATEFORMATTER), LocalDate.parse(LocalDate.now().plusDays(2).toString(), Constants.LOCALDATEFORMATTER),
				LocalTime.parse("08:00 AM", Constants.LOCALTIMEFORMATTER), LocalTime.parse("07:00 PM", Constants.LOCALTIMEFORMATTER), ratesMap),
				new ArrayList<PriceHit>(){{
					add(new PriceHit(ratesMap.get(PricePlan.DAYHOUR.name()), ratesMap.get(PricePlan.DAYHOUR.name()).multiply(BigDecimal.valueOf(11*3)), 
							PricePlan.DAYHOUR, LocalTime.parse("08:00 AM", Constants.LOCALTIMEFORMATTER), LocalTime.parse("07:00 PM", Constants.LOCALTIMEFORMATTER)));
				}}),
		TODAY_DAYHOUR_8AMT10_30AM(new CostingInputVO(LocalDate.parse(LocalDate.now().toString(), Constants.LOCALDATEFORMATTER), LocalDate.parse(LocalDate.now().toString(), Constants.LOCALDATEFORMATTER),
				LocalTime.parse("08:00 AM", Constants.LOCALTIMEFORMATTER), LocalTime.parse("10:30 AM", Constants.LOCALTIMEFORMATTER), ratesMap),
				new ArrayList<PriceHit>(){{
					add(new PriceHit(ratesMap.get(PricePlan.DAYHOUR.name()), ratesMap.get(PricePlan.DAYHOUR.name()).divide(BigDecimal.valueOf(2)), 
							PricePlan.DAYHOUR, LocalTime.parse("10:01 AM", Constants.LOCALTIMEFORMATTER), LocalTime.parse("10:30 AM", Constants.LOCALTIMEFORMATTER)));
					add(new PriceHit(ratesMap.get(PricePlan.DAYHOUR.name()), ratesMap.get(PricePlan.DAYHOUR.name()).multiply(BigDecimal.valueOf(2)), 
							PricePlan.DAYHOUR, LocalTime.parse("08:00 AM", Constants.LOCALTIMEFORMATTER), LocalTime.parse("10:00 AM", Constants.LOCALTIMEFORMATTER)));
				}}),
		TODAY_DAYHOUR_8_30AMT10AM(new CostingInputVO(LocalDate.parse(LocalDate.now().toString(), Constants.LOCALDATEFORMATTER), LocalDate.parse(LocalDate.now().toString(), Constants.LOCALDATEFORMATTER),
				LocalTime.parse("08:30 AM", Constants.LOCALTIMEFORMATTER), LocalTime.parse("10:00 AM", Constants.LOCALTIMEFORMATTER), ratesMap),
				new ArrayList<PriceHit>(){{
					add(new PriceHit(ratesMap.get(PricePlan.DAYHOUR.name()), ratesMap.get(PricePlan.DAYHOUR.name()).divide(BigDecimal.valueOf(2)), 
							PricePlan.DAYHOUR, LocalTime.parse("08:30 AM", Constants.LOCALTIMEFORMATTER), LocalTime.parse("08:59 AM", Constants.LOCALTIMEFORMATTER)));
					add(new PriceHit(ratesMap.get(PricePlan.DAYHOUR.name()), ratesMap.get(PricePlan.DAYHOUR.name()).multiply(BigDecimal.valueOf(1)), 
							PricePlan.DAYHOUR, LocalTime.parse("09:00 AM", Constants.LOCALTIMEFORMATTER), LocalTime.parse("10:00 AM", Constants.LOCALTIMEFORMATTER)));
				}}),
		TODAY_DAYHOUR_8_30AMT10_30AM(new CostingInputVO(LocalDate.parse(LocalDate.now().toString(), Constants.LOCALDATEFORMATTER), LocalDate.parse(LocalDate.now().toString(), Constants.LOCALDATEFORMATTER),
				LocalTime.parse("08:30 AM", Constants.LOCALTIMEFORMATTER), LocalTime.parse("10:30 AM", Constants.LOCALTIMEFORMATTER), ratesMap),
				new ArrayList<PriceHit>(){{
					add(new PriceHit(ratesMap.get(PricePlan.DAYHOUR.name()), ratesMap.get(PricePlan.DAYHOUR.name()).multiply(BigDecimal.valueOf(2)), 
							PricePlan.DAYHOUR, LocalTime.parse("08:30 AM", Constants.LOCALTIMEFORMATTER), LocalTime.parse("10:30 AM", Constants.LOCALTIMEFORMATTER)));
				}}),				
		TODAY_NIGHTHOUR_8PMT07AM(new CostingInputVO(LocalDate.parse(LocalDate.now().toString(), Constants.LOCALDATEFORMATTER), LocalDate.parse(LocalDate.now().toString(), Constants.LOCALDATEFORMATTER),
				LocalTime.parse("08:00 PM", Constants.LOCALTIMEFORMATTER), LocalTime.parse("07:00 AM", Constants.LOCALTIMEFORMATTER), ratesMap),
				new ArrayList<PriceHit>(){{
					add(new PriceHit(ratesMap.get(PricePlan.NIGHTHOUR.name()), ratesMap.get(PricePlan.NIGHTHOUR.name()).multiply(BigDecimal.valueOf(11)), 
							PricePlan.NIGHTHOUR, LocalTime.parse("08:00 PM", Constants.LOCALTIMEFORMATTER), LocalTime.parse("07:00 AM", Constants.LOCALTIMEFORMATTER)));
				}}),
		TODAY_DAYHOUR_7PMTO7_59PM_NIGHTHOUR_8PMTO10PM(new CostingInputVO(LocalDate.parse(LocalDate.now().toString(), Constants.LOCALDATEFORMATTER), LocalDate.parse(LocalDate.now().toString(), Constants.LOCALDATEFORMATTER),
				LocalTime.parse("07:00 PM", Constants.LOCALTIMEFORMATTER), LocalTime.parse("10:00 PM", Constants.LOCALTIMEFORMATTER), ratesMap),
				new ArrayList<PriceHit>(){{
					add(new PriceHit(ratesMap.get(PricePlan.DAYHOUR.name()), ratesMap.get(PricePlan.DAYHOUR.name()).multiply(BigDecimal.valueOf(1)), 
							PricePlan.DAYHOUR, LocalTime.parse("07:00 PM", Constants.LOCALTIMEFORMATTER), LocalTime.parse("07:59 PM", Constants.LOCALTIMEFORMATTER)));
					add(new PriceHit(ratesMap.get(PricePlan.NIGHTHOUR.name()), ratesMap.get(PricePlan.NIGHTHOUR.name()).multiply(BigDecimal.valueOf(2)), 
							PricePlan.NIGHTHOUR, LocalTime.parse("08:00 PM", Constants.LOCALTIMEFORMATTER), LocalTime.parse("10:00 PM", Constants.LOCALTIMEFORMATTER)));
				}}),
		TODAY_DAYHOUR_7_30PMTO7_59PM_NIGHTHOUR_8PMTO10PM(new CostingInputVO(LocalDate.parse(LocalDate.now().toString(), Constants.LOCALDATEFORMATTER), LocalDate.parse(LocalDate.now().toString(), Constants.LOCALDATEFORMATTER),
				LocalTime.parse("07:30 PM", Constants.LOCALTIMEFORMATTER), LocalTime.parse("10:00 PM", Constants.LOCALTIMEFORMATTER), ratesMap),
				new ArrayList<PriceHit>(){{
					add(new PriceHit(ratesMap.get(PricePlan.DAYHOUR.name()), ratesMap.get(PricePlan.DAYHOUR.name()).divide(BigDecimal.valueOf(2)), 
							PricePlan.DAYHOUR, LocalTime.parse("07:30 PM", Constants.LOCALTIMEFORMATTER), LocalTime.parse("07:59 PM", Constants.LOCALTIMEFORMATTER)));
					add(new PriceHit(ratesMap.get(PricePlan.NIGHTHOUR.name()), ratesMap.get(PricePlan.NIGHTHOUR.name()).multiply(BigDecimal.valueOf(2)), 
							PricePlan.NIGHTHOUR, LocalTime.parse("08:00 PM", Constants.LOCALTIMEFORMATTER), LocalTime.parse("10:00 PM", Constants.LOCALTIMEFORMATTER)));
				}}),
		TODAY_NIGHTHOUR_7_30AMTO7_59AM_DAYHOUR_8AMTO10AM(new CostingInputVO(LocalDate.parse(LocalDate.now().toString(), Constants.LOCALDATEFORMATTER), LocalDate.parse(LocalDate.now().toString(), Constants.LOCALDATEFORMATTER),
				LocalTime.parse("07:30 AM", Constants.LOCALTIMEFORMATTER), LocalTime.parse("10:00 AM", Constants.LOCALTIMEFORMATTER), ratesMap),
				new ArrayList<PriceHit>(){{
					add(new PriceHit(ratesMap.get(PricePlan.NIGHTHOUR.name()), ratesMap.get(PricePlan.NIGHTHOUR.name()).divide(BigDecimal.valueOf(2)), 
							PricePlan.NIGHTHOUR, LocalTime.parse("07:30 AM", Constants.LOCALTIMEFORMATTER), LocalTime.parse("07:59 AM", Constants.LOCALTIMEFORMATTER)));
					add(new PriceHit(ratesMap.get(PricePlan.DAYHOUR.name()), ratesMap.get(PricePlan.DAYHOUR.name()).multiply(BigDecimal.valueOf(2)), 
							PricePlan.DAYHOUR, LocalTime.parse("08:00 AM", Constants.LOCALTIMEFORMATTER), LocalTime.parse("10:00 AM", Constants.LOCALTIMEFORMATTER)));
				}}),
		TODAY_12HOURSDAY_8AMT8PM(new CostingInputVO(LocalDate.parse(LocalDate.now().toString(), Constants.LOCALDATEFORMATTER), LocalDate.parse(LocalDate.now().toString(), Constants.LOCALDATEFORMATTER),
				LocalTime.parse("08:00 AM", Constants.LOCALTIMEFORMATTER), LocalTime.parse("08:00 PM", Constants.LOCALTIMEFORMATTER), ratesMap),
				new ArrayList<PriceHit>(){{
					add(new PriceHit(ratesMap.get(PricePlan._12HOURSDAY.name()), ratesMap.get(PricePlan._12HOURSDAY.name()).multiply(BigDecimal.valueOf(1)), 
							PricePlan._12HOURSDAY, LocalTime.parse("08:00 AM", Constants.LOCALTIMEFORMATTER), LocalTime.parse("08:00 PM", Constants.LOCALTIMEFORMATTER)));
				}}),
		_12HOURSDAY_12AMT12PM(new CostingInputVO(LocalDate.parse(LocalDate.now().toString(), Constants.LOCALDATEFORMATTER), LocalDate.parse(LocalDate.now().toString(), Constants.LOCALDATEFORMATTER),
				LocalTime.parse("12:00 AM", Constants.LOCALTIMEFORMATTER), LocalTime.parse("12:00 PM", Constants.LOCALTIMEFORMATTER), ratesMap),
				new ArrayList<PriceHit>(){{
					add(new PriceHit(ratesMap.get(PricePlan.NIGHTHOUR.name()), ratesMap.get(PricePlan.NIGHTHOUR.name()).multiply(BigDecimal.valueOf(8)), 
							PricePlan.NIGHTHOUR, LocalTime.parse("12:00 AM", Constants.LOCALTIMEFORMATTER), LocalTime.parse("07:59 AM", Constants.LOCALTIMEFORMATTER)));
					add(new PriceHit(ratesMap.get(PricePlan.DAYHOUR.name()), ratesMap.get(PricePlan.DAYHOUR.name()).multiply(BigDecimal.valueOf(4)), 
							PricePlan.DAYHOUR, LocalTime.parse("08:00 AM", Constants.LOCALTIMEFORMATTER), LocalTime.parse("12:00 PM", Constants.LOCALTIMEFORMATTER)));
				}}),
		_12HOURSDAY_12AMT12PM_MULTIPLEDAYS(new CostingInputVO(LocalDate.parse(LocalDate.now().toString(), Constants.LOCALDATEFORMATTER), LocalDate.parse(LocalDate.now().plusDays(2).toString(), Constants.LOCALDATEFORMATTER),
				LocalTime.parse("12:00 AM", Constants.LOCALTIMEFORMATTER), LocalTime.parse("12:00 PM", Constants.LOCALTIMEFORMATTER), ratesMap),
				new ArrayList<PriceHit>(){{
					add(new PriceHit(ratesMap.get(PricePlan.NIGHTHOUR.name()), ratesMap.get(PricePlan.NIGHTHOUR.name()).multiply(BigDecimal.valueOf(8*3)), 
							PricePlan.NIGHTHOUR, LocalTime.parse("12:00 AM", Constants.LOCALTIMEFORMATTER), LocalTime.parse("07:59 AM", Constants.LOCALTIMEFORMATTER)));
					add(new PriceHit(ratesMap.get(PricePlan.DAYHOUR.name()), ratesMap.get(PricePlan.DAYHOUR.name()).multiply(BigDecimal.valueOf(4*3)), 
							PricePlan.DAYHOUR, LocalTime.parse("08:00 AM", Constants.LOCALTIMEFORMATTER), LocalTime.parse("12:00 PM", Constants.LOCALTIMEFORMATTER)));
				}}),
		TODAY_12HOURSNIGHT_8PMT8AM(new CostingInputVO(LocalDate.parse(LocalDate.now().toString(), Constants.LOCALDATEFORMATTER), LocalDate.parse(LocalDate.now().toString(), Constants.LOCALDATEFORMATTER),
				LocalTime.parse("08:00 PM", Constants.LOCALTIMEFORMATTER), LocalTime.parse("08:00 AM", Constants.LOCALTIMEFORMATTER), ratesMap),
				new ArrayList<PriceHit>(){{
					add(new PriceHit(ratesMap.get(PricePlan._12HOURSNIGHT.name()), ratesMap.get(PricePlan._12HOURSNIGHT.name()).multiply(BigDecimal.valueOf(1)), 
							PricePlan._12HOURSNIGHT, LocalTime.parse("08:00 PM", Constants.LOCALTIMEFORMATTER), LocalTime.parse("08:00 AM", Constants.LOCALTIMEFORMATTER)));
				}}),
		_12HOURSNIGHT_12PMT12AM(new CostingInputVO(LocalDate.parse(LocalDate.now().toString(), Constants.LOCALDATEFORMATTER), LocalDate.parse(LocalDate.now().toString(), Constants.LOCALDATEFORMATTER),
				LocalTime.parse("12:00 PM", Constants.LOCALTIMEFORMATTER), LocalTime.parse("12:00 AM", Constants.LOCALTIMEFORMATTER), ratesMap),
				new ArrayList<PriceHit>(){{
					add(new PriceHit(ratesMap.get(PricePlan.DAYHOUR.name()), ratesMap.get(PricePlan.DAYHOUR.name()).multiply(BigDecimal.valueOf(8)), 
							PricePlan.DAYHOUR, LocalTime.parse("12:00 PM", Constants.LOCALTIMEFORMATTER), LocalTime.parse("07:59 PM", Constants.LOCALTIMEFORMATTER)));
					add(new PriceHit(ratesMap.get(PricePlan.NIGHTHOUR.name()), ratesMap.get(PricePlan.NIGHTHOUR.name()).multiply(BigDecimal.valueOf(4)), 
							PricePlan.NIGHTHOUR, LocalTime.parse("08:00 PM", Constants.LOCALTIMEFORMATTER), LocalTime.parse("12:00 AM", Constants.LOCALTIMEFORMATTER)));
				}}),
		_12HOURSNIGHT_12PMT12AM_MULTIPLEDAYS(new CostingInputVO(LocalDate.parse(LocalDate.now().toString(), Constants.LOCALDATEFORMATTER), LocalDate.parse(LocalDate.now().plusDays(2).toString(), Constants.LOCALDATEFORMATTER),
				LocalTime.parse("12:00 PM", Constants.LOCALTIMEFORMATTER), LocalTime.parse("12:00 AM", Constants.LOCALTIMEFORMATTER), ratesMap),
				new ArrayList<PriceHit>(){{
					add(new PriceHit(ratesMap.get(PricePlan.DAYHOUR.name()), ratesMap.get(PricePlan.DAYHOUR.name()).multiply(BigDecimal.valueOf(8*3)), 
							PricePlan.DAYHOUR, LocalTime.parse("12:00 PM", Constants.LOCALTIMEFORMATTER), LocalTime.parse("07:59 PM", Constants.LOCALTIMEFORMATTER)));
					add(new PriceHit(ratesMap.get(PricePlan.NIGHTHOUR.name()), ratesMap.get(PricePlan.NIGHTHOUR.name()).multiply(BigDecimal.valueOf(4*3)), 
							PricePlan.NIGHTHOUR, LocalTime.parse("08:00 PM", Constants.LOCALTIMEFORMATTER), LocalTime.parse("12:00 AM", Constants.LOCALTIMEFORMATTER)));
				}}),
		FULLDAY_8AMT8AM(new CostingInputVO(LocalDate.parse(LocalDate.now().toString(), Constants.LOCALDATEFORMATTER), LocalDate.parse(LocalDate.now().plusDays(1).toString(), Constants.LOCALDATEFORMATTER),
				LocalTime.parse("08:00 AM", Constants.LOCALTIMEFORMATTER), LocalTime.parse("08:00 AM", Constants.LOCALTIMEFORMATTER), ratesMap),
				new ArrayList<PriceHit>(){{
					add(new PriceHit(ratesMap.get(PricePlan.FULLDAY.name()), ratesMap.get(PricePlan.FULLDAY.name()).multiply(BigDecimal.valueOf(1)), 
							PricePlan.FULLDAY, LocalTime.parse("08:00 AM", Constants.LOCALTIMEFORMATTER), LocalTime.parse("08:00 AM", Constants.LOCALTIMEFORMATTER)));
				}});
	
		
		private CostingServiceUseCase(CostingInputVO costingInputVO,
				List<PriceHit> priceHits) {
			this.costingInputVO = costingInputVO;
			this.priceHits = priceHits;
		}
		private CostingInputVO costingInputVO;
		private List<PriceHit> priceHits;
		
		public CostingInputVO getCostingInputVO() {
			return costingInputVO;
		}
		public List<PriceHit> getPriceHits() {
			return priceHits;
		}
		
		
	}

}
