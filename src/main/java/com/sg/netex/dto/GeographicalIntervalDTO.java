package com.sg.netex.dto;

import org.rutebanken.netex.model.GeographicalInterval;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="GeographicalInterval")
public class GeographicalIntervalDTO extends BaseDTO {
	
	private GeographicalInterval geographicalInterval;
	
	public GeographicalInterval getGeographicalInterval() {
		return geographicalInterval;
	}

	public void setGeographicalInterval(GeographicalInterval geographicalInterval) {
		this.geographicalInterval = geographicalInterval;
	}
	
	
}
