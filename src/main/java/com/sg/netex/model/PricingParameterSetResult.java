package com.sg.netex.model;

import java.util.List;

import org.rutebanken.netex.model.DiscountingRule;
import org.springframework.http.HttpStatus;

public class PricingParameterSetResult {
	
	private List<DiscountingRule> gridView;
	
	private List<DiscountingRule> chosenView;

	private String result;
	
	private HttpStatus status;
	
	public List<DiscountingRule> getGridView() {
		return gridView;
	}

	public void setGridView(List<DiscountingRule> gridView) {
		this.gridView = gridView;
	}

	public List<DiscountingRule> getChosenView() {
		return chosenView;
	}

	public void setChosenView(List<DiscountingRule> chosenView) {
		this.chosenView = chosenView;
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
