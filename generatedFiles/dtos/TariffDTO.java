package com.sg.netex.dto;

import org.rutebanken.netex.model.Tariff;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Tariff")
public class TariffDTO extends BaseDTO {
	
	private Tariff tariff;
	
	public Tariff getTariff() {
		return tariff;
	}

	public void setTariff(Tariff tariff) {
		this.tariff = tariff;
	}
	
	
}
