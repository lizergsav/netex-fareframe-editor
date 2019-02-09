package com.sg.netex.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.sg.netex.dto.ControllableElementDTO;

public interface ControllableElementRepository extends MongoRepository<ControllableElementDTO, String> {
	
}
