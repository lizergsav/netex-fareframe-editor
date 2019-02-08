package com.sg.netex.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.rutebanken.netex.model.TypeOfTravelDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sg.netex.config.UtilConfig;
import com.sg.netex.dto.TypeOfTravelDocumentDTO;
import com.sg.netex.model.ButtonEvents;
import com.sg.netex.model.GenericTable;
import com.sg.netex.repository.TypeOfTravelDocumentRepository;

@Controller
public class TypeOfTravelDocumentController {

	@Autowired
	TypeOfTravelDocumentRepository typeOfTravelDocumentRepo;

	@Autowired
	private UtilConfig dozer;
	
	@RequestMapping(value = "/typeOfTravelDocuments", method = RequestMethod.GET)
    public ResponseEntity<List<TypeOfTravelDocumentDTO>> typeOfTravelDocuments(Model model) {
    	List<TypeOfTravelDocumentDTO> typeOfTravelDocuments = typeOfTravelDocumentRepo.findAll();
    	
        return new ResponseEntity<>(typeOfTravelDocuments,HttpStatus.OK);
    }
	
	
	// the simple table view for thymeleaf
	@RequestMapping(value = "/typeOfTravelDocumentGrid", method = RequestMethod.GET)
    public String typeOfTravelDocument(Model model) {
		
		List<TypeOfTravelDocumentDTO> typeOfTravelDocuments = typeOfTravelDocumentRepo.findAll();
    	
		List<GenericTable> tableItems = new ArrayList<>();
		
		for (TypeOfTravelDocumentDTO list : typeOfTravelDocuments) {
			GenericTable item = new GenericTable();
			if (list.getTypeOfTravelDocument().getDescription() != null)
				item.setDescription(list.getTypeOfTravelDocument().getDescription().getValue());
			item.setFareFrameType(list.getFareFrameType());
			if (list.getName() != null)
				item.setName(list.getName());
			item.setMongoId(list.getMongoId());
			if (list.getTypeOfTravelDocument().getName() != null)
				item.setName(list.getTypeOfTravelDocument().getName().getValue());
			if (list.getOperator() != null)
				item.setObjectType(list.getOperator());
			if (list.getTypeOfTravelDocument().getPrivateCode()!= null)
				item.setPrivateCode(list.getTypeOfTravelDocument().getPrivateCode().getValue());
			item.setVersion(list.getTypeOfTravelDocument().getVersion());
			
			tableItems.add(item);
		}
		
		model.addAttribute("tableItems",tableItems);
		
        ButtonEvents action = new ButtonEvents();
        action.setGetUrl("/typeOfTravelDocuments");
        action.setModifyItemUrl("/typeOfTravelDocument");
        action.setDeleteItemUrl("/typeOfTravelDocument");
        model.addAttribute("action", action);
        
        
        return "/html/genericTable.html :: grid";
    }
	
	@RequestMapping(value = "/typeOfTravelDocument", method = RequestMethod.GET)
    public String newTypeOfTravelDocument(Model model,@RequestParam(required=false) String mongoId) {
    	
		Optional<TypeOfTravelDocumentDTO> typeOfTravelDocument;
		
		if (mongoId != null && mongoId.trim().length() > 0) {
			 typeOfTravelDocument = typeOfTravelDocumentRepo.findById(mongoId);
		} else {
			typeOfTravelDocument = Optional.of(new TypeOfTravelDocumentDTO());
			TypeOfTravelDocument doc = new TypeOfTravelDocument();
			typeOfTravelDocument.get().setTypeOfTravelDocument(doc);
			
			typeOfTravelDocument.get().getTypeOfTravelDocument().setId(dozer.generateNetexName("typeOfTravelDocument"));
			typeOfTravelDocument.get().getTypeOfTravelDocument().setVersion("latest");
		}
				
        model.addAttribute("item", typeOfTravelDocument);

        return "resourceFrame/typeOfTravelDocument.html :: edit";
    }
	
	@RequestMapping(value = "/typeOfTravelDocument", method = RequestMethod.POST)
    public String saveTypeOfTravelDocument(@ModelAttribute TypeOfTravelDocumentDTO typeOfTravelDocument, BindingResult errors, Model model) {
		
		typeOfTravelDocument.setMongoId(dozer.generateMongoId(typeOfTravelDocument.getTypeOfTravelDocument().getId(), typeOfTravelDocument.getTypeOfTravelDocument().getVersion(), "typeOfTravelDocument"));
		typeOfTravelDocument.setId(typeOfTravelDocument.getTypeOfTravelDocument().getId());
		typeOfTravelDocument.setVersion(typeOfTravelDocument.getTypeOfTravelDocument().getVersion());
		
		typeOfTravelDocumentRepo.save(typeOfTravelDocument);
		
		return "index";
    }

	@RequestMapping(value = "/typeOfTravelDocument", method = RequestMethod.DELETE)
    public String deleteTypeOfTravelDocument(Model model,@RequestParam(required=true) String mongoId) {
		
		if (typeOfTravelDocumentRepo.findById(mongoId).isPresent()) {
			typeOfTravelDocumentRepo.deleteById(mongoId);
		} else {
			System.out.println("Missing "+ mongoId + " id");
		}
		
		return "html/deleted";
    }
}
