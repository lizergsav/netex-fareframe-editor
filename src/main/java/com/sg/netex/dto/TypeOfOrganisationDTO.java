package com.sg.netex.dto;

import org.rutebanken.netex.model.TypeOfOrganisation;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="TypeOfOrganisation")
public class TypeOfOrganisationDTO extends BaseDTO {
	
	private TypeOfOrganisation typeOfOrganisation;
	
	public TypeOfOrganisation getTypeOfOrganisation() {
		return typeOfOrganisation;
	}

	public void setTypeOfOrganisation(TypeOfOrganisation typeOfOrganisation) {
		this.typeOfOrganisation = typeOfOrganisation;
	}
	
	
}
