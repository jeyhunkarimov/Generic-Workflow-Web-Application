package com.karimovceyhun.workflow.menu;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletRequest;

import com.karimovceyhun.workflow.data.User;
import com.karimovceyhun.workflow.interfaces.IUserService;
import com.karimovceyhun.workflow.managed.ListingManagedBean;
import com.karimovceyhun.workflow.services.ServiceFinder;

@ManagedBean(name = "passwordBean")
@RequestScoped
public class PasswordBean extends ListingManagedBean<User>{
	
	public PasswordBean() {
		super("UserBean");
	}

	private String password;
	
	private String passwordAgain;
	
	private String passwordEx;
	
	private String result;
	
	public void setResult(String result) {
		this.result = result;
	}
	public String getResult() {
		return result;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword() {
		return password;
	}
	
	public void changePassword()
	{
		Long id=null;
		
		FacesContext context = FacesContext.getCurrentInstance ();
		HttpServletRequest request = ( HttpServletRequest ) context.getExternalContext ().getRequest ();
		try
		{
			id = ( Long ) request.getSession ().getAttribute ( "id" );
		}
		catch ( Exception e )
		{
			// TODO: handle exception
		}
		
		IUserService userService = (IUserService) ServiceFinder.findBean("UserService");
		
		User user = userService.findUser(id);
		
		if(user != null && user.getPassword().equals(passwordEx))
		{
			try
			{
				user.setPassword(this.getPassword());
				userService.save(user);
				this.setResult("Successful Update");
			}
			catch(Exception e)
			{
				this.setResult("Update failed");
			}		
		}
		else
		{
			this.setResult("Your old password is not correct.");
		}
	}
	@Override
	protected void loadData() {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void doBeforeSave(List<FacesMessage> messages) {
		// TODO Auto-generated method stub
		
	}
	public String getPasswordAgain() {
		return passwordAgain;
	}
	public void setPasswordAgain(String passwordAgain) {
		this.passwordAgain = passwordAgain;
	}
	public String getPasswordEx() {
		return passwordEx;
	}
	public void setPasswordEx(String passwordEx) {
		this.passwordEx = passwordEx;
	}
}
