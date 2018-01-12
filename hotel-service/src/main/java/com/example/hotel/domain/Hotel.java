package com.example.hotel.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "hotels")
public class Hotel {
	@Id
	private String id;
	
	@NotBlank
	@Size(max = 255)
	private String name;
}
