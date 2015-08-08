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
	
	private enum CostingServiceUseCase{
		@SuppressWarnings("serial")
		TODAY_DAYTIME(new CostingInputVO(LocalDate.parse(LocalDate.now().toString(), Constants.LOCALDATEFORMATTER), LocalDate.parse(LocalDate.now().toString(), Constants.LOCALDATEFORMATTER),
				LocalTime.parse(LocalTime.now().plusHours(1).toString(Constants.LOCALTIMEFORMATTER), Constants.LOCALTIMEFORMATTER), LocalTime.parse(LocalTime.now().plusHours(3).toString(Constants.LOCALTIMEFORMATTER), Constants.LOCALTIMEFORMATTER), ratesMap),
				new ArrayList<PriceHit>(){{
					add(new PriceHit(new BigDecimal(3), new BigDecimal(3).multiply(BigDecimal.valueOf(2)), 
							PricePlan.DAYHOUR, LocalTime.parse(LocalTime.now().plusHours(1).toString(Constants.LOCALTIMEFORMATTER), Constants.LOCALTIMEFORMATTER), LocalTime.parse(LocalTime.now().plusHours(3).toString(Constants.LOCALTIMEFORMATTER), Constants.LOCALTIMEFORMATTER)));
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
