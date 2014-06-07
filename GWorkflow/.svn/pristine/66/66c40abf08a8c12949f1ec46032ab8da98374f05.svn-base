package com.karimovceyhun.workflow.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User implements Bean,Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5946300123705257752L;
	private String fullName;
	private Long id;
	private String email;
	private String password;
	
	@Id
	@Override
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() 
	{
		return id;
	}

	@Override
	public void setId(Long id) 
	{
	    this.id = id;
	}
	@Column(unique=true)
	public String getFullName() 
	{
		return fullName;
	}

	public void setFullName(String fullName) 
	{
		this.fullName = fullName;
	}

	public String getEmail() 
	{
		return email;
	}

	public void setEmail(String email) 
	{
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) 
	{
		this.password = password;
	}

	@Override
	public boolean equals(Object obj) {
		if( obj instanceof User )
		{
			return ((User)obj).getId().equals(getId());
		}
		return super.equals(obj);
	}
	
//	@Override
//	public Object clone() {
//
//        User obj = new User();
//        obj.setEmail(this.email);
//        obj.setFullName(this.fullName);
//        obj.setId(this.id);
//        obj.setPassword(this.password);
//        return obj;
//    }
}
