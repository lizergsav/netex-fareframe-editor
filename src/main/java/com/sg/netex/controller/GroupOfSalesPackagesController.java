package com.sg.netex.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.rutebanken.netex.model.GroupOfSalesPackages;
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
import com.sg.netex.dto.GroupOfSalesPackagesDTO;
import com.sg.netex.model.ButtonEvents;
import com.sg.netex.model.GenericTable;
import com.sg.netex.repository.GroupOfSalesPackagesRepository;

@Controller
public class GroupOfSalesPackagesController {

	@Autowired
	GroupOfSalesPackagesRepository groupOfSalesPackagesRepo;

	@Autowired
	private UtilConfig dozer;
	
	@RequestMapping(value = "/groupOfSalesPackagess", method = RequestMethod.GET)
    public ResponseEntity<List<GroupOfSalesPackagesDTO>> groupOfSalesPackagess(Model model) {
    	List<GroupOfSalesPackagesDTO> groupOfSalesPackagess = groupOfSalesPackagesRepo.findAll();
    	
        return new ResponseEntity<>(groupOfSalesPackagess,HttpStatus.OK);
    }
	
	
	// the simple table view for thymeleaf
	@RequestMapping(value = "/groupOfSalesPackagesGrid", method = RequestMethod.GET)
    public String groupOfSalesPackages(Model model) {
		
		List<GroupOfSalesPackagesDTO> groupOfSalesPackagess = groupOfSalesPackagesRepo.findAll();
    	
		List<GenericTable> tableItems = new ArrayList<>();
		
		for (GroupOfSalesPackagesDTO list : groupOfSalesPackagess) {
			GenericTable item = new GenericTable();
			item.setFareFrameType(list.getFareFrameType());
			if (list.getName() != null)
				item.setName(list.getName());
			item.setMongoId(list.getMongoId());
			if (list.getGroupOfSalesPackages().getName() != null)
				item.setName(list.getGroupOfSalesPackages().getName().getValue());
			if (list.getOperator() != null)
				item.setObjectType(list.getOperator());

			item.setVersion(list.getGroupOfSalesPackages().getVersion());
			
			tableItems.add(item);
		}
		
		model.addAttribute("tableItems",tableItems);
		
        ButtonEvents action = new ButtonEvents();
        action.setGetUrl("/groupOfSalesPackagess");
        action.setModifyItemUrl("/groupOfSalesPackages");
        action.setDeleteItemUrl("/groupOfSalesPackages");
        model.addAttribute("action", action);
        
        
        return "/html/simpleTable.html :: grid";
    }
	
	@RequestMapping(value = "/groupOfSalesPackages", method = RequestMethod.GET)
    public String newGroupOfSalesPackages(Model model,@RequestParam(required=false) String mongoId) {
    	
		Optional<GroupOfSalesPackagesDTO> groupOfSalesPackages;
		
		if (mongoId != null && mongoId.trim().length() > 0) {
			groupOfSalesPackages = groupOfSalesPackagesRepo.findById(mongoId);
		} else {
			groupOfSalesPackages = Optional.of(new GroupOfSalesPackagesDTO());
			GroupOfSalesPackages doc = new GroupOfSalesPackages();
			groupOfSalesPackages.get().setGroupOfSalesPackages(doc);
			
			groupOfSalesPackages.get().getGroupOfSalesPackages().setId(dozer.generateNetexName("groupOfSalesPackages"));
			groupOfSalesPackages.get().getGroupOfSalesPackages().setVersion("latest");
		}
				
        model.addAttribute("item", groupOfSalesPackages);

        return "fares/groupOfSalesPackages.html :: edit";
    }
	
	@RequestMapping(value = "/groupOfSalesPackages", method = RequestMethod.POST)
    public String saveGroupOfSalesPackages(@ModelAttribute GroupOfSalesPackagesDTO groupOfSalesPackages, BindingResult errors, Model model) {
		
		groupOfSalesPackages.setMongoId(dozer.generateMongoId(groupOfSalesPackages.getGroupOfSalesPackages().getId(), groupOfSalesPackages.getGroupOfSalesPackages().getVersion(), "groupOfSalesPackages"));
		groupOfSalesPackages.setId(groupOfSalesPackages.getGroupOfSalesPackages().getId());
		groupOfSalesPackages.setVersion(groupOfSalesPackages.getGroupOfSalesPackages().getVersion());
		
		groupOfSalesPackagesRepo.save(groupOfSalesPackages);
		
		return "index";
    }

	@RequestMapping(value = "/groupOfSalesPackages", method = RequestMethod.DELETE)
    public String deleteGroupOfSalesPackages(Model model,@RequestParam(required=true) String mongoId) {
		
		if (groupOfSalesPackagesRepo.findById(mongoId).isPresent()) {
			groupOfSalesPackagesRepo.deleteById(mongoId);
		} else {
			System.out.println("Missing "+ mongoId + " id");
		}
		
		return "index";
    }
}
