package com.sg.netex.dto;

import org.rutebanken.netex.model.TypeOfFareProduct;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="TypeOfFareProduct")
public class TypeOfFareProductDTO extends BaseDTO {
	
	private TypeOfFareProduct typeOfFareProduct;
	
	public TypeOfFareProduct getTypeOfFareProduct() {
		return typeOfFareProduct;
	}

	public void setTypeOfFareProduct(TypeOfFareProduct typeOfFareProduct) {
		this.typeOfFareProduct = typeOfFareProduct;
	}
	
	
}
