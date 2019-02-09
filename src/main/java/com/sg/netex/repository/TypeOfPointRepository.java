package com.sg.netex.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.sg.netex.dto.TypeOfPointDTO;

public interface TypeOfPointRepository extends MongoRepository<TypeOfPointDTO, String> {
	
}
