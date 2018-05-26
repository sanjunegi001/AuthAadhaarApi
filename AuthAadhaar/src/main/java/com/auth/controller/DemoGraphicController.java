package com.auth.controller;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class DemoGraphicController {

	private static Logger LOG = org.slf4j.LoggerFactory.getLogger(DemoGraphicController.class);
	
	
	@RequestMapping({"/demoHome"})
	public String DemoHome() {
		LOG.info("User In DemoGraphic Section");
		return "demo";	
	}
	
}
