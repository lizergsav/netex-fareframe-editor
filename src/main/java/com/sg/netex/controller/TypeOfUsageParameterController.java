package com.sg.netex.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.rutebanken.netex.model.TypeOfUsageParameter;
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
import com.sg.netex.dto.TypeOfUsageParameterDTO;
import com.sg.netex.model.ButtonEvents;
import com.sg.netex.model.GenericTable;
import com.sg.netex.repository.TypeOfUsageParameterRepository;

@Controller
public class TypeOfUsageParameterController {

	@Autowired
	TypeOfUsageParameterRepository typeOfUsageParameterRepo;

	@Autowired
	private UtilConfig dozer;
	
	@RequestMapping(value = "/typeOfUsageParameters", method = RequestMethod.GET)
    public ResponseEntity<List<TypeOfUsageParameterDTO>> typeOfUsageParameters(Model model) {
    	List<TypeOfUsageParameterDTO> typeOfUsageParameters = typeOfUsageParameterRepo.findAll();
    	
        return new ResponseEntity<>(typeOfUsageParameters,HttpStatus.OK);
    }
	
	
	// the simple table view for thymeleaf
	@RequestMapping(value = "/typeOfUsageParameterGrid", method = RequestMethod.GET)
    public String typeOfUsageParameter(Model model) {
		
		List<TypeOfUsageParameterDTO> typeOfUsageParameters = typeOfUsageParameterRepo.findAll();
    	
		List<GenericTable> tableItems = new ArrayList<>();
		
		for (TypeOfUsageParameterDTO list : typeOfUsageParameters) {
			GenericTable item = new GenericTable();
			if (list.getTypeOfUsageParameter().getDescription() != null)
				item.setDescription(list.getTypeOfUsageParameter().getDescription().getValue());
			item.setFareFrameType(list.getFareFrameType());
			if (list.getName() != null)
				item.setName(list.getName());
			item.setMongoId(list.getMongoId());
			if (list.getTypeOfUsageParameter().getName() != null)
				item.setName(list.getTypeOfUsageParameter().getName().getValue());
			if (list.getOperator() != null)
				item.setObjectType(list.getOperator());
			if (list.getTypeOfUsageParameter().getPrivateCode()!= null)
				item.setPrivateCode(list.getTypeOfUsageParameter().getPrivateCode().getValue());
			item.setVersion(list.getTypeOfUsageParameter().getVersion());
			
			tableItems.add(item);
		}
		
		model.addAttribute("tableItems",tableItems);
		
        ButtonEvents action = new ButtonEvents();
        action.setGetUrl("/typeOfUsageParameters");
        action.setModifyItemUrl("/typeOfUsageParameter");
        action.setDeleteItemUrl("/typeOfUsageParameter");
        model.addAttribute("action", action);
        
        
        return "/html/genericTable.html :: grid";
    }
	
	@RequestMapping(value = "/typeOfUsageParameter", method = RequestMethod.GET)
    public String newTypeOfUsageParameter(Model model,@RequestParam(required=false) String mongoId) {
    	
		Optional<TypeOfUsageParameterDTO> typeOfUsageParameter;
		
		if (mongoId != null && mongoId.trim().length() > 0) {
			typeOfUsageParameter = typeOfUsageParameterRepo.findById(mongoId);
		} else {
			typeOfUsageParameter = Optional.of(new TypeOfUsageParameterDTO());
			TypeOfUsageParameter doc = new TypeOfUsageParameter();
			typeOfUsageParameter.get().setTypeOfUsageParameter(doc);
			
			typeOfUsageParameter.get().getTypeOfUsageParameter().setId(dozer.generateNetexName("typeOfUsageParameter"));
			typeOfUsageParameter.get().getTypeOfUsageParameter().setVersion("latest");
		}
				
        model.addAttribute("item", typeOfUsageParameter);

        return "resourceFrame/typeOfUsageParameter.html :: edit";
    }
	
	@RequestMapping(value = "/typeOfUsageParameter", method = RequestMethod.POST)
    public String saveTypeOfUsageParameter(@ModelAttribute TypeOfUsageParameterDTO typeOfUsageParameter, BindingResult errors, Model model) {
		
		typeOfUsageParameter.setMongoId(dozer.generateMongoId(typeOfUsageParameter.getTypeOfUsageParameter().getId(), typeOfUsageParameter.getTypeOfUsageParameter().getVersion(), "typeOfUsageParameter"));
		typeOfUsageParameter.setId(typeOfUsageParameter.getTypeOfUsageParameter().getId());
		typeOfUsageParameter.setVersion(typeOfUsageParameter.getTypeOfUsageParameter().getVersion());
		
		typeOfUsageParameterRepo.save(typeOfUsageParameter);
		
		return "index";
    }

	@RequestMapping(value = "/typeOfUsageParameter", method = RequestMethod.DELETE)
    public String deleteTypeOfUsageParameter(Model model,@RequestParam(required=true) String mongoId) {
		
		if (typeOfUsageParameterRepo.findById(mongoId).isPresent()) {
			typeOfUsageParameterRepo.deleteById(mongoId);
		} else {
			System.out.println("Missing "+ mongoId + " id");
		}
		
		return "index";
    }
}
