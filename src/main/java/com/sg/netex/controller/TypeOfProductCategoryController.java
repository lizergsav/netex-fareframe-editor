package com.sg.netex.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.rutebanken.netex.model.TypeOfProductCategory;
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
import com.sg.netex.dto.TypeOfProductCategoryDTO;
import com.sg.netex.model.ButtonEvents;
import com.sg.netex.model.GenericTable;
import com.sg.netex.repository.TypeOfProductCategoryRepository;

@Controller
public class TypeOfProductCategoryController {

	@Autowired
	private UtilConfig dozer;

	@Autowired
	TypeOfProductCategoryRepository typeOfProductCategoryRepository; 

	@RequestMapping(value = "/typeOfProductCategories", method = RequestMethod.GET)
    public ResponseEntity<List<TypeOfProductCategoryDTO>> typeOfProductCategories(Model model) {
    	List<TypeOfProductCategoryDTO> items = typeOfProductCategoryRepository.findAll();
    	
        return new ResponseEntity<>(items,HttpStatus.OK);
    }
	
	//ProductCategory
	@RequestMapping(value = "/typeOfProductCategoryGrid", method = RequestMethod.GET)
    public String typeOfProductCategory(Model model) {
		
		List<TypeOfProductCategoryDTO> types = typeOfProductCategoryRepository.findAll();
    	
		List<GenericTable> tableItems = new ArrayList<>();
		
		for (TypeOfProductCategoryDTO list : types) {
			GenericTable item = new GenericTable();
			if (list.getTypeOfProductCategory().getDescription() != null)
				item.setDescription(list.getTypeOfProductCategory().getDescription().getValue());
			item.setFareFrameType(list.getFareFrameType());
			if (list.getName() != null)
				item.setName(list.getName());
			item.setMongoId(list.getMongoId());
			if (list.getTypeOfProductCategory().getName() != null)
				item.setName(list.getTypeOfProductCategory().getName().getValue());
			if (list.getOperator() != null)
				item.setObjectType(list.getOperator());
			if (list.getTypeOfProductCategory().getPrivateCode()!= null)
				item.setPrivateCode(list.getTypeOfProductCategory().getPrivateCode().getValue());
			item.setVersion(list.getTypeOfProductCategory().getVersion());
			
			tableItems.add(item);
		}
		
		model.addAttribute("tableItems",tableItems);
		
        ButtonEvents action = new ButtonEvents();
        action.setGetUrl("/typeOfProductCategories");
        action.setModifyItemUrl("/typeOfProductCategory");
        action.setDeleteItemUrl("/typeOfProductCategory");
        model.addAttribute("action", action);
        
        
        return "/html/genericTable.html :: grid";
    }
	
	@RequestMapping(value = "/typeOfProductCategory", method = RequestMethod.GET)
    public String manageTypeOfProductCategory(Model model,@RequestParam(required=false) String mongoId) {
    	
		Optional<TypeOfProductCategoryDTO> item;
		
		if (mongoId != null && mongoId.trim().length() > 0) {
			 item = typeOfProductCategoryRepository.findById(mongoId);
		} else {
			item = Optional.of(new TypeOfProductCategoryDTO());
			TypeOfProductCategory doc = new TypeOfProductCategory();
			item.get().setTypeOfProductCategory(doc);
			
			item.get().getTypeOfProductCategory().setId(dozer.generateNetexName("typeOfProductCategory"));
			item.get().getTypeOfProductCategory().setVersion("latest");
		}
				
        model.addAttribute("item", item);

        return "resourceFrame/typeOfProductCategory.html :: edit";
    }
	
	@RequestMapping(value = "/typeOfProductCategory", method = RequestMethod.POST)
    public String saveTypeOfProductCategory(@ModelAttribute TypeOfProductCategoryDTO input, BindingResult errors, Model model) {
		
		input.setMongoId(dozer.generateMongoId(input.getTypeOfProductCategory().getId(), input.getTypeOfProductCategory().getVersion(), "typeOfProductCategory"));
		input.setId(input.getTypeOfProductCategory().getId());
		input.setVersion(input.getTypeOfProductCategory().getVersion());
		
		
		typeOfProductCategoryRepository.save(input);
		
		return "index.html";
    }

	@RequestMapping(value = "/typeOfProductCategory", method = RequestMethod.DELETE)
    public String deletetypeOfProductCategory(Model model,@RequestParam(required=true) String mongoId) {
		
		if (typeOfProductCategoryRepository.findById(mongoId).isPresent()) {
			typeOfProductCategoryRepository.deleteById(mongoId);
		} else {
			System.out.println("Missing "+ mongoId + " id");
		}
		
		return "html/deleted";
    }
	
}
