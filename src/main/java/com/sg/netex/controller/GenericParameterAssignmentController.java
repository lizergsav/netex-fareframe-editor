package com.sg.netex.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.rutebanken.netex.model.GenericParameterAssignment;
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
import com.sg.netex.dto.GenericParameterAssignmentDTO;
import com.sg.netex.model.ButtonEvents;
import com.sg.netex.model.GenericTable;
import com.sg.netex.repository.GenericParameterAssignmentRepository;

@Controller
public class GenericParameterAssignmentController {

	@Autowired
	GenericParameterAssignmentRepository genericParameterAssignmentRepo;

	@Autowired
	private UtilConfig dozer;
	
	@RequestMapping(value = "/genericParameterAssignments", method = RequestMethod.GET)
    public ResponseEntity<List<GenericParameterAssignmentDTO>> genericParameterAssignments(Model model) {
    	List<GenericParameterAssignmentDTO> genericParameterAssignments = genericParameterAssignmentRepo.findAll();
    	
        return new ResponseEntity<>(genericParameterAssignments,HttpStatus.OK);
    }
	
	
	// the simple table view for thymeleaf
	@RequestMapping(value = "/genericParameterAssignmentGrid", method = RequestMethod.GET)
    public String genericParameterAssignment(Model model) {
		
		List<GenericParameterAssignmentDTO> genericParameterAssignments = genericParameterAssignmentRepo.findAll();
    	
		List<GenericTable> tableItems = new ArrayList<>();
		
		for (GenericParameterAssignmentDTO list : genericParameterAssignments) {
			GenericTable item = new GenericTable();
			item.setFareFrameType(list.getFareFrameType());
			if (list.getName() != null)
				item.setName(list.getName());
			item.setMongoId(list.getMongoId());
			if (list.getGenericParameterAssignment().getName() != null)
				item.setName(list.getGenericParameterAssignment().getName().getValue());
			if (list.getOperator() != null)
				item.setObjectType(list.getOperator());

			item.setVersion(list.getGenericParameterAssignment().getVersion());
			
			tableItems.add(item);
		}
		
		model.addAttribute("tableItems",tableItems);
		
        ButtonEvents action = new ButtonEvents();
        action.setGetUrl("/genericParameterAssignments");
        action.setModifyItemUrl("/genericParameterAssignment");
        action.setDeleteItemUrl("/genericParameterAssignment");
        model.addAttribute("action", action);
        
        
        return "/html/simpleTable.html :: grid";
    }
	
	@RequestMapping(value = "/genericParameterAssignment", method = RequestMethod.GET)
    public String newGenericParameterAssignment(Model model,@RequestParam(required=false) String mongoId) {
    	
		Optional<GenericParameterAssignmentDTO> genericParameterAssignment;
		
		if (mongoId != null && mongoId.trim().length() > 0) {
			genericParameterAssignment = genericParameterAssignmentRepo.findById(mongoId);
		} else {
			genericParameterAssignment = Optional.of(new GenericParameterAssignmentDTO());
			GenericParameterAssignment doc = new GenericParameterAssignment();
			genericParameterAssignment.get().setGenericParameterAssignment(doc);
			
			genericParameterAssignment.get().getGenericParameterAssignment().setId(dozer.generateNetexName("genericParameterAssignment"));
			genericParameterAssignment.get().getGenericParameterAssignment().setVersion("latest");
		}
				
        model.addAttribute("item", genericParameterAssignment);

        return "fares/genericParameterAssignment.html :: edit";
    }
	
	@RequestMapping(value = "/genericParameterAssignment", method = RequestMethod.POST)
    public String saveGenericParameterAssignment(@ModelAttribute GenericParameterAssignmentDTO genericParameterAssignment, BindingResult errors, Model model) {
		
		genericParameterAssignment.setMongoId(dozer.generateMongoId(genericParameterAssignment.getGenericParameterAssignment().getId(), genericParameterAssignment.getGenericParameterAssignment().getVersion(), "genericParameterAssignment"));
		genericParameterAssignment.setId(genericParameterAssignment.getGenericParameterAssignment().getId());
		genericParameterAssignment.setVersion(genericParameterAssignment.getGenericParameterAssignment().getVersion());
		
		genericParameterAssignmentRepo.save(genericParameterAssignment);
		
		return "index";
    }

	@RequestMapping(value = "/genericParameterAssignment", method = RequestMethod.DELETE)
    public String deleteGenericParameterAssignment(Model model,@RequestParam(required=true) String mongoId) {
		
		if (genericParameterAssignmentRepo.findById(mongoId).isPresent()) {
			genericParameterAssignmentRepo.deleteById(mongoId);
		} else {
			System.out.println("Missing "+ mongoId + " id");
		}
		
		return "index";
    }
}
