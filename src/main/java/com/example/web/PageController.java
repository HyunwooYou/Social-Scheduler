package com.example.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PageController {

	// 게시물 수정
	@RequestMapping(value="recommendation", method=RequestMethod.GET)
	String goToRecommendation() {		
		
		return "recommendation";
	}
	
	@RequestMapping(value="developer", method=RequestMethod.GET)
	String goToDeveloper() {		
		
		return "developer";
	}
}
