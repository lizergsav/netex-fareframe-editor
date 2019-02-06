package com.sg.netex.dto;

import org.rutebanken.netex.model.TypeOfService;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="TypeOfService")
public class TypeOfServiceDTO extends BaseDTO {

	private TypeOfService typeOfService;

	public TypeOfService getTypeOfService() {
		return typeOfService;
	}

	public void setTypeOfService(TypeOfService typeOfService) {
		this.typeOfService = typeOfService;
	}

	
}
