package com.sg.netex.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class Helper {

	public static List<String> getEnumValues(String enumName){
		List<String> result = new ArrayList<>();
		
		Class<?> clz;
		try {
			
			if (enumName.contains("."))
				clz = Class.forName(enumName);
			else
				clz = Class.forName("org.rutebanken.netex.model.".concat(enumName));
			for (Object item: clz.getEnumConstants()) {
				result.add(item.toString());
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static Boolean getBooleanValue(Boolean value) {
		return value;
	}
	
}
