package com.practicaldime.chat;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jetty.websocket.api.Session;

import com.google.gson.Gson;
import com.practicaldime.zesty.websock.AppWsListener;

public class Chat implements AppWsListener{
	
	private static final Map<String, Session> USERS = new ConcurrentHashMap<>();
    private static final Gson GSON = new Gson();
    
    //externalize these two fields
    private final String topic;
    private final Function<String, String> provider = (topic) -> String.format("/%s/(.+)$", topic != null? topic : "events");
    
    public Chat(String topic) {
		super();
		this.topic = topic;
	}

	private String extractSessionUser(Session sess) {
    	String url = sess.getUpgradeRequest().getRequestURI().toString();
        String regex = provider.apply(topic);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        String user = null;
        if (matcher.find()) {
            user = matcher.group(1);
        }
        return user;
    }
	
	private String createMessage(String from, String to, String time, String message) {
		return GSON.toJson(new Msg(from, to, time, message));
	}
	
	private String createMessage(String from, String to, String time, String error, int status) {
		return GSON.toJson(new Msg(from, to, time, error, status));
	}

	@Override
	public void onConnect(Session sess) throws IOException {
		String user = extractSessionUser(sess);
		if(user != null) {
            sess.getRemote().sendString(createMessage("server", user, timestamp(), "Hi " + user + "! You are now online"));
            USERS.put(user, sess);
        }
        else {
        	sess.getRemote().sendString(createMessage("server", user, timestamp(), "could not identify user", 400));
        }
	}

	@Override
	public void onString(Session sess, String message) throws IOException {
		String dateTime = timestamp();
		String user = extractSessionUser(sess);
        
        //broadcast out message
        for(String to : USERS.keySet()) {
        	Session dest = USERS.get(to);
            dest.getRemote().sendString(createMessage(user, to, dateTime, message));
		};
	}

	@Override
	public void onBinary(Session sess, byte[] payload, int offset, int len) throws IOException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void onClose(Session sess, int statusCode, String reason) throws IOException {
		String user = extractSessionUser(sess);
		if (USERS.keySet().contains(user)) {
            Session dest = USERS.remove(user);
            dest.close();
        }
	}

	@Override
	public void onError(Throwable cause) throws IOException {
		String dateTime = timestamp();
		for(String to : USERS.keySet()) {
        	Session dest = USERS.get(to);
            dest.getRemote().sendString(createMessage("server", to, dateTime, cause.getMessage()));
		};
	}

	@Override
	public void sendString(String message) throws IOException {
		String dateTime = timestamp();
		for(String to : USERS.keySet()) {
        	Session dest = USERS.get(to);
            dest.getRemote().sendString(createMessage("server", to, dateTime, message));
		};
	}

	@Override
	public void sendPartial(String message, Boolean isLast) throws IOException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void close() {
		for(String user : USERS.keySet()) {
			Session sess = USERS.get(user);
			sess.close();
		}
	}

	@Override
	public String timestamp() {
		return new SimpleDateFormat("dd MMM, yy 'at' mm:hh:ssa").format(new Date());
	}
}
