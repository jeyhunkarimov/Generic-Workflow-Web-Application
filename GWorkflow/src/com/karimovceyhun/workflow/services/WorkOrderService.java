package com.karimovceyhun.workflow.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.karimovceyhun.workflow.data.User;
import com.karimovceyhun.workflow.data.WorkOrder;
import com.karimovceyhun.workflow.interfaces.IUserService;
import com.karimovceyhun.workflow.interfaces.IWorkOrderService;

public class WorkOrderService extends Service implements IWorkOrderService,Serializable 
{

	private static final long serialVersionUID = -3466278001514900198L;

	public WorkOrderService(SessionFactory sessionFactory) {
		super(sessionFactory);
		// TODO Auto-generated constructor stub
	}

	public List<WorkOrder> getWOIncludingMe(Long userId)
	{
		DetachedCriteria dc = DetachedCriteria.forClass(WorkOrder.class);
		List<Long> userIds = new ArrayList<Long>();
		userIds.add(userId);
		dc.createCriteria("workflow").createCriteria("userAbleToMonitor", "uam").add(Restrictions.eq("uam.id", userId));
		return  ((IUserService)ServiceFinder.findBean("UserService")).list(WorkOrder.class, dc);
	}

	public void addUserToMonitoringList(Long userId,Long woId)
	{
		User usr = this.find(User.class, userId);
		WorkOrder wo = this.find(WorkOrder.class, woId);
		if(wo.getMonitoringUser() == null)
		{
			wo.setMonitoringUser(new ArrayList<User>());
		}
		wo.getMonitoringUser().add(usr);
		this.save(wo);
	}
	
	public void removeUserFromMonitoringList(Long userId,Long woId)
	{
		User usr = this.find(User.class, userId);
		WorkOrder wo = this.find(WorkOrder.class, woId);
		if(wo.getMonitoringUser() == null)
		{
			wo.setMonitoringUser(new ArrayList<User>());
		}
		wo.getMonitoringUser().remove(usr);
		this.save(wo);
	}
	
	
}
