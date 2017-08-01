package com.example.service;

import org.springframework.security.core.authority.AuthorityUtils;

import com.example.domain.User;

import groovy.transform.EqualsAndHashCode;

@SuppressWarnings("serial")
@EqualsAndHashCode(callSuper=false)
public class LoginUserDetails extends 
		org.springframework.security.core.userdetails.User {
	@SuppressWarnings("unused")
	private final User user; 
	
	public LoginUserDetails(User user){
		super(user.getWriter(), user.getPassword(),
			AuthorityUtils.createAuthorityList("ROLE_USER"));
		this.user = user; 
	}
}
