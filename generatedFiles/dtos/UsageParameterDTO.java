package com.sg.netex.dto;

import org.rutebanken.netex.model.UsageParameter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="UsageParameter")
public class UsageParameterDTO extends BaseDTO {
	
	private UsageParameter usageParameter;
	
	public UsageParameter getUsageParameter() {
		return usageParameter;
	}

	public void setUsageParameter(UsageParameter usageParameter) {
		this.usageParameter = usageParameter;
	}
	
	
}
