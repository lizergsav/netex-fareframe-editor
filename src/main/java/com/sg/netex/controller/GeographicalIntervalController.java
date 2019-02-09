package com.sg.netex.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.rutebanken.netex.model.GeographicalInterval;
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
import com.sg.netex.dto.GeographicalIntervalDTO;
import com.sg.netex.model.ButtonEvents;
import com.sg.netex.model.GenericTable;
import com.sg.netex.repository.GeographicalIntervalRepository;

@Controller
public class GeographicalIntervalController {

	@Autowired
	GeographicalIntervalRepository geographicalIntervalRepo;

	@Autowired
	private UtilConfig dozer;
	
	@RequestMapping(value = "/geographicalIntervals", method = RequestMethod.GET)
    public ResponseEntity<List<GeographicalIntervalDTO>> geographicalIntervals(Model model) {
    	List<GeographicalIntervalDTO> geographicalIntervals = geographicalIntervalRepo.findAll();
    	
        return new ResponseEntity<>(geographicalIntervals,HttpStatus.OK);
    }
	
	
	// the simple table view for thymeleaf
	@RequestMapping(value = "/geographicalIntervalGrid", method = RequestMethod.GET)
    public String geographicalInterval(Model model) {
		
		List<GeographicalIntervalDTO> geographicalIntervals = geographicalIntervalRepo.findAll();
    	
		List<GenericTable> tableItems = new ArrayList<>();
		
		for (GeographicalIntervalDTO list : geographicalIntervals) {
			GenericTable item = new GenericTable();
			item.setFareFrameType(list.getFareFrameType());
			if (list.getName() != null)
				item.setName(list.getName());
			item.setMongoId(list.getMongoId());
			if (list.getGeographicalInterval().getName() != null)
				item.setName(list.getGeographicalInterval().getName().getValue());
			if (list.getOperator() != null)
				item.setObjectType(list.getOperator());

			item.setVersion(list.getGeographicalInterval().getVersion());
			
			tableItems.add(item);
		}
		
		model.addAttribute("tableItems",tableItems);
		
        ButtonEvents action = new ButtonEvents();
        action.setGetUrl("/geographicalIntervals");
        action.setModifyItemUrl("/geographicalInterval");
        action.setDeleteItemUrl("/geographicalInterval");
        model.addAttribute("action", action);
        
        
        return "/html/simpleTable.html :: grid";
    }
	
	@RequestMapping(value = "/geographicalInterval", method = RequestMethod.GET)
    public String newGeographicalInterval(Model model,@RequestParam(required=false) String mongoId) {
    	
		Optional<GeographicalIntervalDTO> geographicalInterval;
		
		if (mongoId != null && mongoId.trim().length() > 0) {
			geographicalInterval = geographicalIntervalRepo.findById(mongoId);
		} else {
			geographicalInterval = Optional.of(new GeographicalIntervalDTO());
			GeographicalInterval doc = new GeographicalInterval();
			geographicalInterval.get().setGeographicalInterval(doc);
			
			geographicalInterval.get().getGeographicalInterval().setId(dozer.generateNetexName("geographicalInterval"));
			geographicalInterval.get().getGeographicalInterval().setVersion("latest");
		}
				
        model.addAttribute("item", geographicalInterval);

        return "common/geographicalInterval.html :: edit";
    }
	
	@RequestMapping(value = "/geographicalInterval", method = RequestMethod.POST)
    public String saveGeographicalInterval(@ModelAttribute GeographicalIntervalDTO geographicalInterval, BindingResult errors, Model model) {
		
		geographicalInterval.setMongoId(dozer.generateMongoId(geographicalInterval.getGeographicalInterval().getId(), geographicalInterval.getGeographicalInterval().getVersion(), "geographicalInterval"));
		geographicalInterval.setId(geographicalInterval.getGeographicalInterval().getId());
		geographicalInterval.setVersion(geographicalInterval.getGeographicalInterval().getVersion());
		
		geographicalIntervalRepo.save(geographicalInterval);
		
		return "index";
    }

	@RequestMapping(value = "/geographicalInterval", method = RequestMethod.DELETE)
    public String deleteGeographicalInterval(Model model,@RequestParam(required=true) String mongoId) {
		
		if (geographicalIntervalRepo.findById(mongoId).isPresent()) {
			geographicalIntervalRepo.deleteById(mongoId);
		} else {
			System.out.println("Missing "+ mongoId + " id");
		}
		
		return "index";
    }
}
