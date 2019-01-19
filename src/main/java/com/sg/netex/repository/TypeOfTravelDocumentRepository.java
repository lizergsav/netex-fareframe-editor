package com.sg.netex.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sg.netex.model.TypeOfTravelDocumentDTO;

public interface TypeOfTravelDocumentRepository extends MongoRepository<TypeOfTravelDocumentDTO, String> {
	
	public Optional<TypeOfTravelDocumentDTO> findById(String id, String version);
	public Optional<TypeOfTravelDocumentDTO> findById(String id);
	
}
