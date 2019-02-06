package com.sg.netex.dto;


import org.rutebanken.netex.model.EntityInVersionStructure;
import org.springframework.data.annotation.Id;

import com.sg.netex.dto.type.FareFrameType;

public class BaseDTO extends EntityInVersionStructure{
	
	@Id
	private String mongoId;
		
	private FareFrameType fareFrameType;
	
	private String operator;
	
	private String name;
	
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

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


}
