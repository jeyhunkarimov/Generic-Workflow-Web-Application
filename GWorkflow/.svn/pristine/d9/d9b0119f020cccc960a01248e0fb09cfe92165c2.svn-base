package com.karimovceyhun.workflow.data;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="status")
public class Status implements Bean, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2160719392870808548L;
	
	public static int PROCESS = 1;
	public static int DECISION = 2;
	public static int SEPERATOR = 3;
	public static List<Status> allStatuses; 
	
	public static int NEW = 1;
	public static int ASSIGNED = 2;
	public static int CLOSED = 3;
	public static int ACCEPTED = 4;
	public static int REJECTED = 5;
	public static int OTHER = 0;
	
	private Long id;
	private String label;
	private Integer forNode;
	private Integer statusType = OTHER;
	private String color;
	
	@Id
	@GeneratedValue
	@Override
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public Integer getForNode() {
		return forNode;
	}
	
	public void setForNode(Integer forNode) {
		this.forNode = forNode;
	}
	
	public int getStatusType() {
		return statusType;
	}
	
	public void setStatusType(int statusType) {
		this.statusType = statusType;
	}
	
	public String getColor() {
		return color;
	}
	
	public void setColor(String color) {
		this.color = color;
	}
	
	@Override
	public boolean equals(Object obj) {
		if( obj instanceof Status )
		{
			return ((Status)obj).getId().equals(getId());
		}
		return super.equals(obj);
	}
	
}
