package com.practicaldime.flags.context;

import java.util.function.Supplier;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppContext {

private AnnotationConfigApplicationContext ctx;
	
	public void init() {
		ctx = new AnnotationConfigApplicationContext("com.practicaldime.flags");
	}
	
	@SuppressWarnings("unchecked")
	public <T>T getBean(String name, Class<T> type){
		if(type == null) {
			return (T) ctx.getBean(name);
		}
		else if(name == null || name.trim().length() == 0) {
			return ctx.getBean(type);
		}
		else {
			return ctx.getBean(name, type);
		}
	}
	
	@Bean(name="repo")
	public Supplier<String> repo() {
		return () -> "data/all-countries.json";
	}
}
