package com.sg.netex.dto;

import org.rutebanken.netex.model.LimitationGroupingType;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="LimitationGroupingType")
public class LimitationGroupingTypeDTO extends BaseDTO {
	
	private LimitationGroupingType limitationGroupingType;
	
	public LimitationGroupingType getLimitationGroupingType() {
		return limitationGroupingType;
	}

	public void setLimitationGroupingType(LimitationGroupingType limitationGroupingType) {
		this.limitationGroupingType = limitationGroupingType;
	}
	
	
}
