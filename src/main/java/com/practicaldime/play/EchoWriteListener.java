package com.practicaldime.play;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.AsyncContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

public class EchoWriteListener implements WriteListener {

	private ServletOutputStream output = null;
	private AsyncContext context = null;
	private ByteArrayOutputStream baos = new ByteArrayOutputStream();
	private boolean done = false;

	public EchoWriteListener(ServletOutputStream output, AsyncContext context) {
		super();
		this.output = output;
		this.context = context;
	}
	
	public synchronized void readWrite(byte[] bytes, int len, ServletOutputStream output) throws IOException {
		if(bytes == null) {
			baos.writeTo(output);
			baos.flush();
			baos.reset();
		}
		else {
			baos.write(bytes, 0, len);
		}
	}
	
	public void done() {
		this.done = true;
	}

	public void onWritePossible() throws IOException {       
		while (baos.size() > 0  && output.isReady()) {
			readWrite(null, 0, output);
		}
		if (done && baos.size() == 0) {
			context.complete();
		}
	}
	
	public void onError(final Throwable t) {
        context.complete();
        t.printStackTrace();
    }
}
