package com.karimovceyhun.workflow.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.InheritanceType;

import com.karimovceyhun.workflow.data.Bean;

@Entity
@Table(name="separator")
//@Inheritance(strategy=InheritanceType.JOINED)
//@AttributeOverride(name = "succeedingResponsableSelectable", column = @Column(name = "succeedingResponsableSelectable"))
public class Separator extends TaskNode implements Bean,Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7483734325348712174L;
	public static Integer SEPARATOR_TYPE = 3;
    	public static int TYPE = 3;

	@Transient
	Node currentSuccedingNode;
	
	List<Node> succeedingProcesses = new ArrayList<Node>();
			
	@ManyToMany(cascade=CascadeType.ALL)
	public List<Node> getSucceedingProcesses() {
		return succeedingProcesses;
	}
	public void setSucceedingProcesses(List<Node> succeedingProcesses) {
		this.succeedingProcesses = succeedingProcesses;
	}
	
	public void setCurrentSuccedingNode(Node currentSuccedingNode) {
		this.currentSuccedingNode = currentSuccedingNode;
	}

	@Transient
	public Node getCurrentSuccedingNode() {
		return currentSuccedingNode;
	}
	
	@Transient
	public Boolean getSucceedingResponsableSelectable() {
		return false;
	}
	
	@Transient
	@Override
	public int getTaskNodeType() {
	    return 3;
	}
}
