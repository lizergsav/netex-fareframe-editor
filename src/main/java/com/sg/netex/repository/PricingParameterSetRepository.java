package com.sg.netex.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.sg.netex.dto.PricingParameterSetDTO;

public interface PricingParameterSetRepository extends MongoRepository<PricingParameterSetDTO, String> {
	
}
