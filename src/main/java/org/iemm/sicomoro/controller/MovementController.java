package org.iemm.sicomoro.controller;

import org.iemm.sicomoro.exception.BussinesLogicException;
import org.iemm.sicomoro.service.MovementService;
import org.iemm.sicomoro.service.MovementService.MovementTypeE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/movement")
public class MovementController {
	
	
	private Logger LOG = LoggerFactory.getLogger(MovementController.class);

	
	@Autowired
	private MovementService movementService;
	
	@RequestMapping("/new")
	public String newObject(Model model){
		LOG.trace(">> new()");
		model.addAttribute("movementForm", new MovementForm());
		model.addAttribute("movementTypes", MovementTypeE.values());
		return "/jsp/movement/newAndEdit.jsp";
	}
	
	@RequestMapping("/save")
	public String save(
			@ModelAttribute(value = "movementForm") MovementForm form,
			@RequestParam MovementTypeE movementType) {
		LOG.trace(">> save()");
		int idMovement;
		try {
			idMovement = movementService.createMovement(form.getMovement().getAmount(), form.getMovement().getIdContributor(),
					form.getMovement().getMovementDate(), form.getMovement().getDescription(), movementType);
		} catch (BussinesLogicException e) {
			return "";
		}
		return "redirect:/movement/edit/" + idMovement + "/edit.html";
	}

	/**
	 * @return the movementService
	 */
	public MovementService getMovementService() {
		return movementService;
	}

	/**
	 * @param movementService the movementService to set
	 */
	public void setMovementService(MovementService movementService) {
		this.movementService = movementService;
	}

}
