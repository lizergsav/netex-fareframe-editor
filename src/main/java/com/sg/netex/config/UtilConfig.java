package com.sg.netex.config;

import java.util.UUID;

import org.dozer.DozerBeanMapper;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UtilConfig {

	public DozerBeanMapper mapper;

	public String generateMongoId(String id,String version, String type) {
		
		return type.concat("_").concat(id).concat("_").concat(version);
	}
	
	public String generateNetexName(String type) {
		
		if (type.trim().length() > 0)
			return type.concat("-").concat(UUID.randomUUID().toString());
		else 
			return UUID.randomUUID().toString();
	}
}
