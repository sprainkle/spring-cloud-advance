package com.sprainkle.oauth2.client.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

/**
 * @author sprainkle
 * @date 2022/1/29
 */
@Controller
public class AppController {

	@GetMapping("hello")
	public ModelAndView welcome(Principal principal) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("welcome");
		mav.addObject("name", principal.getName());
		return mav;
	}
}
