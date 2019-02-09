package com.sg.netex.dto;

import org.rutebanken.netex.model.GroupTicket;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="GroupTicket")
public class GroupTicketDTO extends BaseDTO {
	
	private GroupTicket groupTicket;
	
	public GroupTicket getGroupTicket() {
		return groupTicket;
	}

	public void setGroupTicket(GroupTicket groupTicket) {
		this.groupTicket = groupTicket;
	}
	
	
}
