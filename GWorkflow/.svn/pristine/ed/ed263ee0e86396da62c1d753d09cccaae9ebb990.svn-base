package com.karimovceyhun.workflow.data;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.karimovceyhun.workflow.data.Bean;

@Entity
@Table(name="collector")
public class Collector extends Node implements Bean,Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7483734325348712174L;
	
	Node succeedingProcess;
	List<Node> predecessorProcesses;
	
	@OneToOne(cascade=CascadeType.ALL)
	public Node getSucceedingProcess() {
		return succeedingProcess;
	}
	public void setSucceedingProcess(Node succeedingProcess) {
		this.succeedingProcess = succeedingProcess;
	}
	
	@ManyToMany(cascade=CascadeType.ALL)
	public List<Node> getPredecessorProcesses() {
		return predecessorProcesses;
	}
	public void setPredecessorProcesses(List<Node> predecessorProcesses) {
		this.predecessorProcesses = predecessorProcesses;
	}
	
	
}
