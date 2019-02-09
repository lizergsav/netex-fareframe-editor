package com.sg.netex.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.sg.netex.dto.TypeOfOrganisationDTO;

public interface TypeOfOrganisationRepository extends MongoRepository<TypeOfOrganisationDTO, String> {
	
}
