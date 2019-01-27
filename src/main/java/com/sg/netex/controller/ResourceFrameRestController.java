package com.sg.netex.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sg.netex.dto.TypeOfTravelDocumentDTO;
import com.sg.netex.repository.TypeOfTravelDocumentRepository;

@Controller
public class ResourceFrameRestController {

	@Autowired
	TypeOfTravelDocumentRepository typeOfTravelDocumentRepo;
	
	@RequestMapping(value = "/typeOfTravelDocuments", method = RequestMethod.GET)
    public ResponseEntity<List<TypeOfTravelDocumentDTO>> typeOfTravelDocuments(Model model) {
    	List<TypeOfTravelDocumentDTO> typeOfTravelDocuments = typeOfTravelDocumentRepo.findAll();
    	
        return new ResponseEntity<>(typeOfTravelDocuments,HttpStatus.OK);
    }
	
	
	

}