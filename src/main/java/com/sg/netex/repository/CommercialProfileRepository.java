package com.sg.netex.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.sg.netex.dto.CommercialProfileDTO;

public interface CommercialProfileRepository extends MongoRepository<CommercialProfileDTO, String> {
	
}
