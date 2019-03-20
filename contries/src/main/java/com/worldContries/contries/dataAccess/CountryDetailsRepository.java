package com.worldContries.contries.dataAccess;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.worldContries.contries.domain.ContryDetailsWrapper;

@Repository
public class CountryDetailsRepository {
	
	 @Autowired
	 private JdbcTemplate jdbcTemplate;
	 	

	public List<ContryDetailsWrapper> findCountryByCode(String contryCode) throws Exception{
		
		List<ContryDetailsWrapper> result = new ArrayList<ContryDetailsWrapper>();
		Integer officialLanguagesCount = jdbcTemplate.queryForObject("SELECT count(distinct language) FROM country_language WHERE is_official = true and country_code = ?" , new Object[] {contryCode}, Integer.class);

		//if the country has more than one Official Language --> return all official languages
			if(officialLanguagesCount > 1) {
				return this.findAll(contryCode);
			}
		//if the country has no Official Language
			if(officialLanguagesCount == 0) {
				ContryDetailsWrapper contryDetailsWrapper = jdbcTemplate.queryForObject(" SELECT c.name as name, c.continent  as continent , c.population as population, c.life_expectancy  as life_expectancy, l.language as language   FROM country c inner join country_language l on c.code = l.country_code where c.code=? limit 1" , new ContryDetailsRowMapper(),contryCode);
				contryDetailsWrapper.setLanguage("");
				result.add(contryDetailsWrapper);
				return result;
			}
			ContryDetailsWrapper contryDetailsWrapper = jdbcTemplate.queryForObject(" SELECT c.name as name, c.continent  as continent , c.population as population, c.life_expectancy  as life_expectancy, l.language as language   FROM country c inner join country_language l on c.code = l.country_code where l.is_official = true and c.code=?" , new ContryDetailsRowMapper(),contryCode);
			result.add(contryDetailsWrapper);
			return result;		
	}

	public List<ContryDetailsWrapper> findAll(String contryCode){
		 
		List<ContryDetailsWrapper> contryDetailsWrapper = new ArrayList<ContryDetailsWrapper>();
		
		contryDetailsWrapper = jdbcTemplate.query(" SELECT c.name as name, c.continent  as continent , c.population as population, c.life_expectancy  as life_expectancy, l.language as language   FROM country c inner join country_language l on c.code = l.country_code where l.is_official = true and c.code=?" , new ContryDetailsRowMapper(),contryCode);
		
		return contryDetailsWrapper;
	}
	
}

class ContryDetailsRowMapper implements RowMapper<ContryDetailsWrapper>
{
    @Override
    public ContryDetailsWrapper mapRow(ResultSet rs, int rowNum) throws SQLException {
    	String name = rs.getString(1);
    	String continent = rs.getString(2);
    	Integer population = rs.getInt(3);
    	float lifeExpectancy;
		try {
			lifeExpectancy = rs.getFloat(4);
		} catch (NullPointerException e) {
			lifeExpectancy = 0;
		}
    	String language = rs.getString(5); 
    	return new ContryDetailsWrapper(name, continent,population,lifeExpectancy,language);
      
    }
}