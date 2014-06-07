package com.karimovceyhun.workflow.data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Table(name="workorder")
public class WorkOrder extends AttachmentHolder implements Bean,Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7483734325348712174L;

	//TODO field values;
	String summary;
	Date startDate;
	Date endDate;
	List<User> monitoringUser;
	User requesterUser;
	List<Task> tasks;
	List<History> history;
	List<Note> notes;
	Workflow workflow;
	Project project;
	Set<FieldValue> fieldValues;
	Date dueDate;
	
	
	public void setFieldValues(Set<FieldValue> fieldValues) {
		this.fieldValues = fieldValues;
	}
	
	@ManyToMany(cascade=CascadeType.ALL)
	public Set<FieldValue> getFieldValues() {
		return fieldValues;
	}
	
	public void setProject(Project project) {
		this.project = project;
	}
	
	@ManyToOne
	public Project getProject() {
		return project;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	@ManyToMany
	@LazyCollection(LazyCollectionOption.FALSE)

	public List<User> getMonitoringUser() {
		return monitoringUser;
	}
	public void setMonitoringUser(List<User> monitoringUser) {
		this.monitoringUser = monitoringUser;
	}
	
	@OneToOne
	public User getRequesterUser() 
	{
		return requesterUser;
	}
	public void setRequesterUser(User requesterUser) {
		this.requesterUser = requesterUser;
	}
	
	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="workorder")
	//@LazyCollection(LazyCollectionOption.FALSE)
	public List<Task> getTasks() {
		return tasks;
	}
	
	@ManyToMany
	public List<History> getHistory() {
		return history;
	}
	public void setHistory(List<History> history) {
		this.history = history;
	}
	
	@ManyToMany
	public List<Note> getNotes() {
		return notes;
	}
	public void setNotes(List<Note> notes) {
		this.notes = notes;
	}
	
	public void setWorkflow(Workflow workflow) {
		this.workflow = workflow;
	}
	
	@OneToOne
	public Workflow getWorkflow() {
		return workflow;
	}
	
	public String getSummary() {
		return summary;
	}
	
	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	public Date getDueDate()
	{
		return dueDate;
	}

	public void setDueDate( Date dueDate )
	{
		this.dueDate = dueDate;
	}

}
