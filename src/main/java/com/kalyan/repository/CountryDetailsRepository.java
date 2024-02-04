package com.kalyan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kalyan.entity.CountryMasterEntity;

public interface CountryDetailsRepository extends JpaRepository<CountryMasterEntity, Integer> {

}
