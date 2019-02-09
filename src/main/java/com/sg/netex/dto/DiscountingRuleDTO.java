package com.sg.netex.dto;

import org.rutebanken.netex.model.DiscountingRule;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="DiscountingRule")
public class DiscountingRuleDTO extends BaseDTO {
	
	private DiscountingRule discountingRule;
	
	public DiscountingRule getDiscountingRule() {
		return discountingRule;
	}

	public void setDiscountingRule(DiscountingRule discountingRule) {
		this.discountingRule = discountingRule;
	}
	
	
}
