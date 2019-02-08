package com.sg.netex.dto;

import org.rutebanken.netex.model.[(${ObjectName})];
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="[(${ObjectName})]")
public class [(${ObjectName})]DTO extends BaseDTO {
	
	private [(${ObjectName})] [(${objectName})];
	
	public [(${ObjectName})] get[(${ObjectName})]() {
		return [(${objectName})];
	}

	public void set[(${ObjectName})]([(${ObjectName})] [(${objectName})]) {
		this.[(${objectName})] = [(${objectName})];
	}
	
	
}
