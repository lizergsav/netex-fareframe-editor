package com.sg.netex.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.sg.netex.dto.DirectionDTO;

public interface DirectionRepository extends MongoRepository<DirectionDTO, String> {
	
}
