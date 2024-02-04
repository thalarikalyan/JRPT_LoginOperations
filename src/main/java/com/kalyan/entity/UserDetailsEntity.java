package com.kalyan.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "USER_DETAILS_MASTER")
@Data
public class UserDetailsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ID")
	private Integer userId;
	@Column(name = "FIRST_NAME")
	private String firstName;
	@Column(name = "LAST_NAME")
	private String lastName;
	@Column(name = "USER_EMAIL")
	private String userEmail;
	@Column(name = "USER_PASSWORD")
	private String userPassword;
	@Column(name = "USER_MOBILE")
	private Long userMobile;
	@Column(name = "DOB")
	private LocalDate dob;
	@Column(name = "GENDER")
	private String gender;
	@Column(name = "CITY_ID")
	private Integer cityId;
	@Column(name = "STATE_ID")
	private Integer stateId;
	@Column(name = "COUNTRY_ID")
	private Integer countryId;
	@Column(name = "ACCOUNT_STATUS")
	private String accountStatus;
	@Column(name = "CREATED_DATE", updatable = false)
	@CreationTimestamp
	private LocalDateTime createdDate;
	@Column(name = "UPDATED_DATE", insertable = false)
	@CreationTimestamp
	private LocalDateTime updateDate;

}
