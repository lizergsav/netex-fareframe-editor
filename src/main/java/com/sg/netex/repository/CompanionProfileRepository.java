package com.sg.netex.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.sg.netex.dto.CompanionProfileDTO;

public interface CompanionProfileRepository extends MongoRepository<CompanionProfileDTO, String> {
	
}
