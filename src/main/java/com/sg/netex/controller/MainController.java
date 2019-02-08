package com.sg.netex.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sg.netex.dto.BaseDTO;
import com.sg.netex.repository.BaseDTORepository;

@Controller
public class MainController {

	@Autowired
	BaseDTORepository baseRepo;
	
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
                
        return "index";
    }
    
	@RequestMapping(value = "/BaseInfo", method = RequestMethod.GET)
    public ResponseEntity<List<BaseDTO>> baseInfo(Model model) {
    	List<BaseDTO> base = baseRepo.findAll();
    	
        return new ResponseEntity<>(base,HttpStatus.OK);
    }

    
}
