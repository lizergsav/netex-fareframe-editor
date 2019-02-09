package com.sg.netex.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.sg.netex.dto.GroupOfSalesPackagesDTO;

public interface GroupOfSalesPackagesRepository extends MongoRepository<GroupOfSalesPackagesDTO, String> {
	
}
