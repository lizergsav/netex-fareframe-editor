package com.sg.netex.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.sg.netex.dto.GeographicalIntervalPriceDTO;

public interface GeographicalIntervalPriceRepository extends MongoRepository<GeographicalIntervalPriceDTO, String> {
	
}
