package com.sg.netex.dto;

import org.rutebanken.netex.model.GenericParameterAssignment;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="GenericParameterAssignment")
public class GenericParameterAssignmentDTO extends BaseDTO {
	
	private GenericParameterAssignment genericParameterAssignment;
	
	public GenericParameterAssignment getGenericParameterAssignment() {
		return genericParameterAssignment;
	}

	public void setGenericParameterAssignment(GenericParameterAssignment genericParameterAssignment) {
		this.genericParameterAssignment = genericParameterAssignment;
	}
	
	
}
