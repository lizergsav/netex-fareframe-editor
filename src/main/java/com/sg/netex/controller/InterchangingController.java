package com.sg.netex.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.rutebanken.netex.model.Interchanging;
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
import com.sg.netex.dto.InterchangingDTO;
import com.sg.netex.model.ButtonEvents;
import com.sg.netex.model.GenericTable;
import com.sg.netex.repository.InterchangingRepository;

@Controller
public class InterchangingController {

	@Autowired
	InterchangingRepository interchangingRepo;

	@Autowired
	private UtilConfig dozer;
	
	@RequestMapping(value = "/interchangings", method = RequestMethod.GET)
    public ResponseEntity<List<InterchangingDTO>> interchangings(Model model) {
    	List<InterchangingDTO> interchangings = interchangingRepo.findAll();
    	
        return new ResponseEntity<>(interchangings,HttpStatus.OK);
    }
	
	
	// the simple table view for thymeleaf
	@RequestMapping(value = "/interchangingGrid", method = RequestMethod.GET)
    public String interchanging(Model model) {
		
		List<InterchangingDTO> interchangings = interchangingRepo.findAll();
    	
		List<GenericTable> tableItems = new ArrayList<>();
		
		for (InterchangingDTO list : interchangings) {
			GenericTable item = new GenericTable();
			item.setFareFrameType(list.getFareFrameType());
			if (list.getName() != null)
				item.setName(list.getName());
			item.setMongoId(list.getMongoId());
			if (list.getInterchanging().getName() != null)
				item.setName(list.getInterchanging().getName().getValue());
			if (list.getOperator() != null)
				item.setObjectType(list.getOperator());

			item.setVersion(list.getInterchanging().getVersion());
			
			tableItems.add(item);
		}
		
		model.addAttribute("tableItems",tableItems);
		
        ButtonEvents action = new ButtonEvents();
        action.setGetUrl("/interchangings");
        action.setModifyItemUrl("/interchanging");
        action.setDeleteItemUrl("/interchanging");
        model.addAttribute("action", action);
        
        
        return "/html/simpleTable.html :: grid";
    }
	
	@RequestMapping(value = "/interchanging", method = RequestMethod.GET)
    public String newInterchanging(Model model,@RequestParam(required=false) String mongoId) {
    	
		Optional<InterchangingDTO> interchanging;
		
		if (mongoId != null && mongoId.trim().length() > 0) {
			interchanging = interchangingRepo.findById(mongoId);
		} else {
			interchanging = Optional.of(new InterchangingDTO());
			Interchanging doc = new Interchanging();
			interchanging.get().setInterchanging(doc);
			
			interchanging.get().getInterchanging().setId(dozer.generateNetexName("interchanging"));
			interchanging.get().getInterchanging().setVersion("latest");
		}
				
        model.addAttribute("item", interchanging);

        return "fares/interchanging.html :: edit";
    }
	
	@RequestMapping(value = "/interchanging", method = RequestMethod.POST)
    public String saveInterchanging(@ModelAttribute InterchangingDTO interchanging, BindingResult errors, Model model) {
		
		interchanging.setMongoId(dozer.generateMongoId(interchanging.getInterchanging().getId(), interchanging.getInterchanging().getVersion(), "interchanging"));
		interchanging.setId(interchanging.getInterchanging().getId());
		interchanging.setVersion(interchanging.getInterchanging().getVersion());
		
		interchangingRepo.save(interchanging);
		
		return "index";
    }

	@RequestMapping(value = "/interchanging", method = RequestMethod.DELETE)
    public String deleteInterchanging(Model model,@RequestParam(required=true) String mongoId) {
		
		if (interchangingRepo.findById(mongoId).isPresent()) {
			interchangingRepo.deleteById(mongoId);
		} else {
			System.out.println("Missing "+ mongoId + " id");
		}
		
		return "index";
    }
}
