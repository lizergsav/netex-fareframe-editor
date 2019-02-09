package com.sg.netex.dto;

import org.rutebanken.netex.model.PointOfInterestClassification;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="PointOfInterestClassification")
public class PointOfInterestClassificationDTO extends BaseDTO {
	
	private PointOfInterestClassification pointOfInterestClassification;
	
	public PointOfInterestClassification getPointOfInterestClassification() {
		return pointOfInterestClassification;
	}

	public void setPointOfInterestClassification(PointOfInterestClassification pointOfInterestClassification) {
		this.pointOfInterestClassification = pointOfInterestClassification;
	}
	
	
}
