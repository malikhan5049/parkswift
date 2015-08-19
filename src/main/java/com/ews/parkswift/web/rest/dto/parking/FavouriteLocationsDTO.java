package com.ews.parkswift.web.rest.dto.parking;


public class FavouriteLocationsDTO {
	
		private Long favLocId;
		
		private Long locId;
		
	    private String location;
	    
	    private String businessType;
	    
	    private Long userId;
	    
		public Long getUserId() {
			return userId;
		}

		public void setUserId(Long userId) {
			this.userId = userId;
		}

		public Long getFavLocId() {
			return favLocId;
		}

		public void setFavLocId(Long favLocId) {
			this.favLocId = favLocId;
		}

		public Long getLocId() {
			return locId;
		}

		public void setLocId(Long locId) {
			this.locId = locId;
		}

		public String getLocation() {
			return location;
		}

		public void setLocation(String location) {
			this.location = location;
		}

		public String getBusinessType() {
			return businessType;
		}

		public void setBusinessType(String businessType) {
			this.businessType = businessType;
		}

	   
}
