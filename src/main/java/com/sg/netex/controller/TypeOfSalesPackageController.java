package com.sg.netex.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.rutebanken.netex.model.TypeOfSalesPackage;
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
import com.sg.netex.dto.TypeOfSalesPackageDTO;
import com.sg.netex.model.ButtonEvents;
import com.sg.netex.model.GenericTable;
import com.sg.netex.repository.TypeOfSalesPackageRepository;

@Controller
public class TypeOfSalesPackageController {

	@Autowired
	TypeOfSalesPackageRepository typeOfSalesPackageRepo;

	@Autowired
	private UtilConfig dozer;
	
	@RequestMapping(value = "/typeOfSalesPackages", method = RequestMethod.GET)
    public ResponseEntity<List<TypeOfSalesPackageDTO>> typeOfSalesPackages(Model model) {
    	List<TypeOfSalesPackageDTO> typeOfSalesPackages = typeOfSalesPackageRepo.findAll();
    	
        return new ResponseEntity<>(typeOfSalesPackages,HttpStatus.OK);
    }
	
	
	// the simple table view for thymeleaf
	@RequestMapping(value = "/typeOfSalesPackageGrid", method = RequestMethod.GET)
    public String typeOfSalesPackage(Model model) {
		
		List<TypeOfSalesPackageDTO> typeOfSalesPackages = typeOfSalesPackageRepo.findAll();
    	
		List<GenericTable> tableItems = new ArrayList<>();
		
		for (TypeOfSalesPackageDTO list : typeOfSalesPackages) {
			GenericTable item = new GenericTable();
			if (list.getTypeOfSalesPackage().getDescription() != null)
				item.setDescription(list.getTypeOfSalesPackage().getDescription().getValue());
			item.setFareFrameType(list.getFareFrameType());
			if (list.getName() != null)
				item.setName(list.getName());
			item.setMongoId(list.getMongoId());
			if (list.getTypeOfSalesPackage().getName() != null)
				item.setName(list.getTypeOfSalesPackage().getName().getValue());
			if (list.getOperator() != null)
				item.setObjectType(list.getOperator());
			if (list.getTypeOfSalesPackage().getPrivateCode()!= null)
				item.setPrivateCode(list.getTypeOfSalesPackage().getPrivateCode().getValue());
			item.setVersion(list.getTypeOfSalesPackage().getVersion());
			
			tableItems.add(item);
		}
		
		model.addAttribute("tableItems",tableItems);
		
        ButtonEvents action = new ButtonEvents();
        action.setGetUrl("/typeOfSalesPackages");
        action.setModifyItemUrl("/typeOfSalesPackage");
        action.setDeleteItemUrl("/typeOfSalesPackage");
        model.addAttribute("action", action);
        
        
        return "/html/genericTable.html :: grid";
    }
	
	@RequestMapping(value = "/typeOfSalesPackage", method = RequestMethod.GET)
    public String newTypeOfSalesPackage(Model model,@RequestParam(required=false) String mongoId) {
    	
		Optional<TypeOfSalesPackageDTO> typeOfSalesPackage;
		
		if (mongoId != null && mongoId.trim().length() > 0) {
			typeOfSalesPackage = typeOfSalesPackageRepo.findById(mongoId);
		} else {
			typeOfSalesPackage = Optional.of(new TypeOfSalesPackageDTO());
			TypeOfSalesPackage doc = new TypeOfSalesPackage();
			typeOfSalesPackage.get().setTypeOfSalesPackage(doc);
			
			typeOfSalesPackage.get().getTypeOfSalesPackage().setId(dozer.generateNetexName("typeOfSalesPackage"));
			typeOfSalesPackage.get().getTypeOfSalesPackage().setVersion("latest");
		}
				
        model.addAttribute("item", typeOfSalesPackage);

        return "resourceFrame/typeOfSalesPackage.html :: edit";
    }
	
	@RequestMapping(value = "/typeOfSalesPackage", method = RequestMethod.POST)
    public String saveTypeOfSalesPackage(@ModelAttribute TypeOfSalesPackageDTO typeOfSalesPackage, BindingResult errors, Model model) {
		
		typeOfSalesPackage.setMongoId(dozer.generateMongoId(typeOfSalesPackage.getTypeOfSalesPackage().getId(), typeOfSalesPackage.getTypeOfSalesPackage().getVersion(), "typeOfSalesPackage"));
		typeOfSalesPackage.setId(typeOfSalesPackage.getTypeOfSalesPackage().getId());
		typeOfSalesPackage.setVersion(typeOfSalesPackage.getTypeOfSalesPackage().getVersion());
		
		typeOfSalesPackageRepo.save(typeOfSalesPackage);
		
		return "index";
    }

	@RequestMapping(value = "/typeOfSalesPackage", method = RequestMethod.DELETE)
    public String deleteTypeOfSalesPackage(Model model,@RequestParam(required=true) String mongoId) {
		
		if (typeOfSalesPackageRepo.findById(mongoId).isPresent()) {
			typeOfSalesPackageRepo.deleteById(mongoId);
		} else {
			System.out.println("Missing "+ mongoId + " id");
		}
		
		return "index";
    }
}
