package com.sg.netex.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.rutebanken.netex.model.TypeOfPoint;
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
import com.sg.netex.dto.TypeOfPointDTO;
import com.sg.netex.model.ButtonEvents;
import com.sg.netex.model.GenericTable;
import com.sg.netex.repository.TypeOfPointRepository;

@Controller
public class TypeOfPointController {

	@Autowired
	TypeOfPointRepository typeOfPointRepo;

	@Autowired
	private UtilConfig dozer;
	
	@RequestMapping(value = "/typeOfPoints", method = RequestMethod.GET)
    public ResponseEntity<List<TypeOfPointDTO>> typeOfPoints(Model model) {
    	List<TypeOfPointDTO> typeOfPoints = typeOfPointRepo.findAll();
    	
        return new ResponseEntity<>(typeOfPoints,HttpStatus.OK);
    }
	
	
	// the simple table view for thymeleaf
	@RequestMapping(value = "/typeOfPointGrid", method = RequestMethod.GET)
    public String typeOfPoint(Model model) {
		
		List<TypeOfPointDTO> typeOfPoints = typeOfPointRepo.findAll();
    	
		List<GenericTable> tableItems = new ArrayList<>();
		
		for (TypeOfPointDTO list : typeOfPoints) {
			GenericTable item = new GenericTable();
			if (list.getTypeOfPoint().getDescription() != null)
				item.setDescription(list.getTypeOfPoint().getDescription().getValue());
			item.setFareFrameType(list.getFareFrameType());
			if (list.getName() != null)
				item.setName(list.getName());
			item.setMongoId(list.getMongoId());
			if (list.getTypeOfPoint().getName() != null)
				item.setName(list.getTypeOfPoint().getName().getValue());
			if (list.getOperator() != null)
				item.setObjectType(list.getOperator());
			if (list.getTypeOfPoint().getPrivateCode()!= null)
				item.setPrivateCode(list.getTypeOfPoint().getPrivateCode().getValue());
			item.setVersion(list.getTypeOfPoint().getVersion());
			
			tableItems.add(item);
		}
		
		model.addAttribute("tableItems",tableItems);
		
        ButtonEvents action = new ButtonEvents();
        action.setGetUrl("/typeOfPoints");
        action.setModifyItemUrl("/typeOfPoint");
        action.setDeleteItemUrl("/typeOfPoint");
        model.addAttribute("action", action);
        
        
        return "/html/genericTable.html :: grid";
    }
	
	@RequestMapping(value = "/typeOfPoint", method = RequestMethod.GET)
    public String newTypeOfPoint(Model model,@RequestParam(required=false) String mongoId) {
    	
		Optional<TypeOfPointDTO> typeOfPoint;
		
		if (mongoId != null && mongoId.trim().length() > 0) {
			typeOfPoint = typeOfPointRepo.findById(mongoId);
		} else {
			typeOfPoint = Optional.of(new TypeOfPointDTO());
			TypeOfPoint doc = new TypeOfPoint();
			typeOfPoint.get().setTypeOfPoint(doc);
			
			typeOfPoint.get().getTypeOfPoint().setId(dozer.generateNetexName("typeOfPoint"));
			typeOfPoint.get().getTypeOfPoint().setVersion("latest");
		}
				
        model.addAttribute("item", typeOfPoint);

        return "resourceFrame/typeOfPoint.html :: edit";
    }
	
	@RequestMapping(value = "/typeOfPoint", method = RequestMethod.POST)
    public String saveTypeOfPoint(@ModelAttribute TypeOfPointDTO typeOfPoint, BindingResult errors, Model model) {
		
		typeOfPoint.setMongoId(dozer.generateMongoId(typeOfPoint.getTypeOfPoint().getId(), typeOfPoint.getTypeOfPoint().getVersion(), "typeOfPoint"));
		typeOfPoint.setId(typeOfPoint.getTypeOfPoint().getId());
		typeOfPoint.setVersion(typeOfPoint.getTypeOfPoint().getVersion());
		
		typeOfPointRepo.save(typeOfPoint);
		
		return "index";
    }

	@RequestMapping(value = "/typeOfPoint", method = RequestMethod.DELETE)
    public String deleteTypeOfPoint(Model model,@RequestParam(required=true) String mongoId) {
		
		if (typeOfPointRepo.findById(mongoId).isPresent()) {
			typeOfPointRepo.deleteById(mongoId);
		} else {
			System.out.println("Missing "+ mongoId + " id");
		}
		
		return "index";
    }
}
