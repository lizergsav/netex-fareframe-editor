package com.sg.netex.dto;

import org.rutebanken.netex.model.ValidableElement;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="ValidableElement")
public class ValidableElementDTO extends BaseDTO {
	
	private ValidableElement validableElement;
	
	public ValidableElement getValidableElement() {
		return validableElement;
	}

	public void setValidableElement(ValidableElement validableElement) {
		this.validableElement = validableElement;
	}
	
	
}
