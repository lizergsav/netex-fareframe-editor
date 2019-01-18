package com.example.netex.model;

import org.rutebanken.netex.model.TypeOfTravelDocument;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="TypeOfTravelDocument")
public class TypeOfTravelDocumentDTO extends TypeOfTravelDocument {

	@Id
	private String mongoId;

	public String getMongoId() {
		return mongoId;
	}

	public void setMongoId(String mongoId) {
		this.mongoId = mongoId;
	}
	
	
}
