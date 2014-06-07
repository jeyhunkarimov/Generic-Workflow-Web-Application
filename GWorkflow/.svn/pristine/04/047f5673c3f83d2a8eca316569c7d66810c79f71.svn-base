package com.karimovceyhun.workflow.converter;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.karimovceyhun.workflow.data.User;
import com.karimovceyhun.workflow.interfaces.IUserService;
import com.karimovceyhun.workflow.services.ServiceFinder;

public class UserConverter implements Converter {

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
            	IUserService ius = (IUserService)ServiceFinder.findBean("UserService");
            	DetachedCriteria detachedCriteria = DetachedCriteria.forClass(User.class);
        		detachedCriteria.add(Restrictions.eq("fullName",value));
        		List<User> userList = ius.list(User.class,detachedCriteria);
            	return userList.get(0);  
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
            return String.valueOf(((User) value).getFullName());  
        }
	}

}
