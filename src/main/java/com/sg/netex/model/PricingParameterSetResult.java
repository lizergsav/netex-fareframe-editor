package com.sg.netex.model;

import java.util.List;

import org.rutebanken.netex.model.DiscountingRule;
import org.springframework.http.HttpStatus;

public class PricingParameterSetResult {
	
	private List<DiscountingRule> availableDiscounts;
	
	private List<DiscountingRule> storedDiscounts;

	private String result;
	
	private HttpStatus status;
		
	public List<DiscountingRule> getAvailableDiscounts() {
		return availableDiscounts;
	}

	public void setAvailableDiscounts(List<DiscountingRule> availableDiscounts) {
		this.availableDiscounts = availableDiscounts;
	}

	public List<DiscountingRule> getStoredDiscounts() {
		return storedDiscounts;
	}

	public void setStoredDiscounts(List<DiscountingRule> storedDiscounts) {
		this.storedDiscounts = storedDiscounts;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}
}
