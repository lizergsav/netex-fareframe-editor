package com.sg.netex.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.rutebanken.netex.model.ValidableElement;
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
import com.sg.netex.dto.ValidableElementDTO;
import com.sg.netex.model.ButtonEvents;
import com.sg.netex.model.GenericTable;
import com.sg.netex.repository.ValidableElementRepository;

@Controller
public class ValidableElementController {

	@Autowired
	ValidableElementRepository validableElementRepo;

	@Autowired
	private UtilConfig dozer;
	
	@RequestMapping(value = "/validableElements", method = RequestMethod.GET)
    public ResponseEntity<List<ValidableElementDTO>> validableElements(Model model) {
    	List<ValidableElementDTO> validableElements = validableElementRepo.findAll();
    	
        return new ResponseEntity<>(validableElements,HttpStatus.OK);
    }
	
	
	// the simple table view for thymeleaf
	@RequestMapping(value = "/validableElementGrid", method = RequestMethod.GET)
    public String validableElement(Model model) {
		
		List<ValidableElementDTO> validableElements = validableElementRepo.findAll();
    	
		List<GenericTable> tableItems = new ArrayList<>();
		
		for (ValidableElementDTO list : validableElements) {
			GenericTable item = new GenericTable();
			item.setFareFrameType(list.getFareFrameType());
			if (list.getName() != null)
				item.setName(list.getName());
			item.setMongoId(list.getMongoId());
			if (list.getValidableElement().getName() != null)
				item.setName(list.getValidableElement().getName().getValue());
			if (list.getOperator() != null)
				item.setObjectType(list.getOperator());

			item.setVersion(list.getValidableElement().getVersion());
			
			tableItems.add(item);
		}
		
		model.addAttribute("tableItems",tableItems);
		
        ButtonEvents action = new ButtonEvents();
        action.setGetUrl("/validableElements");
        action.setModifyItemUrl("/validableElement");
        action.setDeleteItemUrl("/validableElement");
        model.addAttribute("action", action);
        
        
        return "/html/simpleTable.html :: grid";
    }
	
	@RequestMapping(value = "/validableElement", method = RequestMethod.GET)
    public String newValidableElement(Model model,@RequestParam(required=false) String mongoId) {
    	
		Optional<ValidableElementDTO> validableElement;
		
		if (mongoId != null && mongoId.trim().length() > 0) {
			validableElement = validableElementRepo.findById(mongoId);
		} else {
			validableElement = Optional.of(new ValidableElementDTO());
			ValidableElement doc = new ValidableElement();
			validableElement.get().setValidableElement(doc);
			
			validableElement.get().getValidableElement().setId(dozer.generateNetexName("validableElement"));
			validableElement.get().getValidableElement().setVersion("latest");
		}
				
        model.addAttribute("item", validableElement);

        return "fares/validableElement.html :: edit";
    }
	
	@RequestMapping(value = "/validableElement", method = RequestMethod.POST)
    public String saveValidableElement(@ModelAttribute ValidableElementDTO validableElement, BindingResult errors, Model model) {
		
		validableElement.setMongoId(dozer.generateMongoId(validableElement.getValidableElement().getId(), validableElement.getValidableElement().getVersion(), "validableElement"));
		validableElement.setId(validableElement.getValidableElement().getId());
		validableElement.setVersion(validableElement.getValidableElement().getVersion());
		
		validableElementRepo.save(validableElement);
		
		return "index";
    }

	@RequestMapping(value = "/validableElement", method = RequestMethod.DELETE)
    public String deleteValidableElement(Model model,@RequestParam(required=true) String mongoId) {
		
		if (validableElementRepo.findById(mongoId).isPresent()) {
			validableElementRepo.deleteById(mongoId);
		} else {
			System.out.println("Missing "+ mongoId + " id");
		}
		
		return "index";
    }
}
