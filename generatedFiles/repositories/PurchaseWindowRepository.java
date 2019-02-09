package com.sg.netex.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.sg.netex.dto.PurchaseWindowDTO;

public interface PurchaseWindowRepository extends MongoRepository<PurchaseWindowDTO, String> {
	
}
