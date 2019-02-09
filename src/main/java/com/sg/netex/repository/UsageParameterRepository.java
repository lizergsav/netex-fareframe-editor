package com.sg.netex.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.sg.netex.dto.UsageParameterDTO;

public interface UsageParameterRepository extends MongoRepository<UsageParameterDTO, String> {
	
}
