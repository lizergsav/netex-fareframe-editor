package com.sg.netex.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.sg.netex.dto.TravelDocumentDTO;

public interface TravelDocumentRepository extends MongoRepository<TravelDocumentDTO, String> {
	
}
