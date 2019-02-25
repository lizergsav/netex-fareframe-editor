package com.sg.netex.dto;

import java.util.ArrayList;
import java.util.List;

import org.rutebanken.netex.model.DiscountingRule;
import org.rutebanken.netex.model.PricingParameterSet;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="PricingParameterSet")
public class PricingParameterSetDTO extends BaseDTO {
	
	private PricingParameterSet pricingParameterSet;
	
	private List<DiscountingRule> pricingRules = new ArrayList<>();
	
	public PricingParameterSet getPricingParameterSet() {
		return pricingParameterSet;
	}

	public void setPricingParameterSet(PricingParameterSet pricingParameterSet) {
		this.pricingParameterSet = pricingParameterSet;
	}

	public List<DiscountingRule> getPricingRules() {
		return pricingRules;
	}

	public void setPricingRules(List<DiscountingRule> pricingRules) {
		this.pricingRules = pricingRules;
	}
	
	
}
