package com.sg.netex.dto;


import org.rutebanken.netex.model.EntityInVersionStructure;
import org.springframework.data.annotation.Id;

import com.sg.netex.dto.type.FareFrameType;

public class BaseDTO extends EntityInVersionStructure{
	
	@Id
	private String mongoId;
		
	private FareFrameType fareFrameType;
	
	private String objectType;
	
	public String getMongoId() {
		return mongoId;
	}

	public void setMongoId(String mongoId) {
		this.mongoId = mongoId;
	}

	public FareFrameType getFareFrameType() {
		return fareFrameType;
	}

	public void setFareFrameType(FareFrameType fareFrameType) {
		this.fareFrameType = fareFrameType;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}
}
