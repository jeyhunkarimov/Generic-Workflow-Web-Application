package com.karimovceyhun.workflow.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import com.karimovceyhun.workflow.data.Project;
import com.karimovceyhun.workflow.interfaces.IProjectService;
import com.karimovceyhun.workflow.services.ServiceFinder;

public class ProjectConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) throws ConverterException {

        if (value == null || value.trim().equals("")) {  
            return null;  
        } else {  
            try {  
            	IProjectService projectService = (IProjectService) ServiceFinder.findBean("ProjectService");
            	return projectService.findProjectByName(value);
  
            } catch(NumberFormatException exception) {  
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
            return String.valueOf(((Project) value).getName());  
        }
	}

}
