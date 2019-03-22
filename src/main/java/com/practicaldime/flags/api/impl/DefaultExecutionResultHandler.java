package com.practicaldime.flags.api.impl;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Component;

import com.practicaldime.flags.api.ExecutionResultHandler;

import graphql.ExecutionResult;

@Component
public class DefaultExecutionResultHandler implements ExecutionResultHandler {

    @Override
    public CompletableFuture<Map<String, Object>> handleExecutionResult(CompletableFuture<ExecutionResult> executionResultCF) {
        return executionResultCF.thenApply(ExecutionResult::toSpecification);
    }
}
