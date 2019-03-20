package com.practicaldime.chat.builder;

import java.util.function.BiConsumer;

import org.eclipse.jetty.websocket.api.Session;

public interface EventHandler {

	void on(String event, BiConsumer<String, Session> onEvent);
}
