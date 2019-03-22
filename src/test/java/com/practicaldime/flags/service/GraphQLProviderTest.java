package com.practicaldime.flags.service;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import graphql.GraphQL;

public class GraphQLProviderTest {

	GraphQLProvider provider = new GraphQLProvider();

	@Test
	public void testGet() {
		GraphQL gql = provider.get();
		assertNotNull("Expecting non-null value", gql);
	}
}
