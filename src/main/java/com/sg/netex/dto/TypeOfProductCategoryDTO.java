package com.sg.netex.dto;

import org.rutebanken.netex.model.TypeOfProductCategory;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="TypeOfProductCategory")
public class TypeOfProductCategoryDTO extends BaseDTO {

	private TypeOfProductCategory typeOfProductCategory;

	public TypeOfProductCategory getTypeOfProductCategory() {
		return typeOfProductCategory;
	}

	public void setTypeOfProductCategory(TypeOfProductCategory typeOfProductCategory) {
		this.typeOfProductCategory = typeOfProductCategory;
	}

	
}
