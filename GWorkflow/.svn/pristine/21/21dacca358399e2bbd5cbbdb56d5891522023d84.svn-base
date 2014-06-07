package com.karimovceyhun.workflow.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import com.karimovceyhun.workflow.data.Workflow;
import com.karimovceyhun.workflow.interfaces.IWorkflowService;
import com.karimovceyhun.workflow.services.ServiceFinder;

public class WorkflowConverter implements Converter {

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
            	IWorkflowService ius = (IWorkflowService)ServiceFinder.findBean("WorkflowService");
            	return ius.findWorkflowbyName(value);
            } 
            catch(NumberFormatException exception) 
            {  
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid player"));  
            }  
        }  

	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) throws ConverterException {
		if (value == null || value.equals("")) {  
            return "";  
        } else {  
            return String.valueOf(((Workflow) value).getName());  
        }
	}

}
