package com.sg.netex.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.sg.netex.dto.TypeOfFacilityDTO;

public interface TypeOfFacilityRepository extends MongoRepository<TypeOfFacilityDTO, String> {
	
}
