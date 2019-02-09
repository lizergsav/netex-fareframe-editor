package com.sg.netex.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.sg.netex.dto.SalesPackageDTO;

public interface SalesPackageRepository extends MongoRepository<SalesPackageDTO, String> {
	
}
