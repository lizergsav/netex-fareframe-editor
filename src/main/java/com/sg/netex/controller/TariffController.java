package com.sg.netex.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.rutebanken.netex.model.Tariff;
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
import com.sg.netex.dto.TariffDTO;
import com.sg.netex.model.ButtonEvents;
import com.sg.netex.model.GenericTable;
import com.sg.netex.repository.TariffRepository;

@Controller
public class TariffController {

	@Autowired
	TariffRepository tariffRepo;

	@Autowired
	private UtilConfig dozer;
	
	@RequestMapping(value = "/tariffs", method = RequestMethod.GET)
    public ResponseEntity<List<TariffDTO>> tariffs(Model model) {
    	List<TariffDTO> tariffs = tariffRepo.findAll();
    	
        return new ResponseEntity<>(tariffs,HttpStatus.OK);
    }
	
	
	// the simple table view for thymeleaf
	@RequestMapping(value = "/tariffGrid", method = RequestMethod.GET)
    public String tariff(Model model) {
		
		List<TariffDTO> tariffs = tariffRepo.findAll();
    	
		List<GenericTable> tableItems = new ArrayList<>();
		
		for (TariffDTO list : tariffs) {
			GenericTable item = new GenericTable();
			item.setFareFrameType(list.getFareFrameType());
			if (list.getName() != null)
				item.setName(list.getName());
			item.setMongoId(list.getMongoId());
			if (list.getTariff().getName() != null)
				item.setName(list.getTariff().getName().getValue());
			if (list.getOperator() != null)
				item.setObjectType(list.getOperator());

			item.setVersion(list.getTariff().getVersion());
			
			tableItems.add(item);
		}
		
		model.addAttribute("tableItems",tableItems);
		
        ButtonEvents action = new ButtonEvents();
        action.setGetUrl("/tariffs");
        action.setModifyItemUrl("/tariff");
        action.setDeleteItemUrl("/tariff");
        model.addAttribute("action", action);
        
        
        return "/html/simpleTable.html :: grid";
    }
	
	@RequestMapping(value = "/tariff", method = RequestMethod.GET)
    public String newTariff(Model model,@RequestParam(required=false) String mongoId) {
    	
		Optional<TariffDTO> tariff;
		
		if (mongoId != null && mongoId.trim().length() > 0) {
			tariff = tariffRepo.findById(mongoId);
		} else {
			tariff = Optional.of(new TariffDTO());
			Tariff doc = new Tariff();
			tariff.get().setTariff(doc);
			
			tariff.get().getTariff().setId(dozer.generateNetexName("tariff"));
			tariff.get().getTariff().setVersion("latest");
		}
				
        model.addAttribute("item", tariff);

        return "fares/tariff.html :: edit";
    }
	
	@RequestMapping(value = "/tariff", method = RequestMethod.POST)
    public String saveTariff(@ModelAttribute TariffDTO tariff, BindingResult errors, Model model) {
		
		tariff.setMongoId(dozer.generateMongoId(tariff.getTariff().getId(), tariff.getTariff().getVersion(), "tariff"));
		tariff.setId(tariff.getTariff().getId());
		tariff.setVersion(tariff.getTariff().getVersion());
		
		tariffRepo.save(tariff);
		
		return "index";
    }

	@RequestMapping(value = "/tariff", method = RequestMethod.DELETE)
    public String deleteTariff(Model model,@RequestParam(required=true) String mongoId) {
		
		if (tariffRepo.findById(mongoId).isPresent()) {
			tariffRepo.deleteById(mongoId);
		} else {
			System.out.println("Missing "+ mongoId + " id");
		}
		
		return "index";
    }
}
