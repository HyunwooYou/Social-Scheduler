package com.example.web;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class SearchWriterForm {

	@NotNull
	@Size(min = 1, max = 30)
	private String writer;
}
