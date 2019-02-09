package com.sg.netex.dto;

import org.rutebanken.netex.model.GeographicalIntervalPrice;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="GeographicalIntervalPrice")
public class GeographicalIntervalPriceDTO extends BaseDTO {
	
	private GeographicalIntervalPrice geographicalIntervalPrice;
	
	public GeographicalIntervalPrice getGeographicalIntervalPrice() {
		return geographicalIntervalPrice;
	}

	public void setGeographicalIntervalPrice(GeographicalIntervalPrice geographicalIntervalPrice) {
		this.geographicalIntervalPrice = geographicalIntervalPrice;
	}
	
	
}
