package com.sg.netex.dto;

import org.rutebanken.netex.model.CompanionProfile;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="CompanionProfile")
public class CompanionProfileDTO extends BaseDTO {
	
	private CompanionProfile companionProfile;
	
	public CompanionProfile getCompanionProfile() {
		return companionProfile;
	}

	public void setCompanionProfile(CompanionProfile companionProfile) {
		this.companionProfile = companionProfile;
	}
	
	
}
