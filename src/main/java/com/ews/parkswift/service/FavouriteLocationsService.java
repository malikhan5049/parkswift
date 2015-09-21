package com.ews.parkswift.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ews.parkswift.domain.Favourite;
import com.ews.parkswift.domain.ParkingLocationImage;
import com.ews.parkswift.domain.User;
import com.ews.parkswift.repository.FavouriteRepository;
import com.ews.parkswift.repository.UserRepository;
import com.ews.parkswift.web.rest.dto.parking.FavouriteLocationsDTO;

@Service
@Transactional
public class FavouriteLocationsService {

    private final Logger log = LoggerFactory.getLogger(ParkingLocationImageService.class);

    @Inject
    private FavouriteRepository favouriteRepository;
    
    @Inject
    private UserRepository userRepository;
    
    @Inject
    private ParkingLocationImageService parkingLocationImageService;
    
    @PersistenceContext
	private EntityManager entityManager;
    
	public int deleteByUserIdAndLocId(Long locId) {
		User user = userRepository.findUserByLoginName();
		String qry = "delete from Favourite fav where fav.user.id=:userId and fav.parkingLocation.id=:locId";
		Query query = this.entityManager.createQuery(qry);
		query.setParameter("userId", user.getId());
		query.setParameter("locId", locId);
		return query.executeUpdate();
	}
    
    
    public void save(Favourite favourite){
    	User user = userRepository.findUserByLoginName();
    	favourite.setUser(user);
    	favouriteRepository.save(favourite);
    }
    
    public List<FavouriteLocationsDTO> getAllByUser(){
    	
    	 List<Favourite> listFavouriteLocations = favouriteRepository.findAllForCurrentUser();
         List<FavouriteLocationsDTO> listReturning = new ArrayList<>();
         FavouriteLocationsDTO _favouriteLocationsDTO = null;

         for(Favourite favourite: listFavouriteLocations){
         	_favouriteLocationsDTO = new FavouriteLocationsDTO(); 
         	listReturning.add(populateFavouriteLocationsDTO(favourite,_favouriteLocationsDTO));
         }
         
         return listReturning;
    }
    
    private FavouriteLocationsDTO populateFavouriteLocationsDTO(Favourite favourite, FavouriteLocationsDTO favouriteLocationsDTO){
		favouriteLocationsDTO.setFavLocId(favourite.getId());
		favouriteLocationsDTO.setLocId(favourite.getParkingLocation().getId());
		favouriteLocationsDTO.setLocation(favourite.getParkingLocation().getAddressLine1());
		favouriteLocationsDTO.setBusinessType(favourite.getParkingLocation().getBussinessType());
		favouriteLocationsDTO.setUserId(favourite.getUser().getId());
		
		ParkingLocationImage image = parkingLocationImageService.findImageURLByLocation(favourite.getParkingLocation().getId());
		if(image!=null){
			favouriteLocationsDTO.setUrl(image.getURL());
		}
		
		return favouriteLocationsDTO;
}
    
    boolean checkIfLocationIsFavourite(Long locId) throws Exception{
    	User user = null;
    	try{
    		user = userRepository.findUserByLoginName();
    	}catch(Exception e){
    		
    	}
		String qry = "from Favourite fav where fav.user.id=? and fav.parkingLocation.id=?";
		Query query = this.entityManager.createQuery(qry);
		query.setParameter(1, user.getId());
		query.setParameter(2, locId);
		return query.getResultList().size()>0?true:false;
    }
    

}
