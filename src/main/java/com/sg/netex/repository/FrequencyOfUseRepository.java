package com.sg.netex.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.sg.netex.dto.FrequencyOfUseDTO;

public interface FrequencyOfUseRepository extends MongoRepository<FrequencyOfUseDTO, String> {
	
}
