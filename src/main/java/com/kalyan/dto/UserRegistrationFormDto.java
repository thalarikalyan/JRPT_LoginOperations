package com.kalyan.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UserRegistrationFormDto {

	private Integer userId;
	private String firstName;
	private String lastName;
	private String userEmail;
	private Long userMobile;
	private LocalDate dob;
	private String gender;
	private Integer cityId;
	private Integer stateId;
	private Integer countryId;

}
