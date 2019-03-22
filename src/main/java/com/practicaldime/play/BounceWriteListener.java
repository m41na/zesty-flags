package com.practicaldime.play;

import java.io.IOException;
import java.util.Queue;

import javax.servlet.AsyncContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

public class BounceWriteListener implements WriteListener {

	private ServletOutputStream output = null;
	private Queue<String> queue = null;
	private AsyncContext context = null;

	public BounceWriteListener(ServletOutputStream output, Queue<String> queue, AsyncContext context) {
		super();
		this.output = output;
		this.queue = queue;
		this.context = context;
	}

	public void onWritePossible() throws IOException {
		while (queue.peek() != null && output.isReady()) {
			String data = (String) queue.poll();
			output.print(data);
		}
		if (queue.peek() == null) {
			context.complete();
		}
	}
	
	public void onError(final Throwable t) {
        context.complete();
        t.printStackTrace();
    }
}
