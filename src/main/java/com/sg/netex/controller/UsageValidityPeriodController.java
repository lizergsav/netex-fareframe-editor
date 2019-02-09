package com.sg.netex.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.rutebanken.netex.model.UsageValidityPeriod;
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
import com.sg.netex.dto.UsageValidityPeriodDTO;
import com.sg.netex.model.ButtonEvents;
import com.sg.netex.model.GenericTable;
import com.sg.netex.repository.UsageValidityPeriodRepository;

@Controller
public class UsageValidityPeriodController {

	@Autowired
	UsageValidityPeriodRepository usageValidityPeriodRepo;

	@Autowired
	private UtilConfig dozer;
	
	@RequestMapping(value = "/usageValidityPeriods", method = RequestMethod.GET)
    public ResponseEntity<List<UsageValidityPeriodDTO>> usageValidityPeriods(Model model) {
    	List<UsageValidityPeriodDTO> usageValidityPeriods = usageValidityPeriodRepo.findAll();
    	
        return new ResponseEntity<>(usageValidityPeriods,HttpStatus.OK);
    }
	
	
	// the simple table view for thymeleaf
	@RequestMapping(value = "/usageValidityPeriodGrid", method = RequestMethod.GET)
    public String usageValidityPeriod(Model model) {
		
		List<UsageValidityPeriodDTO> usageValidityPeriods = usageValidityPeriodRepo.findAll();
    	
		List<GenericTable> tableItems = new ArrayList<>();
		
		for (UsageValidityPeriodDTO list : usageValidityPeriods) {
			GenericTable item = new GenericTable();
			item.setFareFrameType(list.getFareFrameType());
			if (list.getName() != null)
				item.setName(list.getName());
			item.setMongoId(list.getMongoId());
			if (list.getUsageValidityPeriod().getName() != null)
				item.setName(list.getUsageValidityPeriod().getName().getValue());
			if (list.getOperator() != null)
				item.setObjectType(list.getOperator());

			item.setVersion(list.getUsageValidityPeriod().getVersion());
			
			tableItems.add(item);
		}
		
		model.addAttribute("tableItems",tableItems);
		
        ButtonEvents action = new ButtonEvents();
        action.setGetUrl("/usageValidityPeriods");
        action.setModifyItemUrl("/usageValidityPeriod");
        action.setDeleteItemUrl("/usageValidityPeriod");
        model.addAttribute("action", action);
        
        
        return "/html/simpleTable.html :: grid";
    }
	
	@RequestMapping(value = "/usageValidityPeriod", method = RequestMethod.GET)
    public String newUsageValidityPeriod(Model model,@RequestParam(required=false) String mongoId) {
    	
		Optional<UsageValidityPeriodDTO> usageValidityPeriod;
		
		if (mongoId != null && mongoId.trim().length() > 0) {
			usageValidityPeriod = usageValidityPeriodRepo.findById(mongoId);
		} else {
			usageValidityPeriod = Optional.of(new UsageValidityPeriodDTO());
			UsageValidityPeriod doc = new UsageValidityPeriod();
			usageValidityPeriod.get().setUsageValidityPeriod(doc);
			
			usageValidityPeriod.get().getUsageValidityPeriod().setId(dozer.generateNetexName("usageValidityPeriod"));
			usageValidityPeriod.get().getUsageValidityPeriod().setVersion("latest");
		}
				
        model.addAttribute("item", usageValidityPeriod);

        return "fares/usageValidityPeriod.html :: edit";
    }
	
	@RequestMapping(value = "/usageValidityPeriod", method = RequestMethod.POST)
    public String saveUsageValidityPeriod(@ModelAttribute UsageValidityPeriodDTO usageValidityPeriod, BindingResult errors, Model model) {
		
		usageValidityPeriod.setMongoId(dozer.generateMongoId(usageValidityPeriod.getUsageValidityPeriod().getId(), usageValidityPeriod.getUsageValidityPeriod().getVersion(), "usageValidityPeriod"));
		usageValidityPeriod.setId(usageValidityPeriod.getUsageValidityPeriod().getId());
		usageValidityPeriod.setVersion(usageValidityPeriod.getUsageValidityPeriod().getVersion());
		
		usageValidityPeriodRepo.save(usageValidityPeriod);
		
		return "index";
    }

	@RequestMapping(value = "/usageValidityPeriod", method = RequestMethod.DELETE)
    public String deleteUsageValidityPeriod(Model model,@RequestParam(required=true) String mongoId) {
		
		if (usageValidityPeriodRepo.findById(mongoId).isPresent()) {
			usageValidityPeriodRepo.deleteById(mongoId);
		} else {
			System.out.println("Missing "+ mongoId + " id");
		}
		
		return "index";
    }
}
