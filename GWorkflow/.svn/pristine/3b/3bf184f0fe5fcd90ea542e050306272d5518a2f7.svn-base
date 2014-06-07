package com.karimovceyhun.workflow.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;

import com.karimovceyhun.workflow.data.Bean;
import com.karimovceyhun.workflow.data.Status;
import com.karimovceyhun.workflow.data.StatusChange;
import com.karimovceyhun.workflow.interfaces.IStatusService;

public class StatusService extends Service implements IStatusService,Serializable 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2077371760011461507L;

	public StatusService(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public Status findStatus(Integer forNode, int statusType) 
	{
		if(Status.allStatuses == null || Status.allStatuses.size() == 0)
		{
			Status.allStatuses = this.list(Status.class);
		}
		for(Status status:Status.allStatuses)
		{
			if(status.getForNode().equals(forNode)  && status.getStatusType() == statusType)
			{
				return status;
			}
		}
		return null;
	}


	public List<Status> getChangedStatuses(Long oldStatusId)
	{
		if(StatusChange.allStatusChanges == null || StatusChange.allStatusChanges.size() == 0)
		{
			StatusChange.allStatusChanges = this.list(StatusChange.class);
		}
		List<Status> ret = new ArrayList<Status>();
		for(StatusChange sc:StatusChange.allStatusChanges)
		{
			if(sc.getOldStatus().getId().equals(oldStatusId))
			{
				ret.add(sc.getNewStatus());
			}
		}
		return ret;
	}


}
