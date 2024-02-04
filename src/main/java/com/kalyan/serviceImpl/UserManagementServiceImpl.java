package com.kalyan.serviceImpl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.kalyan.constants.UserManagementConstants;
import com.kalyan.dto.LoginFormDto;
import com.kalyan.dto.UserRegistrationFormDto;
import com.kalyan.dto.UserUnlockFormDto;
import com.kalyan.entity.CityMasterEntity;
import com.kalyan.entity.CountryMasterEntity;
import com.kalyan.entity.StateMasterEntity;
import com.kalyan.entity.UserDetailsEntity;
import com.kalyan.repository.CitiesDetailsRepository;
import com.kalyan.repository.CountryDetailsRepository;
import com.kalyan.repository.StatesDetailsRepository;
import com.kalyan.repository.UserDetailsRepository;
import com.kalyan.service.IUserManagementService;

@Service
public class UserManagementServiceImpl implements IUserManagementService {

	@Autowired
	private UserDetailsRepository userDetailsRepository;

	@Autowired
	private CountryDetailsRepository countryDetailsRepository;

	@Autowired
	private StatesDetailsRepository statesDetailsRepository;

	@Autowired
	private CitiesDetailsRepository citiesDetailsRepository;

	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String sender;

	@Override
	public String userRegistration(UserRegistrationFormDto userRegistrationFormDto) {
		// create Entity Object
		UserDetailsEntity userDetailsEntity = new UserDetailsEntity();
		BeanUtils.copyProperties(userRegistrationFormDto, userDetailsEntity);
		boolean checkUserAlreadyAvailable = checkUserAlreadyAvailable(userDetailsEntity.getUserEmail());
		if (!checkUserAlreadyAvailable) {
			// generate random password for the user
			userDetailsEntity.setUserPassword(generateRandomPassword());
			userDetailsEntity.setAccountStatus(UserManagementConstants.LOCKED);
			UserDetailsEntity userDetailsInfo = userDetailsRepository.save(userDetailsEntity);
			if (userDetailsInfo.getUserId() != null) {
				// Read the data from the file
				String readDataFromtheFile = readDataFromtheFile(UserManagementConstants.URL, userDetailsEntity);
				boolean sendSimpleEmail = sendSimpleEmail(userDetailsEntity, readDataFromtheFile);
				if (sendSimpleEmail)
					return "User Details are saved and Sent email Successfully!!";
				else
					return "Data Saved Successfully but failure to Send Email";

			}

			else
				return "Issue in saving User Details";

		} else
			return "User Already Existing";

	}

	@Override
	public String userLogin(LoginFormDto loginFormDto) {
		Optional<UserDetailsEntity> findByUseremailAndUserpassword = userDetailsRepository
				.findByUserEmailAndUserPassword(loginFormDto.getUserEmail(), loginFormDto.getUserPassword());
		if (findByUseremailAndUserpassword.isPresent()
				&& findByUseremailAndUserpassword.get().getAccountStatus().equals(UserManagementConstants.UNLOCKED))
			return "Welcome to the Page.";
		else
			return "Invalid Credentials";

	}

	// check Already user email register or not
	private boolean checkUserAlreadyAvailable(String useremail) {

		UserDetailsEntity getUserDetails = userDetailsRepository.findByUserEmail(useremail);
		if (getUserDetails != null)
			return true;
		else
			return false;

	}

	// generate Random password
	private static String generateRandomPassword() {

		SecureRandom secureRandom = new SecureRandom();
		StringBuilder password = new StringBuilder();

		for (int i = 0; i < UserManagementConstants.PASSWORD_LENGTH; i++) {
			int randomIndex = secureRandom.nextInt(UserManagementConstants.ALPHANUMERIC_CHARS.length());
			char randomChar = UserManagementConstants.ALPHANUMERIC_CHARS.charAt(randomIndex);
			password.append(randomChar);
		}

		return password.toString();
	}

	@Override
	public Map<Integer, String> loadAllCountries() {
		// create Map
		Map<Integer, String> countryDetailsMap = new HashMap<>();
		// Load All the countries
		List<CountryMasterEntity> findAllCountries = countryDetailsRepository.findAll();
		for (CountryMasterEntity countryMasterEntity : findAllCountries) {
			countryDetailsMap.put(countryMasterEntity.getCountryId(), countryMasterEntity.getCountryName());

		}

		return countryDetailsMap;
	}

	@Override
	public Map<Integer, String> loadAllStates(Integer countryId) {

		// create Map
		Map<Integer, String> stateDetailsMap = new HashMap<>();
		List<StateMasterEntity> findByCountryId = statesDetailsRepository.findByCountryId(countryId);
		for (StateMasterEntity statesMasterEntity : findByCountryId) {
			stateDetailsMap.put(statesMasterEntity.getStateId(), statesMasterEntity.getStateName());
		}

		return stateDetailsMap;
	}

	@Override
	public Map<Integer, String> loadAllCities(Integer stateId) {

		// create Map
		Map<Integer, String> citiesDetailsMap = new HashMap<>();
		List<CityMasterEntity> findByStateId = citiesDetailsRepository.findByStateId(stateId);
		for (CityMasterEntity cityMasterEntity : findByStateId) {
			citiesDetailsMap.put(cityMasterEntity.getCityId(), cityMasterEntity.getCityName());
		}

		return citiesDetailsMap;
	}

	@Override
	public String unLockUserAccount(UserUnlockFormDto userUnlockFormDto) {
		if (userUnlockFormDto.getNewPassword().equals(userUnlockFormDto.getConfirmPassword())) {
			UserDetailsEntity findByUseremailEntity = userDetailsRepository
					.findByUserEmail(userUnlockFormDto.getUserEmail());
			if (findByUseremailEntity != null) {
				if (findByUseremailEntity.getUserPassword().equals(userUnlockFormDto.getTempPassword())) {
					findByUseremailEntity.setUserPassword(userUnlockFormDto.getNewPassword());
					findByUseremailEntity.setAccountStatus(UserManagementConstants.UNLOCKED);
					userDetailsRepository.save(findByUseremailEntity);

				} else
					return "Enter Correct Temperory Password!";
			} else
				return "No User Details Found";

		} else {
			return "New Password and Confirm Password both are not Equal!!";
		}

		return "User Account is Unlocked!!";
	}

	@Override
	public String forgotPassword(String email) {

		UserDetailsEntity findByUserDetailsEntity = userDetailsRepository.findByUserEmail(email);
		if (findByUserDetailsEntity.getUserEmail() != null && findByUserDetailsEntity.getUserEmail().equals(email)) {
			String readDataFromFile = readDataFromtheFile(UserManagementConstants.URL, findByUserDetailsEntity);
			boolean sendSimpleEmail = sendSimpleEmail(findByUserDetailsEntity, readDataFromFile);
			if (sendSimpleEmail)
				return "Successfully Sent the Temporary Passpord";
			else
				return "Failure to Sent the Temporary Passport to the user";

		} else
			return "Email is not Registered!!";

	}

	public boolean sendSimpleEmail(UserDetailsEntity userDetailsEntity, String readDataFromtheFile) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom(sender);
		mailMessage.setTo(userDetailsEntity.getUserEmail());
		mailMessage.setSubject("Sending Temporary Password");
		mailMessage.setText(readDataFromtheFile);
		javaMailSender.send(mailMessage);
		return userDetailsEntity.getUserEmail() != null ? true : false;
	}

	private String readDataFromtheFile(String fileName, UserDetailsEntity detailsEntity) {
		String line = null;
		StringBuilder fileContent = new StringBuilder();
		try {

			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			while ((line = reader.readLine()) != null) {
				line = line.replace("{PASSWORD}", detailsEntity.getUserPassword());
				line = line.replace("{NAME}", detailsEntity.getFirstName() + " " + detailsEntity.getLastName());
				fileContent.append(line).append(System.lineSeparator());
			}

			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return fileContent.toString();

	}
}
