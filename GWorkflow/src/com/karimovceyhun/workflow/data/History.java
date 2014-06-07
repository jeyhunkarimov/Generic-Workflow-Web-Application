package com.karimovceyhun.workflow.data;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.karimovceyhun.workflow.data.Bean;

@Entity
@Table(name="history")
public class History implements Bean,Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7483734325348712174L;

	Long id;
	Date dateModified;
	User user;
	String field;
	String chnge;
	
	
	@Id
	@GeneratedValue
	@Override
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getDateModified() {
		return dateModified;
	}
	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}
	
	@ManyToOne
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getChnge() {
		return chnge;
	}
	public void setChnge(String chnge) {
		this.chnge = chnge;
	}
	
	
}
