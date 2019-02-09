package com.sg.netex.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.sg.netex.dto.BrandingDTO;

public interface BrandingRepository extends MongoRepository<BrandingDTO, String> {
	
}
