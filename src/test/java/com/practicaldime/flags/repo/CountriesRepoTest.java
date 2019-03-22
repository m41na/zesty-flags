package com.practicaldime.flags.repo;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.practicaldime.flags.api.impl.JacksonConfiguration;
import com.practicaldime.flags.entity.Country;

public class CountriesRepoTest {

	CountriesRepo repo = new CountriesRepo(new JacksonConfiguration().objectMapper(), () -> "data/all-countries.json");
	
	@Test
	public void testGetCountries() {
		List<Country> countries = repo.getCountries();
		assertTrue("Expected non-empty list", countries.size() > 0);
	}
}
