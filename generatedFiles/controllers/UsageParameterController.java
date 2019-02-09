package com.sg.netex.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.rutebanken.netex.model.UsageParameter;
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
import com.sg.netex.dto.UsageParameterDTO;
import com.sg.netex.model.ButtonEvents;
import com.sg.netex.model.GenericTable;
import com.sg.netex.repository.UsageParameterRepository;

@Controller
public class UsageParameterController {

	@Autowired
	UsageParameterRepository usageParameterRepo;

	@Autowired
	private UtilConfig dozer;
	
	@RequestMapping(value = "/usageParameters", method = RequestMethod.GET)
    public ResponseEntity<List<UsageParameterDTO>> usageParameters(Model model) {
    	List<UsageParameterDTO> usageParameters = usageParameterRepo.findAll();
    	
        return new ResponseEntity<>(usageParameters,HttpStatus.OK);
    }
	
	
	// the simple table view for thymeleaf
	@RequestMapping(value = "/usageParameterGrid", method = RequestMethod.GET)
    public String usageParameter(Model model) {
		
		List<UsageParameterDTO> usageParameters = usageParameterRepo.findAll();
    	
		List<GenericTable> tableItems = new ArrayList<>();
		
		for (UsageParameterDTO list : usageParameters) {
			GenericTable item = new GenericTable();
			item.setFareFrameType(list.getFareFrameType());
			if (list.getName() != null)
				item.setName(list.getName());
			item.setMongoId(list.getMongoId());
			if (list.getUsageParameter().getName() != null)
				item.setName(list.getUsageParameter().getName().getValue());
			if (list.getOperator() != null)
				item.setObjectType(list.getOperator());

			item.setVersion(list.getUsageParameter().getVersion());
			
			tableItems.add(item);
		}
		
		model.addAttribute("tableItems",tableItems);
		
        ButtonEvents action = new ButtonEvents();
        action.setGetUrl("/usageParameters");
        action.setModifyItemUrl("/usageParameter");
        action.setDeleteItemUrl("/usageParameter");
        model.addAttribute("action", action);
        
        
        return "/html/simpleTable.html :: grid";
    }
	
	@RequestMapping(value = "/usageParameter", method = RequestMethod.GET)
    public String newUsageParameter(Model model,@RequestParam(required=false) String mongoId) {
    	
		Optional<UsageParameterDTO> usageParameter;
		
		if (mongoId != null && mongoId.trim().length() > 0) {
			usageParameter = usageParameterRepo.findById(mongoId);
		} else {
			usageParameter = Optional.of(new UsageParameterDTO());
			UsageParameter doc = new UsageParameter();
			usageParameter.get().setUsageParameter(doc);
			
			usageParameter.get().getUsageParameter().setId(dozer.generateNetexName("usageParameter"));
			usageParameter.get().getUsageParameter().setVersion("latest");
		}
				
        model.addAttribute("item", usageParameter);

        return "fares/usageParameter.html :: edit";
    }
	
	@RequestMapping(value = "/usageParameter", method = RequestMethod.POST)
    public String saveUsageParameter(@ModelAttribute UsageParameterDTO usageParameter, BindingResult errors, Model model) {
		
		usageParameter.setMongoId(dozer.generateMongoId(usageParameter.getUsageParameter().getId(), usageParameter.getUsageParameter().getVersion(), "usageParameter"));
		usageParameter.setId(usageParameter.getUsageParameter().getId());
		usageParameter.setVersion(usageParameter.getUsageParameter().getVersion());
		
		usageParameterRepo.save(usageParameter);
		
		return "index";
    }

	@RequestMapping(value = "/usageParameter", method = RequestMethod.DELETE)
    public String deleteUsageParameter(Model model,@RequestParam(required=true) String mongoId) {
		
		if (usageParameterRepo.findById(mongoId).isPresent()) {
			usageParameterRepo.deleteById(mongoId);
		} else {
			System.out.println("Missing "+ mongoId + " id");
		}
		
		return "index";
    }
}
