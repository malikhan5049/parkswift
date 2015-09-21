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
import com.ews.parkswift.domain.Feedback;
import com.ews.parkswift.domain.ParkingLocation;
import com.ews.parkswift.domain.ParkingLocationContactInfo;
import com.ews.parkswift.domain.ParkingLocationFacility;
import com.ews.parkswift.domain.ParkingLocationImage;
import com.ews.parkswift.domain.ParkingSpace;
import com.ews.parkswift.domain.ParkingSpaceVehicleType;
import com.ews.parkswift.domain.PaypallAccount;
import com.ews.parkswift.domain.User;
import com.ews.parkswift.repository.FeedbackRepository;
import com.ews.parkswift.repository.ParkingLocationContactInfoRepository;
import com.ews.parkswift.repository.ParkingLocationFacilityRepository;
import com.ews.parkswift.repository.ParkingLocationImageRepository;
import com.ews.parkswift.repository.ParkingLocationRepository;
import com.ews.parkswift.repository.ParkingSpaceRepository;
import com.ews.parkswift.repository.PaypallAccountRepository;
import com.ews.parkswift.web.rest.dto.ParkingSpaceDTO;
import com.ews.parkswift.web.rest.dto.parking.AvailableParkingDTO;
import com.ews.parkswift.web.rest.dto.parking.ParkingLocationDTO;

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
    private ParkingLocationContactInfoRepository parkingLocationContactInfoRepository;
    @Inject
    private ParkingLocationFacilityRepository parkingLocationFacilityRepository;
    @Inject
    private FeedbackRepository feedbackRepository;
    
    
    @Inject
    private UserService userService;
    
    public void save(ParkingLocation parkingLocation) throws ServiceException, IOException{
    	User user = userService.getUser();
    	parkingLocation.setUser(user);
    	
    	if(parkingLocation.getPaypallAccount()!=null){
    		PaypallAccount payPallAccount = paypallAccountRepository.findOneByEmail(parkingLocation.getPaypallAccount().getEmail());
    		if(null==payPallAccount){
    			payPallAccount = new PaypallAccount();
    			payPallAccount.setEmail(parkingLocation.getPaypallAccount().getEmail());
    			payPallAccount.setIsDefault(true);
    			payPallAccount.setUser(user);
    			paypallAccountRepository.save(payPallAccount);
    		}
    		updateDefaultPaypallAccount(payPallAccount);
    		parkingLocation.setPaypallAccount(payPallAccount);
    	}
    	if(parkingLocation.getId()!=null){
    		ParkingLocation pl = parkingLocationRepository.findOne(parkingLocation.getId());
    		if(null!=pl){
    			parkingLocation.setParkingSpaceEntitys(pl.getParkingSpaceEntitys());
    		}
    	}
    	
    	parkingLocationRepository.save(parkingLocation);
    }
    
    public void update(ParkingLocationDTO parkingLocationDTO) throws ServiceException, IOException{
    	ParkingLocation parkingLocation = new ParkingLocation();
    	User user = userService.getUser();
    	if(parkingLocationDTO.getId()!=null && !parkingLocationDTO.getId().equals("")){
    		parkingLocation.setId(parkingLocationDTO.getId());
    	}
    	parkingLocation.setUser(user);
    	parkingLocation.setBussinessType(parkingLocationDTO.getBussinessType());
    	parkingLocation.setAddressLine1(parkingLocationDTO.getAddressLine1());
    	parkingLocation.setAddressLine2(parkingLocationDTO.getAddressLine2());
    	parkingLocation.setCity(parkingLocationDTO.getCity());
    	parkingLocation.setState(parkingLocationDTO.getState());
    	parkingLocation.setCountry(parkingLocationDTO.getCountry());
    	parkingLocation.setZipCode(parkingLocationDTO.getZipCode());
    	parkingLocation.setLongitude(parkingLocationDTO.getLongitude());
    	parkingLocation.setLattitude(parkingLocationDTO.getLattitude());
    	parkingLocation.setActive(parkingLocationDTO.isActive());
    	
    	if(parkingLocationDTO.getPaypallAccount()!=null){
    		PaypallAccount payPallAccount = parkingLocationDTO.getPaypallAccount();
    		payPallAccount.setUser(user);
    		updateDefaultPaypallAccount(payPallAccount);
    		paypallAccountRepository.save(payPallAccount);
    		parkingLocation.setPaypallAccount(payPallAccount);
    	}
    	
    	ParkingLocationContactInfo parkingLocationContactInfo = new ParkingLocationContactInfo();
    	parkingLocationContactInfo = parkingLocationDTO.getParkingLocationContactInfo();
    	parkingLocationContactInfoRepository.save(parkingLocationContactInfo);
    	parkingLocation.setParkingLocationContactInfo(parkingLocationContactInfo);
    	
    	for(ParkingLocationFacility facility: parkingLocationDTO.getParkingLocationFacilitys()){
    		parkingLocationFacilityRepository.save(facility);
    		parkingLocation.getParkingLocationFacilitys().add(facility);
    	}
    	
    	for(Feedback feedback: parkingLocationDTO.getFeedbacks()){
    		feedbackRepository.save(feedback);
    		parkingLocation.getFeedbacks().add(feedback);
    	}
    	
    	for(ParkingLocationImage image: parkingLocationDTO.getParkingLocationImages()){
    		parkingLocationImageRepository.save(image);
    		parkingLocation.getParkingLocationImages().add(image);
    	}
    	
    	for(ParkingSpace parkingSpace: parkingLocationDTO.getParkingSpaceEntitys()){
    		parkingSpaceRepository.save(parkingSpace);
    		parkingLocation.getParkingSpaceEntitys().add(parkingSpace);
    	}
    	
    	parkingLocationRepository.saveAndFlush(parkingLocation);
    }
    
    
    
//    @SuppressWarnings("serial")
//	public void update(ParkingLocation parkingLocation) throws ServiceException, IOException{
//    	save(parkingLocation);
//    	List<ParkingLocationImage> imagesFromDB = parkingLocationImageRepository.findAllByParkingLocation(parkingLocation);
//    	File imageFolder = new File(Constants.LOCATION_IMAGES_FOLDER_PATH+File.separator+parkingLocation.getId());
//    	List<ParkingLocationImage> imagesNotInDBButInDirectory = imagesNotInDBButInDirectory(
//				imagesFromDB, imageFolder);
//    	deleteImagesFromDirectory(imagesNotInDBButInDirectory, imageFolder);
//    }

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
