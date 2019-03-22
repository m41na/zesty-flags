package com.practicaldime.flags.api;

import java.util.concurrent.CompletableFuture;

import graphql.ExecutionResult;

public interface GraphQLInvocation {

	CompletableFuture<ExecutionResult> invoke(GraphQLInvocationData invocationData);
}
