package com.sg.netex.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.rutebanken.netex.model.GroupTicket;
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
import com.sg.netex.dto.GroupTicketDTO;
import com.sg.netex.model.ButtonEvents;
import com.sg.netex.model.GenericTable;
import com.sg.netex.repository.GroupTicketRepository;

@Controller
public class GroupTicketController {

	@Autowired
	GroupTicketRepository groupTicketRepo;

	@Autowired
	private UtilConfig dozer;
	
	@RequestMapping(value = "/groupTickets", method = RequestMethod.GET)
    public ResponseEntity<List<GroupTicketDTO>> groupTickets(Model model) {
    	List<GroupTicketDTO> groupTickets = groupTicketRepo.findAll();
    	
        return new ResponseEntity<>(groupTickets,HttpStatus.OK);
    }
	
	
	// the simple table view for thymeleaf
	@RequestMapping(value = "/groupTicketGrid", method = RequestMethod.GET)
    public String groupTicket(Model model) {
		
		List<GroupTicketDTO> groupTickets = groupTicketRepo.findAll();
    	
		List<GenericTable> tableItems = new ArrayList<>();
		
		for (GroupTicketDTO list : groupTickets) {
			GenericTable item = new GenericTable();
			item.setFareFrameType(list.getFareFrameType());
			if (list.getName() != null)
				item.setName(list.getName());
			item.setMongoId(list.getMongoId());
			if (list.getGroupTicket().getName() != null)
				item.setName(list.getGroupTicket().getName().getValue());
			if (list.getOperator() != null)
				item.setObjectType(list.getOperator());

			item.setVersion(list.getGroupTicket().getVersion());
			
			tableItems.add(item);
		}
		
		model.addAttribute("tableItems",tableItems);
		
        ButtonEvents action = new ButtonEvents();
        action.setGetUrl("/groupTickets");
        action.setModifyItemUrl("/groupTicket");
        action.setDeleteItemUrl("/groupTicket");
        model.addAttribute("action", action);
        
        
        return "/html/simpleTable.html :: grid";
    }
	
	@RequestMapping(value = "/groupTicket", method = RequestMethod.GET)
    public String newGroupTicket(Model model,@RequestParam(required=false) String mongoId) {
    	
		Optional<GroupTicketDTO> groupTicket;
		
		if (mongoId != null && mongoId.trim().length() > 0) {
			groupTicket = groupTicketRepo.findById(mongoId);
		} else {
			groupTicket = Optional.of(new GroupTicketDTO());
			GroupTicket doc = new GroupTicket();
			groupTicket.get().setGroupTicket(doc);
			
			groupTicket.get().getGroupTicket().setId(dozer.generateNetexName("groupTicket"));
			groupTicket.get().getGroupTicket().setVersion("latest");
		}
				
        model.addAttribute("item", groupTicket);

        return "fares/groupTicket.html :: edit";
    }
	
	@RequestMapping(value = "/groupTicket", method = RequestMethod.POST)
    public String saveGroupTicket(@ModelAttribute GroupTicketDTO groupTicket, BindingResult errors, Model model) {
		
		groupTicket.setMongoId(dozer.generateMongoId(groupTicket.getGroupTicket().getId(), groupTicket.getGroupTicket().getVersion(), "groupTicket"));
		groupTicket.setId(groupTicket.getGroupTicket().getId());
		groupTicket.setVersion(groupTicket.getGroupTicket().getVersion());
		
		groupTicketRepo.save(groupTicket);
		
		return "index";
    }

	@RequestMapping(value = "/groupTicket", method = RequestMethod.DELETE)
    public String deleteGroupTicket(Model model,@RequestParam(required=true) String mongoId) {
		
		if (groupTicketRepo.findById(mongoId).isPresent()) {
			groupTicketRepo.deleteById(mongoId);
		} else {
			System.out.println("Missing "+ mongoId + " id");
		}
		
		return "index";
    }
}
