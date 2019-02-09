package com.sg.netex.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.sg.netex.dto.DistributionChannelDTO;

public interface DistributionChannelRepository extends MongoRepository<DistributionChannelDTO, String> {
	
}
