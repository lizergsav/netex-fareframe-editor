package com.sg.netex.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.rutebanken.netex.model.TravelDocument;
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
import com.sg.netex.dto.TravelDocumentDTO;
import com.sg.netex.model.ButtonEvents;
import com.sg.netex.model.GenericTable;
import com.sg.netex.repository.TravelDocumentRepository;

@Controller
public class TravelDocumentController {

	@Autowired
	TravelDocumentRepository travelDocumentRepo;

	@Autowired
	private UtilConfig dozer;
	
	@RequestMapping(value = "/travelDocuments", method = RequestMethod.GET)
    public ResponseEntity<List<TravelDocumentDTO>> travelDocuments(Model model) {
    	List<TravelDocumentDTO> travelDocuments = travelDocumentRepo.findAll();
    	
        return new ResponseEntity<>(travelDocuments,HttpStatus.OK);
    }
	
	
	// the simple table view for thymeleaf
	@RequestMapping(value = "/travelDocumentGrid", method = RequestMethod.GET)
    public String travelDocument(Model model) {
		
		List<TravelDocumentDTO> travelDocuments = travelDocumentRepo.findAll();
    	
		List<GenericTable> tableItems = new ArrayList<>();
		
		for (TravelDocumentDTO list : travelDocuments) {
			GenericTable item = new GenericTable();
			item.setFareFrameType(list.getFareFrameType());
			if (list.getName() != null)
				item.setName(list.getName());
			item.setMongoId(list.getMongoId());
			if (list.getTravelDocument().getName() != null)
				item.setName(list.getTravelDocument().getName().getValue());
			if (list.getOperator() != null)
				item.setObjectType(list.getOperator());

			item.setVersion(list.getTravelDocument().getVersion());
			
			tableItems.add(item);
		}
		
		model.addAttribute("tableItems",tableItems);
		
        ButtonEvents action = new ButtonEvents();
        action.setGetUrl("/travelDocuments");
        action.setModifyItemUrl("/travelDocument");
        action.setDeleteItemUrl("/travelDocument");
        model.addAttribute("action", action);
        
        
        return "/html/simpleTable.html :: grid";
    }
	
	@RequestMapping(value = "/travelDocument", method = RequestMethod.GET)
    public String newTravelDocument(Model model,@RequestParam(required=false) String mongoId) {
    	
		Optional<TravelDocumentDTO> travelDocument;
		
		if (mongoId != null && mongoId.trim().length() > 0) {
			travelDocument = travelDocumentRepo.findById(mongoId);
		} else {
			travelDocument = Optional.of(new TravelDocumentDTO());
			TravelDocument doc = new TravelDocument();
			travelDocument.get().setTravelDocument(doc);
			
			travelDocument.get().getTravelDocument().setId(dozer.generateNetexName("travelDocument"));
			travelDocument.get().getTravelDocument().setVersion("latest");
		}
				
        model.addAttribute("item", travelDocument);

        return "common/travelDocument.html :: edit";
    }
	
	@RequestMapping(value = "/travelDocument", method = RequestMethod.POST)
    public String saveTravelDocument(@ModelAttribute TravelDocumentDTO travelDocument, BindingResult errors, Model model) {
		
		travelDocument.setMongoId(dozer.generateMongoId(travelDocument.getTravelDocument().getId(), travelDocument.getTravelDocument().getVersion(), "travelDocument"));
		travelDocument.setId(travelDocument.getTravelDocument().getId());
		travelDocument.setVersion(travelDocument.getTravelDocument().getVersion());
		
		travelDocumentRepo.save(travelDocument);
		
		return "index";
    }

	@RequestMapping(value = "/travelDocument", method = RequestMethod.DELETE)
    public String deleteTravelDocument(Model model,@RequestParam(required=true) String mongoId) {
		
		if (travelDocumentRepo.findById(mongoId).isPresent()) {
			travelDocumentRepo.deleteById(mongoId);
		} else {
			System.out.println("Missing "+ mongoId + " id");
		}
		
		return "index";
    }
}
