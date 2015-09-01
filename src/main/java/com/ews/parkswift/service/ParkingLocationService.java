package com.ews.parkswift.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ews.parkswift.config.Constants;
import com.ews.parkswift.domain.ParkingLocation;
import com.ews.parkswift.domain.ParkingLocationFacility;
import com.ews.parkswift.domain.ParkingLocationImage;
import com.ews.parkswift.domain.ParkingSpace;
import com.ews.parkswift.domain.ParkingSpaceVehicleType;
import com.ews.parkswift.domain.PaypallAccount;
import com.ews.parkswift.domain.User;
import com.ews.parkswift.repository.ParkingLocationImageRepository;
import com.ews.parkswift.repository.ParkingLocationRepository;
import com.ews.parkswift.repository.ParkingSpaceRepository;
import com.ews.parkswift.repository.PaypallAccountRepository;
import com.ews.parkswift.web.rest.dto.ParkingSpaceDTO;
import com.ews.parkswift.web.rest.dto.parking.AvailableParkingDTO;

@Service
@Transactional
public class ParkingLocationService {

    private final Logger log = LoggerFactory.getLogger(ParkingLocationService.class);
    
    @Inject
    private ParkingLocationRepository parkingLocationRepository;
    @Inject
    private PaypallAccountRepository paypallAccountRepository;
    @Inject
    private ParkingLocationImageRepository parkingLocationImageRepository;
    @Inject
    private ParkingSpaceRepository parkingSpaceRepository;
    
    @Inject
    private UserService userService;
    
    public void save(ParkingLocation parkingLocation) throws ServiceException, IOException{
    	User user = userService.getUser();
    	parkingLocation.setUser(user);
    	
    	if(parkingLocation.getPaypallAccount()!=null){
    		PaypallAccount payPallAccount = parkingLocation.getPaypallAccount();
    		payPallAccount.setUser(user);
    		updateDefaultPaypallAccount(payPallAccount);
    		paypallAccountRepository.save(payPallAccount);
    		
    	}
    	
    	parkingLocationRepository.save(parkingLocation);
    }
    
    @SuppressWarnings("serial")
	public void update(ParkingLocation parkingLocation) throws ServiceException, IOException{
    	save(parkingLocation);
    	List<ParkingLocationImage> imagesFromDB = parkingLocationImageRepository.findAllByParkingLocation(parkingLocation);
    	File imageFolder = new File(Constants.LOCATION_IMAGES_FOLDER_PATH+File.separator+parkingLocation.getId());
    	List<ParkingLocationImage> imagesNotInDBButInDirectory = imagesNotInDBButInDirectory(
				imagesFromDB, imageFolder);
    	deleteImagesFromDirectory(imagesNotInDBButInDirectory, imageFolder);
    }

	private List<ParkingLocationImage> imagesNotInDBButInDirectory(
			List<ParkingLocationImage> imagesFromDB, File imageFolder) {
		List<ParkingLocationImage> imagesNotInDBButInDirectory = new ArrayList<>();
    	for(File file : imageFolder.listFiles()){
    		final Long imageId = Long.valueOf(file.getName().split("\\.")[0]);
    		final String imageType = file.getName().split("\\.")[1];
    		if(!imagesFromDB.stream().anyMatch(((e)->e.getId() == imageId))){
    			imagesNotInDBButInDirectory.add(new ParkingLocationImage(){{
    				setId(imageId);
    				setType(imageType);
    			}});
    		}
    	}
		return imagesNotInDBButInDirectory;
	}
    
    public void deleteImagesFromDirectory(List<ParkingLocationImage> parkingLocationImages, File imageFolder){
    	parkingLocationImages.forEach((e)->{
    		String imagePath = imageFolder+File.separator+e.getId()+"."+e.getType();
    		FileUtils.deleteQuietly(new File(imagePath));
    	});
    }

    public String  createSingleImageInDirectory(ParkingLocationImage e) {
		String url="";;	
		File imageFolder = new File(Constants.LOCATION_IMAGES_FOLDER_PATH);
			OutputStream os = null;
			try {
				FileUtils.forceMkdir(imageFolder);
				if(e.getImage()!=null){
					String imagePath = imageFolder+File.separator+e.getId()+"."+e.getType();
					os = new FileOutputStream(imagePath);
					os.write(e.getImage());
					e.setImage(null);
					url = "http://"+Constants.LOCATION_IMAGES_FOLDER_URL+"/"+e.getId()+"."+e.getType();
					e.setURL(url);
					return url;
				}
				
				
			} catch (Exception exception) {
				log.error("error writing image for parking location",exception);
			}finally{
				try {
					if(os!=null)
						os.close();
				} catch (Exception exception) {
					log.error("",exception);
				}
			}
		return url;	
	}
	
	


	private void updateDefaultPaypallAccount(
			PaypallAccount payPallAccount) {
		
		if(payPallAccount.getIsDefault()){
			paypallAccountRepository.findAllForCurrentUser().stream().filter(pa->!pa.getEmail().equals(payPallAccount.getEmail())).
				collect(Collectors.toList()).stream().forEach(pa->pa.setIsDefault(false));
			
		}
	}



	@SuppressWarnings("serial")
	public void delete(Long id) throws IOException {
		List<ParkingLocationImage> parkingLocationImages = parkingLocationImageRepository.findAllByParkingLocation(new ParkingLocation(){{
			setId(id);
		}});
		File imageFolder = new File(Constants.LOCATION_IMAGES_FOLDER_PATH+File.separator+id);
		deleteImagesFromDirectory(parkingLocationImages, imageFolder);
		FileUtils.deleteDirectory(imageFolder);
		parkingLocationRepository.delete(id);
		
	}

	public List<ParkingLocation> findAllForCurrentUser() {
		return parkingLocationRepository.findAllForCurrentUser().stream().peek((ParkingLocation parkingLocation)->{
			setParkingSpaces(parkingLocation);
		}).collect(Collectors.toList());
	}

	private void setParkingSpaces(ParkingLocation parkingLocation) {
		Set<ParkingSpaceDTO> parkingSpaces = new HashSet<>();
		parkingSpaceRepository.findAllByParkingLocationId(parkingLocation.getId()).forEach((parkingSpace)->{
			parkingSpaces.add(new ParkingSpaceDTO(parkingSpace.getId(), parkingSpace.getNick(), parkingSpace.getNumberOfSpaces()));
		});
		parkingLocation.setParkingSpaces(parkingSpaces);
	}

	public ParkingLocation findOne(Long id) {
		ParkingLocation parkingLocation = parkingLocationRepository.findOne(id);
		setParkingSpaces(parkingLocation);
		return parkingLocation;
	}
	
	public AvailableParkingDTO findLocationById(Long id){
		AvailableParkingDTO availableParkingDTO = new AvailableParkingDTO();
		ParkingLocation parkingLocation = parkingLocationRepository.findOne(id);
		if(parkingLocation!=null){
			populateAvailableLocationDTO(availableParkingDTO, parkingLocation);
		}
		return availableParkingDTO;
	}
	
	
	private void populateAvailableLocationDTO(AvailableParkingDTO availableParkingDTO, ParkingLocation parkingLocation){
		availableParkingDTO.setLocId(parkingLocation.getId());
    	availableParkingDTO.setBussinessType(parkingLocation.getBussinessType());
    	availableParkingDTO.setAddressLine1(parkingLocation.getAddressLine1());
    	availableParkingDTO.setAddressLine2(parkingLocation.getAddressLine2());
    	availableParkingDTO.setCity(parkingLocation.getCity());
    	availableParkingDTO.setState(parkingLocation.getState());
    	availableParkingDTO.setZipCode(parkingLocation.getZipCode());
    	availableParkingDTO.setCountry(parkingLocation.getCountry());
    	availableParkingDTO.setLongitude(parkingLocation.getLongitude());
    	availableParkingDTO.setLattitude(parkingLocation.getLattitude());
    	
    	for (ParkingLocationImage plImage : parkingLocation.getParkingLocationImages())
    		availableParkingDTO.getParkingLocationImages().add(plImage.getURL());
    	
    	for (ParkingLocationFacility plFacility : parkingLocation.getParkingLocationFacilitys())
    		availableParkingDTO.getParkingLocationFacilitys().add(plFacility.getFacility());
    	
		List<ParkingSpace> listParkingSpace = parkingSpaceRepository.findAllByParkingLocationId(parkingLocation.getId());
		for(ParkingSpace parkingSpace: listParkingSpace){
			for(ParkingSpaceVehicleType parkingSpaceVehicleType: parkingSpace.getParkingSpaceVehicleTypes()){
				availableParkingDTO.getParkingSpaceVehicleTypes().add(parkingSpaceVehicleType.getType());
				availableParkingDTO.setParkingSpacePriceEntrys(parkingSpace.getParkingSpacePriceEntrys());
			}
		}
	}

}
