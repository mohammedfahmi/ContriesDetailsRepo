package com.worldContries.contries.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.worldContries.contries.dataAccess.CountryDetailsRepository;
import com.worldContries.contries.domain.ContryDetailsWrapper;


@RestController
@RequestMapping("/")
public class CountriesController {

	private CountryDetailsRepository countryDetailsRepository;
	
	@Autowired
	public CountriesController(CountryDetailsRepository countryDetailsRepository){
		this.countryDetailsRepository =countryDetailsRepository;
	}
	
	@ExceptionHandler(CountryNotFoundException.class)
	public ResponseEntity rulesForInvalidCountryCode(){
		return new ResponseEntity("INVALID_COUNTRY_CODE",HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(InternalErrorException.class)
	public ResponseEntity rulesForIternalErrors(){
		return new ResponseEntity("INTERNAL_ERROR",HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@RequestMapping(value="/{countryCode}",method=RequestMethod.GET)
	public List<ContryDetailsWrapper> getCountryDetails(@PathVariable("countryCode") String contryCode) throws CountryNotFoundException, InternalErrorException
	{
		if(contryCode.length()>3) {
			throw new CountryNotFoundException();
		}
		List<ContryDetailsWrapper> contryDetails = null;
		try {
			contryDetails = countryDetailsRepository.findCountryByCode(contryCode);
		}
		catch(org.springframework.dao.EmptyResultDataAccessException e) {
			throw new CountryNotFoundException();
		}
		catch(Exception e) {
			throw new InternalErrorException();
		}
		if(contryDetails == null) {
			throw new CountryNotFoundException();
		}
		return contryDetails;
	}

}
