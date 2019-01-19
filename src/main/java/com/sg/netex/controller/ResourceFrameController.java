package com.sg.netex.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sg.netex.model.TypeOfTravelDocumentDTO;
import com.sg.netex.repository.TypeOfTravelDocumentRepository;

@Controller
public class ResourceFrameController {

	@Autowired
	TypeOfTravelDocumentRepository docRepo;
	
	@RequestMapping(value = "/typeOfTravelDocument", method = RequestMethod.GET)
    public String typeOfTravelDocument(Model model) {
    	List<TypeOfTravelDocumentDTO> typeOfTravelDocuments = docRepo.findAll();
    	
        model.addAttribute("typeOfTravelDocuments", typeOfTravelDocuments);

        return "resourceFrame/typeOfTravelDocument.html";
    }
	
	@RequestMapping(value = "/newTypeOfTravelDocument", method = RequestMethod.GET)
    public String newTypeOfTravelDocument(Model model) {
    	
		TypeOfTravelDocumentDTO typeOfTravelDocument = new TypeOfTravelDocumentDTO();
		typeOfTravelDocument.setId(UUID.randomUUID().toString());
		typeOfTravelDocument.setVersion("latest");
		
        model.addAttribute("typeOfTravelDocument", typeOfTravelDocument);

        return "resourceFrame/newTypeOfTravelDocument.html";
    }
	
	@RequestMapping(value = "/saveTypeOfTravelDocument", method = RequestMethod.POST)
    public String saveTypeOfTravelDocument(@ModelAttribute TypeOfTravelDocumentDTO typeOfTravelDocument, BindingResult errors, Model model) {
		
		typeOfTravelDocument.setMongoId(typeOfTravelDocument.getId().concat("_").concat(typeOfTravelDocument.getVersion()));
		docRepo.save(typeOfTravelDocument);
		
		return "resourceFrame/typeOfTravelDocument.html";
    }
	
	@RequestMapping(value = "/modifyTypeOfTravelDocument", method = RequestMethod.GET)
    public String newTypeOfTravelDocument(@RequestParam(required=true) String id,@RequestParam(required=true) String version, Model model) {
    	
		Optional<TypeOfTravelDocumentDTO> typeOfTravelDocument = docRepo.findById(id, version);
		
        model.addAttribute("typeOfTravelDocument", typeOfTravelDocument);

        return "resourceFrame/newTypeOfTravelDocument.html";
    }
}
