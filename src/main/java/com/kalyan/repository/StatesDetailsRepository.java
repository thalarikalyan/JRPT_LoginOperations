package com.kalyan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kalyan.entity.StateMasterEntity;

public interface StatesDetailsRepository extends JpaRepository<StateMasterEntity, Integer> {

//select * from state_master where country_id=?
	public List<StateMasterEntity> findByCountryId(Integer countryid);

}
