package com.karimovceyhun.workflow.converter;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.karimovceyhun.workflow.data.Status;
import com.karimovceyhun.workflow.interfaces.IStatusService;
import com.karimovceyhun.workflow.services.ServiceFinder;

public class StatusConverter implements Converter
{

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,String value) throws ConverterException 
	{
		
		if (value == null || value.trim().equals("")) 
        {  
            return null;  
        } 
        else 
        {  
            try 
            {  
            	IStatusService iss = (IStatusService)ServiceFinder.findBean("StatusService");
            	DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Status.class);
        		detachedCriteria.add(Restrictions.eq("id",Long.valueOf(value)));
        		List<Status> statusList = iss.list(Status.class,detachedCriteria);
            	return statusList.get(0);  
            } 
            catch(NumberFormatException exception) 
            {  
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid player"));  
            }  
        }  
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,Object value) throws ConverterException 
	{
		if (value == null || value.equals("")) {  
            return "";  
        } else {  
            return String.valueOf(((Status) value).getId());  
        }
	}

}
