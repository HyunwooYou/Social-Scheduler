package com.example.web;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.domain.User;
import com.example.service.UserService;

@Controller
@RequestMapping("users")
public class UserController {
	
	@Autowired
	UserService userService; 
	
	@ModelAttribute
	UserForm setUpForm(){
		return new UserForm();
	}
	
	@RequestMapping(method=RequestMethod.GET)
	String list(Model model){
		List<User> users = userService.findAll();
		model.addAttribute("users", users);
				
		return "users/userInfo";
	}
	
	// 사용자 회원가입
	@RequestMapping(value="create", method=RequestMethod.POST)
	String create(@Validated UserForm form, BindingResult result, Model model){
						
		if(result.hasErrors()){
			return list(model);
		}
		User user=new User();
		BeanUtils.copyProperties(form, user);
		userService.create(user);
		
		return "redirect:/index";		
	}	
	
	@RequestMapping(value="button", method=RequestMethod.GET)
	String button(){
		return "users/button";
	}
}
