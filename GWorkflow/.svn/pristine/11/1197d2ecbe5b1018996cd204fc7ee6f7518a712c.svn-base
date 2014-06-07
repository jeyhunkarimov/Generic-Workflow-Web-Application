package com.karimovceyhun.workflow.interfaces;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import com.karimovceyhun.workflow.data.Workflow;

public interface IWorkflowService  extends IService,Serializable
{
	public Workflow findWorkflow(Long id);
	public List<Workflow> list(int firstRow,int rowCount, String sortField, boolean sortAscending, DetachedCriteria criteriaaa);
	
	public void save(Workflow workflow);
	public Workflow findWorkflowbyName(String name);
	
	public List < Workflow > findWorkflowsDueToUserAbleToOpen(Long userId);
}
