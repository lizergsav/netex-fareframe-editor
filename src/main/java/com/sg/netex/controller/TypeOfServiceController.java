package com.sg.netex.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.rutebanken.netex.model.TypeOfService;
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
import com.sg.netex.dto.TypeOfServiceDTO;
import com.sg.netex.model.ButtonEvents;
import com.sg.netex.model.GenericTable;
import com.sg.netex.repository.TypeOfServiceRepository;

@Controller
public class TypeOfServiceController {
	
	@Autowired
	private UtilConfig dozer;

	@Autowired
	TypeOfServiceRepository typeOfServiceRepository; 
	
	@RequestMapping(value = "/typeOfservices", method = RequestMethod.GET)
    public ResponseEntity<List<TypeOfServiceDTO>> typeOfservices(Model model) {
    	List<TypeOfServiceDTO> typeOfTravelDocuments = typeOfServiceRepository.findAll();
    	
        return new ResponseEntity<>(typeOfTravelDocuments,HttpStatus.OK);
    }
	
	//typeOfService
	@RequestMapping(value = "/typeOfServiceGrid", method = RequestMethod.GET)
    public String typeOfService(Model model) {
		
		List<TypeOfServiceDTO> types = typeOfServiceRepository.findAll();
    	
		List<GenericTable> tableItems = new ArrayList<>();
		
		for (TypeOfServiceDTO list : types) {
			GenericTable item = new GenericTable();
			if (list.getTypeOfService().getDescription() != null)
				item.setDescription(list.getTypeOfService().getDescription().getValue());
			item.setFareFrameType(list.getFareFrameType());
			if (list.getName() != null)
				item.setName(list.getName());
			item.setMongoId(list.getMongoId());
			if (list.getTypeOfService().getName() != null)
				item.setName(list.getTypeOfService().getName().getValue());
			if (list.getOperator() != null)
				item.setObjectType(list.getOperator());
			if (list.getTypeOfService().getPrivateCode()!= null)
				item.setPrivateCode(list.getTypeOfService().getPrivateCode().getValue());
			item.setVersion(list.getTypeOfService().getVersion());
			
			tableItems.add(item);
		}
		
		model.addAttribute("tableItems",tableItems);
		
        ButtonEvents action = new ButtonEvents();
        action.setGetUrl("/typeOfServices");
        action.setModifyItemUrl("/typeOfService");
        action.setDeleteItemUrl("/typeOfService");
        model.addAttribute("action", action);
        
        
        return "/html/genericTable.html :: grid";
    }
	
	@RequestMapping(value = "/typeOfService", method = RequestMethod.GET)
    public String manageTypeOfService(Model model,@RequestParam(required=false) String mongoId) {
    	
		Optional<TypeOfServiceDTO> item;
		
		if (mongoId != null && mongoId.trim().length() > 0) {
			 item = typeOfServiceRepository.findById(mongoId);
		} else {
			item = Optional.of(new TypeOfServiceDTO());
			TypeOfService doc = new TypeOfService();
			item.get().setTypeOfService(doc);
			
			item.get().getTypeOfService().setId(dozer.generateNetexName("typeOfService"));
			item.get().getTypeOfService().setVersion("latest");
		}
				
        model.addAttribute("item", item);

        return "resourceFrame/typeOfService.html :: edit";
    }
	
	@RequestMapping(value = "/typeOfService", method = RequestMethod.POST)
    public String saveTypeOfService(@ModelAttribute TypeOfServiceDTO input, BindingResult errors, Model model) {
		
		input.setMongoId(dozer.generateMongoId(input.getTypeOfService().getId(), input.getTypeOfService().getVersion(), "typeOfService"));
		input.setId(input.getTypeOfService().getId());
		input.setVersion(input.getTypeOfService().getVersion());
		
		typeOfServiceRepository.save(input);
		
		return "index.html";
    }
	
	@RequestMapping(value = "/typeOfService", method = RequestMethod.DELETE)
    public String deletetypeOfService(Model model,@RequestParam(required=true) String mongoId) {
		
		if (typeOfServiceRepository.findById(mongoId).isPresent()) {
			typeOfServiceRepository.deleteById(mongoId);
		} else {
			System.out.println("Missing "+ mongoId + " id");
		}
		
		return "html/deleted";
    }
	
}
