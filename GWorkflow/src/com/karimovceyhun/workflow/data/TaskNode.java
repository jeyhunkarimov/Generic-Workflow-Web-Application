package com.karimovceyhun.workflow.data;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.karimovceyhun.workflow.data.Bean;

@Entity
@Table(name="tasknode")
//@Inheritance(strategy=InheritanceType.JOINED)
//@PrimaryKeyJoinColumn(name="id")
public class TaskNode extends Node implements Bean,Serializable 
{
	private static final long serialVersionUID = -2787071582041006861L;
	
	private Set<User> assignableUser;
	private String description;
	private Boolean succeedingResponsableSelectable;
	private Boolean projectDependant = Boolean.FALSE;
	private Double estimatedTime;// in hours
	private int taskNodeType;
	

	public Boolean getSucceedingResponsableSelectable() {
		return succeedingResponsableSelectable;
	}
	
	public void setSucceedingResponsableSelectable(
			Boolean succeedingResponsableSelectable) {
		this.succeedingResponsableSelectable = succeedingResponsableSelectable;
	}
	
	public void setAssignableUser(Set<User> assignableUser) {
		this.assignableUser = assignableUser;
	}
	
	@ManyToMany(fetch = FetchType.EAGER)
	public Set<User> getAssignableUser() {
		return assignableUser;
	}
	
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setProjectDependant(Boolean projectDependant) {
		this.projectDependant = projectDependant;
	}
	
	public Boolean getProjectDependant() {
		return projectDependant;
	}

	public Double getEstimatedTime()
	{
		return estimatedTime;
	}

	public void setEstimatedTime( Double estimatedTime )
	{
		this.estimatedTime = estimatedTime;
	}
	
	@Transient
	public int getTaskNodeType() {
	    return taskNodeType;
	}

	public void setTaskNodeType(int taskNodeType) {
	    this.taskNodeType = taskNodeType;
	}


}
