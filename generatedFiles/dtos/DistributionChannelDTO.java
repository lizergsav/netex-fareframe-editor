package com.sg.netex.dto;

import org.rutebanken.netex.model.DistributionChannel;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="DistributionChannel")
public class DistributionChannelDTO extends BaseDTO {
	
	private DistributionChannel distributionChannel;
	
	public DistributionChannel getDistributionChannel() {
		return distributionChannel;
	}

	public void setDistributionChannel(DistributionChannel distributionChannel) {
		this.distributionChannel = distributionChannel;
	}
	
	
}
