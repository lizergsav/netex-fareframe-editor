package com.sg.netex.dto;

import org.rutebanken.netex.model.TypeOfUsageParameter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="TypeOfUsageParameter")
public class TypeOfUsageParameterDTO extends BaseDTO {
	
	private TypeOfUsageParameter typeOfUsageParameter;
	
	public TypeOfUsageParameter getTypeOfUsageParameter() {
		return typeOfUsageParameter;
	}

	public void setTypeOfUsageParameter(TypeOfUsageParameter typeOfUsageParameter) {
		this.typeOfUsageParameter = typeOfUsageParameter;
	}
	
	
}
