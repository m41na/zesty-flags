package com.practicaldime.play;

import java.io.IOException;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EchoServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		AsyncContext context = req.startAsync();
        // set up async listener
        context.addListener(new AsyncListener() {
            public void onComplete(AsyncEvent event) throws IOException {
                event.getSuppliedResponse().getOutputStream().print("Complete");

            }

            public void onError(AsyncEvent event) {
                System.out.println(event.getThrowable());
            }

            public void onStartAsync(AsyncEvent event) {
            }

            public void onTimeout(AsyncEvent event) {
                System.out.println("my asyncListener.onTimeout");
            }
        });
        
        ServletInputStream input = req.getInputStream();
        ServletOutputStream output = resp.getOutputStream();
        
        EchoWriteListener writeListener = new EchoWriteListener(output, context);
        output.setWriteListener(writeListener);
        
        ReadListener readListener = new EchoReadListener(input, writeListener, context);
        input.setReadListener(readListener);
	}
}
