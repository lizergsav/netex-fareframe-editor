package com.sg.netex.dto;

import org.rutebanken.netex.model.PriceUnit;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="PriceUnit")
public class PriceUnitDTO extends BaseDTO {
	
	private PriceUnit priceUnit;
	
	public PriceUnit getPriceUnit() {
		return priceUnit;
	}

	public void setPriceUnit(PriceUnit priceUnit) {
		this.priceUnit = priceUnit;
	}
	
	
}
