package com.example.domain;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tweetdata")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TweetData {
	
	@Id
	@GeneratedValue
	private Integer id;
	@Column(nullable = false)
	private String writer;
	@Column(nullable = false)
	private String tweet_Data; 
}
