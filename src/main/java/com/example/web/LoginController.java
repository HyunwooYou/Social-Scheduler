package com.example.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {
	
	@RequestMapping("loginForm")
	String loginForm(){
		//System.out.println("(loginForm)");
		return "loginForm";
	}
	
	@RequestMapping(value={"", "/", "index"}, method=RequestMethod.GET)
	String index(){
		System.out.println("Login");
		return "index";
	}
}
