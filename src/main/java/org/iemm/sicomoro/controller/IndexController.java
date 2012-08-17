package org.iemm.sicomoro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
	
    @RequestMapping("/login")
    public String login(Model model) {
        return "/jsp/main.jsp";
    }

}
