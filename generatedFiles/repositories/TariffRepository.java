package com.sg.netex.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.sg.netex.dto.TariffDTO;

public interface TariffRepository extends MongoRepository<TariffDTO, String> {
	
}
