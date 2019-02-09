package com.sg.netex.dto;

import org.rutebanken.netex.model.SupplementProduct;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="SupplementProduct")
public class SupplementProductDTO extends BaseDTO {
	
	private SupplementProduct supplementProduct;
	
	public SupplementProduct getSupplementProduct() {
		return supplementProduct;
	}

	public void setSupplementProduct(SupplementProduct supplementProduct) {
		this.supplementProduct = supplementProduct;
	}
	
	
}
