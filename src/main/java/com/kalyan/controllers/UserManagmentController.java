package com.kalyan.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kalyan.dto.LoginFormDto;
import com.kalyan.dto.UserRegistrationFormDto;
import com.kalyan.dto.UserUnlockFormDto;
import com.kalyan.service.IUserManagementService;

@RestController
@RequestMapping("/userManagement")
public class UserManagmentController {

	@Autowired
	private IUserManagementService userManagementService;

	@PostMapping("/signUp")
	public ResponseEntity<String> signUpUserDetails(@RequestBody UserRegistrationFormDto userRegistrationFormDto) {
		String userRegistration = userManagementService.userRegistration(userRegistrationFormDto);
		return new ResponseEntity<>(userRegistration, HttpStatus.OK);

	}

	@PostMapping("/signIn")
	public ResponseEntity<String> signInUserDetails(@RequestBody LoginFormDto loginFormDto) {
		String userLogin = userManagementService.userLogin(loginFormDto);
		return new ResponseEntity<>(userLogin, HttpStatus.OK);

	}

	@GetMapping("/loadAllcountries")
	public ResponseEntity<Map<Integer, String>> loadingAllExistingCountries() {
		Map<Integer, String> loadAllCountries = userManagementService.loadAllCountries();
		return new ResponseEntity<Map<Integer, String>>(loadAllCountries, HttpStatus.OK);

	}

	@GetMapping("/loadAllStates/{countryid}")
	public ResponseEntity<Map<Integer, String>> loadingAllExistingStates(@PathVariable Integer countryid) {
		Map<Integer, String> loadAllStates = userManagementService.loadAllStates(countryid);
		return new ResponseEntity<Map<Integer, String>>(loadAllStates, HttpStatus.OK);

	}

	@GetMapping("/loadAllCities/{stateid}")
	public ResponseEntity<Map<Integer, String>> loadingAllExistingCities(@PathVariable Integer stateid) {
		Map<Integer, String> loadAllCities = userManagementService.loadAllCities(stateid);
		return new ResponseEntity<Map<Integer, String>>(loadAllCities, HttpStatus.OK);

	}

	@PostMapping("/unlockAccount")
	public ResponseEntity<String> unLockUserAccountDetails(@RequestBody UserUnlockFormDto userUnlockFormDto) {
		String userUnlockDetails = userManagementService.unLockUserAccount(userUnlockFormDto);
		return new ResponseEntity<>(userUnlockDetails, HttpStatus.OK);

	}

	@PostMapping("/forgotPassword/{email}")
	public ResponseEntity<String> forgotUserAccount(@PathVariable String email) {
		String forgotPassword = userManagementService.forgotPassword(email);
		return new ResponseEntity<>(forgotPassword, HttpStatus.OK);

	}
}
