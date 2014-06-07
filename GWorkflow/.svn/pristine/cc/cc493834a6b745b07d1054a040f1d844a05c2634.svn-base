package com.karimovceyhun.workflow.servlets;
import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



@WebServlet("/Image")
public class ImageServlet  extends HttpServlet
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1971336425149550036L;
	
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		getImage(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException 
	{
		getImage(req, resp);
	}
	
    private void getImage( HttpServletRequest req, HttpServletResponse resp)throws IOException
    {
    	byte[] data = (byte[]) req.getSession().getAttribute("imageInByte");
        //byte[] data = (byte[]) req.getSession().getAttribute("imageInByte");
    	ServletOutputStream op = resp.getOutputStream();

        op.write(data,0,data.length);

        op.flush();
        op.close();
        
    }


}
