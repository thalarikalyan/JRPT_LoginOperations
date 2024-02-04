package com.kalyan.service;

import java.util.Map;

import com.kalyan.dto.LoginFormDto;
import com.kalyan.dto.UserRegistrationFormDto;
import com.kalyan.dto.UserUnlockFormDto;

public interface IUserManagementService {

	public String userRegistration(UserRegistrationFormDto userRegistrationFormDto);

	public Map<Integer, String> loadAllCountries();

	public Map<Integer, String> loadAllStates(Integer countryid);

	public Map<Integer, String> loadAllCities(Integer stateid);

	public String userLogin(LoginFormDto loginFormDto);

	public String unLockUserAccount(UserUnlockFormDto userUnlockFormDto);

	public String forgotPassword(String email);

}
