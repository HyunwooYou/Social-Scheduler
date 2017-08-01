package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.domain.Friend;
import com.example.repository.FriendRepository;

@Service
public class FriendService {

	@Autowired
	FriendRepository friendRepository;
	
	public List<Friend> findAll(){
		return friendRepository.findAll();
	}
	
	public Friend create(Friend friend){
		return friendRepository.save(friend);
	}
}
