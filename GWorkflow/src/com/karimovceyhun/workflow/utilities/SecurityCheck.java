package com.karimovceyhun.workflow.utilities;

import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

public class SecurityCheck 
{
	
	public static boolean securityCheck(String page, String action) {

		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();

		Boolean loginInformation = (Boolean) request.getSession().getAttribute(
				"isLogined");

		Boolean superUser = (Boolean) request.getSession()
				.getAttribute("superUser");

		if(superUser != null && superUser.booleanValue())
		{
			return true;
		}
		else if(loginInformation != null && loginInformation.booleanValue())
		{
			// check for links
			List<String> links = (List<String>) request.getSession(true).getAttribute("links");
			
			boolean allow = false;
			
			if(links != null)
			{
				for(String link : links)
				{
					String[] linkaction = link.split("\\*");
					String linkUrl = linkaction[0];
					String actionUser = linkaction[1];
					                         
					if(actionUser.equals(action) && linkUrl.equals(page))
					{
						allow  = true;
						break;
					}
				}
			}
			
			return allow;
			
		}
		
		return true;
	}
}

