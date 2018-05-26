package com.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.auth.model.deviceDetails;
import com.auth.repository.DeviceDetailRepository;

@Component
public class BioServiceImpl implements BioService{

	
	@Autowired
	DeviceDetailRepository devicerepo;
	
	@Override
	public Boolean findOneByUDC(String udc) {
		// TODO Auto-generated method stub
		return devicerepo.findOneByuDC(udc)==null?false:true;
	}

}
