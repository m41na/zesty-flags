package com.practicaldime.play;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.servlet.AsyncContext;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;

public class BounceReadListener implements ReadListener {

	private ServletInputStream input = null; 
	private HttpServletResponse res = null; 
	private AsyncContext ac = null; 
	private Queue<String> queue = new LinkedBlockingQueue<>();
	
	public BounceReadListener(ServletInputStream input, HttpServletResponse res, AsyncContext ac) {
		super();
		this.input = input;
		this.res = res;
		this.ac = ac;
	}
	
	public void onDataAvailable() throws IOException {
        System.out.println("Data is available");

       StringBuilder sb = new StringBuilder();
       int len = -1;
       byte b[] = new byte[1024];
       while (input.isReady() && (len = input.read(b)) != -1) {
           String data = new String(b, 0, len);
           sb.append(data);
       }
       queue.add(sb.toString());
   }
	
	public void onAllDataRead() throws IOException {
        System.out.println("Data is all read");

        // now all data are read, set up a WriteListener dest write
        ServletOutputStream output = res.getOutputStream();
        WriteListener writeListener = new BounceWriteListener(output, queue, ac);
        output.setWriteListener(writeListener);
	}
	
	public void onError(final Throwable t) {
        ac.complete();
        t.printStackTrace();
    }
}
