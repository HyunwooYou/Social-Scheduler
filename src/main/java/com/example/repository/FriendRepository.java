package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.domain.Friend;

public interface FriendRepository extends JpaRepository<Friend, String>{
		
} 
