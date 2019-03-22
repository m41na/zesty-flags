package com.practicaldime.play;

import java.util.Collections;
import java.util.Map;

import javax.servlet.MultipartConfigElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.practicaldime.zesty.app.AppServer;
import com.practicaldime.zesty.servlet.HandlerConfig;

public class App {
	
	private static final Logger LOG = LoggerFactory.getLogger(App.class);
	
	public static void main(String[] args) {
		int port = 1337;
		String host = "localhost";
		
		Map<String, String> props = Maps.newHashMap();
		props.put("appctx", "/");
		props.put("assets", "play");
		
		HandlerConfig bounce = holder -> {
			holder.setAsyncSupported(true);
		};
		
		HandlerConfig upload = holder -> {
			holder.setName("fileUpload");
			MultipartConfigElement mpce = new MultipartConfigElement("temp", 1024 * 1024 * 50, 1024 * 1024, 5);
			holder.getRegistration().setMultipartConfig(mpce);
		};

		AppServer router = new AppServer(props).router();
		router
		.servlet("/bounce", bounce, new EchoServlet())
		.servlet("/upload", upload, new UploadServlet())
		.get("/upload", (req, res)-> {
			res.render("upload.html", Collections.emptyMap());
			return null;
		})
		.listen(port, host, (msg) -> {
			LOG.info(msg);
		});		
	}
}
