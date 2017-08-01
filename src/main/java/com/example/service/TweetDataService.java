package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.domain.SearchWriter;
import com.example.domain.TweetData;
import com.example.repository.TweetDataRepository;

@Service
public class TweetDataService {

	@Autowired
	TweetDataRepository tweetDataRepository;
	
		
	public List<TweetData> findAll(){
		return tweetDataRepository.findAll();
	}
	
	public TweetData create(TweetData tweetData){
		return tweetDataRepository.save(tweetData);
	}
	
	public List<TweetData> findUserTweetData(SearchWriter search){
		return tweetDataRepository.findUserTweetData(search.getWriter());
	}
	
	public String findWriter(String writer){
		return tweetDataRepository.findWriter(writer);
	}
}
