package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.domain.JsonType;
import com.example.domain.SearchWriter;
import com.example.repository.JsonTypeRepository;

@Service
public class JsonTypeService {

	@Autowired
	JsonTypeRepository jsonTypeRepository;

	public List<JsonType> findAll(){
		return jsonTypeRepository.findAll();
	}
	
	public JsonType create(JsonType jsonType){
		return jsonTypeRepository.save(jsonType);
	}
	
	public List<JsonType> findUserJsonType(SearchWriter search){
		return jsonTypeRepository.findUserJsonType(search.getWriter());
	}
}
