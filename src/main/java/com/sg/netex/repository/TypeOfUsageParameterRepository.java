package com.sg.netex.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.sg.netex.dto.TypeOfUsageParameterDTO;

public interface TypeOfUsageParameterRepository extends MongoRepository<TypeOfUsageParameterDTO, String> {
	
}
