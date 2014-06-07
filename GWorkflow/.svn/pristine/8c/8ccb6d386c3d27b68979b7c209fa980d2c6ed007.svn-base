package com.karimovceyhun.workflow.data;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.karimovceyhun.workflow.data.Bean;

@Entity
@Table(name="terminate")
public class Terminate extends Node implements Bean,Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7483734325348712174L;

	Workflow nextWorkflow;
	
	@OneToOne
	public Workflow getNextWorkflow() {
		return nextWorkflow;
	}
	
	public void setNextWorkflow(Workflow nextWorkflow) {
		this.nextWorkflow = nextWorkflow;
	}
	
	
}
