package com.sg.netex.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.rutebanken.netex.model.GroupOfDistributionChannels;
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
import com.sg.netex.dto.GroupOfDistributionChannelsDTO;
import com.sg.netex.model.ButtonEvents;
import com.sg.netex.model.GenericTable;
import com.sg.netex.repository.GroupOfDistributionChannelsRepository;

@Controller
public class GroupOfDistributionChannelsController {

	@Autowired
	GroupOfDistributionChannelsRepository groupOfDistributionChannelsRepo;

	@Autowired
	private UtilConfig dozer;
	
	@RequestMapping(value = "/groupOfDistributionChannelss", method = RequestMethod.GET)
    public ResponseEntity<List<GroupOfDistributionChannelsDTO>> groupOfDistributionChannelss(Model model) {
    	List<GroupOfDistributionChannelsDTO> groupOfDistributionChannelss = groupOfDistributionChannelsRepo.findAll();
    	
        return new ResponseEntity<>(groupOfDistributionChannelss,HttpStatus.OK);
    }
	
	
	// the simple table view for thymeleaf
	@RequestMapping(value = "/groupOfDistributionChannelsGrid", method = RequestMethod.GET)
    public String groupOfDistributionChannels(Model model) {
		
		List<GroupOfDistributionChannelsDTO> groupOfDistributionChannelss = groupOfDistributionChannelsRepo.findAll();
    	
		List<GenericTable> tableItems = new ArrayList<>();
		
		for (GroupOfDistributionChannelsDTO list : groupOfDistributionChannelss) {
			GenericTable item = new GenericTable();
			item.setFareFrameType(list.getFareFrameType());
			if (list.getName() != null)
				item.setName(list.getName());
			item.setMongoId(list.getMongoId());
			if (list.getGroupOfDistributionChannels().getName() != null)
				item.setName(list.getGroupOfDistributionChannels().getName().getValue());
			if (list.getOperator() != null)
				item.setObjectType(list.getOperator());

			item.setVersion(list.getGroupOfDistributionChannels().getVersion());
			
			tableItems.add(item);
		}
		
		model.addAttribute("tableItems",tableItems);
		
        ButtonEvents action = new ButtonEvents();
        action.setGetUrl("/groupOfDistributionChannelss");
        action.setModifyItemUrl("/groupOfDistributionChannels");
        action.setDeleteItemUrl("/groupOfDistributionChannels");
        model.addAttribute("action", action);
        
        
        return "/html/simpleTable.html :: grid";
    }
	
	@RequestMapping(value = "/groupOfDistributionChannels", method = RequestMethod.GET)
    public String newGroupOfDistributionChannels(Model model,@RequestParam(required=false) String mongoId) {
    	
		Optional<GroupOfDistributionChannelsDTO> groupOfDistributionChannels;
		
		if (mongoId != null && mongoId.trim().length() > 0) {
			groupOfDistributionChannels = groupOfDistributionChannelsRepo.findById(mongoId);
		} else {
			groupOfDistributionChannels = Optional.of(new GroupOfDistributionChannelsDTO());
			GroupOfDistributionChannels doc = new GroupOfDistributionChannels();
			groupOfDistributionChannels.get().setGroupOfDistributionChannels(doc);
			
			groupOfDistributionChannels.get().getGroupOfDistributionChannels().setId(dozer.generateNetexName("groupOfDistributionChannels"));
			groupOfDistributionChannels.get().getGroupOfDistributionChannels().setVersion("latest");
		}
				
        model.addAttribute("item", groupOfDistributionChannels);

        return "fares/groupOfDistributionChannels.html :: edit";
    }
	
	@RequestMapping(value = "/groupOfDistributionChannels", method = RequestMethod.POST)
    public String saveGroupOfDistributionChannels(@ModelAttribute GroupOfDistributionChannelsDTO groupOfDistributionChannels, BindingResult errors, Model model) {
		
		groupOfDistributionChannels.setMongoId(dozer.generateMongoId(groupOfDistributionChannels.getGroupOfDistributionChannels().getId(), groupOfDistributionChannels.getGroupOfDistributionChannels().getVersion(), "groupOfDistributionChannels"));
		groupOfDistributionChannels.setId(groupOfDistributionChannels.getGroupOfDistributionChannels().getId());
		groupOfDistributionChannels.setVersion(groupOfDistributionChannels.getGroupOfDistributionChannels().getVersion());
		
		groupOfDistributionChannelsRepo.save(groupOfDistributionChannels);
		
		return "index";
    }

	@RequestMapping(value = "/groupOfDistributionChannels", method = RequestMethod.DELETE)
    public String deleteGroupOfDistributionChannels(Model model,@RequestParam(required=true) String mongoId) {
		
		if (groupOfDistributionChannelsRepo.findById(mongoId).isPresent()) {
			groupOfDistributionChannelsRepo.deleteById(mongoId);
		} else {
			System.out.println("Missing "+ mongoId + " id");
		}
		
		return "index";
    }
}
