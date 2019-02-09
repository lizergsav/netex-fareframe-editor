package com.sg.netex.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.sg.netex.dto.SupplementProductDTO;

public interface SupplementProductRepository extends MongoRepository<SupplementProductDTO, String> {
	
}
