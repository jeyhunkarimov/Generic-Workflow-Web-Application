package com.karimovceyhun.workflow.interfaces;

import java.io.Serializable;
import java.util.List;

import com.karimovceyhun.workflow.data.Status;
import com.karimovceyhun.workflow.data.StatusChange;

public interface IStatusService extends IService,Serializable  
{
	public Status findStatus(Integer forNode,int statusType);
	
	public List<Status> getChangedStatuses(Long oldStatusId);
}
