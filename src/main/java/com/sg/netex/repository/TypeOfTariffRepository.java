package com.sg.netex.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.sg.netex.dto.TypeOfTariffDTO;

public interface TypeOfTariffRepository extends MongoRepository<TypeOfTariffDTO, String> {
	
}
