package com.sg.netex.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.rutebanken.netex.model.GeographicalUnit;
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
import com.sg.netex.dto.GeographicalUnitDTO;
import com.sg.netex.model.ButtonEvents;
import com.sg.netex.model.GenericTable;
import com.sg.netex.repository.GeographicalUnitRepository;

@Controller
public class GeographicalUnitController {

	@Autowired
	GeographicalUnitRepository geographicalUnitRepo;

	@Autowired
	private UtilConfig dozer;
	
	@RequestMapping(value = "/geographicalUnits", method = RequestMethod.GET)
    public ResponseEntity<List<GeographicalUnitDTO>> geographicalUnits(Model model) {
    	List<GeographicalUnitDTO> geographicalUnits = geographicalUnitRepo.findAll();
    	
        return new ResponseEntity<>(geographicalUnits,HttpStatus.OK);
    }
	
	
	// the simple table view for thymeleaf
	@RequestMapping(value = "/geographicalUnitGrid", method = RequestMethod.GET)
    public String geographicalUnit(Model model) {
		
		List<GeographicalUnitDTO> geographicalUnits = geographicalUnitRepo.findAll();
    	
		List<GenericTable> tableItems = new ArrayList<>();
		
		for (GeographicalUnitDTO list : geographicalUnits) {
			GenericTable item = new GenericTable();
			item.setFareFrameType(list.getFareFrameType());
			if (list.getName() != null)
				item.setName(list.getName());
			item.setMongoId(list.getMongoId());
			if (list.getGeographicalUnit().getName() != null)
				item.setName(list.getGeographicalUnit().getName().getValue());
			if (list.getOperator() != null)
				item.setObjectType(list.getOperator());

			item.setVersion(list.getGeographicalUnit().getVersion());
			
			tableItems.add(item);
		}
		
		model.addAttribute("tableItems",tableItems);
		
        ButtonEvents action = new ButtonEvents();
        action.setGetUrl("/geographicalUnits");
        action.setModifyItemUrl("/geographicalUnit");
        action.setDeleteItemUrl("/geographicalUnit");
        model.addAttribute("action", action);
        
        
        return "/html/simpleTable.html :: grid";
    }
	
	@RequestMapping(value = "/geographicalUnit", method = RequestMethod.GET)
    public String newGeographicalUnit(Model model,@RequestParam(required=false) String mongoId) {
    	
		Optional<GeographicalUnitDTO> geographicalUnit;
		
		if (mongoId != null && mongoId.trim().length() > 0) {
			geographicalUnit = geographicalUnitRepo.findById(mongoId);
		} else {
			geographicalUnit = Optional.of(new GeographicalUnitDTO());
			GeographicalUnit doc = new GeographicalUnit();
			geographicalUnit.get().setGeographicalUnit(doc);
			
			geographicalUnit.get().getGeographicalUnit().setId(dozer.generateNetexName("geographicalUnit"));
			geographicalUnit.get().getGeographicalUnit().setVersion("latest");
		}
				
        model.addAttribute("item", geographicalUnit);

        return "common/geographicalUnit.html :: edit";
    }
	
	@RequestMapping(value = "/geographicalUnit", method = RequestMethod.POST)
    public String saveGeographicalUnit(@ModelAttribute GeographicalUnitDTO geographicalUnit, BindingResult errors, Model model) {
		
		geographicalUnit.setMongoId(dozer.generateMongoId(geographicalUnit.getGeographicalUnit().getId(), geographicalUnit.getGeographicalUnit().getVersion(), "geographicalUnit"));
		geographicalUnit.setId(geographicalUnit.getGeographicalUnit().getId());
		geographicalUnit.setVersion(geographicalUnit.getGeographicalUnit().getVersion());
		
		geographicalUnitRepo.save(geographicalUnit);
		
		return "index";
    }

	@RequestMapping(value = "/geographicalUnit", method = RequestMethod.DELETE)
    public String deleteGeographicalUnit(Model model,@RequestParam(required=true) String mongoId) {
		
		if (geographicalUnitRepo.findById(mongoId).isPresent()) {
			geographicalUnitRepo.deleteById(mongoId);
		} else {
			System.out.println("Missing "+ mongoId + " id");
		}
		
		return "index";
    }
}
