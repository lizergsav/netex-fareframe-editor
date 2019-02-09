package com.sg.netex.dto;

import org.rutebanken.netex.model.TypeOfTariff;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="TypeOfTariff")
public class TypeOfTariffDTO extends BaseDTO {
	
	private TypeOfTariff typeOfTariff;
	
	public TypeOfTariff getTypeOfTariff() {
		return typeOfTariff;
	}

	public void setTypeOfTariff(TypeOfTariff typeOfTariff) {
		this.typeOfTariff = typeOfTariff;
	}
	
	
}
