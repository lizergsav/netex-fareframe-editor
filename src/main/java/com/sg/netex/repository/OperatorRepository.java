package com.sg.netex.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sg.netex.dto.BaseDTO;

public interface OperatorRepository extends MongoRepository<BaseDTO,String> {

	
}
