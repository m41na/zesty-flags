package com.practicaldime.flags.repo;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import com.practicaldime.flags.entity.Country;

@Repository
public class CountriesRepo {

	private ObjectMapper mapper;
	private List<Country> countries = null;
	private final URL url;
	
	@Autowired
	public CountriesRepo(ObjectMapper mapper, Supplier<String> source) {
		super();
		this.mapper = mapper;
		String input = source.get();
		this.url = Resources.getResource(input != null && input.trim().length() > 0? input : "data/all-countries.json");	
	}

	public List<Country> getCountries() {
		if (this.countries == null) {
			try {
				this.countries = mapper.readValue(url, new TypeReference<List<Country>>() {});
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return this.countries;
	}
	
	public Country getCountry(String name) {
		return getCountries().stream().filter(item -> item.name.equalsIgnoreCase(name)).findFirst().get();
	}
}
