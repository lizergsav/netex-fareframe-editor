package com.sg.netex.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.rutebanken.netex.model.TypeOfProductCategory;
import org.rutebanken.netex.model.TypeOfService;
import org.rutebanken.netex.model.TypeOfTravelDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sg.netex.config.UtilConfig;
import com.sg.netex.dto.TypeOfProductCategoryDTO;
import com.sg.netex.dto.TypeOfServiceDTO;
import com.sg.netex.dto.TypeOfTravelDocumentDTO;
import com.sg.netex.model.ButtonEvents;
import com.sg.netex.model.GenericTable;
import com.sg.netex.repository.TypeOfProductCategoryRepository;
import com.sg.netex.repository.TypeOfServiceRepository;
import com.sg.netex.repository.TypeOfTravelDocumentRepository;

@Controller
public class ResourceFrameThymeleafController {

	@Autowired
	TypeOfTravelDocumentRepository typeOfTravelDocumentRepo;

	@Autowired
	TypeOfServiceRepository typeOfServiceRepository; 
	
	@Autowired
	TypeOfProductCategoryRepository typeOfProductCategoryRepository; 
	
	@Autowired
	private UtilConfig dozer;
	
	// the simple table view for thymeleaf
	@RequestMapping(value = "/typeOfTravelDocument", method = RequestMethod.GET)
    public String typeOfTravelDocument(Model model) {
		
		List<TypeOfTravelDocumentDTO> typeOfTravelDocuments = typeOfTravelDocumentRepo.findAll();
    	
		List<GenericTable> tableItems = new ArrayList<>();
		
		for (TypeOfTravelDocumentDTO list : typeOfTravelDocuments) {
			GenericTable item = new GenericTable();
			if (list.getTypeOfTravelDocument().getDescription() != null)
				item.setDescription(list.getTypeOfTravelDocument().getDescription().getValue());
			item.setFareFrameType(list.getFareFrameType());
			if (list.getName() != null)
				item.setName(list.getName());
			item.setMongoId(list.getMongoId());
			if (list.getTypeOfTravelDocument().getName() != null)
				item.setName(list.getTypeOfTravelDocument().getName().getValue());
			if (list.getOperator() != null)
				item.setObjectType(list.getOperator());
			if (list.getTypeOfTravelDocument().getPrivateCode()!= null)
				item.setPrivateCode(list.getTypeOfTravelDocument().getPrivateCode().getValue());
			item.setVersion(list.getTypeOfTravelDocument().getVersion());
			
			tableItems.add(item);
		}
		
		model.addAttribute("tableItems",tableItems);
		
        ButtonEvents action = new ButtonEvents();
        action.setGetUrl("/typeOfTravelDocuments");
        action.setModifyItemUrl("/manageTypeOfTravelDocument");
        model.addAttribute("action", action);
        
        
        return "/html/genericTable.html :: grid";
    }
	
	@RequestMapping(value = "/manageTypeOfTravelDocument", method = RequestMethod.GET)
    public String newTypeOfTravelDocument(Model model,@RequestParam(required=false) String mongoId) {
    	
		Optional<TypeOfTravelDocumentDTO> typeOfTravelDocument;
		
		if (mongoId != null && mongoId.trim().length() > 0) {
			 typeOfTravelDocument = typeOfTravelDocumentRepo.findById(mongoId);
		} else {
			typeOfTravelDocument = Optional.of(new TypeOfTravelDocumentDTO());
			TypeOfTravelDocument doc = new TypeOfTravelDocument();
			typeOfTravelDocument.get().setTypeOfTravelDocument(doc);
			
			typeOfTravelDocument.get().getTypeOfTravelDocument().setId(dozer.generateNetexName("typeOfTravelDocument"));
			typeOfTravelDocument.get().getTypeOfTravelDocument().setVersion("latest");
		}
				
        model.addAttribute("item", typeOfTravelDocument);

        return "resourceFrame/typeOfTravelDocument.html :: edit";
    }
	
	@RequestMapping(value = "/saveTypeOfTravelDocument", method = RequestMethod.POST)
    public String saveTypeOfTravelDocument(@ModelAttribute TypeOfTravelDocumentDTO typeOfTravelDocument, BindingResult errors, Model model) {
		
		typeOfTravelDocument.setMongoId(dozer.generateMongoId(typeOfTravelDocument.getTypeOfTravelDocument().getId(), typeOfTravelDocument.getTypeOfTravelDocument().getVersion(), "typeOfTravelDocument"));
		typeOfTravelDocument.setId(typeOfTravelDocument.getTypeOfTravelDocument().getId());
		typeOfTravelDocument.setVersion(typeOfTravelDocument.getTypeOfTravelDocument().getVersion());
		
		typeOfTravelDocumentRepo.save(typeOfTravelDocument);
		
		return "index.html";
    }

	
	//typeOfService
	@RequestMapping(value = "/typeOfService", method = RequestMethod.GET)
    public String typeOfService(Model model) {
		
		List<TypeOfServiceDTO> types = typeOfServiceRepository.findAll();
    	
		List<GenericTable> tableItems = new ArrayList<>();
		
		for (TypeOfServiceDTO list : types) {
			GenericTable item = new GenericTable();
			if (list.getTypeOfService().getDescription() != null)
				item.setDescription(list.getTypeOfService().getDescription().getValue());
			item.setFareFrameType(list.getFareFrameType());
			if (list.getName() != null)
				item.setName(list.getName());
			item.setMongoId(list.getMongoId());
			if (list.getTypeOfService().getName() != null)
				item.setName(list.getTypeOfService().getName().getValue());
			if (list.getOperator() != null)
				item.setObjectType(list.getOperator());
			if (list.getTypeOfService().getPrivateCode()!= null)
				item.setPrivateCode(list.getTypeOfService().getPrivateCode().getValue());
			item.setVersion(list.getTypeOfService().getVersion());
			
			tableItems.add(item);
		}
		
		model.addAttribute("tableItems",tableItems);
		
        ButtonEvents action = new ButtonEvents();
        action.setGetUrl("/typeOfServices");
        action.setModifyItemUrl("/manageTypeOfService");
        model.addAttribute("action", action);
        
        
        return "/html/genericTable.html :: grid";
    }
	
	@RequestMapping(value = "/manageTypeOfService", method = RequestMethod.GET)
    public String manageTypeOfService(Model model,@RequestParam(required=false) String mongoId) {
    	
		Optional<TypeOfServiceDTO> item;
		
		if (mongoId != null && mongoId.trim().length() > 0) {
			 item = typeOfServiceRepository.findById(mongoId);
		} else {
			item = Optional.of(new TypeOfServiceDTO());
			TypeOfService doc = new TypeOfService();
			item.get().setTypeOfService(doc);
			
			item.get().getTypeOfService().setId(dozer.generateNetexName("typeOfService"));
			item.get().getTypeOfService().setVersion("latest");
		}
				
        model.addAttribute("item", item);

        return "resourceFrame/typeOfService.html :: edit";
    }
	
	@RequestMapping(value = "/saveTypeOfService", method = RequestMethod.POST)
    public String saveTypeOfService(@ModelAttribute TypeOfServiceDTO input, BindingResult errors, Model model) {
		
		input.setMongoId(dozer.generateMongoId(input.getTypeOfService().getId(), input.getTypeOfService().getVersion(), "typeOfService"));
		input.setId(input.getTypeOfService().getId());
		input.setVersion(input.getTypeOfService().getVersion());
		
		typeOfServiceRepository.save(input);
		
		return "index.html";
    }

	//ProductCategory
	@RequestMapping(value = "/typeOfProductCategory", method = RequestMethod.GET)
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
        action.setModifyItemUrl("/manageTypeOfProductCategory");
        model.addAttribute("action", action);
        
        
        return "/html/genericTable.html :: grid";
    }
	
	@RequestMapping(value = "/manageTypeOfProductCategory", method = RequestMethod.GET)
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
	
	@RequestMapping(value = "/saveTypeOfProductCategory", method = RequestMethod.POST)
    public String saveTypeOfProductCategory(@ModelAttribute TypeOfProductCategoryDTO input, BindingResult errors, Model model) {
		
		input.setMongoId(dozer.generateMongoId(input.getTypeOfProductCategory().getId(), input.getTypeOfProductCategory().getVersion(), "typeOfProductCategory"));
		input.setId(input.getTypeOfProductCategory().getId());
		input.setVersion(input.getTypeOfProductCategory().getVersion());
		
		typeOfProductCategoryRepository.save(input);
		
		return "index.html";
    }
	
}