package com.practicaldime.flags.entity;

import java.util.ArrayList;
import java.util.List;

public class Country {

	public String name;
	public List<String> topLevelDomain = new ArrayList<>();
	public String alpha2Code;
	public String alpha3Code;
	public List<String> callingCodes = new ArrayList<>();
	public String capital;
	public List<String> altSpellings = new ArrayList<>();
	public String region;
	public String subregion;
	public Long population;
	public List<Float> latlng = new ArrayList<>();
	public String demonym;
	public Float area;
	public Float gini;
	public List<String> timezones = new ArrayList<>();
    public List<String> borders = new ArrayList<>();
    public String nativeName;
    public String numericCode;
    public List<Currency> currencies = new ArrayList<>();
    public List<Language> languages = new ArrayList<>();
    public Translations translations;
    public String flag;
    public List<RegionalBlock> regionalBlocs = new ArrayList<>();
    public String cioc;
}
