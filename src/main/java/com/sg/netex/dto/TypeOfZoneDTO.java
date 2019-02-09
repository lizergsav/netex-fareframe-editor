package com.sg.netex.dto;

import org.rutebanken.netex.model.TypeOfZone;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="TypeOfZone")
public class TypeOfZoneDTO extends BaseDTO {
	
	private TypeOfZone typeOfZone;
	
	public TypeOfZone getTypeOfZone() {
		return typeOfZone;
	}

	public void setTypeOfZone(TypeOfZone typeOfZone) {
		this.typeOfZone = typeOfZone;
	}
	
	
}
