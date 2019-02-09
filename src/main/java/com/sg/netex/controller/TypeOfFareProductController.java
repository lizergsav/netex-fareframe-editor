package com.sg.netex.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.rutebanken.netex.model.TypeOfFareProduct;
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
import com.sg.netex.dto.TypeOfFareProductDTO;
import com.sg.netex.model.ButtonEvents;
import com.sg.netex.model.GenericTable;
import com.sg.netex.repository.TypeOfFareProductRepository;

@Controller
public class TypeOfFareProductController {

	@Autowired
	TypeOfFareProductRepository typeOfFareProductRepo;

	@Autowired
	private UtilConfig dozer;
	
	@RequestMapping(value = "/typeOfFareProducts", method = RequestMethod.GET)
    public ResponseEntity<List<TypeOfFareProductDTO>> typeOfFareProducts(Model model) {
    	List<TypeOfFareProductDTO> typeOfFareProducts = typeOfFareProductRepo.findAll();
    	
        return new ResponseEntity<>(typeOfFareProducts,HttpStatus.OK);
    }
	
	
	// the simple table view for thymeleaf
	@RequestMapping(value = "/typeOfFareProductGrid", method = RequestMethod.GET)
    public String typeOfFareProduct(Model model) {
		
		List<TypeOfFareProductDTO> typeOfFareProducts = typeOfFareProductRepo.findAll();
    	
		List<GenericTable> tableItems = new ArrayList<>();
		
		for (TypeOfFareProductDTO list : typeOfFareProducts) {
			GenericTable item = new GenericTable();
			if (list.getTypeOfFareProduct().getDescription() != null)
				item.setDescription(list.getTypeOfFareProduct().getDescription().getValue());
			item.setFareFrameType(list.getFareFrameType());
			if (list.getName() != null)
				item.setName(list.getName());
			item.setMongoId(list.getMongoId());
			if (list.getTypeOfFareProduct().getName() != null)
				item.setName(list.getTypeOfFareProduct().getName().getValue());
			if (list.getOperator() != null)
				item.setObjectType(list.getOperator());
			if (list.getTypeOfFareProduct().getPrivateCode()!= null)
				item.setPrivateCode(list.getTypeOfFareProduct().getPrivateCode().getValue());
			item.setVersion(list.getTypeOfFareProduct().getVersion());
			
			tableItems.add(item);
		}
		
		model.addAttribute("tableItems",tableItems);
		
        ButtonEvents action = new ButtonEvents();
        action.setGetUrl("/typeOfFareProducts");
        action.setModifyItemUrl("/typeOfFareProduct");
        action.setDeleteItemUrl("/typeOfFareProduct");
        model.addAttribute("action", action);
        
        
        return "/html/genericTable.html :: grid";
    }
	
	@RequestMapping(value = "/typeOfFareProduct", method = RequestMethod.GET)
    public String newTypeOfFareProduct(Model model,@RequestParam(required=false) String mongoId) {
    	
		Optional<TypeOfFareProductDTO> typeOfFareProduct;
		
		if (mongoId != null && mongoId.trim().length() > 0) {
			typeOfFareProduct = typeOfFareProductRepo.findById(mongoId);
		} else {
			typeOfFareProduct = Optional.of(new TypeOfFareProductDTO());
			TypeOfFareProduct doc = new TypeOfFareProduct();
			typeOfFareProduct.get().setTypeOfFareProduct(doc);
			
			typeOfFareProduct.get().getTypeOfFareProduct().setId(dozer.generateNetexName("typeOfFareProduct"));
			typeOfFareProduct.get().getTypeOfFareProduct().setVersion("latest");
		}
				
        model.addAttribute("item", typeOfFareProduct);

        return "resourceFrame/typeOfFareProduct.html :: edit";
    }
	
	@RequestMapping(value = "/typeOfFareProduct", method = RequestMethod.POST)
    public String saveTypeOfFareProduct(@ModelAttribute TypeOfFareProductDTO typeOfFareProduct, BindingResult errors, Model model) {
		
		typeOfFareProduct.setMongoId(dozer.generateMongoId(typeOfFareProduct.getTypeOfFareProduct().getId(), typeOfFareProduct.getTypeOfFareProduct().getVersion(), "typeOfFareProduct"));
		typeOfFareProduct.setId(typeOfFareProduct.getTypeOfFareProduct().getId());
		typeOfFareProduct.setVersion(typeOfFareProduct.getTypeOfFareProduct().getVersion());
		
		typeOfFareProductRepo.save(typeOfFareProduct);
		
		return "index";
    }

	@RequestMapping(value = "/typeOfFareProduct", method = RequestMethod.DELETE)
    public String deleteTypeOfFareProduct(Model model,@RequestParam(required=true) String mongoId) {
		
		if (typeOfFareProductRepo.findById(mongoId).isPresent()) {
			typeOfFareProductRepo.deleteById(mongoId);
		} else {
			System.out.println("Missing "+ mongoId + " id");
		}
		
		return "index";
    }
}
