package com.sg.netex.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sg.netex.dto.TypeOfServiceDTO;

public interface TypeOfServiceRepository extends MongoRepository<TypeOfServiceDTO, String> {

}
