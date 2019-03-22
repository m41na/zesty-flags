package com.practicaldime.flags.service;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.practicaldime.flags.entity.Country;
import com.practicaldime.flags.repo.CountriesRepo;

import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

@Component
public class GraphQLProvider implements Supplier<GraphQL> {

	private GraphQL graphQL;
	@Autowired
	private CountriesRepo repo;
	private final DataFetcher<List<Country>> countriesFetcher = environment -> repo.getCountries();

	public void init() throws IOException {
		URL url = Resources.getResource("schema.graphql");
		String sdl = Resources.toString(url, Charsets.UTF_8);
		GraphQLSchema graphQLSchema = buildSchema(sdl);
		this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
	}

	private GraphQLSchema buildSchema(String sdl) {
		TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);
		RuntimeWiring runtimeWiring = buildWiring();
		SchemaGenerator schemaGenerator = new SchemaGenerator();
		GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
		return graphQLSchema;
	}

	private RuntimeWiring buildWiring() {
		return RuntimeWiring.newRuntimeWiring()
				.type("Query", builder -> builder
						.dataFetcher("countries", countriesFetcher)
						.dataFetcher("country", env -> repo.getCountry(env.getArgument("name")))).build();
	}
	
	@Override
	@Bean
	public GraphQL get() {
		if(this.graphQL == null) {
			try {
				init();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return this.graphQL;
	}
}