package com.karimovceyhun.workflow.data;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.karimovceyhun.workflow.data.Bean;

@Entity
@Table(name="decision")
public class Decision extends TaskNode implements Bean,Serializable{
	/**
	 * 
	 */
	public static Integer DESICION_TYPE = 2;
    	public static int TYPE = 2;

	private static final long serialVersionUID = 7483734325348712174L;

	Node acceptedsucceedingProcess;
	Node rejectedsucceedingProcess;
	String acceptLabel = "Accept";
	String rejectLabel = "Reject";
	
	
	@OneToOne(cascade=CascadeType.ALL)
	public Node getAcceptedsucceedingProcess() {
		return acceptedsucceedingProcess;
	}
	public void setAcceptedsucceedingProcess(Node acceptedsucceedingProcess) {
		this.acceptedsucceedingProcess = acceptedsucceedingProcess;
	}
	
	@OneToOne(cascade=CascadeType.ALL)
	public Node getRejectedsucceedingProcess() {
		return rejectedsucceedingProcess;
	}
	public void setRejectedsucceedingProcess(Node rejectedsucceedingProcess) {
		this.rejectedsucceedingProcess = rejectedsucceedingProcess;
	}
	public String getAcceptLabel() {
		return acceptLabel;
	}
	public void setAcceptLabel(String acceptLabel) {
		this.acceptLabel = acceptLabel;
	}
	public String getRejectLabel() {
		return rejectLabel;
	}
	public void setRejectLabel(String rejectLabel) {
		this.rejectLabel = rejectLabel;
	}
	
	@Transient
	@Override
	public int getTaskNodeType() {
	    return 2;
	}



	
}
