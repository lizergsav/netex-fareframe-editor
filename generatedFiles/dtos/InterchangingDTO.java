package com.sg.netex.dto;

import org.rutebanken.netex.model.Interchanging;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Interchanging")
public class InterchangingDTO extends BaseDTO {
	
	private Interchanging interchanging;
	
	public Interchanging getInterchanging() {
		return interchanging;
	}

	public void setInterchanging(Interchanging interchanging) {
		this.interchanging = interchanging;
	}
	
	
}
