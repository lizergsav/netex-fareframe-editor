package com.sg.netex.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.rutebanken.netex.model.CommercialProfile;
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
import com.sg.netex.dto.CommercialProfileDTO;
import com.sg.netex.model.ButtonEvents;
import com.sg.netex.model.GenericTable;
import com.sg.netex.repository.CommercialProfileRepository;

@Controller
public class CommercialProfileController {

	@Autowired
	CommercialProfileRepository commercialProfileRepo;

	@Autowired
	private UtilConfig dozer;
	
	@RequestMapping(value = "/commercialProfiles", method = RequestMethod.GET)
    public ResponseEntity<List<CommercialProfileDTO>> commercialProfiles(Model model) {
    	List<CommercialProfileDTO> commercialProfiles = commercialProfileRepo.findAll();
    	
        return new ResponseEntity<>(commercialProfiles,HttpStatus.OK);
    }
	
	
	// the simple table view for thymeleaf
	@RequestMapping(value = "/commercialProfileGrid", method = RequestMethod.GET)
    public String commercialProfile(Model model) {
		
		List<CommercialProfileDTO> commercialProfiles = commercialProfileRepo.findAll();
    	
		List<GenericTable> tableItems = new ArrayList<>();
		
		for (CommercialProfileDTO list : commercialProfiles) {
			GenericTable item = new GenericTable();
			item.setFareFrameType(list.getFareFrameType());
			if (list.getName() != null)
				item.setName(list.getName());
			item.setMongoId(list.getMongoId());
			if (list.getCommercialProfile().getName() != null)
				item.setName(list.getCommercialProfile().getName().getValue());
			if (list.getOperator() != null)
				item.setObjectType(list.getOperator());

			item.setVersion(list.getCommercialProfile().getVersion());
			
			tableItems.add(item);
		}
		
		model.addAttribute("tableItems",tableItems);
		
        ButtonEvents action = new ButtonEvents();
        action.setGetUrl("/commercialProfiles");
        action.setModifyItemUrl("/commercialProfile");
        action.setDeleteItemUrl("/commercialProfile");
        model.addAttribute("action", action);
        
        
        return "/html/simpleTable.html :: grid";
    }
	
	@RequestMapping(value = "/commercialProfile", method = RequestMethod.GET)
    public String newCommercialProfile(Model model,@RequestParam(required=false) String mongoId) {
    	
		Optional<CommercialProfileDTO> commercialProfile;
		
		if (mongoId != null && mongoId.trim().length() > 0) {
			commercialProfile = commercialProfileRepo.findById(mongoId);
		} else {
			commercialProfile = Optional.of(new CommercialProfileDTO());
			CommercialProfile doc = new CommercialProfile();
			commercialProfile.get().setCommercialProfile(doc);
			
			commercialProfile.get().getCommercialProfile().setId(dozer.generateNetexName("commercialProfile"));
			commercialProfile.get().getCommercialProfile().setVersion("latest");
		}
				
        model.addAttribute("item", commercialProfile);

        return "fares/commercialProfile.html :: edit";
    }
	
	@RequestMapping(value = "/commercialProfile", method = RequestMethod.POST)
    public String saveCommercialProfile(@ModelAttribute CommercialProfileDTO commercialProfile, BindingResult errors, Model model) {
		
		commercialProfile.setMongoId(dozer.generateMongoId(commercialProfile.getCommercialProfile().getId(), commercialProfile.getCommercialProfile().getVersion(), "commercialProfile"));
		commercialProfile.setId(commercialProfile.getCommercialProfile().getId());
		commercialProfile.setVersion(commercialProfile.getCommercialProfile().getVersion());
		
		commercialProfileRepo.save(commercialProfile);
		
		return "index";
    }

	@RequestMapping(value = "/commercialProfile", method = RequestMethod.DELETE)
    public String deleteCommercialProfile(Model model,@RequestParam(required=true) String mongoId) {
		
		if (commercialProfileRepo.findById(mongoId).isPresent()) {
			commercialProfileRepo.deleteById(mongoId);
		} else {
			System.out.println("Missing "+ mongoId + " id");
		}
		
		return "index";
    }
}
