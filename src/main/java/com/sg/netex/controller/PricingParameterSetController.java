package com.sg.netex.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.rutebanken.netex.model.DiscountingRule;
import org.rutebanken.netex.model.PricingParameterSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sg.netex.config.UtilConfig;
import com.sg.netex.dto.DiscountingRuleDTO;
import com.sg.netex.dto.PricingParameterSetDTO;
import com.sg.netex.model.ButtonEvents;
import com.sg.netex.model.GenericTable;
import com.sg.netex.model.PricingParameterSetResult;
import com.sg.netex.repository.DiscountingRuleRepository;
import com.sg.netex.repository.PricingParameterSetRepository;

@Controller
public class PricingParameterSetController {

	@Autowired
	PricingParameterSetRepository pricingParameterSetRepo;

	@Autowired
	private DiscountingRuleRepository discountingRuleRepo;
	
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
				
        model.addAttribute("item", pricingParameterSet.get());
 
        /*
        if (pricingParameterSet.get() != null && pricingParameterSet.get().getPricingParameterSet()!= null && pricingParameterSet.get().getPricingParameterSet().getPricingRules() != null && pricingParameterSet.get().getPricingParameterSet().getPricingRules().getPricingRule_() !=  null) {
        	for (Object listItem : pricingParameterSet.get().getPricingParameterSet().getPricingRules().getPricingRule_() ) {
            	pricingRules.add((DiscountingRule) listItem);
            }
            
            model.addAttribute("pricingRules", pricingRules);
            
        }
        */
        
        model.addAttribute("discountingRules", getAvailableDiscountingRules(pricingParameterSet.get().getPricingRules()));
        
        return "common/pricingParameterSet.html :: edit";
    }
	
	@RequestMapping(value = "/pricingParameterSet", method = RequestMethod.POST)
    public String savePricingParameterSet(@ModelAttribute PricingParameterSetDTO pricingParameterSet,@RequestBody(required = false) List<DiscountingRule> discountingRule, BindingResult errors, Model model) {
		
		if (!pricingParameterSet.getMongoId().startsWith("pricingParameterSet"))
			pricingParameterSet.setMongoId(dozer.generateMongoId(pricingParameterSet.getPricingParameterSet().getId(), pricingParameterSet.getPricingParameterSet().getVersion(), "pricingParameterSet"));
		pricingParameterSet.setId(pricingParameterSet.getPricingParameterSet().getId());
		pricingParameterSet.setVersion(pricingParameterSet.getPricingParameterSet().getVersion());
		
		pricingParameterSetRepo.save(pricingParameterSet);
		
		return "index";
    }
	
	@RequestMapping(value = "/pricingParameterSetDiscountingRules", method = RequestMethod.PUT)
    public PricingParameterSetResult savePricingParameterSetDiscountingRules(@RequestParam(required = true) String mongoId,@RequestBody(required = true) List<DiscountingRule> discountingRule, BindingResult errors, Model model) {
		
		Optional<PricingParameterSetDTO> pricingParameterSet;
		pricingParameterSet = pricingParameterSetRepo.findById(mongoId);

       	pricingParameterSet.get().setPricingRules(discountingRule);
    
        pricingParameterSetRepo.save(pricingParameterSet.get());
    	
        PricingParameterSetResult result = new PricingParameterSetResult();
        result.setGridView(pricingParameterSet.get().getPricingRules());
        result.setChosenView(getAvailableDiscountingRules(pricingParameterSet.get().getPricingRules()));
        result.setResult("OK");
        result.setStatus(HttpStatus.OK);
        
		return result;
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
	
	private List<DiscountingRule> getAvailableDiscountingRules(List<DiscountingRule> pricingRules){
		List<DiscountingRule> result = new ArrayList<>();
		for (DiscountingRuleDTO rules: discountingRuleRepo.findAll()) {
	    	if ( !pricingRules.contains(rules.getDiscountingRule()))
	    		result.add(rules.getDiscountingRule());
	    }
		
		return result;
	}
	
}
