package com.sg.netex.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.rutebanken.netex.model.FareFrame;
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
import com.sg.netex.dto.FareFrameDTO;
import com.sg.netex.model.ButtonEvents;
import com.sg.netex.model.GenericTable;
import com.sg.netex.repository.FareFrameRepository;

@Controller
public class FareFrameController {

	@Autowired
	FareFrameRepository fareFrameRepo;

	@Autowired
	private UtilConfig dozer;
	
	@RequestMapping(value = "/fareFrames", method = RequestMethod.GET)
    public ResponseEntity<List<FareFrameDTO>> fareFrames(Model model) {
    	List<FareFrameDTO> fareFrames = fareFrameRepo.findAll();
    	
        return new ResponseEntity<>(fareFrames,HttpStatus.OK);
    }
	
	
	// the simple table view for thymeleaf
	@RequestMapping(value = "/fareFrameGrid", method = RequestMethod.GET)
    public String fareFrame(Model model) {
		
		List<FareFrameDTO> fareFrames = fareFrameRepo.findAll();
    	
		List<GenericTable> tableItems = new ArrayList<>();
		
		for (FareFrameDTO list : fareFrames) {
			GenericTable item = new GenericTable();
			item.setFareFrameType(list.getFareFrameType());
			if (list.getName() != null)
				item.setName(list.getName());
			item.setMongoId(list.getMongoId());
			if (list.getFareFrame().getName() != null)
				item.setName(list.getFareFrame().getName().getValue());
			if (list.getOperator() != null)
				item.setObjectType(list.getOperator());

			item.setVersion(list.getFareFrame().getVersion());
			
			tableItems.add(item);
		}
		
		model.addAttribute("tableItems",tableItems);
		
        ButtonEvents action = new ButtonEvents();
        action.setGetUrl("/fareFrames");
        action.setModifyItemUrl("/fareFrame");
        action.setDeleteItemUrl("/fareFrame");
        model.addAttribute("action", action);
        
        
        return "/html/simpleTable.html :: grid";
    }
	
	@RequestMapping(value = "/fareFrame", method = RequestMethod.GET)
    public String newFareFrame(Model model,@RequestParam(required=false) String mongoId) {
    	
		Optional<FareFrameDTO> fareFrame;
		
		if (mongoId != null && mongoId.trim().length() > 0) {
			fareFrame = fareFrameRepo.findById(mongoId);
		} else {
			fareFrame = Optional.of(new FareFrameDTO());
			FareFrame doc = new FareFrame();
			fareFrame.get().setFareFrame(doc);
			
			fareFrame.get().getFareFrame().setId(dozer.generateNetexName("fareFrame"));
			fareFrame.get().getFareFrame().setVersion("latest");
		}
				
        model.addAttribute("item", fareFrame);

        return "fares/fareFrame.html :: edit";
    }
	
	@RequestMapping(value = "/fareFrame", method = RequestMethod.POST)
    public String saveFareFrame(@ModelAttribute FareFrameDTO fareFrame, BindingResult errors, Model model) {
		
		fareFrame.setMongoId(dozer.generateMongoId(fareFrame.getFareFrame().getId(), fareFrame.getFareFrame().getVersion(), "fareFrame"));
		fareFrame.setId(fareFrame.getFareFrame().getId());
		fareFrame.setVersion(fareFrame.getFareFrame().getVersion());
		
		fareFrameRepo.save(fareFrame);
		
		return "index";
    }

	@RequestMapping(value = "/fareFrame", method = RequestMethod.DELETE)
    public String deleteFareFrame(Model model,@RequestParam(required=true) String mongoId) {
		
		if (fareFrameRepo.findById(mongoId).isPresent()) {
			fareFrameRepo.deleteById(mongoId);
		} else {
			System.out.println("Missing "+ mongoId + " id");
		}
		
		return "index";
    }
}
