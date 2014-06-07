package com.karimovceyhun.workflow.data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="project")
public class Project implements Bean,Serializable
{
	private static final long serialVersionUID = -8322424681802213218L;
	private Long id;
	private String name;
	private Set<User> users;
	private User manager;
	
	

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

	public String getName() 
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}
	
	@ManyToMany(cascade=CascadeType.ALL)
	public Set<User> getUsers() 
	{
		return users;
	}


	public void setUsers(Set<User> users) 
	{
		this.users = users;
	}
	
	@Override
	public boolean equals(Object obj) {
		if( obj instanceof Project )
		{
			return ((Project)obj).getId().equals(getId());
		}
		return super.equals(obj);
	}
	
	@ManyToOne
	public User getManager() {
		return manager;
	}

	public void setManager(User manager) {
		this.manager = manager;
	}
}
