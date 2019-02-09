package com.sg.netex.dto;

import org.rutebanken.netex.model.TypeOfFacility;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="TypeOfFacility")
public class TypeOfFacilityDTO extends BaseDTO {
	
	private TypeOfFacility typeOfFacility;
	
	public TypeOfFacility getTypeOfFacility() {
		return typeOfFacility;
	}

	public void setTypeOfFacility(TypeOfFacility typeOfFacility) {
		this.typeOfFacility = typeOfFacility;
	}
	
	
}
