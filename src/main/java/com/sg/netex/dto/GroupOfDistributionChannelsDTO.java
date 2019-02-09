package com.sg.netex.dto;

import org.rutebanken.netex.model.GroupOfDistributionChannels;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="GroupOfDistributionChannels")
public class GroupOfDistributionChannelsDTO extends BaseDTO {
	
	private GroupOfDistributionChannels groupOfDistributionChannels;
	
	public GroupOfDistributionChannels getGroupOfDistributionChannels() {
		return groupOfDistributionChannels;
	}

	public void setGroupOfDistributionChannels(GroupOfDistributionChannels groupOfDistributionChannels) {
		this.groupOfDistributionChannels = groupOfDistributionChannels;
	}
	
	
}
