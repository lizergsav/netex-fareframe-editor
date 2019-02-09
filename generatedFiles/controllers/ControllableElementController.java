package com.sg.netex.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.rutebanken.netex.model.ControllableElement;
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
import com.sg.netex.dto.ControllableElementDTO;
import com.sg.netex.model.ButtonEvents;
import com.sg.netex.model.GenericTable;
import com.sg.netex.repository.ControllableElementRepository;

@Controller
public class ControllableElementController {

	@Autowired
	ControllableElementRepository controllableElementRepo;

	@Autowired
	private UtilConfig dozer;
	
	@RequestMapping(value = "/controllableElements", method = RequestMethod.GET)
    public ResponseEntity<List<ControllableElementDTO>> controllableElements(Model model) {
    	List<ControllableElementDTO> controllableElements = controllableElementRepo.findAll();
    	
        return new ResponseEntity<>(controllableElements,HttpStatus.OK);
    }
	
	
	// the simple table view for thymeleaf
	@RequestMapping(value = "/controllableElementGrid", method = RequestMethod.GET)
    public String controllableElement(Model model) {
		
		List<ControllableElementDTO> controllableElements = controllableElementRepo.findAll();
    	
		List<GenericTable> tableItems = new ArrayList<>();
		
		for (ControllableElementDTO list : controllableElements) {
			GenericTable item = new GenericTable();
			item.setFareFrameType(list.getFareFrameType());
			if (list.getName() != null)
				item.setName(list.getName());
			item.setMongoId(list.getMongoId());
			if (list.getControllableElement().getName() != null)
				item.setName(list.getControllableElement().getName().getValue());
			if (list.getOperator() != null)
				item.setObjectType(list.getOperator());

			item.setVersion(list.getControllableElement().getVersion());
			
			tableItems.add(item);
		}
		
		model.addAttribute("tableItems",tableItems);
		
        ButtonEvents action = new ButtonEvents();
        action.setGetUrl("/controllableElements");
        action.setModifyItemUrl("/controllableElement");
        action.setDeleteItemUrl("/controllableElement");
        model.addAttribute("action", action);
        
        
        return "/html/simpleTable.html :: grid";
    }
	
	@RequestMapping(value = "/controllableElement", method = RequestMethod.GET)
    public String newControllableElement(Model model,@RequestParam(required=false) String mongoId) {
    	
		Optional<ControllableElementDTO> controllableElement;
		
		if (mongoId != null && mongoId.trim().length() > 0) {
			controllableElement = controllableElementRepo.findById(mongoId);
		} else {
			controllableElement = Optional.of(new ControllableElementDTO());
			ControllableElement doc = new ControllableElement();
			controllableElement.get().setControllableElement(doc);
			
			controllableElement.get().getControllableElement().setId(dozer.generateNetexName("controllableElement"));
			controllableElement.get().getControllableElement().setVersion("latest");
		}
				
        model.addAttribute("item", controllableElement);

        return "fares/controllableElement.html :: edit";
    }
	
	@RequestMapping(value = "/controllableElement", method = RequestMethod.POST)
    public String saveControllableElement(@ModelAttribute ControllableElementDTO controllableElement, BindingResult errors, Model model) {
		
		controllableElement.setMongoId(dozer.generateMongoId(controllableElement.getControllableElement().getId(), controllableElement.getControllableElement().getVersion(), "controllableElement"));
		controllableElement.setId(controllableElement.getControllableElement().getId());
		controllableElement.setVersion(controllableElement.getControllableElement().getVersion());
		
		controllableElementRepo.save(controllableElement);
		
		return "index";
    }

	@RequestMapping(value = "/controllableElement", method = RequestMethod.DELETE)
    public String deleteControllableElement(Model model,@RequestParam(required=true) String mongoId) {
		
		if (controllableElementRepo.findById(mongoId).isPresent()) {
			controllableElementRepo.deleteById(mongoId);
		} else {
			System.out.println("Missing "+ mongoId + " id");
		}
		
		return "index";
    }
}
