package com.sg.netex.utils;

//import java.util.List;

import org.junit.Test;

public class HelperTest {
	
	@Test()
	public void testGetEnumValues() throws ClassNotFoundException {
		
		//Class<?> clz = Class.forName("org.rutebanken.netex.model.MachineReadableEnumeration");
		
		//List<String> result = com.sg.netex.utils.Helper.getEnumValues("MachineReadableEnumeration");

		Helper help = new Helper();
		
		System.out.println(help.replaceDot("typeOfTravelDocument.machineReadable"));
		
	}
}
