package com.sg.netex.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.sg.netex.dto.PriceUnitDTO;

public interface PriceUnitRepository extends MongoRepository<PriceUnitDTO, String> {
	
}
