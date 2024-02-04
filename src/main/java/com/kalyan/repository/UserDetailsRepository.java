package com.kalyan.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kalyan.entity.UserDetailsEntity;

public interface UserDetailsRepository extends JpaRepository<UserDetailsEntity, Integer> {

	public UserDetailsEntity findByUserEmail(String usermail);

	public Optional<UserDetailsEntity> findByUserEmailAndUserPassword(String usermail, String userpassword);

}
