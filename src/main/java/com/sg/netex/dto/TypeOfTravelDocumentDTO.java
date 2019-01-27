package com.sg.netex.dto;

import org.rutebanken.netex.model.TypeOfTravelDocument;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="TypeOfTravelDocument")
public class TypeOfTravelDocumentDTO extends BaseDTO {
	
	private TypeOfTravelDocument typeOfTravelDocument;
	
	public TypeOfTravelDocument getTypeOfTravelDocument() {
		return typeOfTravelDocument;
	}

	public void setTypeOfTravelDocument(TypeOfTravelDocument typeOfTravelDocument) {
		this.typeOfTravelDocument = typeOfTravelDocument;
	}
	
	
}
