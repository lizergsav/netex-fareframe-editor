package com.sg.netex.dto;

import org.rutebanken.netex.model.Direction;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Direction")
public class DirectionDTO extends BaseDTO {
	
	private Direction direction;
	
	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	
}
