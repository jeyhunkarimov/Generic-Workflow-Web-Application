package com.karimovceyhun.workflow.servlets;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/FileDownload")
public class FileDownloadServlet  extends HttpServlet
{

	private static final long serialVersionUID = -5718455032674995758L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		download(req,resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException 
	{
		download(req,resp);
	}
	
	private void download(HttpServletRequest request, HttpServletResponse response)
	{
		response.setContentType("application/force-download");
		String filePath = request.getParameter("path");
		response.addHeader("Content-Disposition", "attachment; filename=\"" + filePath + "\"");
		byte[] buf = new byte[1024];
		try{
		  File file = new File(filePath);
		  long length = file.length();
		  BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
		  ServletOutputStream out = response.getOutputStream();
		  response.setContentLength((int)length);
		  while ((in != null) && ((length = in.read(buf)) != -1)) {
		    out.write(buf, 0, (int)length);
		  }
		  in.close();
		  out.close();
		}catch (Exception exc){
		  exc.printStackTrace();
		}   
	}
	
}