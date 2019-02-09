package com.sg.netex.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.rutebanken.netex.model.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sg.netex.config.UtilConfig;
import com.sg.netex.dto.UserProfileDTO;
import com.sg.netex.model.ButtonEvents;
import com.sg.netex.model.GenericTable;
import com.sg.netex.repository.UserProfileRepository;

@Controller
public class UserProfileController {

	@Autowired
	UserProfileRepository userProfileRepo;

	@Autowired
	private UtilConfig dozer;
	
	@RequestMapping(value = "/userProfiles", method = RequestMethod.GET)
    public ResponseEntity<List<UserProfileDTO>> userProfiles(Model model) {
    	List<UserProfileDTO> userProfiles = userProfileRepo.findAll();
    	
        return new ResponseEntity<>(userProfiles,HttpStatus.OK);
    }
	
	
	// the simple table view for thymeleaf
	@RequestMapping(value = "/userProfileGrid", method = RequestMethod.GET)
    public String userProfile(Model model) {
		
		List<UserProfileDTO> userProfiles = userProfileRepo.findAll();
    	
		List<GenericTable> tableItems = new ArrayList<>();
		
		for (UserProfileDTO list : userProfiles) {
			GenericTable item = new GenericTable();
			item.setFareFrameType(list.getFareFrameType());
			if (list.getName() != null)
				item.setName(list.getName());
			item.setMongoId(list.getMongoId());
			if (list.getUserProfile().getName() != null)
				item.setName(list.getUserProfile().getName().getValue());
			if (list.getOperator() != null)
				item.setObjectType(list.getOperator());

			item.setVersion(list.getUserProfile().getVersion());
			
			tableItems.add(item);
		}
		
		model.addAttribute("tableItems",tableItems);
		
        ButtonEvents action = new ButtonEvents();
        action.setGetUrl("/userProfiles");
        action.setModifyItemUrl("/userProfile");
        action.setDeleteItemUrl("/userProfile");
        model.addAttribute("action", action);
        
        
        return "/html/simpleTable.html :: grid";
    }
	
	@RequestMapping(value = "/userProfile", method = RequestMethod.GET)
    public String newUserProfile(Model model,@RequestParam(required=false) String mongoId) {
    	
		Optional<UserProfileDTO> userProfile;
		
		if (mongoId != null && mongoId.trim().length() > 0) {
			userProfile = userProfileRepo.findById(mongoId);
		} else {
			userProfile = Optional.of(new UserProfileDTO());
			UserProfile doc = new UserProfile();
			userProfile.get().setUserProfile(doc);
			
			userProfile.get().getUserProfile().setId(dozer.generateNetexName("userProfile"));
			userProfile.get().getUserProfile().setVersion("latest");
		}
				
        model.addAttribute("item", userProfile);

        return "fares/userProfile.html :: edit";
    }
	
	@RequestMapping(value = "/userProfile", method = RequestMethod.POST)
    public String saveUserProfile(@ModelAttribute UserProfileDTO userProfile, BindingResult errors, Model model) {
		
		userProfile.setMongoId(dozer.generateMongoId(userProfile.getUserProfile().getId(), userProfile.getUserProfile().getVersion(), "userProfile"));
		userProfile.setId(userProfile.getUserProfile().getId());
		userProfile.setVersion(userProfile.getUserProfile().getVersion());
		
		userProfileRepo.save(userProfile);
		
		return "index";
    }

	@RequestMapping(value = "/userProfile", method = RequestMethod.DELETE)
    public String deleteUserProfile(Model model,@RequestParam(required=true) String mongoId) {
		
		if (userProfileRepo.findById(mongoId).isPresent()) {
			userProfileRepo.deleteById(mongoId);
		} else {
			System.out.println("Missing "+ mongoId + " id");
		}
		
		return "index";
    }
}
