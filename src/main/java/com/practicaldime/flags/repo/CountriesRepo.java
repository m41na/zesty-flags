package com.practicaldime.flags.repo;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.practicaldime.flags.entity.Country;

public class CountriesRepo {

	private Gson gson = new Gson();
	private List<Country> countries = null;

	public List<Country> getCountries() {
		if (this.countries == null) {
			try {
				this.countries = gson.fromJson(new FileReader("/data/all-countries.json"),
						new TypeToken<List<Country>>() {
						}.getType());
			} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
		return this.countries;
	}
}
