package com.sg.netex.dto;

import org.rutebanken.netex.model.GroupOfSalesPackages;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="GroupOfSalesPackages")
public class GroupOfSalesPackagesDTO extends BaseDTO {
	
	private GroupOfSalesPackages groupOfSalesPackages;
	
	public GroupOfSalesPackages getGroupOfSalesPackages() {
		return groupOfSalesPackages;
	}

	public void setGroupOfSalesPackages(GroupOfSalesPackages groupOfSalesPackages) {
		this.groupOfSalesPackages = groupOfSalesPackages;
	}
	
	
}
