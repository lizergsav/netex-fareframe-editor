package com.sg.netex.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.sg.netex.dto.GroupTicketDTO;

public interface GroupTicketRepository extends MongoRepository<GroupTicketDTO, String> {
	
}
