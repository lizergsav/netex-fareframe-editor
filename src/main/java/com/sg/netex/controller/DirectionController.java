package com.sg.netex.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.rutebanken.netex.model.Direction;
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
import com.sg.netex.dto.DirectionDTO;
import com.sg.netex.model.ButtonEvents;
import com.sg.netex.model.GenericTable;
import com.sg.netex.repository.DirectionRepository;

@Controller
public class DirectionController {

	@Autowired
	DirectionRepository directionRepo;

	@Autowired
	private UtilConfig dozer;
	
	@RequestMapping(value = "/directions", method = RequestMethod.GET)
    public ResponseEntity<List<DirectionDTO>> directions(Model model) {
    	List<DirectionDTO> directions = directionRepo.findAll();
    	
        return new ResponseEntity<>(directions,HttpStatus.OK);
    }
	
	
	// the simple table view for thymeleaf
	@RequestMapping(value = "/directionGrid", method = RequestMethod.GET)
    public String direction(Model model) {
		
		List<DirectionDTO> directions = directionRepo.findAll();
    	
		List<GenericTable> tableItems = new ArrayList<>();
		
		for (DirectionDTO list : directions) {
			GenericTable item = new GenericTable();
			if (list.getDirection().getDescription() != null)
				item.setDescription(list.getDirection().getDescription().getValue());
			item.setFareFrameType(list.getFareFrameType());
			if (list.getName() != null)
				item.setName(list.getName());
			item.setMongoId(list.getMongoId());
			if (list.getDirection().getName() != null)
				item.setName(list.getDirection().getName().getValue());
			if (list.getOperator() != null)
				item.setObjectType(list.getOperator());
			if (list.getDirection().getPrivateCode()!= null)
				item.setPrivateCode(list.getDirection().getPrivateCode().getValue());
			item.setVersion(list.getDirection().getVersion());
			
			tableItems.add(item);
		}
		
		model.addAttribute("tableItems",tableItems);
		
        ButtonEvents action = new ButtonEvents();
        action.setGetUrl("/directions");
        action.setModifyItemUrl("/direction");
        action.setDeleteItemUrl("/direction");
        model.addAttribute("action", action);
        
        
        return "/html/genericTable.html :: grid";
    }
	
	@RequestMapping(value = "/direction", method = RequestMethod.GET)
    public String newDirection(Model model,@RequestParam(required=false) String mongoId) {
    	
		Optional<DirectionDTO> direction;
		
		if (mongoId != null && mongoId.trim().length() > 0) {
			direction = directionRepo.findById(mongoId);
		} else {
			direction = Optional.of(new DirectionDTO());
			Direction doc = new Direction();
			direction.get().setDirection(doc);
			
			direction.get().getDirection().setId(dozer.generateNetexName("direction"));
			direction.get().getDirection().setVersion("latest");
		}
				
        model.addAttribute("item", direction);

        return "resourceFrame/direction.html :: edit";
    }
	
	@RequestMapping(value = "/direction", method = RequestMethod.POST)
    public String saveDirection(@ModelAttribute DirectionDTO direction, BindingResult errors, Model model) {
		
		direction.setMongoId(dozer.generateMongoId(direction.getDirection().getId(), direction.getDirection().getVersion(), "direction"));
		direction.setId(direction.getDirection().getId());
		direction.setVersion(direction.getDirection().getVersion());
		
		directionRepo.save(direction);
		
		return "index";
    }

	@RequestMapping(value = "/direction", method = RequestMethod.DELETE)
    public String deleteDirection(Model model,@RequestParam(required=true) String mongoId) {
		
		if (directionRepo.findById(mongoId).isPresent()) {
			directionRepo.deleteById(mongoId);
		} else {
			System.out.println("Missing "+ mongoId + " id");
		}
		
		return "index";
    }
}
