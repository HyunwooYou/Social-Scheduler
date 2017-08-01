package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.domain.TweetData;

public interface TweetDataRepository extends JpaRepository<TweetData, Integer> {
	
	/*
	 * FROM 뒤에 TweetData는 com.example.domain.TweetData와 같이 
	 * class명과 동일하게 입력해야 한다. 
	 */
	@Query("SELECT x FROM TweetData x WHERE x.writer = :writer ORDER BY x.id desc")
	List<TweetData> findUserTweetData(@Param("writer") String writer);
	
	/*
	 * writer가 존재하는지 확인하는 쿼리. 
	 */
	@Query("SELECT x.writer FROM TweetData x WHERE x.writer = :writer")
	String findWriter(@Param("writer") String writer);
}
