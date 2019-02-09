package com.sg.netex.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.sg.netex.dto.GenericParameterAssignmentDTO;

public interface GenericParameterAssignmentRepository extends MongoRepository<GenericParameterAssignmentDTO, String> {
	
}
