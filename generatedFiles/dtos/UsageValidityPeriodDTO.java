package com.sg.netex.dto;

import org.rutebanken.netex.model.UsageValidityPeriod;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="UsageValidityPeriod")
public class UsageValidityPeriodDTO extends BaseDTO {
	
	private UsageValidityPeriod usageValidityPeriod;
	
	public UsageValidityPeriod getUsageValidityPeriod() {
		return usageValidityPeriod;
	}

	public void setUsageValidityPeriod(UsageValidityPeriod usageValidityPeriod) {
		this.usageValidityPeriod = usageValidityPeriod;
	}
	
	
}
