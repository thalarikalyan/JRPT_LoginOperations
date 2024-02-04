package com.kalyan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kalyan.entity.CityMasterEntity;

public interface CitiesDetailsRepository extends JpaRepository<CityMasterEntity, Integer> {

	// select * from cities_master where state_id=?
	List<CityMasterEntity> findByStateId(Integer stateId);

}
