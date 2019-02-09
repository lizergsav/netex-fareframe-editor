package com.sg.netex.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.rutebanken.netex.model.TypeOfZone;
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
import com.sg.netex.dto.TypeOfZoneDTO;
import com.sg.netex.model.ButtonEvents;
import com.sg.netex.model.GenericTable;
import com.sg.netex.repository.TypeOfZoneRepository;

@Controller
public class TypeOfZoneController {

	@Autowired
	TypeOfZoneRepository typeOfZoneRepo;

	@Autowired
	private UtilConfig dozer;
	
	@RequestMapping(value = "/typeOfZones", method = RequestMethod.GET)
    public ResponseEntity<List<TypeOfZoneDTO>> typeOfZones(Model model) {
    	List<TypeOfZoneDTO> typeOfZones = typeOfZoneRepo.findAll();
    	
        return new ResponseEntity<>(typeOfZones,HttpStatus.OK);
    }
	
	
	// the simple table view for thymeleaf
	@RequestMapping(value = "/typeOfZoneGrid", method = RequestMethod.GET)
    public String typeOfZone(Model model) {
		
		List<TypeOfZoneDTO> typeOfZones = typeOfZoneRepo.findAll();
    	
		List<GenericTable> tableItems = new ArrayList<>();
		
		for (TypeOfZoneDTO list : typeOfZones) {
			GenericTable item = new GenericTable();
			if (list.getTypeOfZone().getDescription() != null)
				item.setDescription(list.getTypeOfZone().getDescription().getValue());
			item.setFareFrameType(list.getFareFrameType());
			if (list.getName() != null)
				item.setName(list.getName());
			item.setMongoId(list.getMongoId());
			if (list.getTypeOfZone().getName() != null)
				item.setName(list.getTypeOfZone().getName().getValue());
			if (list.getOperator() != null)
				item.setObjectType(list.getOperator());
			if (list.getTypeOfZone().getPrivateCode()!= null)
				item.setPrivateCode(list.getTypeOfZone().getPrivateCode().getValue());
			item.setVersion(list.getTypeOfZone().getVersion());
			
			tableItems.add(item);
		}
		
		model.addAttribute("tableItems",tableItems);
		
        ButtonEvents action = new ButtonEvents();
        action.setGetUrl("/typeOfZones");
        action.setModifyItemUrl("/typeOfZone");
        action.setDeleteItemUrl("/typeOfZone");
        model.addAttribute("action", action);
        
        
        return "/html/genericTable.html :: grid";
    }
	
	@RequestMapping(value = "/typeOfZone", method = RequestMethod.GET)
    public String newTypeOfZone(Model model,@RequestParam(required=false) String mongoId) {
    	
		Optional<TypeOfZoneDTO> typeOfZone;
		
		if (mongoId != null && mongoId.trim().length() > 0) {
			typeOfZone = typeOfZoneRepo.findById(mongoId);
		} else {
			typeOfZone = Optional.of(new TypeOfZoneDTO());
			TypeOfZone doc = new TypeOfZone();
			typeOfZone.get().setTypeOfZone(doc);
			
			typeOfZone.get().getTypeOfZone().setId(dozer.generateNetexName("typeOfZone"));
			typeOfZone.get().getTypeOfZone().setVersion("latest");
		}
				
        model.addAttribute("item", typeOfZone);

        return "resourceFrame/typeOfZone.html :: edit";
    }
	
	@RequestMapping(value = "/typeOfZone", method = RequestMethod.POST)
    public String saveTypeOfZone(@ModelAttribute TypeOfZoneDTO typeOfZone, BindingResult errors, Model model) {
		
		typeOfZone.setMongoId(dozer.generateMongoId(typeOfZone.getTypeOfZone().getId(), typeOfZone.getTypeOfZone().getVersion(), "typeOfZone"));
		typeOfZone.setId(typeOfZone.getTypeOfZone().getId());
		typeOfZone.setVersion(typeOfZone.getTypeOfZone().getVersion());
		
		typeOfZoneRepo.save(typeOfZone);
		
		return "index";
    }

	@RequestMapping(value = "/typeOfZone", method = RequestMethod.DELETE)
    public String deleteTypeOfZone(Model model,@RequestParam(required=true) String mongoId) {
		
		if (typeOfZoneRepo.findById(mongoId).isPresent()) {
			typeOfZoneRepo.deleteById(mongoId);
		} else {
			System.out.println("Missing "+ mongoId + " id");
		}
		
		return "index";
    }
}
