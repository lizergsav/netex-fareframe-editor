package com.sg.netex.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.rutebanken.netex.model.TypeOfTariff;
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
import com.sg.netex.dto.TypeOfTariffDTO;
import com.sg.netex.model.ButtonEvents;
import com.sg.netex.model.GenericTable;
import com.sg.netex.repository.TypeOfTariffRepository;

@Controller
public class TypeOfTariffController {

	@Autowired
	TypeOfTariffRepository typeOfTariffRepo;

	@Autowired
	private UtilConfig dozer;
	
	@RequestMapping(value = "/typeOfTariffs", method = RequestMethod.GET)
    public ResponseEntity<List<TypeOfTariffDTO>> typeOfTariffs(Model model) {
    	List<TypeOfTariffDTO> typeOfTariffs = typeOfTariffRepo.findAll();
    	
        return new ResponseEntity<>(typeOfTariffs,HttpStatus.OK);
    }
	
	
	// the simple table view for thymeleaf
	@RequestMapping(value = "/typeOfTariffGrid", method = RequestMethod.GET)
    public String typeOfTariff(Model model) {
		
		List<TypeOfTariffDTO> typeOfTariffs = typeOfTariffRepo.findAll();
    	
		List<GenericTable> tableItems = new ArrayList<>();
		
		for (TypeOfTariffDTO list : typeOfTariffs) {
			GenericTable item = new GenericTable();
			if (list.getTypeOfTariff().getDescription() != null)
				item.setDescription(list.getTypeOfTariff().getDescription().getValue());
			item.setFareFrameType(list.getFareFrameType());
			if (list.getName() != null)
				item.setName(list.getName());
			item.setMongoId(list.getMongoId());
			if (list.getTypeOfTariff().getName() != null)
				item.setName(list.getTypeOfTariff().getName().getValue());
			if (list.getOperator() != null)
				item.setObjectType(list.getOperator());
			if (list.getTypeOfTariff().getPrivateCode()!= null)
				item.setPrivateCode(list.getTypeOfTariff().getPrivateCode().getValue());
			item.setVersion(list.getTypeOfTariff().getVersion());
			
			tableItems.add(item);
		}
		
		model.addAttribute("tableItems",tableItems);
		
        ButtonEvents action = new ButtonEvents();
        action.setGetUrl("/typeOfTariffs");
        action.setModifyItemUrl("/typeOfTariff");
        action.setDeleteItemUrl("/typeOfTariff");
        model.addAttribute("action", action);
        
        
        return "/html/genericTable.html :: grid";
    }
	
	@RequestMapping(value = "/typeOfTariff", method = RequestMethod.GET)
    public String newTypeOfTariff(Model model,@RequestParam(required=false) String mongoId) {
    	
		Optional<TypeOfTariffDTO> typeOfTariff;
		
		if (mongoId != null && mongoId.trim().length() > 0) {
			typeOfTariff = typeOfTariffRepo.findById(mongoId);
		} else {
			typeOfTariff = Optional.of(new TypeOfTariffDTO());
			TypeOfTariff doc = new TypeOfTariff();
			typeOfTariff.get().setTypeOfTariff(doc);
			
			typeOfTariff.get().getTypeOfTariff().setId(dozer.generateNetexName("typeOfTariff"));
			typeOfTariff.get().getTypeOfTariff().setVersion("latest");
		}
				
        model.addAttribute("item", typeOfTariff);

        return "resourceFrame/typeOfTariff.html :: edit";
    }
	
	@RequestMapping(value = "/typeOfTariff", method = RequestMethod.POST)
    public String saveTypeOfTariff(@ModelAttribute TypeOfTariffDTO typeOfTariff, BindingResult errors, Model model) {
		
		typeOfTariff.setMongoId(dozer.generateMongoId(typeOfTariff.getTypeOfTariff().getId(), typeOfTariff.getTypeOfTariff().getVersion(), "typeOfTariff"));
		typeOfTariff.setId(typeOfTariff.getTypeOfTariff().getId());
		typeOfTariff.setVersion(typeOfTariff.getTypeOfTariff().getVersion());
		
		typeOfTariffRepo.save(typeOfTariff);
		
		return "index";
    }

	@RequestMapping(value = "/typeOfTariff", method = RequestMethod.DELETE)
    public String deleteTypeOfTariff(Model model,@RequestParam(required=true) String mongoId) {
		
		if (typeOfTariffRepo.findById(mongoId).isPresent()) {
			typeOfTariffRepo.deleteById(mongoId);
		} else {
			System.out.println("Missing "+ mongoId + " id");
		}
		
		return "index";
    }
}
