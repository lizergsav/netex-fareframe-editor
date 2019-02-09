package com.sg.netex;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GenerateClassesTest extends AbstractTestNGSpringContextTests {
  
	  @Autowired
	  @Qualifier("textTemplateEngine")
	  TemplateEngine textTemplateEngine;

	
	@Test(enabled = true)
	public void f() {
	  
		List<String> resource = new ArrayList<>();
			/*
			 * Resource
			resource.add("typeOfTravelDocument");
			resource.add("typeOfService");
			resource.add("typeOfProductCategory");
			
			resource.add("typeOfFacility");
			resource.add("typeOfFareProduct");
			resource.add("branding");
			resource.add("priceUnit");
			resource.add("typeOfPoint");
			resource.add("typeOfTariff");
			resource.add("typeOfUsageParameter");
			resource.add("pointOfInterestClassification");
			resource.add("typeOfOrganisation");
			resource.add("typeOfZone");
			resource.add("direction");
			resource.add("typeOfSalesPackage");
			*/
		/*
		 * common
		resource.add("PricingParameterSet");
		resource.add("GeographicalUnit");
		resource.add("GeographicalInterval");
		resource.add("FareTable");
		resource.add("GeographicalIntervalPrice");
		resource.add("DiscountingRule");
		resource.add("TravelDocument");
		*/
	
		resource.add("FareFrame");
		resource.add("Tariff");
		resource.add("ValidableElement");
		resource.add("GenericParameterAssignment");
		resource.add("ControllableElement");
		resource.add("FrequencyOfUse");
		resource.add("UsageValidityPeriod");
		resource.add("UsageParameter");
		resource.add("UsageValidityPeriod");
		resource.add("PurchaseWindow");
		resource.add("Transferability");
		resource.add("Interchanging");
		resource.add("UserProfile");
		resource.add("GroupTicket");
		resource.add("CompanionProfile");
		resource.add("CommercialProfile");
		resource.add("PreassignedFareProduct");
		resource.add("SupplementProduct");
		resource.add("DistributionChannel");
		resource.add("GroupOfDistributionChannels");
		resource.add("SalesPackage");
		resource.add("GroupOfSalesPackages");
		
		for (String item: resource) {
			generateDto(item);
			generateRepository(item);
			generateController(item);
			generateHTML(item);

		}
			
	  	  
  }
	
	private void generateHTML(String object) {
		String uper = object.substring(0, 1).toUpperCase() + object.substring(1);
		String lower = object.substring(0, 1).toLowerCase() + object.substring(1);
				
		Context context = new Context();
		context.setVariable("objectName", lower);
		context.setVariable("ObjectName", uper);
		  
		String text = textTemplateEngine.process("htmlFile", context);
		writeUsingFileWriter(text, "generatedFiles/html/fares/".concat(uper.concat(".html")));
	}
	
	private void generateController(String object) {
		String uper = object.substring(0, 1).toUpperCase() + object.substring(1);
		String lower = object.substring(0, 1).toLowerCase() + object.substring(1);
				
		Context context = new Context();
		context.setVariable("objectName", lower);
		context.setVariable("ObjectName", uper);
		  
		String text = textTemplateEngine.process("ControllerTemplate", context);
		writeUsingFileWriter(text, "generatedFiles/controllers/".concat(uper.concat("Controller.java")));
	}
	
	private void generateDto(String object) {
		String uper = object.substring(0, 1).toUpperCase() + object.substring(1);
		String lower = object.substring(0, 1).toLowerCase() + object.substring(1);
				
		Context context = new Context();
		context.setVariable("objectName", lower);
		context.setVariable("ObjectName", uper);
		  
		String text = textTemplateEngine.process("DTOTemplate", context);
		writeUsingFileWriter(text, "generatedFiles/dtos/".concat(uper.concat("DTO.java")));
	}
	
	private void generateRepository(String object) {
		String uper = object.substring(0, 1).toUpperCase() + object.substring(1);
		String lower = object.substring(0, 1).toLowerCase() + object.substring(1);
				
		Context context = new Context();
		context.setVariable("objectName", lower);
		context.setVariable("ObjectName", uper);
		  
		String text = textTemplateEngine.process("RepositoryTemplate", context);
		writeUsingFileWriter(text, "generatedFiles/repositories/".concat(uper.concat("Repository.java")));
		
	}
	
	private void writeUsingFileWriter(String data, String path) {
        File file = new File(path);
        FileWriter fr = null;
        try {
            Files.deleteIfExists(file.toPath());
        	fr = new FileWriter(file);
            fr.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            //close resources
            try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
	
}
