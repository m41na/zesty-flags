package com.practicaldime.chat;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.practicaldime.zesty.app.AppServer;
import com.practicaldime.zesty.servlet.HandlerRequest;
import com.practicaldime.zesty.servlet.HandlerResponse;

public class App {
	
	private static final Logger LOG = LoggerFactory.getLogger(App.class);
	
	public static void main(String[] args) {
		int port = 1337;
		String host = "localhost";
		
		Map<String, String> props = Maps.newHashMap();
		props.put("appctx", "/ws");
		props.put("assets", "www/chat");
		props.put("cors", "true");

		AppServer router = new AppServer(props).router();
		router.websocket("/chat/*", ()-> new Chat("chat"))
		.get("/check", "", "application/json", null, (HandlerRequest request, HandlerResponse response) -> {
			response.send(String.format("incoming request: '%s'", request.getRequestURI()));
			return null;
		})
		.listen(port, host, (msg) -> {
			LOG.info(msg);
		});		
	}
}
