package com.sg.netex.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.rutebanken.netex.model.DistributionChannel;
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
import com.sg.netex.dto.DistributionChannelDTO;
import com.sg.netex.model.ButtonEvents;
import com.sg.netex.model.GenericTable;
import com.sg.netex.repository.DistributionChannelRepository;

@Controller
public class DistributionChannelController {

	@Autowired
	DistributionChannelRepository distributionChannelRepo;

	@Autowired
	private UtilConfig dozer;
	
	@RequestMapping(value = "/distributionChannels", method = RequestMethod.GET)
    public ResponseEntity<List<DistributionChannelDTO>> distributionChannels(Model model) {
    	List<DistributionChannelDTO> distributionChannels = distributionChannelRepo.findAll();
    	
        return new ResponseEntity<>(distributionChannels,HttpStatus.OK);
    }
	
	
	// the simple table view for thymeleaf
	@RequestMapping(value = "/distributionChannelGrid", method = RequestMethod.GET)
    public String distributionChannel(Model model) {
		
		List<DistributionChannelDTO> distributionChannels = distributionChannelRepo.findAll();
    	
		List<GenericTable> tableItems = new ArrayList<>();
		
		for (DistributionChannelDTO list : distributionChannels) {
			GenericTable item = new GenericTable();
			item.setFareFrameType(list.getFareFrameType());
			if (list.getName() != null)
				item.setName(list.getName());
			item.setMongoId(list.getMongoId());
			if (list.getDistributionChannel().getName() != null)
				item.setName(list.getDistributionChannel().getName().getValue());
			if (list.getOperator() != null)
				item.setObjectType(list.getOperator());

			item.setVersion(list.getDistributionChannel().getVersion());
			
			tableItems.add(item);
		}
		
		model.addAttribute("tableItems",tableItems);
		
        ButtonEvents action = new ButtonEvents();
        action.setGetUrl("/distributionChannels");
        action.setModifyItemUrl("/distributionChannel");
        action.setDeleteItemUrl("/distributionChannel");
        model.addAttribute("action", action);
        
        
        return "/html/simpleTable.html :: grid";
    }
	
	@RequestMapping(value = "/distributionChannel", method = RequestMethod.GET)
    public String newDistributionChannel(Model model,@RequestParam(required=false) String mongoId) {
    	
		Optional<DistributionChannelDTO> distributionChannel;
		
		if (mongoId != null && mongoId.trim().length() > 0) {
			distributionChannel = distributionChannelRepo.findById(mongoId);
		} else {
			distributionChannel = Optional.of(new DistributionChannelDTO());
			DistributionChannel doc = new DistributionChannel();
			distributionChannel.get().setDistributionChannel(doc);
			
			distributionChannel.get().getDistributionChannel().setId(dozer.generateNetexName("distributionChannel"));
			distributionChannel.get().getDistributionChannel().setVersion("latest");
		}
				
        model.addAttribute("item", distributionChannel);

        return "fares/distributionChannel.html :: edit";
    }
	
	@RequestMapping(value = "/distributionChannel", method = RequestMethod.POST)
    public String saveDistributionChannel(@ModelAttribute DistributionChannelDTO distributionChannel, BindingResult errors, Model model) {
		
		distributionChannel.setMongoId(dozer.generateMongoId(distributionChannel.getDistributionChannel().getId(), distributionChannel.getDistributionChannel().getVersion(), "distributionChannel"));
		distributionChannel.setId(distributionChannel.getDistributionChannel().getId());
		distributionChannel.setVersion(distributionChannel.getDistributionChannel().getVersion());
		
		distributionChannelRepo.save(distributionChannel);
		
		return "index";
    }

	@RequestMapping(value = "/distributionChannel", method = RequestMethod.DELETE)
    public String deleteDistributionChannel(Model model,@RequestParam(required=true) String mongoId) {
		
		if (distributionChannelRepo.findById(mongoId).isPresent()) {
			distributionChannelRepo.deleteById(mongoId);
		} else {
			System.out.println("Missing "+ mongoId + " id");
		}
		
		return "index";
    }
}
