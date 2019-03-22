package com.practicaldime.flags.api;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import graphql.ExecutionResult;

public interface ExecutionResultHandler {

	CompletableFuture<Map<String, Object>> handleExecutionResult(CompletableFuture<ExecutionResult> executionResultCF);
}
