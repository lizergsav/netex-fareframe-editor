package com.sg.netex.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.sg.netex.dto.GroupOfDistributionChannelsDTO;

public interface GroupOfDistributionChannelsRepository extends MongoRepository<GroupOfDistributionChannelsDTO, String> {
	
}
