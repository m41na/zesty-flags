package com.practicaldime.flags;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.practicaldime.flags.api.GraphQLInvocationData;
import com.practicaldime.flags.context.AppContext;
import com.practicaldime.flags.service.GraphQLHandler;
import com.practicaldime.flags.service.GraphQLRequest;
import com.practicaldime.zesty.app.AppServer;
import com.practicaldime.zesty.servlet.HandlerConfig;
import com.practicaldime.zesty.servlet.HandlerRequest;
import com.practicaldime.zesty.servlet.HandlerResponse;

public class App {

	private static final Logger LOG = LoggerFactory.getLogger(App.class);
	
	public static void main(String[] args) {
		int port = 4000;
		String host = "localhost";
		
		Map<String, String> props = Maps.newHashMap();
		props.put("appctx", "/");
		props.put("cors", "true");
		
		AppContext ctx = new AppContext();
		ctx.init();
		//get handler bean
		GraphQLHandler handler = ctx.getBean("", GraphQLHandler.class);

		AppServer router = new AppServer(props).router();
		
		HandlerConfig config = holder -> {
			holder.setAsyncSupported(true);
		};
		
		router.get("/graphql", "", "application/json", config, (HandlerRequest request, HandlerResponse response) -> {
			String query = request.param("query");
			String operationName = request.param("operationName");
			String variablesJson = request.param("variables");	        
			try {
				response.setContentType("application/json");
				byte[] bytes = handler.handle(query, operationName, variablesJson).get(); //consider switching dest non-blocking
				response.bytes(bytes);
				
			} catch (ExecutionException | InterruptedException e) {
				response.status(400);
				response.send(e.getMessage());
			}
			return null;
		})
		.post("/graphql", "application/json", "application/json", config, (HandlerRequest request, HandlerResponse response) -> {
			GraphQLRequest req = request.body(GraphQLRequest.class); 
			try {
				response.setContentType("application/json");
				byte[] bytes = handler.handle(new GraphQLInvocationData(req.getQuery(), req.getOperationName(), req.getVariables())).get();
				response.bytes(bytes);
			} catch (ExecutionException | InterruptedException  e) {
				response.status(400);
				response.send(e.getMessage());
			}
			return null;
		})
		.listen(port, host, (msg) -> {
			LOG.info(msg);
		});		
	}
}
