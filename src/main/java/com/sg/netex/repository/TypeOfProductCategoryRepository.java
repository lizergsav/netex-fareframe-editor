package com.sg.netex.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sg.netex.dto.TypeOfProductCategoryDTO;

public interface TypeOfProductCategoryRepository extends MongoRepository<TypeOfProductCategoryDTO, String> {

}
