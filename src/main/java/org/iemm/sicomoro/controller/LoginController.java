package org.iemm.sicomoro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {

	@RequestMapping(value = "/welcome", method = RequestMethod.GET)
	public String printWelcome(ModelMap model) {
		return "/jsp/main.jsp";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(ModelMap model) {
		return "index.jsp";
	}

	@RequestMapping(value = "/loginfailed", method = RequestMethod.GET)
	public String loginerror(ModelMap model) {
		model.addAttribute("error", "true");
		return "index.jsp";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(ModelMap model) {
		return "index.jsp";
	}

}
