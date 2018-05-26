package com.auth.repository;

import org.springframework.stereotype.Repository;
import com.auth.model.deviceDetails;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface DeviceDetailRepository extends JpaRepository<deviceDetails,Long>{

	public deviceDetails findOneByuDC(String udc);
	
}
