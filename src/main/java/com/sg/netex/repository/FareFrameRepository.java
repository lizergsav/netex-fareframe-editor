package com.sg.netex.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.sg.netex.dto.FareFrameDTO;

public interface FareFrameRepository extends MongoRepository<FareFrameDTO, String> {
	
}
