package com.sg.netex.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.rutebanken.netex.model.PointOfInterestClassification;
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
import com.sg.netex.dto.PointOfInterestClassificationDTO;
import com.sg.netex.model.ButtonEvents;
import com.sg.netex.model.GenericTable;
import com.sg.netex.repository.PointOfInterestClassificationRepository;

@Controller
public class PointOfInterestClassificationController {

	@Autowired
	PointOfInterestClassificationRepository pointOfInterestClassificationRepo;

	@Autowired
	private UtilConfig dozer;
	
	@RequestMapping(value = "/pointOfInterestClassifications", method = RequestMethod.GET)
    public ResponseEntity<List<PointOfInterestClassificationDTO>> pointOfInterestClassifications(Model model) {
    	List<PointOfInterestClassificationDTO> pointOfInterestClassifications = pointOfInterestClassificationRepo.findAll();
    	
        return new ResponseEntity<>(pointOfInterestClassifications,HttpStatus.OK);
    }
	
	
	// the simple table view for thymeleaf
	@RequestMapping(value = "/pointOfInterestClassificationGrid", method = RequestMethod.GET)
    public String pointOfInterestClassification(Model model) {
		
		List<PointOfInterestClassificationDTO> pointOfInterestClassifications = pointOfInterestClassificationRepo.findAll();
    	
		List<GenericTable> tableItems = new ArrayList<>();
		
		for (PointOfInterestClassificationDTO list : pointOfInterestClassifications) {
			GenericTable item = new GenericTable();
			if (list.getPointOfInterestClassification().getDescription() != null)
				item.setDescription(list.getPointOfInterestClassification().getDescription().getValue());
			item.setFareFrameType(list.getFareFrameType());
			if (list.getName() != null)
				item.setName(list.getName());
			item.setMongoId(list.getMongoId());
			if (list.getPointOfInterestClassification().getName() != null)
				item.setName(list.getPointOfInterestClassification().getName().getValue());
			if (list.getOperator() != null)
				item.setObjectType(list.getOperator());
			if (list.getPointOfInterestClassification().getPrivateCode()!= null)
				item.setPrivateCode(list.getPointOfInterestClassification().getPrivateCode().getValue());
			item.setVersion(list.getPointOfInterestClassification().getVersion());
			
			tableItems.add(item);
		}
		
		model.addAttribute("tableItems",tableItems);
		
        ButtonEvents action = new ButtonEvents();
        action.setGetUrl("/pointOfInterestClassifications");
        action.setModifyItemUrl("/pointOfInterestClassification");
        action.setDeleteItemUrl("/pointOfInterestClassification");
        model.addAttribute("action", action);
        
        
        return "/html/genericTable.html :: grid";
    }
	
	@RequestMapping(value = "/pointOfInterestClassification", method = RequestMethod.GET)
    public String newPointOfInterestClassification(Model model,@RequestParam(required=false) String mongoId) {
    	
		Optional<PointOfInterestClassificationDTO> pointOfInterestClassification;
		
		if (mongoId != null && mongoId.trim().length() > 0) {
			pointOfInterestClassification = pointOfInterestClassificationRepo.findById(mongoId);
		} else {
			pointOfInterestClassification = Optional.of(new PointOfInterestClassificationDTO());
			PointOfInterestClassification doc = new PointOfInterestClassification();
			pointOfInterestClassification.get().setPointOfInterestClassification(doc);
			
			pointOfInterestClassification.get().getPointOfInterestClassification().setId(dozer.generateNetexName("pointOfInterestClassification"));
			pointOfInterestClassification.get().getPointOfInterestClassification().setVersion("latest");
		}
				
        model.addAttribute("item", pointOfInterestClassification);

        return "resourceFrame/pointOfInterestClassification.html :: edit";
    }
	
	@RequestMapping(value = "/pointOfInterestClassification", method = RequestMethod.POST)
    public String savePointOfInterestClassification(@ModelAttribute PointOfInterestClassificationDTO pointOfInterestClassification, BindingResult errors, Model model) {
		
		pointOfInterestClassification.setMongoId(dozer.generateMongoId(pointOfInterestClassification.getPointOfInterestClassification().getId(), pointOfInterestClassification.getPointOfInterestClassification().getVersion(), "pointOfInterestClassification"));
		pointOfInterestClassification.setId(pointOfInterestClassification.getPointOfInterestClassification().getId());
		pointOfInterestClassification.setVersion(pointOfInterestClassification.getPointOfInterestClassification().getVersion());
		
		pointOfInterestClassificationRepo.save(pointOfInterestClassification);
		
		return "index";
    }

	@RequestMapping(value = "/pointOfInterestClassification", method = RequestMethod.DELETE)
    public String deletePointOfInterestClassification(Model model,@RequestParam(required=true) String mongoId) {
		
		if (pointOfInterestClassificationRepo.findById(mongoId).isPresent()) {
			pointOfInterestClassificationRepo.deleteById(mongoId);
		} else {
			System.out.println("Missing "+ mongoId + " id");
		}
		
		return "index";
    }
}
