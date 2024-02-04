package com.kalyan.dto;

import lombok.Data;

@Data
public class UserUnlockFormDto {

	private String userEmail;
	private String tempPassword;
	private String newPassword;
	private String confirmPassword;

}
