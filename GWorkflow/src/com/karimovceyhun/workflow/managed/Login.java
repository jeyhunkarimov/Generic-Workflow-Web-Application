package com.karimovceyhun.workflow.managed;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.karimovceyhun.workflow.data.User;
import com.karimovceyhun.workflow.interfaces.IUserService;
import com.karimovceyhun.workflow.services.ServiceFinder;




@ManagedBean(name="login")
@RequestScoped
public class Login implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1964179001486153178L;
	private String email;
	private String password;
	private String superUserId = "admin";
	private String superUserPassword = "admin";


	public String login() 
	{
		
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        request.getSession().invalidate();
		// is it super user
		// super user i set ediyoruz ve logine success donuyoruz.
		if(email.equals(superUserId) && password.equals(superUserPassword))
		{
	        request.getSession().setAttribute("isLogined", new Boolean(true));
	        request.getSession().setAttribute("superUser", new Boolean(true));
	        /**
	         * This user has access to anything.
	         * we need to be careful about user spesific pages for it.
	         */
			return "success";
		}
		
		
		// otherwise
		IUserService userService = (IUserService) (ServiceFinder.findBean("UserService"));
		User user;
		if((user = userService.checkForTheUser(getEmail(), getPassword())) != null)
		{
			request.getSession().setAttribute("superUser", new Boolean(false));
	        request.getSession().setAttribute("isLogined", new Boolean(true));
			request.getSession().setAttribute("id", user.getId());
			request.getSession().setAttribute("name", user.getFullName());

			return "success";
		}
		else
		{
			System.out.println("false");
			return "failed";
		}	
	}
	
	
	public String getPassword() 
	{
		return password;
	}
	public void setPassword(String password) 
	{
		this.password = password;
	}
	public String getEmail() 
	{
		return email;
	}
	public void setEmail(String email) 
	{
		this.email = email;
	}
	
	public String getSuperUserId() 
	{
		return superUserId;
	}

	public void setSuperUserId(String superUserId) 
	{
		this.superUserId = superUserId;
	}


	public String getSuperUserPassword() 
	{
		return superUserPassword;
	}

	public void setSuperUserPassword(String superUserPassword) 
	{
		this.superUserPassword = superUserPassword;
	}
	
	
	
	public void forgetPassword()
	{
		String email = getEmail().trim();
		if(!email.equals(""))
		{
			IUserService userService = (IUserService) (ServiceFinder.findBean("UserService"));
			try
			{
				userService.forgetPassword(email);
				String messageText = "Mail sent";
				FacesMessage message = new FacesMessage(messageText, messageText);
				FacesContext.getCurrentInstance().addMessage(null, message);
				
			}
			catch(Exception e)
			{
				String messageText = "Mail could not be sent";
				FacesMessage message = new FacesMessage(messageText, messageText);
				FacesContext.getCurrentInstance().addMessage(null, message);
			}
			
		}
		else
		{
			String messageText = "Please enter the email address";
			FacesMessage message = new FacesMessage(messageText, messageText);
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
		
	}

}
