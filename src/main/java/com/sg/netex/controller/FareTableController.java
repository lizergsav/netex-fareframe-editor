package com.sg.netex.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.rutebanken.netex.model.FareTable;
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
import com.sg.netex.dto.FareTableDTO;
import com.sg.netex.model.ButtonEvents;
import com.sg.netex.model.GenericTable;
import com.sg.netex.repository.FareTableRepository;

@Controller
public class FareTableController {

	@Autowired
	FareTableRepository fareTableRepo;

	@Autowired
	private UtilConfig dozer;
	
	@RequestMapping(value = "/fareTables", method = RequestMethod.GET)
    public ResponseEntity<List<FareTableDTO>> fareTables(Model model) {
    	List<FareTableDTO> fareTables = fareTableRepo.findAll();
    	
        return new ResponseEntity<>(fareTables,HttpStatus.OK);
    }
	
	
	// the simple table view for thymeleaf
	@RequestMapping(value = "/fareTableGrid", method = RequestMethod.GET)
    public String fareTable(Model model) {
		
		List<FareTableDTO> fareTables = fareTableRepo.findAll();
    	
		List<GenericTable> tableItems = new ArrayList<>();
		
		for (FareTableDTO list : fareTables) {
			GenericTable item = new GenericTable();
			item.setFareFrameType(list.getFareFrameType());
			if (list.getName() != null)
				item.setName(list.getName());
			item.setMongoId(list.getMongoId());
			if (list.getFareTable().getName() != null)
				item.setName(list.getFareTable().getName().getValue());
			if (list.getOperator() != null)
				item.setObjectType(list.getOperator());

			item.setVersion(list.getFareTable().getVersion());
			
			tableItems.add(item);
		}
		
		model.addAttribute("tableItems",tableItems);
		
        ButtonEvents action = new ButtonEvents();
        action.setGetUrl("/fareTables");
        action.setModifyItemUrl("/fareTable");
        action.setDeleteItemUrl("/fareTable");
        model.addAttribute("action", action);
        
        
        return "/html/simpleTable.html :: grid";
    }
	
	@RequestMapping(value = "/fareTable", method = RequestMethod.GET)
    public String newFareTable(Model model,@RequestParam(required=false) String mongoId) {
    	
		Optional<FareTableDTO> fareTable;
		
		if (mongoId != null && mongoId.trim().length() > 0) {
			fareTable = fareTableRepo.findById(mongoId);
		} else {
			fareTable = Optional.of(new FareTableDTO());
			FareTable doc = new FareTable();
			fareTable.get().setFareTable(doc);
			
			fareTable.get().getFareTable().setId(dozer.generateNetexName("fareTable"));
			fareTable.get().getFareTable().setVersion("latest");
		}
				
        model.addAttribute("item", fareTable);

        return "common/fareTable.html :: edit";
    }
	
	@RequestMapping(value = "/fareTable", method = RequestMethod.POST)
    public String saveFareTable(@ModelAttribute FareTableDTO fareTable, BindingResult errors, Model model) {
		
		fareTable.setMongoId(dozer.generateMongoId(fareTable.getFareTable().getId(), fareTable.getFareTable().getVersion(), "fareTable"));
		fareTable.setId(fareTable.getFareTable().getId());
		fareTable.setVersion(fareTable.getFareTable().getVersion());
		
		fareTableRepo.save(fareTable);
		
		return "index";
    }

	@RequestMapping(value = "/fareTable", method = RequestMethod.DELETE)
    public String deleteFareTable(Model model,@RequestParam(required=true) String mongoId) {
		
		if (fareTableRepo.findById(mongoId).isPresent()) {
			fareTableRepo.deleteById(mongoId);
		} else {
			System.out.println("Missing "+ mongoId + " id");
		}
		
		return "index";
    }
}
