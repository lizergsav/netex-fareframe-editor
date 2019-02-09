package com.sg.netex.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.rutebanken.netex.model.GeographicalIntervalPrice;
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
import com.sg.netex.dto.GeographicalIntervalPriceDTO;
import com.sg.netex.model.ButtonEvents;
import com.sg.netex.model.GenericTable;
import com.sg.netex.repository.GeographicalIntervalPriceRepository;

@Controller
public class GeographicalIntervalPriceController {

	@Autowired
	GeographicalIntervalPriceRepository geographicalIntervalPriceRepo;

	@Autowired
	private UtilConfig dozer;
	
	@RequestMapping(value = "/geographicalIntervalPrices", method = RequestMethod.GET)
    public ResponseEntity<List<GeographicalIntervalPriceDTO>> geographicalIntervalPrices(Model model) {
    	List<GeographicalIntervalPriceDTO> geographicalIntervalPrices = geographicalIntervalPriceRepo.findAll();
    	
        return new ResponseEntity<>(geographicalIntervalPrices,HttpStatus.OK);
    }
	
	
	// the simple table view for thymeleaf
	@RequestMapping(value = "/geographicalIntervalPriceGrid", method = RequestMethod.GET)
    public String geographicalIntervalPrice(Model model) {
		
		List<GeographicalIntervalPriceDTO> geographicalIntervalPrices = geographicalIntervalPriceRepo.findAll();
    	
		List<GenericTable> tableItems = new ArrayList<>();
		
		for (GeographicalIntervalPriceDTO list : geographicalIntervalPrices) {
			GenericTable item = new GenericTable();
			item.setFareFrameType(list.getFareFrameType());
			if (list.getName() != null)
				item.setName(list.getName());
			item.setMongoId(list.getMongoId());
			if (list.getGeographicalIntervalPrice().getName() != null)
				item.setName(list.getGeographicalIntervalPrice().getName().getValue());
			if (list.getOperator() != null)
				item.setObjectType(list.getOperator());

			item.setVersion(list.getGeographicalIntervalPrice().getVersion());
			
			tableItems.add(item);
		}
		
		model.addAttribute("tableItems",tableItems);
		
        ButtonEvents action = new ButtonEvents();
        action.setGetUrl("/geographicalIntervalPrices");
        action.setModifyItemUrl("/geographicalIntervalPrice");
        action.setDeleteItemUrl("/geographicalIntervalPrice");
        model.addAttribute("action", action);
        
        
        return "/html/simpleTable.html :: grid";
    }
	
	@RequestMapping(value = "/geographicalIntervalPrice", method = RequestMethod.GET)
    public String newGeographicalIntervalPrice(Model model,@RequestParam(required=false) String mongoId) {
    	
		Optional<GeographicalIntervalPriceDTO> geographicalIntervalPrice;
		
		if (mongoId != null && mongoId.trim().length() > 0) {
			geographicalIntervalPrice = geographicalIntervalPriceRepo.findById(mongoId);
		} else {
			geographicalIntervalPrice = Optional.of(new GeographicalIntervalPriceDTO());
			GeographicalIntervalPrice doc = new GeographicalIntervalPrice();
			geographicalIntervalPrice.get().setGeographicalIntervalPrice(doc);
			
			geographicalIntervalPrice.get().getGeographicalIntervalPrice().setId(dozer.generateNetexName("geographicalIntervalPrice"));
			geographicalIntervalPrice.get().getGeographicalIntervalPrice().setVersion("latest");
		}
				
        model.addAttribute("item", geographicalIntervalPrice);

        return "common/geographicalIntervalPrice.html :: edit";
    }
	
	@RequestMapping(value = "/geographicalIntervalPrice", method = RequestMethod.POST)
    public String saveGeographicalIntervalPrice(@ModelAttribute GeographicalIntervalPriceDTO geographicalIntervalPrice, BindingResult errors, Model model) {
		
		geographicalIntervalPrice.setMongoId(dozer.generateMongoId(geographicalIntervalPrice.getGeographicalIntervalPrice().getId(), geographicalIntervalPrice.getGeographicalIntervalPrice().getVersion(), "geographicalIntervalPrice"));
		geographicalIntervalPrice.setId(geographicalIntervalPrice.getGeographicalIntervalPrice().getId());
		geographicalIntervalPrice.setVersion(geographicalIntervalPrice.getGeographicalIntervalPrice().getVersion());
		
		geographicalIntervalPriceRepo.save(geographicalIntervalPrice);
		
		return "index";
    }

	@RequestMapping(value = "/geographicalIntervalPrice", method = RequestMethod.DELETE)
    public String deleteGeographicalIntervalPrice(Model model,@RequestParam(required=true) String mongoId) {
		
		if (geographicalIntervalPriceRepo.findById(mongoId).isPresent()) {
			geographicalIntervalPriceRepo.deleteById(mongoId);
		} else {
			System.out.println("Missing "+ mongoId + " id");
		}
		
		return "index";
    }
}
