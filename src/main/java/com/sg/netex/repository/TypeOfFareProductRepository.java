package com.sg.netex.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.sg.netex.dto.TypeOfFareProductDTO;

public interface TypeOfFareProductRepository extends MongoRepository<TypeOfFareProductDTO, String> {
	
}
