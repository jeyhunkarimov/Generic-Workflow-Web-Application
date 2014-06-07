package com.karimovceyhun.workflow.managed;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.karimovceyhun.workflow.data.Task;
import com.karimovceyhun.workflow.data.User;
import com.karimovceyhun.workflow.interfaces.ITaskService;
import com.karimovceyhun.workflow.services.ServiceFinder;
import com.karimovceyhun.workflow.utilities.JSFUtil;

@ManagedBean ( name = "taskListingManagedBean" )
@ViewScoped
public class TaskListingManagedBean extends ListingManagedBean < User > implements Serializable
{

	private static final long		serialVersionUID	= - 7612326977935156486L;
	private LazyDataModel < Task >	assignedToMeTasks;
	private LazyDataModel < Task >	closedByMeTasks;
	private LazyDataModel < Task >	monitoringByMeTasks;
	private LazyDataModel < Task >	managedByMeTasks;
	private LazyDataModel < Task >	reportedByMeTasks;
	private LazyDataModel < Task >	assignableToMeTasks;

	private Long					currentUserId;

	public TaskListingManagedBean()
	{
		super ( "TaskBean" );
		FacesContext context = FacesContext.getCurrentInstance ();
		HttpServletRequest request = ( HttpServletRequest ) context.getExternalContext ().getRequest ();
		try
		{
			currentUserId = ( Long ) request.getSession ().getAttribute ( "id" );
		}
		catch ( Exception e )
		{
			// TODO: handle exception
		}
	}

	@Override
	protected void loadData()
	{
		if ( currentUserId != null )
		{
			assignedToMeTasks = new LazyDataModel < Task > ()
			{
				private static final long	serialVersionUID	= 1L;

				@Override
				public List < Task > load( int first, int pageSize, String sortField, SortOrder sortOrder, Map < String , String > filters )
				{
					List retList = new ArrayList<>();
					retList.addAll(new LinkedHashSet<>(getTaskService ().getAssignedToMeTasks ( currentUserId , first , pageSize , sortField , sortOrder.equals ( SortOrder.ASCENDING ) )));
					return retList;
				}
			};
			assignedToMeTasks.setRowCount ( getTaskService ().getAssignedToMeTasksCount ( currentUserId ) );

			closedByMeTasks = new LazyDataModel < Task > ()
			{
				static final long	serialVersionUID	= 1L;

				@Override
				public List < Task > load( int first, int pageSize, String sortField, SortOrder sortOrder, Map < String , String > filters )
				{
					List retList = new ArrayList<>();
					retList.addAll(new LinkedHashSet<>(getTaskService ().getClosedByMeTasks ( currentUserId , first , pageSize , sortField , sortOrder.equals ( SortOrder.ASCENDING ) )));
					return retList;
				}
			};
			closedByMeTasks.setRowCount ( getTaskService ().getClosedByMeTasksCount ( currentUserId ) );

			monitoringByMeTasks = new LazyDataModel < Task > ()
			{
				private static final long	serialVersionUID	= 1L;

				@Override
				public List < Task > load( int first, int pageSize, String sortField, SortOrder sortOrder, Map < String , String > filters )
				{
					List retList = new ArrayList<>();
					retList.addAll(new LinkedHashSet<>(getTaskService ().getMonitoringByMeTasks ( currentUserId , first , pageSize , sortField , sortOrder.equals ( SortOrder.ASCENDING ) )));
					return retList;
				}
			};
			monitoringByMeTasks.setRowCount ( getTaskService ().getMonitoringByMeTasksCount ( currentUserId ) );
			
			managedByMeTasks = new LazyDataModel < Task > ()
			{
				private static final long	serialVersionUID	= 1L;

				@Override
				public List < Task > load( int first, int pageSize, String sortField, SortOrder sortOrder, Map < String , String > filters )
				{
					List retList = new ArrayList<>();
					retList.addAll(new LinkedHashSet<>(getTaskService ().getManagedByMeTasks ( currentUserId , first , pageSize , sortField , sortOrder.equals ( SortOrder.ASCENDING ) )));
					return retList;
				}
			};
			managedByMeTasks.setRowCount ( getTaskService ().getManagedByMeTasksCount ( currentUserId ) );

			reportedByMeTasks = new LazyDataModel < Task > ()
			{
				private static final long	serialVersionUID	= 1L;

				@Override
				public List < Task > load( int first, int pageSize, String sortField, SortOrder sortOrder, Map < String , String > filters )
				{
					List retList = new ArrayList<>();
					retList.addAll(new LinkedHashSet<>(getTaskService ().getReportedByMeTasks ( currentUserId , first , pageSize , sortField , sortOrder.equals ( SortOrder.ASCENDING ) )));
					return retList;
				}
			};
			reportedByMeTasks.setRowCount ( getTaskService ().getReportedByMeTasksCount ( currentUserId ) );

			assignableToMeTasks = new LazyDataModel < Task > ()
			{
				private static final long	serialVersionUID	= 1L;

				@Override
				public List < Task > load( int first, int pageSize, String sortField, SortOrder sortOrder, Map < String , String > filters )
				{
					List retList = new ArrayList<>();
					retList.addAll(new LinkedHashSet<>(getTaskService ().getAssignableToMeTasks ( currentUserId , first , pageSize , sortField , sortOrder.equals ( SortOrder.ASCENDING ) )));
					return retList;
				}
			};
			assignableToMeTasks.setRowCount ( getTaskService ().getAssignableToMeTasksCount ( currentUserId ) );
			// setAssignedToMeTasks(getTaskService().getAssignedToMeTasks(currentUserId));
			// setClosedByMeTasks(getTaskService().getClosedByMeTasks(currentUserId));
			// setMonitoringByMeTasks(getTaskService().getMonitoringByMeTasks(currentUserId));
			// setReportedByMeTasks(getTaskService().getReportedByMeTasks(currentUserId));
		}
	}

	@Override
	protected void doBeforeSave( List < FacesMessage > messages )
	{

	}

	public LazyDataModel < Task > getAssignedToMeTasks()
	{
		if ( assignedToMeTasks == null )
		{
			loadData ();
		}
		return assignedToMeTasks;
	}

	public void setAssignedToMeTasks( LazyDataModel < Task > tasks )
	{
		this.assignedToMeTasks = tasks;
	}

	public ITaskService getTaskService()
	{
		return ( ITaskService ) ServiceFinder.findBean ( "TaskService" );
	}

	public Long getCurrentUserId()
	{
		return currentUserId;
	}

	public void setCurrentUserId( Long currentUserId )
	{
		this.currentUserId = currentUserId;
	}

	public String selectTask()
	{
		String id = JSFUtil.getRequestParameter ( "taskId" );
		FacesContext context = FacesContext.getCurrentInstance ();
		HttpServletRequest request = ( HttpServletRequest ) context.getExternalContext ().getRequest ();
		request.getSession ().setAttribute ( "taskId" , Long.valueOf ( id ) );
		return "taskView";
	}

	public LazyDataModel < Task > getClosedByMeTasks()
	{
		if ( closedByMeTasks == null )
		{
			loadData ();
		}
		return closedByMeTasks;
	}

	public void setClosedByMeTasks( LazyDataModel < Task > closedByMeTasks )
	{
		this.closedByMeTasks = closedByMeTasks;
	}

	public LazyDataModel < Task > getMonitoringByMeTasks()
	{
		if ( monitoringByMeTasks == null )
		{
			loadData ();
		}
		return monitoringByMeTasks;
	}

	public void setMonitoringByMeTasks( LazyDataModel < Task > monitoringByMeTasks )
	{
		this.monitoringByMeTasks = monitoringByMeTasks;
	}

	public LazyDataModel < Task > getReportedByMeTasks()
	{
		if ( reportedByMeTasks == null )
		{
			loadData ();
		}
		return reportedByMeTasks;
	}

	public void setReportedByMeTasks( LazyDataModel < Task > reportedByMeTasks )
	{
		this.reportedByMeTasks = reportedByMeTasks;
	}

	public LazyDataModel < Task > getAssignableToMeTasks()
	{
		return assignableToMeTasks;
	}

	public void setAssignableToMeTasks( LazyDataModel < Task > assignableToMeTasks )
	{
		this.assignableToMeTasks = assignableToMeTasks;
	}

	public void setManagedByMeTasks(LazyDataModel<Task> managedByMeTasks) {
		this.managedByMeTasks = managedByMeTasks;
	}
	
	public LazyDataModel<Task> getManagedByMeTasks() {
		return managedByMeTasks;
	}
	
}
