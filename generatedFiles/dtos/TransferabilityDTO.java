package com.sg.netex.dto;

import org.rutebanken.netex.model.Transferability;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Transferability")
public class TransferabilityDTO extends BaseDTO {
	
	private Transferability transferability;
	
	public Transferability getTransferability() {
		return transferability;
	}

	public void setTransferability(Transferability transferability) {
		this.transferability = transferability;
	}
	
	
}
