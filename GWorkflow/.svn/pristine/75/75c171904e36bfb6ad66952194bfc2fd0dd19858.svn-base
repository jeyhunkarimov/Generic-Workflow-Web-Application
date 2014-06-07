package com.karimovceyhun.workflow.data;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.karimovceyhun.workflow.data.Bean;

@Entity
@Table(name="node")
//@Inheritance(strategy=InheritanceType.JOINED)
//@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public class Node implements Bean,Serializable {
	private Long id;
	private String name;
	private Object graphObject;
	private Integer type;
	


	private List<EmailNotification> emailNotifications;
	
	public void setEmailNotifications(List<EmailNotification> emailNotifications) 
	{
		this.emailNotifications = emailNotifications;
	}
	
	@ManyToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	public List<EmailNotification> getEmailNotifications() {
		return emailNotifications;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = -2787071582041006861L;
	
	public void setId(Long id) 
	{
		this.id = id;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	@Transient
	public Object getGraphObject() {
		return graphObject;
	}

	public void setGraphObject(Object graphObject) {
		this.graphObject = graphObject;
	}
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
}
