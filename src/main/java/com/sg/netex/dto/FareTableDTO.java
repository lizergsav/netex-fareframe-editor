package com.sg.netex.dto;

import org.rutebanken.netex.model.FareTable;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="FareTable")
public class FareTableDTO extends BaseDTO {
	
	private FareTable fareTable;
	
	public FareTable getFareTable() {
		return fareTable;
	}

	public void setFareTable(FareTable fareTable) {
		this.fareTable = fareTable;
	}
	
	
}
