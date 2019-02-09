package com.sg.netex.dto;

import org.rutebanken.netex.model.CommercialProfile;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="CommercialProfile")
public class CommercialProfileDTO extends BaseDTO {
	
	private CommercialProfile commercialProfile;
	
	public CommercialProfile getCommercialProfile() {
		return commercialProfile;
	}

	public void setCommercialProfile(CommercialProfile commercialProfile) {
		this.commercialProfile = commercialProfile;
	}
	
	
}
