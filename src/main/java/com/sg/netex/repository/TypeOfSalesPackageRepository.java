package com.sg.netex.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.sg.netex.dto.TypeOfSalesPackageDTO;

public interface TypeOfSalesPackageRepository extends MongoRepository<TypeOfSalesPackageDTO, String> {
	
}
