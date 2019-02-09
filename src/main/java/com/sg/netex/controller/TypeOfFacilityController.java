package com.sg.netex.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.rutebanken.netex.model.TypeOfFacility;
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
import com.sg.netex.dto.TypeOfFacilityDTO;
import com.sg.netex.model.ButtonEvents;
import com.sg.netex.model.GenericTable;
import com.sg.netex.repository.TypeOfFacilityRepository;

@Controller
public class TypeOfFacilityController {

	@Autowired
	TypeOfFacilityRepository typeOfFacilityRepo;

	@Autowired
	private UtilConfig dozer;
	
	@RequestMapping(value = "/typeOfFacilitys", method = RequestMethod.GET)
    public ResponseEntity<List<TypeOfFacilityDTO>> typeOfFacilitys(Model model) {
    	List<TypeOfFacilityDTO> typeOfFacilitys = typeOfFacilityRepo.findAll();
    	
        return new ResponseEntity<>(typeOfFacilitys,HttpStatus.OK);
    }
	
	
	// the simple table view for thymeleaf
	@RequestMapping(value = "/typeOfFacilityGrid", method = RequestMethod.GET)
    public String typeOfFacility(Model model) {
		
		List<TypeOfFacilityDTO> typeOfFacilitys = typeOfFacilityRepo.findAll();
    	
		List<GenericTable> tableItems = new ArrayList<>();
		
		for (TypeOfFacilityDTO list : typeOfFacilitys) {
			GenericTable item = new GenericTable();
			if (list.getTypeOfFacility().getDescription() != null)
				item.setDescription(list.getTypeOfFacility().getDescription().getValue());
			item.setFareFrameType(list.getFareFrameType());
			if (list.getName() != null)
				item.setName(list.getName());
			item.setMongoId(list.getMongoId());
			if (list.getTypeOfFacility().getName() != null)
				item.setName(list.getTypeOfFacility().getName().getValue());
			if (list.getOperator() != null)
				item.setObjectType(list.getOperator());
			if (list.getTypeOfFacility().getPrivateCode()!= null)
				item.setPrivateCode(list.getTypeOfFacility().getPrivateCode().getValue());
			item.setVersion(list.getTypeOfFacility().getVersion());
			
			tableItems.add(item);
		}
		
		model.addAttribute("tableItems",tableItems);
		
        ButtonEvents action = new ButtonEvents();
        action.setGetUrl("/typeOfFacilitys");
        action.setModifyItemUrl("/typeOfFacility");
        action.setDeleteItemUrl("/typeOfFacility");
        model.addAttribute("action", action);
        
        
        return "/html/genericTable.html :: grid";
    }
	
	@RequestMapping(value = "/typeOfFacility", method = RequestMethod.GET)
    public String newTypeOfFacility(Model model,@RequestParam(required=false) String mongoId) {
    	
		Optional<TypeOfFacilityDTO> typeOfFacility;
		
		if (mongoId != null && mongoId.trim().length() > 0) {
			typeOfFacility = typeOfFacilityRepo.findById(mongoId);
		} else {
			typeOfFacility = Optional.of(new TypeOfFacilityDTO());
			TypeOfFacility doc = new TypeOfFacility();
			typeOfFacility.get().setTypeOfFacility(doc);
			
			typeOfFacility.get().getTypeOfFacility().setId(dozer.generateNetexName("typeOfFacility"));
			typeOfFacility.get().getTypeOfFacility().setVersion("latest");
		}
				
        model.addAttribute("item", typeOfFacility);

        return "resourceFrame/typeOfFacility.html :: edit";
    }
	
	@RequestMapping(value = "/typeOfFacility", method = RequestMethod.POST)
    public String saveTypeOfFacility(@ModelAttribute TypeOfFacilityDTO typeOfFacility, BindingResult errors, Model model) {
		
		typeOfFacility.setMongoId(dozer.generateMongoId(typeOfFacility.getTypeOfFacility().getId(), typeOfFacility.getTypeOfFacility().getVersion(), "typeOfFacility"));
		typeOfFacility.setId(typeOfFacility.getTypeOfFacility().getId());
		typeOfFacility.setVersion(typeOfFacility.getTypeOfFacility().getVersion());
		
		typeOfFacilityRepo.save(typeOfFacility);
		
		return "index";
    }

	@RequestMapping(value = "/typeOfFacility", method = RequestMethod.DELETE)
    public String deleteTypeOfFacility(Model model,@RequestParam(required=true) String mongoId) {
		
		if (typeOfFacilityRepo.findById(mongoId).isPresent()) {
			typeOfFacilityRepo.deleteById(mongoId);
		} else {
			System.out.println("Missing "+ mongoId + " id");
		}
		
		return "index";
    }
}
