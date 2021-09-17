package com.population.app.model;

public class countrystats {
	private String country;
	private String countryCode;
	private String  year;
	private String population;
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getPopulation() {
		return population;
	}
	public void setPopulation(String population) {
		this.population = population;
	}
	@Override
	public String toString() {
		return "countrystats [country=" + country + ", countryCode=" + countryCode + ", year=" + year + ", population="
				+ population + "]";
	}
	
}
