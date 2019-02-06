package com.sg.netex.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import com.sg.netex.dto.BaseDTO;

@Component
public interface BaseDTORepository extends MongoRepository<BaseDTO, String>{

}
