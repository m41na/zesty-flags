package com.practicaldime.play;

import java.io.IOException;

import javax.servlet.AsyncContext;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;

public class EchoReadListener implements ReadListener {

	private ServletInputStream input = null;
	private AsyncContext ac = null;
	EchoWriteListener writeListener = null;

	public EchoReadListener(ServletInputStream input, EchoWriteListener writeListener, AsyncContext ac) {
		super();
		this.input = input;
		this.ac = ac;
		this.writeListener = writeListener;
	}

	public void onDataAvailable() throws IOException {
		System.out.println("Data is available");

		int len = -1;
		byte bytes[] = new byte[1024];
		while (input.isReady() && (len = input.read(bytes)) != -1) {
			writeListener.readWrite(bytes, len, null);
		}
	}

	public void onAllDataRead() throws IOException {
		System.out.println("Data is all read");
		writeListener.done();
	}

	public void onError(final Throwable t) {
		ac.complete();
		t.printStackTrace();
	}
}
