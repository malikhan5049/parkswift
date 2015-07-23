package com.ews.parkswift.web.rest.dto;


public class ParkingSpaceDTO {
	
	private Long id;

	private Integer numberOfSpaces;
    
    private String nick;

	public ParkingSpaceDTO(Long id, String nick, Integer numberOfSpaces) {
		this.id = id; 
		this.nick = nick;
		this.numberOfSpaces = numberOfSpaces;
	}

	public Long getId() {
		return id;
	}


	public Integer getNumberOfSpaces() {
		return numberOfSpaces;
	}


	public String getNick() {
		return nick;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ParkingSpaceDTO other = (ParkingSpaceDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
    
    
    
}
