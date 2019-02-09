package com.sg.netex.dto;

import org.rutebanken.netex.model.PreassignedFareProduct;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="PreassignedFareProduct")
public class PreassignedFareProductDTO extends BaseDTO {
	
	private PreassignedFareProduct preassignedFareProduct;
	
	public PreassignedFareProduct getPreassignedFareProduct() {
		return preassignedFareProduct;
	}

	public void setPreassignedFareProduct(PreassignedFareProduct preassignedFareProduct) {
		this.preassignedFareProduct = preassignedFareProduct;
	}
	
	
}
