package com.sg.netex.dto;

import org.rutebanken.netex.model.TravelDocument;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="TravelDocument")
public class TravelDocumentDTO extends BaseDTO {
	
	private TravelDocument travelDocument;
	
	public TravelDocument getTravelDocument() {
		return travelDocument;
	}

	public void setTravelDocument(TravelDocument travelDocument) {
		this.travelDocument = travelDocument;
	}
	
	
}
