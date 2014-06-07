package com.karimovceyhun.workflow.data;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="task")
public class Task implements Bean, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2160719392870808548L;

	private Long id;
	private User responsible;
	private Date startTime;
	private Date endTime;
	private TaskNode node;
	private Status status ;
	private WorkOrder workorder;
	private Task previousTask;
	private Long priority;
	private Date deadline;

	
	
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
	public TaskNode getNode() {
		return node;
	}
	
	public void setNode(TaskNode node) {
		this.node = node;
	}
	
	@OneToOne
	public User getResponsible() {
		return responsible;
	}
	public void setResponsible(User responsible) {
		this.responsible = responsible;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	@ManyToOne
	public Status getStatus() {
		return status;
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}
	
	public void setWorkorder(WorkOrder workorder) {
		this.workorder = workorder;
	}
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "workorder_id", insertable = true, updatable = true)
	public WorkOrder getWorkorder() {
		return workorder;
	}
	
	@OneToOne
	public Task getPreviousTask() {
		return previousTask;
	}

	public void setPreviousTask(Task previousTask) {
		this.previousTask = previousTask;
	}
	
	public Long getPriority()
	{
		return priority;
	}

	public void setPriority( Long priority )
	{
		this.priority = priority;
	}
	
	public Date getDeadline() {
	    return deadline;
	}

	public void setDeadline(Date deadline) {
	    this.deadline = deadline;
	}



}
