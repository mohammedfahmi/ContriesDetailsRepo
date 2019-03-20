package com.worldContries.contries;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.worldContries.contries.controllers.CountriesController;
import com.worldContries.contries.controllers.CountryNotFoundException;
import com.worldContries.contries.dataAccess.CountryDetailsRepository;
import com.worldContries.contries.domain.ContryDetailsWrapper;

public class ContriesDetailsControllerTest {
	private MockMvc mockMvc;

	@Test
	public void testSuccessfulGetContryByCode() {
		String ExpectedResponse ="[{\"name\":\"Bahrain\",\"continent\":\"Asia\",\"population\":617000,\"lifeExpectancy\":73.0,\"language\":\"Arabic\"}]";
		
		ContryDetailsWrapper mockresult = new ContryDetailsWrapper("Bahrain","Asia",617000,(float) 73.0,"Arabic");
		List<ContryDetailsWrapper> result = new ArrayList<ContryDetailsWrapper>();
		result.add(mockresult);
		CountryDetailsRepository mockRepository = mock(CountryDetailsRepository.class);
		try {
			when(mockRepository.findCountryByCode("BHR")).thenReturn(result);
		} catch (Exception e1) {

		}
		
		CountriesController controller = new CountriesController(mockRepository);
		
		mockMvc = MockMvcBuilders
		        .standaloneSetup(controller)
		        .build();
		
		try {
	        mockMvc.perform(get("/BHR"))
            .andExpect(status().isOk())
            .andExpect(content().string(ExpectedResponse));
 
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void testSuccessfulGetContryByCodeWithMultipleOfficialLanguages() {
		String ExpectedResponse ="[{\"name\":\"Afghanistan\",\"continent\":\"Asia\",\"population\":22720000,\"lifeExpectancy\":45.9,\"language\":\"Pashto\"},{\"name\":\"Afghanistan\",\"continent\":\"Asia\",\"population\":22720000,\"lifeExpectancy\":45.9,\"language\":\"Dari\"}]";
		
		ContryDetailsWrapper mockresult = new ContryDetailsWrapper("Afghanistan","Asia",22720000,(float) 45.9,"Pashto");
		List<ContryDetailsWrapper> result = new ArrayList<ContryDetailsWrapper>();
		result.add(mockresult);
		
		mockresult = new ContryDetailsWrapper("Afghanistan","Asia",22720000,(float) 45.9,"Dari");
		result.add(mockresult);
		
		CountryDetailsRepository mockRepository = mock(CountryDetailsRepository.class);
		try {
			when(mockRepository.findCountryByCode("AFG")).thenReturn(result);
		} catch (Exception e1) {

		}
		
		CountriesController controller = new CountriesController(mockRepository);
		
		mockMvc = MockMvcBuilders
		        .standaloneSetup(controller)
		        .build();
		
		try {
	        mockMvc.perform(get("/AFG"))
            .andExpect(status().isOk())
            .andExpect(content().string(ExpectedResponse));
 
		} catch (Exception e) {
			fail();
		}
	}	
	
	@Test
	public void testInternalServerFailureGetContryByCode() {
		String ExpectedResponse ="INTERNAL_ERROR";
		
		CountryDetailsRepository mockRepository = mock(CountryDetailsRepository.class);
		try {
			when(mockRepository.findCountryByCode("BHR")).thenThrow(Exception.class);
		} catch (Exception e1) {

		}
		
		CountriesController controller = new CountriesController(mockRepository);
		
		mockMvc = MockMvcBuilders
		        .standaloneSetup(controller)
		        .build();
		
		try {
	        mockMvc.perform(get("/BHR"))
            .andExpect(status().is5xxServerError())
            .andExpect(content().string(ExpectedResponse));
 
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void testNotFoundCountryFailureGetContryByCode() {
		String ExpectedResponse ="INVALID_COUNTRY_CODE";
		
		CountryDetailsRepository mockRepository = mock(CountryDetailsRepository.class);
		try {
			when(mockRepository.findCountryByCode("AMA")).thenReturn(null);
		} catch (Exception e1) {
		}

		CountriesController controller = new CountriesController(mockRepository);
		
		mockMvc = MockMvcBuilders
		        .standaloneSetup(controller)
		        .build();
		
		try {
	        mockMvc.perform(get("/AMA"))
            .andExpect(status().is5xxServerError())
            .andExpect(content().string(ExpectedResponse));
 
		} catch (Exception e) {
			fail();
		}
	}
}
