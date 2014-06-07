package com.karimovceyhun.workflow.data;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="statuschange")
public class StatusChange implements Bean, Serializable{
	
	/**
	 * 
	 */
	public static List<StatusChange> allStatusChanges;
	private static final long serialVersionUID = -2160719392870808548L;
	
	private Long id;
	private Status oldStatus;
	private Status newStatus;
	private String action;
	
	@Id
	@GeneratedValue
	@Override
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne
	public Status getOldStatus() {
		return oldStatus;
	}

	public void setOldStatus(Status oldStatus) {
		this.oldStatus = oldStatus;
	}

	@ManyToOne
	public Status getNewStatus() {
		return newStatus;
	}

	public void setNewStatus(Status newStatus) {
		this.newStatus = newStatus;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
	public String getAction() {
		return action;
	}
	
}
