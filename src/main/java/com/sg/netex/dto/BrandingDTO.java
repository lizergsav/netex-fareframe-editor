package com.sg.netex.dto;

import org.rutebanken.netex.model.Branding;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Branding")
public class BrandingDTO extends BaseDTO {
	
	private Branding branding;
	
	public Branding getBranding() {
		return branding;
	}

	public void setBranding(Branding branding) {
		this.branding = branding;
	}
	
	
}
