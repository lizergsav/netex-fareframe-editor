package com.sg.netex;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
public class RunApplicationTest extends AbstractTestNGSpringContextTests {
  
	  @Autowired
	  @Qualifier("textTemplateEngine")
	  TemplateEngine textTemplateEngine;

	
	@Test(enabled = true)
	public void f() {
	  
	  Context context = new Context();
	  context.setVariable("objectName", "typeOfTravelDocument");
	  context.setVariable("ObjectName", "TypeOfTravelDocument");
	  
	  String text = textTemplateEngine.process("DTOTemplate", context);

	  writeUsingFileWriter(text, "test.txt");
	  
	  System.out.println("done");
	  
  }
	
	private void generateDto(String name) {
		
	}
	
	private void writeUsingFileWriter(String data, String path) {
        File file = new File(path);
        FileWriter fr = null;
        try {
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
