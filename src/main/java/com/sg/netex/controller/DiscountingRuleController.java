package com.sg.netex.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.rutebanken.netex.model.DiscountingRule;
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
import com.sg.netex.dto.DiscountingRuleDTO;
import com.sg.netex.model.ButtonEvents;
import com.sg.netex.model.GenericTable;
import com.sg.netex.repository.DiscountingRuleRepository;

@Controller
public class DiscountingRuleController {

	@Autowired
	DiscountingRuleRepository discountingRuleRepo;

	@Autowired
	private UtilConfig dozer;
	
	@RequestMapping(value = "/discountingRules", method = RequestMethod.GET)
    public ResponseEntity<List<DiscountingRuleDTO>> discountingRules(Model model) {
    	List<DiscountingRuleDTO> discountingRules = discountingRuleRepo.findAll();
    	
        return new ResponseEntity<>(discountingRules,HttpStatus.OK);
    }
	
	
	// the simple table view for thymeleaf
	@RequestMapping(value = "/discountingRuleGrid", method = RequestMethod.GET)
    public String discountingRule(Model model) {
		
		List<DiscountingRuleDTO> discountingRules = discountingRuleRepo.findAll();
    	
		List<GenericTable> tableItems = new ArrayList<>();
		
		for (DiscountingRuleDTO list : discountingRules) {
			GenericTable item = new GenericTable();
			item.setFareFrameType(list.getFareFrameType());
			if (list.getName() != null)
				item.setName(list.getName());
			item.setMongoId(list.getMongoId());
			if (list.getDiscountingRule().getName() != null)
				item.setName(list.getDiscountingRule().getName().getValue());
			if (list.getOperator() != null)
				item.setObjectType(list.getOperator());

			item.setVersion(list.getDiscountingRule().getVersion());
			
			tableItems.add(item);
		}
		
		model.addAttribute("tableItems",tableItems);
		
        ButtonEvents action = new ButtonEvents();
        action.setGetUrl("/discountingRules");
        action.setModifyItemUrl("/discountingRule");
        action.setDeleteItemUrl("/discountingRule");
        model.addAttribute("action", action);
        
        
        return "/html/simpleTable.html :: grid";
    }
	
	@RequestMapping(value = "/discountingRule", method = RequestMethod.GET)
    public String newDiscountingRule(Model model,@RequestParam(required=false) String mongoId) {
    	
		Optional<DiscountingRuleDTO> discountingRule;
		
		if (mongoId != null && mongoId.trim().length() > 0) {
			discountingRule = discountingRuleRepo.findById(mongoId);
		} else {
			discountingRule = Optional.of(new DiscountingRuleDTO());
			DiscountingRule doc = new DiscountingRule();
			discountingRule.get().setDiscountingRule(doc);
			
			discountingRule.get().getDiscountingRule().setId(dozer.generateNetexName("discountingRule"));
			discountingRule.get().getDiscountingRule().setVersion("latest");
		}
				
        model.addAttribute("item", discountingRule);

        return "common/discountingRule.html :: edit";
    }
	
	@RequestMapping(value = "/discountingRule", method = RequestMethod.POST)
    public String saveDiscountingRule(@ModelAttribute DiscountingRuleDTO discountingRule, BindingResult errors, Model model) {
		
		discountingRule.setMongoId(dozer.generateMongoId(discountingRule.getDiscountingRule().getId(), discountingRule.getDiscountingRule().getVersion(), "discountingRule"));
		discountingRule.setId(discountingRule.getDiscountingRule().getId());
		discountingRule.setVersion(discountingRule.getDiscountingRule().getVersion());
		
		discountingRuleRepo.save(discountingRule);
		
		return "index";
    }

	@RequestMapping(value = "/discountingRule", method = RequestMethod.DELETE)
    public String deleteDiscountingRule(Model model,@RequestParam(required=true) String mongoId) {
		
		if (discountingRuleRepo.findById(mongoId).isPresent()) {
			discountingRuleRepo.deleteById(mongoId);
		} else {
			System.out.println("Missing "+ mongoId + " id");
		}
		
		return "index";
    }
}
