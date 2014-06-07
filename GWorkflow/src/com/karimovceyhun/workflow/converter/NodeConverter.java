package com.karimovceyhun.workflow.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import com.karimovceyhun.workflow.data.Node;
import com.karimovceyhun.workflow.managed.configuration.WorkflowEditorManagedBean;
public class NodeConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) throws ConverterException {

        if (value == null || value.trim().equals("")) {  
            return null;  
        } else {  
            try {  
            	WorkflowEditorManagedBean bean = (WorkflowEditorManagedBean) context.getApplication().getVariableResolver().resolveVariable(context, "workflowEditorManagedBean");
  
                for (Node e : bean.getSucceedingNodes()) {  
                    if ( e.getName().equals(value) ) {  
                        return e;  
                    }  
                }  
  
            } catch(NumberFormatException exception) {  
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid player"));  
            }  
        }  
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) throws ConverterException {
		if (value == null || value.equals("")) {  
            return "";  
        } else {  
            return String.valueOf(((Node) value).getName());  
        }
	}

}
