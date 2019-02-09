package com.sg.netex.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.rutebanken.netex.model.Branding;
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
import com.sg.netex.dto.BrandingDTO;
import com.sg.netex.model.ButtonEvents;
import com.sg.netex.model.GenericTable;
import com.sg.netex.repository.BrandingRepository;

@Controller
public class BrandingController {

	@Autowired
	BrandingRepository brandingRepo;

	@Autowired
	private UtilConfig dozer;
	
	@RequestMapping(value = "/brandings", method = RequestMethod.GET)
    public ResponseEntity<List<BrandingDTO>> brandings(Model model) {
    	List<BrandingDTO> brandings = brandingRepo.findAll();
    	
        return new ResponseEntity<>(brandings,HttpStatus.OK);
    }
	
	
	// the simple table view for thymeleaf
	@RequestMapping(value = "/brandingGrid", method = RequestMethod.GET)
    public String branding(Model model) {
		
		List<BrandingDTO> brandings = brandingRepo.findAll();
    	
		List<GenericTable> tableItems = new ArrayList<>();
		
		for (BrandingDTO list : brandings) {
			GenericTable item = new GenericTable();
			if (list.getBranding().getDescription() != null)
				item.setDescription(list.getBranding().getDescription().getValue());
			item.setFareFrameType(list.getFareFrameType());
			if (list.getName() != null)
				item.setName(list.getName());
			item.setMongoId(list.getMongoId());
			if (list.getBranding().getName() != null)
				item.setName(list.getBranding().getName().getValue());
			if (list.getOperator() != null)
				item.setObjectType(list.getOperator());
			if (list.getBranding().getPrivateCode()!= null)
				item.setPrivateCode(list.getBranding().getPrivateCode().getValue());
			item.setVersion(list.getBranding().getVersion());
			
			tableItems.add(item);
		}
		
		model.addAttribute("tableItems",tableItems);
		
        ButtonEvents action = new ButtonEvents();
        action.setGetUrl("/brandings");
        action.setModifyItemUrl("/branding");
        action.setDeleteItemUrl("/branding");
        model.addAttribute("action", action);
        
        
        return "/html/genericTable.html :: grid";
    }
	
	@RequestMapping(value = "/branding", method = RequestMethod.GET)
    public String newBranding(Model model,@RequestParam(required=false) String mongoId) {
    	
		Optional<BrandingDTO> branding;
		
		if (mongoId != null && mongoId.trim().length() > 0) {
			branding = brandingRepo.findById(mongoId);
		} else {
			branding = Optional.of(new BrandingDTO());
			Branding doc = new Branding();
			branding.get().setBranding(doc);
			
			branding.get().getBranding().setId(dozer.generateNetexName("branding"));
			branding.get().getBranding().setVersion("latest");
		}
				
        model.addAttribute("item", branding);

        return "resourceFrame/branding.html :: edit";
    }
	
	@RequestMapping(value = "/branding", method = RequestMethod.POST)
    public String saveBranding(@ModelAttribute BrandingDTO branding, BindingResult errors, Model model) {
		
		branding.setMongoId(dozer.generateMongoId(branding.getBranding().getId(), branding.getBranding().getVersion(), "branding"));
		branding.setId(branding.getBranding().getId());
		branding.setVersion(branding.getBranding().getVersion());
		
		brandingRepo.save(branding);
		
		return "index";
    }

	@RequestMapping(value = "/branding", method = RequestMethod.DELETE)
    public String deleteBranding(Model model,@RequestParam(required=true) String mongoId) {
		
		if (brandingRepo.findById(mongoId).isPresent()) {
			brandingRepo.deleteById(mongoId);
		} else {
			System.out.println("Missing "+ mongoId + " id");
		}
		
		return "index";
    }
}
