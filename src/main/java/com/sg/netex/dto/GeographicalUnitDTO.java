package com.sg.netex.dto;

import org.rutebanken.netex.model.GeographicalUnit;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="GeographicalUnit")
public class GeographicalUnitDTO extends BaseDTO {
	
	private GeographicalUnit geographicalUnit;
	
	public GeographicalUnit getGeographicalUnit() {
		return geographicalUnit;
	}

	public void setGeographicalUnit(GeographicalUnit geographicalUnit) {
		this.geographicalUnit = geographicalUnit;
	}
	
	
}
