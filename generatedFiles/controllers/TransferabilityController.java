package com.sg.netex.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.rutebanken.netex.model.Transferability;
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
import com.sg.netex.dto.TransferabilityDTO;
import com.sg.netex.model.ButtonEvents;
import com.sg.netex.model.GenericTable;
import com.sg.netex.repository.TransferabilityRepository;

@Controller
public class TransferabilityController {

	@Autowired
	TransferabilityRepository transferabilityRepo;

	@Autowired
	private UtilConfig dozer;
	
	@RequestMapping(value = "/transferabilitys", method = RequestMethod.GET)
    public ResponseEntity<List<TransferabilityDTO>> transferabilitys(Model model) {
    	List<TransferabilityDTO> transferabilitys = transferabilityRepo.findAll();
    	
        return new ResponseEntity<>(transferabilitys,HttpStatus.OK);
    }
	
	
	// the simple table view for thymeleaf
	@RequestMapping(value = "/transferabilityGrid", method = RequestMethod.GET)
    public String transferability(Model model) {
		
		List<TransferabilityDTO> transferabilitys = transferabilityRepo.findAll();
    	
		List<GenericTable> tableItems = new ArrayList<>();
		
		for (TransferabilityDTO list : transferabilitys) {
			GenericTable item = new GenericTable();
			item.setFareFrameType(list.getFareFrameType());
			if (list.getName() != null)
				item.setName(list.getName());
			item.setMongoId(list.getMongoId());
			if (list.getTransferability().getName() != null)
				item.setName(list.getTransferability().getName().getValue());
			if (list.getOperator() != null)
				item.setObjectType(list.getOperator());

			item.setVersion(list.getTransferability().getVersion());
			
			tableItems.add(item);
		}
		
		model.addAttribute("tableItems",tableItems);
		
        ButtonEvents action = new ButtonEvents();
        action.setGetUrl("/transferabilitys");
        action.setModifyItemUrl("/transferability");
        action.setDeleteItemUrl("/transferability");
        model.addAttribute("action", action);
        
        
        return "/html/simpleTable.html :: grid";
    }
	
	@RequestMapping(value = "/transferability", method = RequestMethod.GET)
    public String newTransferability(Model model,@RequestParam(required=false) String mongoId) {
    	
		Optional<TransferabilityDTO> transferability;
		
		if (mongoId != null && mongoId.trim().length() > 0) {
			transferability = transferabilityRepo.findById(mongoId);
		} else {
			transferability = Optional.of(new TransferabilityDTO());
			Transferability doc = new Transferability();
			transferability.get().setTransferability(doc);
			
			transferability.get().getTransferability().setId(dozer.generateNetexName("transferability"));
			transferability.get().getTransferability().setVersion("latest");
		}
				
        model.addAttribute("item", transferability);

        return "fares/transferability.html :: edit";
    }
	
	@RequestMapping(value = "/transferability", method = RequestMethod.POST)
    public String saveTransferability(@ModelAttribute TransferabilityDTO transferability, BindingResult errors, Model model) {
		
		transferability.setMongoId(dozer.generateMongoId(transferability.getTransferability().getId(), transferability.getTransferability().getVersion(), "transferability"));
		transferability.setId(transferability.getTransferability().getId());
		transferability.setVersion(transferability.getTransferability().getVersion());
		
		transferabilityRepo.save(transferability);
		
		return "index";
    }

	@RequestMapping(value = "/transferability", method = RequestMethod.DELETE)
    public String deleteTransferability(Model model,@RequestParam(required=true) String mongoId) {
		
		if (transferabilityRepo.findById(mongoId).isPresent()) {
			transferabilityRepo.deleteById(mongoId);
		} else {
			System.out.println("Missing "+ mongoId + " id");
		}
		
		return "index";
    }
}
