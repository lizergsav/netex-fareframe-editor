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
	TypeOfProductCategoryRepository typeOfProductCategoryRepo;

	@Autowired
	private UtilConfig dozer;
	
	@RequestMapping(value = "/typeOfProductCategorys", method = RequestMethod.GET)
    public ResponseEntity<List<TypeOfProductCategoryDTO>> typeOfProductCategorys(Model model) {
    	List<TypeOfProductCategoryDTO> typeOfProductCategorys = typeOfProductCategoryRepo.findAll();
    	
        return new ResponseEntity<>(typeOfProductCategorys,HttpStatus.OK);
    }
	
	
	// the simple table view for thymeleaf
	@RequestMapping(value = "/typeOfProductCategoryGrid", method = RequestMethod.GET)
    public String typeOfProductCategory(Model model) {
		
		List<TypeOfProductCategoryDTO> typeOfProductCategorys = typeOfProductCategoryRepo.findAll();
    	
		List<GenericTable> tableItems = new ArrayList<>();
		
		for (TypeOfProductCategoryDTO list : typeOfProductCategorys) {
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
        action.setGetUrl("/typeOfProductCategorys");
        action.setModifyItemUrl("/typeOfProductCategory");
        action.setDeleteItemUrl("/typeOfProductCategory");
        model.addAttribute("action", action);
        
        
        return "/html/genericTable.html :: grid";
    }
	
	@RequestMapping(value = "/typeOfProductCategory", method = RequestMethod.GET)
    public String newTypeOfProductCategory(Model model,@RequestParam(required=false) String mongoId) {
    	
		Optional<TypeOfProductCategoryDTO> typeOfProductCategory;
		
		if (mongoId != null && mongoId.trim().length() > 0) {
			typeOfProductCategory = typeOfProductCategoryRepo.findById(mongoId);
		} else {
			typeOfProductCategory = Optional.of(new TypeOfProductCategoryDTO());
			TypeOfProductCategory doc = new TypeOfProductCategory();
			typeOfProductCategory.get().setTypeOfProductCategory(doc);
			
			typeOfProductCategory.get().getTypeOfProductCategory().setId(dozer.generateNetexName("typeOfProductCategory"));
			typeOfProductCategory.get().getTypeOfProductCategory().setVersion("latest");
		}
				
        model.addAttribute("item", typeOfProductCategory);

        return "resourceFrame/typeOfProductCategory.html :: edit";
    }
	
	@RequestMapping(value = "/typeOfProductCategory", method = RequestMethod.POST)
    public String saveTypeOfProductCategory(@ModelAttribute TypeOfProductCategoryDTO typeOfProductCategory, BindingResult errors, Model model) {
		
		typeOfProductCategory.setMongoId(dozer.generateMongoId(typeOfProductCategory.getTypeOfProductCategory().getId(), typeOfProductCategory.getTypeOfProductCategory().getVersion(), "typeOfProductCategory"));
		typeOfProductCategory.setId(typeOfProductCategory.getTypeOfProductCategory().getId());
		typeOfProductCategory.setVersion(typeOfProductCategory.getTypeOfProductCategory().getVersion());
		
		typeOfProductCategoryRepo.save(typeOfProductCategory);
		
		return "index";
    }

	@RequestMapping(value = "/typeOfProductCategory", method = RequestMethod.DELETE)
    public String deleteTypeOfProductCategory(Model model,@RequestParam(required=true) String mongoId) {
		
		if (typeOfProductCategoryRepo.findById(mongoId).isPresent()) {
			typeOfProductCategoryRepo.deleteById(mongoId);
		} else {
			System.out.println("Missing "+ mongoId + " id");
		}
		
		return "index";
    }
}
