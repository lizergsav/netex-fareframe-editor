package com.sg.netex.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.sg.netex.dto.ValidableElementDTO;

public interface ValidableElementRepository extends MongoRepository<ValidableElementDTO, String> {
	
}
