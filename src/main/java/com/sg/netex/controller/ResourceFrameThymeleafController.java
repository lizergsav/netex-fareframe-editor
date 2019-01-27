package com.sg.netex.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.rutebanken.netex.model.TypeOfTravelDocument;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ResourceFrameThymeleafController {

	@Autowired
	TypeOfTravelDocumentRepository typeOfTravelDocumentRepo;
	
	@Autowired
	private UtilConfig dozer;
	
	// the simple table view for thymeleaf
	@RequestMapping(value = "/typeOfTravelDocument", method = RequestMethod.GET)
    public String typeOfTravelDocument(Model model) {
		
		List<TypeOfTravelDocumentDTO> typeOfTravelDocuments = typeOfTravelDocumentRepo.findAll();
    	List<GenericTable> tableItems = new ArrayList<>();
		
		for (TypeOfTravelDocumentDTO list : typeOfTravelDocuments) {
			GenericTable item = new GenericTable();
			if (list.getTypeOfTravelDocument().getDescription() != null)
				item.setDescription(list.getTypeOfTravelDocument().getDescription().getValue());
			item.setFareFrameType(list.getFareFrameType());
			item.setMongoId(list.getMongoId());
			if (list.getTypeOfTravelDocument().getName() != null)
				item.setName(list.getTypeOfTravelDocument().getName().getValue());
			if (list.getObjectType() != null)
				item.setObjectType(list.getObjectType());
			if (list.getTypeOfTravelDocument().getPrivateCode()!= null)
				item.setPrivateCode(list.getTypeOfTravelDocument().getPrivateCode().getValue());
			item.setVersion(list.getTypeOfTravelDocument().getVersion());
			
			tableItems.add(item);
		}
		
		model.addAttribute("tableItems",tableItems);
		
        ButtonEvents action = new ButtonEvents();
        action.setGetUrl("/typeOfTravelDocuments");
        action.setModifyItemUrl("/manageTypeOfTravelDocument");
        model.addAttribute("action", action);
        
        
        return "genericTable.html :: grid";
    }
	
	@RequestMapping(value = "/manageTypeOfTravelDocument", method = RequestMethod.GET)
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
	
	@RequestMapping(value = "/saveTypeOfTravelDocument", method = RequestMethod.POST)
    public String saveTypeOfTravelDocument(@ModelAttribute TypeOfTravelDocumentDTO typeOfTravelDocument, BindingResult errors, Model model) {
		
		typeOfTravelDocument.setMongoId(dozer.generateMongoId(typeOfTravelDocument.getTypeOfTravelDocument().getId(), typeOfTravelDocument.getTypeOfTravelDocument().getVersion(), "typeOfTravelDocument"));
		typeOfTravelDocument.setId(typeOfTravelDocument.getTypeOfTravelDocument().getId());
		typeOfTravelDocument.setVersion(typeOfTravelDocument.getTypeOfTravelDocument().getVersion());
		
		typeOfTravelDocumentRepo.save(typeOfTravelDocument);
		
		return "index.html";
    }
	
}
