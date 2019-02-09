package com.sg.netex.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.rutebanken.netex.model.PricingParameterSet;
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
import com.sg.netex.dto.PricingParameterSetDTO;
import com.sg.netex.model.ButtonEvents;
import com.sg.netex.model.GenericTable;
import com.sg.netex.repository.PricingParameterSetRepository;

@Controller
public class PricingParameterSetController {

	@Autowired
	PricingParameterSetRepository pricingParameterSetRepo;

	@Autowired
	private UtilConfig dozer;
	
	@RequestMapping(value = "/pricingParameterSets", method = RequestMethod.GET)
    public ResponseEntity<List<PricingParameterSetDTO>> pricingParameterSets(Model model) {
    	List<PricingParameterSetDTO> pricingParameterSets = pricingParameterSetRepo.findAll();
    	
        return new ResponseEntity<>(pricingParameterSets,HttpStatus.OK);
    }
	
	
	// the simple table view for thymeleaf
	@RequestMapping(value = "/pricingParameterSetGrid", method = RequestMethod.GET)
    public String pricingParameterSet(Model model) {
		
		List<PricingParameterSetDTO> pricingParameterSets = pricingParameterSetRepo.findAll();
    	
		List<GenericTable> tableItems = new ArrayList<>();
		
		for (PricingParameterSetDTO list : pricingParameterSets) {
			GenericTable item = new GenericTable();
			item.setFareFrameType(list.getFareFrameType());
			if (list.getName() != null)
				item.setName(list.getName());
			item.setMongoId(list.getMongoId());
			if (list.getPricingParameterSet().getName() != null)
				item.setName(list.getPricingParameterSet().getName().getValue());
			if (list.getOperator() != null)
				item.setObjectType(list.getOperator());

			item.setVersion(list.getPricingParameterSet().getVersion());
			
			tableItems.add(item);
		}
		
		model.addAttribute("tableItems",tableItems);
		
        ButtonEvents action = new ButtonEvents();
        action.setGetUrl("/pricingParameterSets");
        action.setModifyItemUrl("/pricingParameterSet");
        action.setDeleteItemUrl("/pricingParameterSet");
        model.addAttribute("action", action);
        
        
        return "/html/simpleTable.html :: grid";
    }
	
	@RequestMapping(value = "/pricingParameterSet", method = RequestMethod.GET)
    public String newPricingParameterSet(Model model,@RequestParam(required=false) String mongoId) {
    	
		Optional<PricingParameterSetDTO> pricingParameterSet;
		
		if (mongoId != null && mongoId.trim().length() > 0) {
			pricingParameterSet = pricingParameterSetRepo.findById(mongoId);
		} else {
			pricingParameterSet = Optional.of(new PricingParameterSetDTO());
			PricingParameterSet doc = new PricingParameterSet();
			pricingParameterSet.get().setPricingParameterSet(doc);
			
			pricingParameterSet.get().getPricingParameterSet().setId(dozer.generateNetexName("pricingParameterSet"));
			pricingParameterSet.get().getPricingParameterSet().setVersion("latest");
		}
				
        model.addAttribute("item", pricingParameterSet);

        return "common/pricingParameterSet.html :: edit";
    }
	
	@RequestMapping(value = "/pricingParameterSet", method = RequestMethod.POST)
    public String savePricingParameterSet(@ModelAttribute PricingParameterSetDTO pricingParameterSet, BindingResult errors, Model model) {
		
		pricingParameterSet.setMongoId(dozer.generateMongoId(pricingParameterSet.getPricingParameterSet().getId(), pricingParameterSet.getPricingParameterSet().getVersion(), "pricingParameterSet"));
		pricingParameterSet.setId(pricingParameterSet.getPricingParameterSet().getId());
		pricingParameterSet.setVersion(pricingParameterSet.getPricingParameterSet().getVersion());
		
		pricingParameterSetRepo.save(pricingParameterSet);
		
		return "index";
    }

	@RequestMapping(value = "/pricingParameterSet", method = RequestMethod.DELETE)
    public String deletePricingParameterSet(Model model,@RequestParam(required=true) String mongoId) {
		
		if (pricingParameterSetRepo.findById(mongoId).isPresent()) {
			pricingParameterSetRepo.deleteById(mongoId);
		} else {
			System.out.println("Missing "+ mongoId + " id");
		}
		
		return "index";
    }
}
