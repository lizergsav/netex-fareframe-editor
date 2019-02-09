package com.sg.netex.dto;

import org.rutebanken.netex.model.ControllableElement;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="ControllableElement")
public class ControllableElementDTO extends BaseDTO {
	
	private ControllableElement controllableElement;
	
	public ControllableElement getControllableElement() {
		return controllableElement;
	}

	public void setControllableElement(ControllableElement controllableElement) {
		this.controllableElement = controllableElement;
	}
	
	
}
