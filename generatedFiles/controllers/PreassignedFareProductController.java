package com.sg.netex.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.rutebanken.netex.model.PreassignedFareProduct;
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
import com.sg.netex.dto.PreassignedFareProductDTO;
import com.sg.netex.model.ButtonEvents;
import com.sg.netex.model.GenericTable;
import com.sg.netex.repository.PreassignedFareProductRepository;

@Controller
public class PreassignedFareProductController {

	@Autowired
	PreassignedFareProductRepository preassignedFareProductRepo;

	@Autowired
	private UtilConfig dozer;
	
	@RequestMapping(value = "/preassignedFareProducts", method = RequestMethod.GET)
    public ResponseEntity<List<PreassignedFareProductDTO>> preassignedFareProducts(Model model) {
    	List<PreassignedFareProductDTO> preassignedFareProducts = preassignedFareProductRepo.findAll();
    	
        return new ResponseEntity<>(preassignedFareProducts,HttpStatus.OK);
    }
	
	
	// the simple table view for thymeleaf
	@RequestMapping(value = "/preassignedFareProductGrid", method = RequestMethod.GET)
    public String preassignedFareProduct(Model model) {
		
		List<PreassignedFareProductDTO> preassignedFareProducts = preassignedFareProductRepo.findAll();
    	
		List<GenericTable> tableItems = new ArrayList<>();
		
		for (PreassignedFareProductDTO list : preassignedFareProducts) {
			GenericTable item = new GenericTable();
			item.setFareFrameType(list.getFareFrameType());
			if (list.getName() != null)
				item.setName(list.getName());
			item.setMongoId(list.getMongoId());
			if (list.getPreassignedFareProduct().getName() != null)
				item.setName(list.getPreassignedFareProduct().getName().getValue());
			if (list.getOperator() != null)
				item.setObjectType(list.getOperator());

			item.setVersion(list.getPreassignedFareProduct().getVersion());
			
			tableItems.add(item);
		}
		
		model.addAttribute("tableItems",tableItems);
		
        ButtonEvents action = new ButtonEvents();
        action.setGetUrl("/preassignedFareProducts");
        action.setModifyItemUrl("/preassignedFareProduct");
        action.setDeleteItemUrl("/preassignedFareProduct");
        model.addAttribute("action", action);
        
        
        return "/html/simpleTable.html :: grid";
    }
	
	@RequestMapping(value = "/preassignedFareProduct", method = RequestMethod.GET)
    public String newPreassignedFareProduct(Model model,@RequestParam(required=false) String mongoId) {
    	
		Optional<PreassignedFareProductDTO> preassignedFareProduct;
		
		if (mongoId != null && mongoId.trim().length() > 0) {
			preassignedFareProduct = preassignedFareProductRepo.findById(mongoId);
		} else {
			preassignedFareProduct = Optional.of(new PreassignedFareProductDTO());
			PreassignedFareProduct doc = new PreassignedFareProduct();
			preassignedFareProduct.get().setPreassignedFareProduct(doc);
			
			preassignedFareProduct.get().getPreassignedFareProduct().setId(dozer.generateNetexName("preassignedFareProduct"));
			preassignedFareProduct.get().getPreassignedFareProduct().setVersion("latest");
		}
				
        model.addAttribute("item", preassignedFareProduct);

        return "fares/preassignedFareProduct.html :: edit";
    }
	
	@RequestMapping(value = "/preassignedFareProduct", method = RequestMethod.POST)
    public String savePreassignedFareProduct(@ModelAttribute PreassignedFareProductDTO preassignedFareProduct, BindingResult errors, Model model) {
		
		preassignedFareProduct.setMongoId(dozer.generateMongoId(preassignedFareProduct.getPreassignedFareProduct().getId(), preassignedFareProduct.getPreassignedFareProduct().getVersion(), "preassignedFareProduct"));
		preassignedFareProduct.setId(preassignedFareProduct.getPreassignedFareProduct().getId());
		preassignedFareProduct.setVersion(preassignedFareProduct.getPreassignedFareProduct().getVersion());
		
		preassignedFareProductRepo.save(preassignedFareProduct);
		
		return "index";
    }

	@RequestMapping(value = "/preassignedFareProduct", method = RequestMethod.DELETE)
    public String deletePreassignedFareProduct(Model model,@RequestParam(required=true) String mongoId) {
		
		if (preassignedFareProductRepo.findById(mongoId).isPresent()) {
			preassignedFareProductRepo.deleteById(mongoId);
		} else {
			System.out.println("Missing "+ mongoId + " id");
		}
		
		return "index";
    }
}
