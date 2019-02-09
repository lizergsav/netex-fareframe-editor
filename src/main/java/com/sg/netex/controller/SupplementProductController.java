package com.sg.netex.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.rutebanken.netex.model.SupplementProduct;
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
import com.sg.netex.dto.SupplementProductDTO;
import com.sg.netex.model.ButtonEvents;
import com.sg.netex.model.GenericTable;
import com.sg.netex.repository.SupplementProductRepository;

@Controller
public class SupplementProductController {

	@Autowired
	SupplementProductRepository supplementProductRepo;

	@Autowired
	private UtilConfig dozer;
	
	@RequestMapping(value = "/supplementProducts", method = RequestMethod.GET)
    public ResponseEntity<List<SupplementProductDTO>> supplementProducts(Model model) {
    	List<SupplementProductDTO> supplementProducts = supplementProductRepo.findAll();
    	
        return new ResponseEntity<>(supplementProducts,HttpStatus.OK);
    }
	
	
	// the simple table view for thymeleaf
	@RequestMapping(value = "/supplementProductGrid", method = RequestMethod.GET)
    public String supplementProduct(Model model) {
		
		List<SupplementProductDTO> supplementProducts = supplementProductRepo.findAll();
    	
		List<GenericTable> tableItems = new ArrayList<>();
		
		for (SupplementProductDTO list : supplementProducts) {
			GenericTable item = new GenericTable();
			item.setFareFrameType(list.getFareFrameType());
			if (list.getName() != null)
				item.setName(list.getName());
			item.setMongoId(list.getMongoId());
			if (list.getSupplementProduct().getName() != null)
				item.setName(list.getSupplementProduct().getName().getValue());
			if (list.getOperator() != null)
				item.setObjectType(list.getOperator());

			item.setVersion(list.getSupplementProduct().getVersion());
			
			tableItems.add(item);
		}
		
		model.addAttribute("tableItems",tableItems);
		
        ButtonEvents action = new ButtonEvents();
        action.setGetUrl("/supplementProducts");
        action.setModifyItemUrl("/supplementProduct");
        action.setDeleteItemUrl("/supplementProduct");
        model.addAttribute("action", action);
        
        
        return "/html/simpleTable.html :: grid";
    }
	
	@RequestMapping(value = "/supplementProduct", method = RequestMethod.GET)
    public String newSupplementProduct(Model model,@RequestParam(required=false) String mongoId) {
    	
		Optional<SupplementProductDTO> supplementProduct;
		
		if (mongoId != null && mongoId.trim().length() > 0) {
			supplementProduct = supplementProductRepo.findById(mongoId);
		} else {
			supplementProduct = Optional.of(new SupplementProductDTO());
			SupplementProduct doc = new SupplementProduct();
			supplementProduct.get().setSupplementProduct(doc);
			
			supplementProduct.get().getSupplementProduct().setId(dozer.generateNetexName("supplementProduct"));
			supplementProduct.get().getSupplementProduct().setVersion("latest");
		}
				
        model.addAttribute("item", supplementProduct);

        return "fares/supplementProduct.html :: edit";
    }
	
	@RequestMapping(value = "/supplementProduct", method = RequestMethod.POST)
    public String saveSupplementProduct(@ModelAttribute SupplementProductDTO supplementProduct, BindingResult errors, Model model) {
		
		supplementProduct.setMongoId(dozer.generateMongoId(supplementProduct.getSupplementProduct().getId(), supplementProduct.getSupplementProduct().getVersion(), "supplementProduct"));
		supplementProduct.setId(supplementProduct.getSupplementProduct().getId());
		supplementProduct.setVersion(supplementProduct.getSupplementProduct().getVersion());
		
		supplementProductRepo.save(supplementProduct);
		
		return "index";
    }

	@RequestMapping(value = "/supplementProduct", method = RequestMethod.DELETE)
    public String deleteSupplementProduct(Model model,@RequestParam(required=true) String mongoId) {
		
		if (supplementProductRepo.findById(mongoId).isPresent()) {
			supplementProductRepo.deleteById(mongoId);
		} else {
			System.out.println("Missing "+ mongoId + " id");
		}
		
		return "index";
    }
}
