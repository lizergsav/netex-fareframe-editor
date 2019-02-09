package com.sg.netex.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.sg.netex.dto.PointOfInterestClassificationDTO;

public interface PointOfInterestClassificationRepository extends MongoRepository<PointOfInterestClassificationDTO, String> {
	
}
