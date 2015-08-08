package org.iemm.sicomoro.controller;

import org.iemm.sicomoro.exception.BussinesLogicException;
import org.iemm.sicomoro.service.MovementCutService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/movementcut")
public class MovementCutController {
	
	
	private Logger LOG = LoggerFactory.getLogger(MovementCutController.class);

	
	@Autowired
	private MovementCutService movementCutService;
	
	@RequestMapping("/new")
	public String newObject(Model model){
		LOG.trace(">> new()");
		model.addAttribute("movementCutForm", new MovementCutForm());
		return "/jsp/movementcut/newAndEdit.jsp";
	}
	
	@RequestMapping("/save")
	public String save(
			@ModelAttribute(value = "movementCutForm") MovementCutForm form) {
		LOG.trace(">> save()");
		try {
			movementCutService.createCut(form.getMovementCut()
					.getCutDate(), form.getMovementCut().getDescription());
		} catch (BussinesLogicException e) {
			return "";
		}
		return "/jsp/movementcut/newAndEdit.jsp";
	}

	/**
	 * @return the movementService
	 */
	public MovementCutService getMovementCutService() {
		return movementCutService;
	}

	/**
	 * @param movementService the movementService to set
	 */
	public void setMovementCutService(MovementCutService movementCutService) {
		this.movementCutService = movementCutService;
	}

}
