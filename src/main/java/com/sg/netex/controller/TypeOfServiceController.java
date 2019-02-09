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
	TypeOfServiceRepository typeOfServiceRepo;

	@Autowired
	private UtilConfig dozer;
	
	@RequestMapping(value = "/typeOfServices", method = RequestMethod.GET)
    public ResponseEntity<List<TypeOfServiceDTO>> typeOfServices(Model model) {
    	List<TypeOfServiceDTO> typeOfServices = typeOfServiceRepo.findAll();
    	
        return new ResponseEntity<>(typeOfServices,HttpStatus.OK);
    }
	
	
	// the simple table view for thymeleaf
	@RequestMapping(value = "/typeOfServiceGrid", method = RequestMethod.GET)
    public String typeOfService(Model model) {
		
		List<TypeOfServiceDTO> typeOfServices = typeOfServiceRepo.findAll();
    	
		List<GenericTable> tableItems = new ArrayList<>();
		
		for (TypeOfServiceDTO list : typeOfServices) {
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
    public String newTypeOfService(Model model,@RequestParam(required=false) String mongoId) {
    	
		Optional<TypeOfServiceDTO> typeOfService;
		
		if (mongoId != null && mongoId.trim().length() > 0) {
			typeOfService = typeOfServiceRepo.findById(mongoId);
		} else {
			typeOfService = Optional.of(new TypeOfServiceDTO());
			TypeOfService doc = new TypeOfService();
			typeOfService.get().setTypeOfService(doc);
			
			typeOfService.get().getTypeOfService().setId(dozer.generateNetexName("typeOfService"));
			typeOfService.get().getTypeOfService().setVersion("latest");
		}
				
        model.addAttribute("item", typeOfService);

        return "resourceFrame/typeOfService.html :: edit";
    }
	
	@RequestMapping(value = "/typeOfService", method = RequestMethod.POST)
    public String saveTypeOfService(@ModelAttribute TypeOfServiceDTO typeOfService, BindingResult errors, Model model) {
		
		typeOfService.setMongoId(dozer.generateMongoId(typeOfService.getTypeOfService().getId(), typeOfService.getTypeOfService().getVersion(), "typeOfService"));
		typeOfService.setId(typeOfService.getTypeOfService().getId());
		typeOfService.setVersion(typeOfService.getTypeOfService().getVersion());
		
		typeOfServiceRepo.save(typeOfService);
		
		return "index";
    }

	@RequestMapping(value = "/typeOfService", method = RequestMethod.DELETE)
    public String deleteTypeOfService(Model model,@RequestParam(required=true) String mongoId) {
		
		if (typeOfServiceRepo.findById(mongoId).isPresent()) {
			typeOfServiceRepo.deleteById(mongoId);
		} else {
			System.out.println("Missing "+ mongoId + " id");
		}
		
		return "index";
    }
}
