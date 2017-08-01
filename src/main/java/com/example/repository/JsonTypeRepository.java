package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.domain.JsonType;

public interface JsonTypeRepository extends JpaRepository<JsonType, Integer>{

	/*
	 * writer이름에 따라서  writer, interest에 대한 정보를 List형식으로 반환. 
	 */
	@Query("SELECT x FROM JsonType x WHERE x.writer = :writer ORDER BY x.id desc")
	List<JsonType> findUserJsonType(@Param("writer") String writer);
}
