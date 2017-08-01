package com.example.web;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


import lombok.Data;

@Data
public class JsonTypeForm {

	@NotNull
	@Size(min = 1, max = 20)
	private String writer;

	@NotNull
	@Size(min = 1, max = 300)
	private String interest; 
}
