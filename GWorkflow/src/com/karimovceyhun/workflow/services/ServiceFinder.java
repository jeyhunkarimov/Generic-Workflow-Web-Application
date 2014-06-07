package com.karimovceyhun.workflow.services;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class ServiceFinder 
{

	/**
	 * Service finder class to find the service. Uses webapplicationcontext to
	 * fetch the beans.
	 * 
	 * @param beanName
	 * @return
	 */
	public static Object findBean(String beanName) 
	{
		FacesContext context = FacesContext.getCurrentInstance();

		ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();
		ApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		Object o = appContext.getBean(beanName);
		return o;
	}

	public static Object findBean(String beanName, ServletContext context) 
	{

		ApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(context);
		Object o = appContext.getBean(beanName);

		return o;

	}

}
