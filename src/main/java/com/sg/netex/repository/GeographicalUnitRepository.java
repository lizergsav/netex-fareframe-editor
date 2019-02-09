package com.sg.netex.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.sg.netex.dto.GeographicalUnitDTO;

public interface GeographicalUnitRepository extends MongoRepository<GeographicalUnitDTO, String> {
	
}
