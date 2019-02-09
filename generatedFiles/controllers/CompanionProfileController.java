package com.sg.netex.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.rutebanken.netex.model.CompanionProfile;
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
import com.sg.netex.dto.CompanionProfileDTO;
import com.sg.netex.model.ButtonEvents;
import com.sg.netex.model.GenericTable;
import com.sg.netex.repository.CompanionProfileRepository;

@Controller
public class CompanionProfileController {

	@Autowired
	CompanionProfileRepository companionProfileRepo;

	@Autowired
	private UtilConfig dozer;
	
	@RequestMapping(value = "/companionProfiles", method = RequestMethod.GET)
    public ResponseEntity<List<CompanionProfileDTO>> companionProfiles(Model model) {
    	List<CompanionProfileDTO> companionProfiles = companionProfileRepo.findAll();
    	
        return new ResponseEntity<>(companionProfiles,HttpStatus.OK);
    }
	
	
	// the simple table view for thymeleaf
	@RequestMapping(value = "/companionProfileGrid", method = RequestMethod.GET)
    public String companionProfile(Model model) {
		
		List<CompanionProfileDTO> companionProfiles = companionProfileRepo.findAll();
    	
		List<GenericTable> tableItems = new ArrayList<>();
		
		for (CompanionProfileDTO list : companionProfiles) {
			GenericTable item = new GenericTable();
			item.setFareFrameType(list.getFareFrameType());
			if (list.getName() != null)
				item.setName(list.getName());
			item.setMongoId(list.getMongoId());
			if (list.getCompanionProfile().getName() != null)
				item.setName(list.getCompanionProfile().getName().getValue());
			if (list.getOperator() != null)
				item.setObjectType(list.getOperator());

			item.setVersion(list.getCompanionProfile().getVersion());
			
			tableItems.add(item);
		}
		
		model.addAttribute("tableItems",tableItems);
		
        ButtonEvents action = new ButtonEvents();
        action.setGetUrl("/companionProfiles");
        action.setModifyItemUrl("/companionProfile");
        action.setDeleteItemUrl("/companionProfile");
        model.addAttribute("action", action);
        
        
        return "/html/simpleTable.html :: grid";
    }
	
	@RequestMapping(value = "/companionProfile", method = RequestMethod.GET)
    public String newCompanionProfile(Model model,@RequestParam(required=false) String mongoId) {
    	
		Optional<CompanionProfileDTO> companionProfile;
		
		if (mongoId != null && mongoId.trim().length() > 0) {
			companionProfile = companionProfileRepo.findById(mongoId);
		} else {
			companionProfile = Optional.of(new CompanionProfileDTO());
			CompanionProfile doc = new CompanionProfile();
			companionProfile.get().setCompanionProfile(doc);
			
			companionProfile.get().getCompanionProfile().setId(dozer.generateNetexName("companionProfile"));
			companionProfile.get().getCompanionProfile().setVersion("latest");
		}
				
        model.addAttribute("item", companionProfile);

        return "fares/companionProfile.html :: edit";
    }
	
	@RequestMapping(value = "/companionProfile", method = RequestMethod.POST)
    public String saveCompanionProfile(@ModelAttribute CompanionProfileDTO companionProfile, BindingResult errors, Model model) {
		
		companionProfile.setMongoId(dozer.generateMongoId(companionProfile.getCompanionProfile().getId(), companionProfile.getCompanionProfile().getVersion(), "companionProfile"));
		companionProfile.setId(companionProfile.getCompanionProfile().getId());
		companionProfile.setVersion(companionProfile.getCompanionProfile().getVersion());
		
		companionProfileRepo.save(companionProfile);
		
		return "index";
    }

	@RequestMapping(value = "/companionProfile", method = RequestMethod.DELETE)
    public String deleteCompanionProfile(Model model,@RequestParam(required=true) String mongoId) {
		
		if (companionProfileRepo.findById(mongoId).isPresent()) {
			companionProfileRepo.deleteById(mongoId);
		} else {
			System.out.println("Missing "+ mongoId + " id");
		}
		
		return "index";
    }
}
