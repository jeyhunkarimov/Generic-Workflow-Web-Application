package com.karimovceyhun.workflow.managed;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.karimovceyhun.workflow.data.Project;
import com.karimovceyhun.workflow.data.User;
import com.karimovceyhun.workflow.data.WorkOrder;
import com.karimovceyhun.workflow.interfaces.IService;
import com.karimovceyhun.workflow.interfaces.IUserService;
import com.karimovceyhun.workflow.interfaces.IWorkOrderService;
import com.karimovceyhun.workflow.services.ServiceFinder;
import com.karimovceyhun.workflow.utilities.JSFUtil;

@ManagedBean(name="monitoringPageSelectionManagedBean")
@ViewScoped
public class MonitoringPageSelectionManagedBean  implements Serializable
{
	private static final long serialVersionUID = 8346251826604124597L;
	private List<CustomWO> customWoList;
	

	private User currentUser;
	
	public class CustomWO implements Serializable
	{

		private static final long serialVersionUID = -7851230659208879890L;

		private WorkOrder wo;
		
		private Boolean isMonitoring = false;
		
		
		public WorkOrder getWo() {
			return wo;
		}
		public void setWo(WorkOrder wo) {
			this.wo = wo;
		}
		
		public Boolean getIsMonitoring() 
		{
			return isMonitoring;
		}
		public void setIsMonitoring(Boolean isMonitoring) {
			this.isMonitoring = isMonitoring;
		}
	}
	
	
	public MonitoringPageSelectionManagedBean()
	{
		FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        Long id = (Long) request.getSession().getAttribute("id");
        currentUser = ((IUserService)ServiceFinder.findBean("UserService")).findUser(id);
        loadData();
	}

	public void loadData() 
	{
		customWoList = new ArrayList<MonitoringPageSelectionManagedBean.CustomWO>();
		DetachedCriteria dc = DetachedCriteria.forClass(WorkOrder.class);
		List<Long> userIds = new ArrayList<Long>();
		userIds.add(currentUser.getId());
		dc.createCriteria("workflow").createCriteria("userAbleToMonitor", "uam").add(Restrictions.eq("uam.id", currentUser.getId()));
		List<WorkOrder> woList = ((IUserService)ServiceFinder.findBean("UserService")).list(WorkOrder.class, dc);
		if(woList!=null )
		{
			for(WorkOrder wo:woList)
			{
				if(wo.getMonitoringUser().contains(currentUser))
				{
					CustomWO cw = new CustomWO();
					cw.setIsMonitoring(true);
					cw.setWo(wo);
					customWoList.add(cw);
				}
				else
				{
					CustomWO cw = new CustomWO();
					cw.setIsMonitoring(false);
					cw.setWo(wo);
					customWoList.add(cw);
				}
			}
		}
	}

	public String addToMonitoringList()
	{
		String id = JSFUtil.getRequestParameter("woId");
		((IWorkOrderService)ServiceFinder.findBean("WorkOrderService")).addUserToMonitoringList(currentUser.getId(), new Long(id));
		
		 for(CustomWO cwo: customWoList)
		 {
			 if(cwo.getWo ().getId ().equals ( new Long ( id ) ))
			 {
				 cwo.setIsMonitoring ( true );
			 }
		 }
		return null;
	}
	
	public String removeFromMonitoringList()
	{
		String id = JSFUtil.getRequestParameter("woId");
		((IWorkOrderService)ServiceFinder.findBean("WorkOrderService")).removeUserFromMonitoringList(currentUser.getId(), new Long(id));
		
		 for(CustomWO cwo: customWoList)
		 {
			 if(cwo.getWo ().getId ().equals ( new Long ( id ) ))
			 {
				 cwo.setIsMonitoring ( false );
			 }
		 }
		return null;
	}
	

	
	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	public List<CustomWO> getCustomWoList() {
		return customWoList;
	}

	public void setCustomWoList(List<CustomWO> customWoList) {
		this.customWoList = customWoList;
	}


	
}
