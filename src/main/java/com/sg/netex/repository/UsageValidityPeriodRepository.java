package com.sg.netex.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.sg.netex.dto.UsageValidityPeriodDTO;

public interface UsageValidityPeriodRepository extends MongoRepository<UsageValidityPeriodDTO, String> {
	
}
