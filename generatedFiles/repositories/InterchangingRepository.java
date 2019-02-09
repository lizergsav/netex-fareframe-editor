package com.sg.netex.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.sg.netex.dto.InterchangingDTO;

public interface InterchangingRepository extends MongoRepository<InterchangingDTO, String> {
	
}
