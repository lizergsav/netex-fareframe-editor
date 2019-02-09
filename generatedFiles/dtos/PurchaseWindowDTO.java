package com.sg.netex.dto;

import org.rutebanken.netex.model.PurchaseWindow;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="PurchaseWindow")
public class PurchaseWindowDTO extends BaseDTO {
	
	private PurchaseWindow purchaseWindow;
	
	public PurchaseWindow getPurchaseWindow() {
		return purchaseWindow;
	}

	public void setPurchaseWindow(PurchaseWindow purchaseWindow) {
		this.purchaseWindow = purchaseWindow;
	}
	
	
}
