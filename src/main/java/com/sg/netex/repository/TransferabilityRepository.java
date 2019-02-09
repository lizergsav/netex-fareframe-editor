package com.sg.netex.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.sg.netex.dto.TransferabilityDTO;

public interface TransferabilityRepository extends MongoRepository<TransferabilityDTO, String> {
	
}
