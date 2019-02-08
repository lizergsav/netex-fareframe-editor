package com.sg.netex.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.sg.netex.dto.TypeOfTravelDocumentDTO;

public interface TypeOfTravelDocumentRepository extends MongoRepository<TypeOfTravelDocumentDTO, String> {
	
}
