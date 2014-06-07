package com.karimovceyhun.workflow.interfaces;

import java.io.Serializable;
import java.util.List;

import com.karimovceyhun.workflow.data.WorkOrder;

public interface IWorkOrderService  extends IService,Serializable
{
	public List<WorkOrder> getWOIncludingMe(Long userId);
	public void removeUserFromMonitoringList(Long userId,Long woId);
	public void addUserToMonitoringList(Long userId,Long woId);
}
