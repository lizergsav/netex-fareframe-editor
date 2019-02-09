package com.sg.netex.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.rutebanken.netex.model.FrequencyOfUse;
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
import com.sg.netex.dto.FrequencyOfUseDTO;
import com.sg.netex.model.ButtonEvents;
import com.sg.netex.model.GenericTable;
import com.sg.netex.repository.FrequencyOfUseRepository;

@Controller
public class FrequencyOfUseController {

	@Autowired
	FrequencyOfUseRepository frequencyOfUseRepo;

	@Autowired
	private UtilConfig dozer;
	
	@RequestMapping(value = "/frequencyOfUses", method = RequestMethod.GET)
    public ResponseEntity<List<FrequencyOfUseDTO>> frequencyOfUses(Model model) {
    	List<FrequencyOfUseDTO> frequencyOfUses = frequencyOfUseRepo.findAll();
    	
        return new ResponseEntity<>(frequencyOfUses,HttpStatus.OK);
    }
	
	
	// the simple table view for thymeleaf
	@RequestMapping(value = "/frequencyOfUseGrid", method = RequestMethod.GET)
    public String frequencyOfUse(Model model) {
		
		List<FrequencyOfUseDTO> frequencyOfUses = frequencyOfUseRepo.findAll();
    	
		List<GenericTable> tableItems = new ArrayList<>();
		
		for (FrequencyOfUseDTO list : frequencyOfUses) {
			GenericTable item = new GenericTable();
			item.setFareFrameType(list.getFareFrameType());
			if (list.getName() != null)
				item.setName(list.getName());
			item.setMongoId(list.getMongoId());
			if (list.getFrequencyOfUse().getName() != null)
				item.setName(list.getFrequencyOfUse().getName().getValue());
			if (list.getOperator() != null)
				item.setObjectType(list.getOperator());

			item.setVersion(list.getFrequencyOfUse().getVersion());
			
			tableItems.add(item);
		}
		
		model.addAttribute("tableItems",tableItems);
		
        ButtonEvents action = new ButtonEvents();
        action.setGetUrl("/frequencyOfUses");
        action.setModifyItemUrl("/frequencyOfUse");
        action.setDeleteItemUrl("/frequencyOfUse");
        model.addAttribute("action", action);
        
        
        return "/html/simpleTable.html :: grid";
    }
	
	@RequestMapping(value = "/frequencyOfUse", method = RequestMethod.GET)
    public String newFrequencyOfUse(Model model,@RequestParam(required=false) String mongoId) {
    	
		Optional<FrequencyOfUseDTO> frequencyOfUse;
		
		if (mongoId != null && mongoId.trim().length() > 0) {
			frequencyOfUse = frequencyOfUseRepo.findById(mongoId);
		} else {
			frequencyOfUse = Optional.of(new FrequencyOfUseDTO());
			FrequencyOfUse doc = new FrequencyOfUse();
			frequencyOfUse.get().setFrequencyOfUse(doc);
			
			frequencyOfUse.get().getFrequencyOfUse().setId(dozer.generateNetexName("frequencyOfUse"));
			frequencyOfUse.get().getFrequencyOfUse().setVersion("latest");
		}
				
        model.addAttribute("item", frequencyOfUse);

        return "fares/frequencyOfUse.html :: edit";
    }
	
	@RequestMapping(value = "/frequencyOfUse", method = RequestMethod.POST)
    public String saveFrequencyOfUse(@ModelAttribute FrequencyOfUseDTO frequencyOfUse, BindingResult errors, Model model) {
		
		frequencyOfUse.setMongoId(dozer.generateMongoId(frequencyOfUse.getFrequencyOfUse().getId(), frequencyOfUse.getFrequencyOfUse().getVersion(), "frequencyOfUse"));
		frequencyOfUse.setId(frequencyOfUse.getFrequencyOfUse().getId());
		frequencyOfUse.setVersion(frequencyOfUse.getFrequencyOfUse().getVersion());
		
		frequencyOfUseRepo.save(frequencyOfUse);
		
		return "index";
    }

	@RequestMapping(value = "/frequencyOfUse", method = RequestMethod.DELETE)
    public String deleteFrequencyOfUse(Model model,@RequestParam(required=true) String mongoId) {
		
		if (frequencyOfUseRepo.findById(mongoId).isPresent()) {
			frequencyOfUseRepo.deleteById(mongoId);
		} else {
			System.out.println("Missing "+ mongoId + " id");
		}
		
		return "index";
    }
}
