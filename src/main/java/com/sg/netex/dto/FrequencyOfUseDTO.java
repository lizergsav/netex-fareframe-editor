package com.sg.netex.dto;

import org.rutebanken.netex.model.FrequencyOfUse;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="FrequencyOfUse")
public class FrequencyOfUseDTO extends BaseDTO {
	
	private FrequencyOfUse frequencyOfUse;
	
	public FrequencyOfUse getFrequencyOfUse() {
		return frequencyOfUse;
	}

	public void setFrequencyOfUse(FrequencyOfUse frequencyOfUse) {
		this.frequencyOfUse = frequencyOfUse;
	}
	
	
}
