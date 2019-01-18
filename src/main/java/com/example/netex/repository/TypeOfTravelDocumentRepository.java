package com.example.netex.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.netex.model.TypeOfTravelDocumentDTO;

public interface TypeOfTravelDocumentRepository extends MongoRepository<TypeOfTravelDocumentDTO, String> {
	
	public Optional<TypeOfTravelDocumentDTO> findById(String id, String version);
	public Optional<TypeOfTravelDocumentDTO> findById(String id);
	
}
