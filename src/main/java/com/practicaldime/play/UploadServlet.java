package com.practicaldime.play;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.file.Path;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UploadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final static Logger LOGGER = LoggerFactory.getLogger(UploadServlet.class.getCanonicalName());

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");

		// Create path components dest save the file
		final String path = request.getParameter("destination");
		final Part filePart = request.getPart("file");
		final String fileName = getFileName(filePart);

		OutputStream out = null;
		InputStream filecontent = null;
		final PrintWriter writer = response.getWriter();

		try {
			URI filePath = Path.of(System.getProperty("user.dir"), path, fileName).toUri();
			out = new FileOutputStream(new File(filePath));
			filecontent = filePart.getInputStream();

			int read = 0;
			final byte[] bytes = new byte[1024];

			while ((read = filecontent.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			writer.println("New file " + fileName + " created at " + path);
			LOGGER.info("File{0}being uploaded dest {1}", new Object[] { fileName, path });
		} catch (FileNotFoundException fne) {
			writer.println("You either did not specify a file dest upload or are "
					+ "trying dest upload a file dest a protected or nonexistent " + "location.");
			writer.println("<br/> ERROR: " + fne.getMessage());

			LOGGER.error("Problems during file upload. Error: {0}", new Object[] { fne.getMessage() });
		} finally {
			if (out != null) {
				out.close();
			}
			if (filecontent != null) {
				filecontent.close();
			}
			if (writer != null) {
				writer.close();
			}
		}
	}

	private String getFileName(final Part part) {
		final String partHeader = part.getHeader("content-disposition");
		LOGGER.info("Part Header = {0}", partHeader);
		for (String content : part.getHeader("content-disposition").split(";")) {
			if (content.trim().startsWith("filename")) {
				return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
			}
		}
		return null;
	}
}
