package com.sg.netex.controller.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.rutebanken.netex.model.DiscountingRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sg.netex.controller.PricingParameterSetController;
import com.sg.netex.dto.PricingParameterSetDTO;
import com.sg.netex.model.PricingParameterSetResult;
import com.sg.netex.repository.PricingParameterSetRepository;

@RestController
public class RestControllers {

	@Autowired
	PricingParameterSetRepository pricingParameterSetRepo;
	
	@Autowired
	PricingParameterSetController pricing;
	
	@RequestMapping(value = "/pricingParameterSetDiscountingRules", method = RequestMethod.POST)
    public ResponseEntity<PricingParameterSetResult> savePricingParameterSetDiscountingRules(@RequestParam(required = true) String mongoId,@RequestBody(required = true) List<DiscountingRule> discountingRule, BindingResult errors, Model model) {
		PricingParameterSetResult result = new PricingParameterSetResult();
		
		Optional<PricingParameterSetDTO> pricingParameterSet;
		pricingParameterSet = pricingParameterSetRepo.findById(mongoId);

		if (pricingParameterSet.get().getPricingRules() != null && discountingRule != null )
			pricingParameterSet.get().getPricingRules().addAll(discountingRule);
    
        pricingParameterSetRepo.save(pricingParameterSet.get());
    	result.setStoredDiscounts(pricingParameterSet.get().getPricingRules());
    	result.setAvailableDiscounts(pricing.getAvailableDiscountingRules(result.getStoredDiscounts()));
    	result.setStatus(HttpStatus.OK);
    	
		return new ResponseEntity<>(result,HttpStatus.OK);
    }
	
	@RequestMapping(value = "/pricingParameterSetDiscountingRules", method = RequestMethod.DELETE)
    public ResponseEntity<PricingParameterSetResult> deletePricingParameterSetDiscountingRules(@RequestParam(required = true) String mongoId,@RequestBody(required = true) List<DiscountingRule> discountingRule, BindingResult errors, Model model) {
		PricingParameterSetResult result = new PricingParameterSetResult();
		
		Optional<PricingParameterSetDTO> pricingParameterSet;
		pricingParameterSet = pricingParameterSetRepo.findById(mongoId);
		int i = 0;
		if (pricingParameterSet.get().getPricingRules() != null && discountingRule != null ) {
			for (DiscountingRule disc : discountingRule) {
				i = 0;
				for (DiscountingRule dp : pricingParameterSet.get().getPricingRules()) {
					if (dp.getId().trim().equals(disc.getId().trim())) {
						pricingParameterSet.get().getPricingRules().remove(i);
						break;
					}
					i++;
				}
			}
		}
		result.setStoredDiscounts(pricingParameterSet.get().getPricingRules());
		result.setAvailableDiscounts(pricing.getAvailableDiscountingRules(result.getStoredDiscounts()));
		
        pricingParameterSetRepo.save(pricingParameterSet.get());
    	
		return new ResponseEntity<>(result,HttpStatus.OK);
    }
	
}
