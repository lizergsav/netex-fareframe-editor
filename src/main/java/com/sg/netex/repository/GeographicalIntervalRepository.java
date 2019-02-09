package com.sg.netex.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.sg.netex.dto.GeographicalIntervalDTO;

public interface GeographicalIntervalRepository extends MongoRepository<GeographicalIntervalDTO, String> {
	
}
