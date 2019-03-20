package com.practicaldime.chat.builder;

import java.util.function.Consumer;

import org.eclipse.jetty.websocket.api.Session;

public class ListenerBuilder {

	public void on(String event, Consumer<EventHandler> onEvent) {

	}

	public static void main(String... args) {
		new ListenerBuilder().on("connection", new Consumer<EventHandler>() {

			@Override
			public void accept(EventHandler client) {
				client.on("register", (String user, Session sess) -> {
					// TODO Auto-generated method stub

				});

				client.on("leave", (String user, Session sess) -> {
					// TODO Auto-generated method stub

				});

				client.on("message", (String user, Session sess) -> {
					// TODO Auto-generated method stub

				});

				client.on("chatrooms", (String user, Session sess) -> {
					// TODO Auto-generated method stub

				});

				client.on("availableUsers", (String user, Session sess) -> {
					// TODO Auto-generated method stub

				});

				client.on("disconnect", (String user, Session sess) -> {
//					  System.out.println("client disconnect...", client.id)
//					    handleDisconnect();
				});

				client.on("error", (String user, Session sess) -> {
//						  System.out.println("received error from client:", client.id);
//						  System.out.println(err)

				});
			}
		});
	}
}
