package com.sg.netex.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.rutebanken.netex.model.SalesPackage;
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
import com.sg.netex.dto.SalesPackageDTO;
import com.sg.netex.model.ButtonEvents;
import com.sg.netex.model.GenericTable;
import com.sg.netex.repository.SalesPackageRepository;

@Controller
public class SalesPackageController {

	@Autowired
	SalesPackageRepository salesPackageRepo;

	@Autowired
	private UtilConfig dozer;
	
	@RequestMapping(value = "/salesPackages", method = RequestMethod.GET)
    public ResponseEntity<List<SalesPackageDTO>> salesPackages(Model model) {
    	List<SalesPackageDTO> salesPackages = salesPackageRepo.findAll();
    	
        return new ResponseEntity<>(salesPackages,HttpStatus.OK);
    }
	
	
	// the simple table view for thymeleaf
	@RequestMapping(value = "/salesPackageGrid", method = RequestMethod.GET)
    public String salesPackage(Model model) {
		
		List<SalesPackageDTO> salesPackages = salesPackageRepo.findAll();
    	
		List<GenericTable> tableItems = new ArrayList<>();
		
		for (SalesPackageDTO list : salesPackages) {
			GenericTable item = new GenericTable();
			item.setFareFrameType(list.getFareFrameType());
			if (list.getName() != null)
				item.setName(list.getName());
			item.setMongoId(list.getMongoId());
			if (list.getSalesPackage().getName() != null)
				item.setName(list.getSalesPackage().getName().getValue());
			if (list.getOperator() != null)
				item.setObjectType(list.getOperator());

			item.setVersion(list.getSalesPackage().getVersion());
			
			tableItems.add(item);
		}
		
		model.addAttribute("tableItems",tableItems);
		
        ButtonEvents action = new ButtonEvents();
        action.setGetUrl("/salesPackages");
        action.setModifyItemUrl("/salesPackage");
        action.setDeleteItemUrl("/salesPackage");
        model.addAttribute("action", action);
        
        
        return "/html/simpleTable.html :: grid";
    }
	
	@RequestMapping(value = "/salesPackage", method = RequestMethod.GET)
    public String newSalesPackage(Model model,@RequestParam(required=false) String mongoId) {
    	
		Optional<SalesPackageDTO> salesPackage;
		
		if (mongoId != null && mongoId.trim().length() > 0) {
			salesPackage = salesPackageRepo.findById(mongoId);
		} else {
			salesPackage = Optional.of(new SalesPackageDTO());
			SalesPackage doc = new SalesPackage();
			salesPackage.get().setSalesPackage(doc);
			
			salesPackage.get().getSalesPackage().setId(dozer.generateNetexName("salesPackage"));
			salesPackage.get().getSalesPackage().setVersion("latest");
		}
				
        model.addAttribute("item", salesPackage);

        return "fares/salesPackage.html :: edit";
    }
	
	@RequestMapping(value = "/salesPackage", method = RequestMethod.POST)
    public String saveSalesPackage(@ModelAttribute SalesPackageDTO salesPackage, BindingResult errors, Model model) {
		
		salesPackage.setMongoId(dozer.generateMongoId(salesPackage.getSalesPackage().getId(), salesPackage.getSalesPackage().getVersion(), "salesPackage"));
		salesPackage.setId(salesPackage.getSalesPackage().getId());
		salesPackage.setVersion(salesPackage.getSalesPackage().getVersion());
		
		salesPackageRepo.save(salesPackage);
		
		return "index";
    }

	@RequestMapping(value = "/salesPackage", method = RequestMethod.DELETE)
    public String deleteSalesPackage(Model model,@RequestParam(required=true) String mongoId) {
		
		if (salesPackageRepo.findById(mongoId).isPresent()) {
			salesPackageRepo.deleteById(mongoId);
		} else {
			System.out.println("Missing "+ mongoId + " id");
		}
		
		return "index";
    }
}
