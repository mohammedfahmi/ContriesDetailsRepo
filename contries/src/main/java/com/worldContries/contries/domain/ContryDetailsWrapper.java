package com.worldContries.contries.domain;

public class ContryDetailsWrapper {

	private String name;
	private String continent;
	private Integer population;
	private float lifeExpectancy;
	private String language;
	
	public ContryDetailsWrapper() {
	}
	
	public ContryDetailsWrapper(String name, String continent, Integer population, float lifeExpectancy,
			String language) {
		this.name = name;
		this.continent = continent;
		this.population = population;
		this.lifeExpectancy = lifeExpectancy;
		this.language = language;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContinent() {
		return continent;
	}
	public void setContinent(String continent) {
		this.continent = continent;
	}
	public Integer getPopulation() {
		return population;
	}
	public void setPopulation(Integer population) {
		this.population = population;
	}
	public float getLifeExpectancy() {
		return lifeExpectancy;
	}
	public void setLifeExpectancy(float lifeExpectancy) {
		this.lifeExpectancy = lifeExpectancy;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	
	
}

