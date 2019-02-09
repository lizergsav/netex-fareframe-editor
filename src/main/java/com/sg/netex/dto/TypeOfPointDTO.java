package com.sg.netex.dto;

import org.rutebanken.netex.model.TypeOfPoint;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="TypeOfPoint")
public class TypeOfPointDTO extends BaseDTO {
	
	private TypeOfPoint typeOfPoint;
	
	public TypeOfPoint getTypeOfPoint() {
		return typeOfPoint;
	}

	public void setTypeOfPoint(TypeOfPoint typeOfPoint) {
		this.typeOfPoint = typeOfPoint;
	}
	
	
}
