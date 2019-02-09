package com.sg.netex.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.rutebanken.netex.model.[(${ObjectName})];
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
import com.sg.netex.dto.[(${ObjectName})]DTO;
import com.sg.netex.model.ButtonEvents;
import com.sg.netex.model.GenericTable;
import com.sg.netex.repository.[(${ObjectName})]Repository;

@Controller
public class [(${ObjectName})]Controller {

	@Autowired
	[(${ObjectName})]Repository [(${objectName})]Repo;

	@Autowired
	private UtilConfig dozer;
	
	@RequestMapping(value = "/[(${objectName})]s", method = RequestMethod.GET)
    public ResponseEntity<List<[(${ObjectName})]DTO>> [(${objectName})]s(Model model) {
    	List<[(${ObjectName})]DTO> [(${objectName})]s = [(${objectName})]Repo.findAll();
    	
        return new ResponseEntity<>([(${objectName})]s,HttpStatus.OK);
    }
	
	
	// the simple table view for thymeleaf
	@RequestMapping(value = "/[(${objectName})]Grid", method = RequestMethod.GET)
    public String [(${objectName})](Model model) {
		
		List<[(${ObjectName})]DTO> [(${objectName})]s = [(${objectName})]Repo.findAll();
    	
		List<GenericTable> tableItems = new ArrayList<>();
		
		for ([(${ObjectName})]DTO list : [(${objectName})]s) {
			GenericTable item = new GenericTable();
			item.setFareFrameType(list.getFareFrameType());
			if (list.getName() != null)
				item.setName(list.getName());
			item.setMongoId(list.getMongoId());
			if (list.get[(${ObjectName})]().getName() != null)
				item.setName(list.get[(${ObjectName})]().getName().getValue());
			if (list.getOperator() != null)
				item.setObjectType(list.getOperator());

			item.setVersion(list.get[(${ObjectName})]().getVersion());
			
			tableItems.add(item);
		}
		
		model.addAttribute("tableItems",tableItems);
		
        ButtonEvents action = new ButtonEvents();
        action.setGetUrl("/[(${objectName})]s");
        action.setModifyItemUrl("/[(${objectName})]");
        action.setDeleteItemUrl("/[(${objectName})]");
        model.addAttribute("action", action);
        
        
        return "/html/simpleTable.html :: grid";
    }
	
	@RequestMapping(value = "/[(${objectName})]", method = RequestMethod.GET)
    public String new[(${ObjectName})](Model model,@RequestParam(required=false) String mongoId) {
    	
		Optional<[(${ObjectName})]DTO> [(${objectName})];
		
		if (mongoId != null && mongoId.trim().length() > 0) {
			[(${objectName})] = [(${objectName})]Repo.findById(mongoId);
		} else {
			[(${objectName})] = Optional.of(new [(${ObjectName})]DTO());
			[(${ObjectName})] doc = new [(${ObjectName})]();
			[(${objectName})].get().set[(${ObjectName})](doc);
			
			[(${objectName})].get().get[(${ObjectName})]().setId(dozer.generateNetexName("[(${objectName})]"));
			[(${objectName})].get().get[(${ObjectName})]().setVersion("latest");
		}
				
        model.addAttribute("item", [(${objectName})]);

        return "fares/[(${objectName})].html :: edit";
    }
	
	@RequestMapping(value = "/[(${objectName})]", method = RequestMethod.POST)
    public String save[(${ObjectName})](@ModelAttribute [(${ObjectName})]DTO [(${objectName})], BindingResult errors, Model model) {
		
		[(${objectName})].setMongoId(dozer.generateMongoId([(${objectName})].get[(${ObjectName})]().getId(), [(${objectName})].get[(${ObjectName})]().getVersion(), "[(${objectName})]"));
		[(${objectName})].setId([(${objectName})].get[(${ObjectName})]().getId());
		[(${objectName})].setVersion([(${objectName})].get[(${ObjectName})]().getVersion());
		
		[(${objectName})]Repo.save([(${objectName})]);
		
		return "index";
    }

	@RequestMapping(value = "/[(${objectName})]", method = RequestMethod.DELETE)
    public String delete[(${ObjectName})](Model model,@RequestParam(required=true) String mongoId) {
		
		if ([(${objectName})]Repo.findById(mongoId).isPresent()) {
			[(${objectName})]Repo.deleteById(mongoId);
		} else {
			System.out.println("Missing "+ mongoId + " id");
		}
		
		return "index";
    }
}
