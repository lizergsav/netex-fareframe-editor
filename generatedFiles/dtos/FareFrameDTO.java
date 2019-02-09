package com.sg.netex.dto;

import org.rutebanken.netex.model.FareFrame;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="FareFrame")
public class FareFrameDTO extends BaseDTO {
	
	private FareFrame fareFrame;
	
	public FareFrame getFareFrame() {
		return fareFrame;
	}

	public void setFareFrame(FareFrame fareFrame) {
		this.fareFrame = fareFrame;
	}
	
	
}
