package com.sg.netex.model;

import com.sg.netex.dto.type.FareFrameType;

public class GenericTable {

	private String mongoId;
	
	private String name;
	
	private String version;
	
	private String privateCode;
	
	private String description;
	
	private String objectType;
	
	private FareFrameType fareFrameType;

	public String getMongoId() {
		return mongoId;
	}

	public void setMongoId(String mongoId) {
		this.mongoId = mongoId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getPrivateCode() {
		return privateCode;
	}

	public void setPrivateCode(String privateCode) {
		this.privateCode = privateCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public FareFrameType getFareFrameType() {
		return fareFrameType;
	}

	public void setFareFrameType(FareFrameType fareFrameType) {
		this.fareFrameType = fareFrameType;
	}
	
}
