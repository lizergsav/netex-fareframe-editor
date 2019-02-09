package com.sg.netex.dto;

import org.rutebanken.netex.model.SalesPackage;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="SalesPackage")
public class SalesPackageDTO extends BaseDTO {
	
	private SalesPackage salesPackage;
	
	public SalesPackage getSalesPackage() {
		return salesPackage;
	}

	public void setSalesPackage(SalesPackage salesPackage) {
		this.salesPackage = salesPackage;
	}
	
	
}
