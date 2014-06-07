package com.karimovceyhun.workflow.data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.FetchMode;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Table(name = "process")
public class Process extends TaskNode implements Bean,Serializable
{

	/**
	 * 
	 */
    	public static int TYPE = 1;
	public static int PROCESS_TYPE = 1;
	private static final long serialVersionUID = 6678185955274830021L;


	Node succeedingProcess;
	

	
	@OneToOne(cascade=CascadeType.ALL)
	public Node getSucceedingProcess() {
		return succeedingProcess;
	}
	
	public void setSucceedingProcess(Node succeedingProcess) {
		this.succeedingProcess = succeedingProcess;
	}

	@Transient
	@Override
	public int getTaskNodeType() {
	    return 1;
	}
}
