package com.sg.netex.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.sg.netex.dto.DiscountingRuleDTO;

public interface DiscountingRuleRepository extends MongoRepository<DiscountingRuleDTO, String> {
	
}
