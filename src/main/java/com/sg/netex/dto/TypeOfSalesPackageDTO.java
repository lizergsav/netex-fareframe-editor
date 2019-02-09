package com.sg.netex.dto;

import org.rutebanken.netex.model.TypeOfSalesPackage;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="TypeOfSalesPackage")
public class TypeOfSalesPackageDTO extends BaseDTO {
	
	private TypeOfSalesPackage typeOfSalesPackage;
	
	public TypeOfSalesPackage getTypeOfSalesPackage() {
		return typeOfSalesPackage;
	}

	public void setTypeOfSalesPackage(TypeOfSalesPackage typeOfSalesPackage) {
		this.typeOfSalesPackage = typeOfSalesPackage;
	}
	
	
}
