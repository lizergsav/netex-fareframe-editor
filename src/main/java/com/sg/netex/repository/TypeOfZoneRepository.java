package com.sg.netex.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.sg.netex.dto.TypeOfZoneDTO;

public interface TypeOfZoneRepository extends MongoRepository<TypeOfZoneDTO, String> {
	
}
