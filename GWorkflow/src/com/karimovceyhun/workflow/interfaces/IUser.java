package com.karimovceyhun.workflow.interfaces;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.karimovceyhun.workflow.data.Bean;

@Entity
public interface IUser extends Bean
{
	public String getFullName();
	
	public void setFullName(String fullName);
	
	public String getEmail();
	
	public void setEmail(String email);
	
	public String getPassword();
	
	public void setPassword(String password);
}
