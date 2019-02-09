package com.sg.netex.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.rutebanken.netex.model.PurchaseWindow;
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
import com.sg.netex.dto.PurchaseWindowDTO;
import com.sg.netex.model.ButtonEvents;
import com.sg.netex.model.GenericTable;
import com.sg.netex.repository.PurchaseWindowRepository;

@Controller
public class PurchaseWindowController {

	@Autowired
	PurchaseWindowRepository purchaseWindowRepo;

	@Autowired
	private UtilConfig dozer;
	
	@RequestMapping(value = "/purchaseWindows", method = RequestMethod.GET)
    public ResponseEntity<List<PurchaseWindowDTO>> purchaseWindows(Model model) {
    	List<PurchaseWindowDTO> purchaseWindows = purchaseWindowRepo.findAll();
    	
        return new ResponseEntity<>(purchaseWindows,HttpStatus.OK);
    }
	
	
	// the simple table view for thymeleaf
	@RequestMapping(value = "/purchaseWindowGrid", method = RequestMethod.GET)
    public String purchaseWindow(Model model) {
		
		List<PurchaseWindowDTO> purchaseWindows = purchaseWindowRepo.findAll();
    	
		List<GenericTable> tableItems = new ArrayList<>();
		
		for (PurchaseWindowDTO list : purchaseWindows) {
			GenericTable item = new GenericTable();
			item.setFareFrameType(list.getFareFrameType());
			if (list.getName() != null)
				item.setName(list.getName());
			item.setMongoId(list.getMongoId());
			if (list.getPurchaseWindow().getName() != null)
				item.setName(list.getPurchaseWindow().getName().getValue());
			if (list.getOperator() != null)
				item.setObjectType(list.getOperator());

			item.setVersion(list.getPurchaseWindow().getVersion());
			
			tableItems.add(item);
		}
		
		model.addAttribute("tableItems",tableItems);
		
        ButtonEvents action = new ButtonEvents();
        action.setGetUrl("/purchaseWindows");
        action.setModifyItemUrl("/purchaseWindow");
        action.setDeleteItemUrl("/purchaseWindow");
        model.addAttribute("action", action);
        
        
        return "/html/simpleTable.html :: grid";
    }
	
	@RequestMapping(value = "/purchaseWindow", method = RequestMethod.GET)
    public String newPurchaseWindow(Model model,@RequestParam(required=false) String mongoId) {
    	
		Optional<PurchaseWindowDTO> purchaseWindow;
		
		if (mongoId != null && mongoId.trim().length() > 0) {
			purchaseWindow = purchaseWindowRepo.findById(mongoId);
		} else {
			purchaseWindow = Optional.of(new PurchaseWindowDTO());
			PurchaseWindow doc = new PurchaseWindow();
			purchaseWindow.get().setPurchaseWindow(doc);
			
			purchaseWindow.get().getPurchaseWindow().setId(dozer.generateNetexName("purchaseWindow"));
			purchaseWindow.get().getPurchaseWindow().setVersion("latest");
		}
				
        model.addAttribute("item", purchaseWindow);

        return "fares/purchaseWindow.html :: edit";
    }
	
	@RequestMapping(value = "/purchaseWindow", method = RequestMethod.POST)
    public String savePurchaseWindow(@ModelAttribute PurchaseWindowDTO purchaseWindow, BindingResult errors, Model model) {
		
		purchaseWindow.setMongoId(dozer.generateMongoId(purchaseWindow.getPurchaseWindow().getId(), purchaseWindow.getPurchaseWindow().getVersion(), "purchaseWindow"));
		purchaseWindow.setId(purchaseWindow.getPurchaseWindow().getId());
		purchaseWindow.setVersion(purchaseWindow.getPurchaseWindow().getVersion());
		
		purchaseWindowRepo.save(purchaseWindow);
		
		return "index";
    }

	@RequestMapping(value = "/purchaseWindow", method = RequestMethod.DELETE)
    public String deletePurchaseWindow(Model model,@RequestParam(required=true) String mongoId) {
		
		if (purchaseWindowRepo.findById(mongoId).isPresent()) {
			purchaseWindowRepo.deleteById(mongoId);
		} else {
			System.out.println("Missing "+ mongoId + " id");
		}
		
		return "index";
    }
}
