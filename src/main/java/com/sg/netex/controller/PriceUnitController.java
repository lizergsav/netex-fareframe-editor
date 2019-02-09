package com.sg.netex.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.rutebanken.netex.model.PriceUnit;
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
import com.sg.netex.dto.PriceUnitDTO;
import com.sg.netex.model.ButtonEvents;
import com.sg.netex.model.GenericTable;
import com.sg.netex.repository.PriceUnitRepository;

@Controller
public class PriceUnitController {

	@Autowired
	PriceUnitRepository priceUnitRepo;

	@Autowired
	private UtilConfig dozer;
	
	@RequestMapping(value = "/priceUnits", method = RequestMethod.GET)
    public ResponseEntity<List<PriceUnitDTO>> priceUnits(Model model) {
    	List<PriceUnitDTO> priceUnits = priceUnitRepo.findAll();
    	
        return new ResponseEntity<>(priceUnits,HttpStatus.OK);
    }
	
	
	// the simple table view for thymeleaf
	@RequestMapping(value = "/priceUnitGrid", method = RequestMethod.GET)
    public String priceUnit(Model model) {
		
		List<PriceUnitDTO> priceUnits = priceUnitRepo.findAll();
    	
		List<GenericTable> tableItems = new ArrayList<>();
		
		for (PriceUnitDTO list : priceUnits) {
			GenericTable item = new GenericTable();
			if (list.getPriceUnit().getDescription() != null)
				item.setDescription(list.getPriceUnit().getDescription().getValue());
			item.setFareFrameType(list.getFareFrameType());
			if (list.getName() != null)
				item.setName(list.getName());
			item.setMongoId(list.getMongoId());
			if (list.getPriceUnit().getName() != null)
				item.setName(list.getPriceUnit().getName().getValue());
			if (list.getOperator() != null)
				item.setObjectType(list.getOperator());
			if (list.getPriceUnit().getPrivateCode()!= null)
				item.setPrivateCode(list.getPriceUnit().getPrivateCode().getValue());
			item.setVersion(list.getPriceUnit().getVersion());
			
			tableItems.add(item);
		}
		
		model.addAttribute("tableItems",tableItems);
		
        ButtonEvents action = new ButtonEvents();
        action.setGetUrl("/priceUnits");
        action.setModifyItemUrl("/priceUnit");
        action.setDeleteItemUrl("/priceUnit");
        model.addAttribute("action", action);
        
        
        return "/html/genericTable.html :: grid";
    }
	
	@RequestMapping(value = "/priceUnit", method = RequestMethod.GET)
    public String newPriceUnit(Model model,@RequestParam(required=false) String mongoId) {
    	
		Optional<PriceUnitDTO> priceUnit;
		
		if (mongoId != null && mongoId.trim().length() > 0) {
			priceUnit = priceUnitRepo.findById(mongoId);
		} else {
			priceUnit = Optional.of(new PriceUnitDTO());
			PriceUnit doc = new PriceUnit();
			priceUnit.get().setPriceUnit(doc);
			
			priceUnit.get().getPriceUnit().setId(dozer.generateNetexName("priceUnit"));
			priceUnit.get().getPriceUnit().setVersion("latest");
		}
				
        model.addAttribute("item", priceUnit);

        return "resourceFrame/priceUnit.html :: edit";
    }
	
	@RequestMapping(value = "/priceUnit", method = RequestMethod.POST)
    public String savePriceUnit(@ModelAttribute PriceUnitDTO priceUnit, BindingResult errors, Model model) {
		
		priceUnit.setMongoId(dozer.generateMongoId(priceUnit.getPriceUnit().getId(), priceUnit.getPriceUnit().getVersion(), "priceUnit"));
		priceUnit.setId(priceUnit.getPriceUnit().getId());
		priceUnit.setVersion(priceUnit.getPriceUnit().getVersion());
		
		priceUnitRepo.save(priceUnit);
		
		return "index";
    }

	@RequestMapping(value = "/priceUnit", method = RequestMethod.DELETE)
    public String deletePriceUnit(Model model,@RequestParam(required=true) String mongoId) {
		
		if (priceUnitRepo.findById(mongoId).isPresent()) {
			priceUnitRepo.deleteById(mongoId);
		} else {
			System.out.println("Missing "+ mongoId + " id");
		}
		
		return "index";
    }
}
