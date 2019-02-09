package com.sg.netex.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.rutebanken.netex.model.TypeOfOrganisation;
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
import com.sg.netex.dto.TypeOfOrganisationDTO;
import com.sg.netex.model.ButtonEvents;
import com.sg.netex.model.GenericTable;
import com.sg.netex.repository.TypeOfOrganisationRepository;

@Controller
public class TypeOfOrganisationController {

	@Autowired
	TypeOfOrganisationRepository typeOfOrganisationRepo;

	@Autowired
	private UtilConfig dozer;
	
	@RequestMapping(value = "/typeOfOrganisations", method = RequestMethod.GET)
    public ResponseEntity<List<TypeOfOrganisationDTO>> typeOfOrganisations(Model model) {
    	List<TypeOfOrganisationDTO> typeOfOrganisations = typeOfOrganisationRepo.findAll();
    	
        return new ResponseEntity<>(typeOfOrganisations,HttpStatus.OK);
    }
	
	
	// the simple table view for thymeleaf
	@RequestMapping(value = "/typeOfOrganisationGrid", method = RequestMethod.GET)
    public String typeOfOrganisation(Model model) {
		
		List<TypeOfOrganisationDTO> typeOfOrganisations = typeOfOrganisationRepo.findAll();
    	
		List<GenericTable> tableItems = new ArrayList<>();
		
		for (TypeOfOrganisationDTO list : typeOfOrganisations) {
			GenericTable item = new GenericTable();
			if (list.getTypeOfOrganisation().getDescription() != null)
				item.setDescription(list.getTypeOfOrganisation().getDescription().getValue());
			item.setFareFrameType(list.getFareFrameType());
			if (list.getName() != null)
				item.setName(list.getName());
			item.setMongoId(list.getMongoId());
			if (list.getTypeOfOrganisation().getName() != null)
				item.setName(list.getTypeOfOrganisation().getName().getValue());
			if (list.getOperator() != null)
				item.setObjectType(list.getOperator());
			if (list.getTypeOfOrganisation().getPrivateCode()!= null)
				item.setPrivateCode(list.getTypeOfOrganisation().getPrivateCode().getValue());
			item.setVersion(list.getTypeOfOrganisation().getVersion());
			
			tableItems.add(item);
		}
		
		model.addAttribute("tableItems",tableItems);
		
        ButtonEvents action = new ButtonEvents();
        action.setGetUrl("/typeOfOrganisations");
        action.setModifyItemUrl("/typeOfOrganisation");
        action.setDeleteItemUrl("/typeOfOrganisation");
        model.addAttribute("action", action);
        
        
        return "/html/genericTable.html :: grid";
    }
	
	@RequestMapping(value = "/typeOfOrganisation", method = RequestMethod.GET)
    public String newTypeOfOrganisation(Model model,@RequestParam(required=false) String mongoId) {
    	
		Optional<TypeOfOrganisationDTO> typeOfOrganisation;
		
		if (mongoId != null && mongoId.trim().length() > 0) {
			typeOfOrganisation = typeOfOrganisationRepo.findById(mongoId);
		} else {
			typeOfOrganisation = Optional.of(new TypeOfOrganisationDTO());
			TypeOfOrganisation doc = new TypeOfOrganisation();
			typeOfOrganisation.get().setTypeOfOrganisation(doc);
			
			typeOfOrganisation.get().getTypeOfOrganisation().setId(dozer.generateNetexName("typeOfOrganisation"));
			typeOfOrganisation.get().getTypeOfOrganisation().setVersion("latest");
		}
				
        model.addAttribute("item", typeOfOrganisation);

        return "resourceFrame/typeOfOrganisation.html :: edit";
    }
	
	@RequestMapping(value = "/typeOfOrganisation", method = RequestMethod.POST)
    public String saveTypeOfOrganisation(@ModelAttribute TypeOfOrganisationDTO typeOfOrganisation, BindingResult errors, Model model) {
		
		typeOfOrganisation.setMongoId(dozer.generateMongoId(typeOfOrganisation.getTypeOfOrganisation().getId(), typeOfOrganisation.getTypeOfOrganisation().getVersion(), "typeOfOrganisation"));
		typeOfOrganisation.setId(typeOfOrganisation.getTypeOfOrganisation().getId());
		typeOfOrganisation.setVersion(typeOfOrganisation.getTypeOfOrganisation().getVersion());
		
		typeOfOrganisationRepo.save(typeOfOrganisation);
		
		return "index";
    }

	@RequestMapping(value = "/typeOfOrganisation", method = RequestMethod.DELETE)
    public String deleteTypeOfOrganisation(Model model,@RequestParam(required=true) String mongoId) {
		
		if (typeOfOrganisationRepo.findById(mongoId).isPresent()) {
			typeOfOrganisationRepo.deleteById(mongoId);
		} else {
			System.out.println("Missing "+ mongoId + " id");
		}
		
		return "index";
    }
}
