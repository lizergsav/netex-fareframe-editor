package com.sg.netex.dto;

import org.rutebanken.netex.model.PricingParameterSet;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="PricingParameterSet")
public class PricingParameterSetDTO extends BaseDTO {
	
	private PricingParameterSet pricingParameterSet;
	
	public PricingParameterSet getPricingParameterSet() {
		return pricingParameterSet;
	}

	public void setPricingParameterSet(PricingParameterSet pricingParameterSet) {
		this.pricingParameterSet = pricingParameterSet;
	}
	
	
}
