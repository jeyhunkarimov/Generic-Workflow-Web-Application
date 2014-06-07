package com.karimovceyhun.workflow.managed;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.TabChangeEvent;

import com.karimovceyhun.workflow.data.Bean;
import com.karimovceyhun.workflow.data.Project;
import com.karimovceyhun.workflow.data.User;
import com.karimovceyhun.workflow.data.Workflow;
import com.karimovceyhun.workflow.interfaces.IService;
import com.karimovceyhun.workflow.interfaces.IStatisticsService;
import com.karimovceyhun.workflow.interfaces.IUserService;
import com.karimovceyhun.workflow.services.ServiceFinder;

@ViewScoped
@ManagedBean ( name = "statisticsManagedBean" )
public class StatisticsManagedBean implements Serializable
{

	private static final long									serialVersionUID				= - 6765850002520107817L;
	private HashMap < String , List < Object > >				queryLists;
	private HashMap < String , Object >							queryResults;

	private HashMap < String , Object >							taskTimeStatistics				= null;
	private HashMap < String , Integer >						taskByStatusStatistics			= null;
	private HashMap < String , Object >							taskDelayStatistics				= null;
	private HashMap < String , List < Integer >>				taskByWorkflowStatistics		= null;

	
	private List < String >										taskTimeKeyList					= null;
	private List < String >										taskByStatusKeyList				= null;
	private List < String >										taskDelayKeyList				= null;
	private List < String >									taskByWorkflowKeyList			= null;

	
	private HashMap < String , Object >							workOrderTimeStatistics			= null;
	private HashMap < String , Integer >						workOrderByStatusStatistics		= null;
	private HashMap < String , List <Integer > >	workOrderByWorkflowStatistics	= null;
	

	private HashMap < String , Object >							workOrderDelayStatistics		= null;

	private List < String >										workOrderTimeKeyList			= null;
	private List < String >										workOrderByStatusKeyList		= null;
	private List < String >									workOrderByWorkflowKeyList		= null;

	
	private List < String >										workOrderDelayKeyList			= null;

	public StatisticsManagedBean()
	{
		queryLists = new HashMap < String , List < Object > > ();
		queryLists.put ( "User" , new ArrayList < Object > ( getService ().list ( User.class ) ) );
		queryLists.put ( "Project" , new ArrayList < Object > ( getService ().list ( Project.class ) ) );
		queryLists.put ( "Workflow" , new ArrayList < Object > ( getService ().list ( Workflow.class ) ) );

		queryResults = new HashMap < String , Object > ();
		for ( String key : queryLists.keySet () )
		{
			queryResults.put ( key , null );
		}
		initialLoadData ();
	}

	public void onTaskTabChange( TabChangeEvent event )
	{

	}

	public void onTabChange( TabChangeEvent event )
	{
		String s = "3";
		s.toCharArray ();
	}

	public IService getService()
	{
		return ( IService ) ServiceFinder.findBean ( "Service" );
	}

	public IStatisticsService getStatisticsService()
	{
		return ( IStatisticsService ) ServiceFinder.findBean ( "StatisticsService" );
	}

	public void loadTaskData()
	{
		taskTimeStatistics = getStatisticsService ().taskTimeStatistics ( queryResults );
		taskTimeKeyList = new ArrayList < String > ( taskTimeStatistics.keySet () );

		taskDelayStatistics = getStatisticsService ().taskDelayStatistics ( queryResults );
		taskDelayKeyList = new ArrayList < String > ( taskDelayStatistics.keySet () );
	}

	public void initialLoadData()
	{
		taskTimeStatistics = getStatisticsService ().taskTimeStatistics ( queryResults );
		taskTimeKeyList = new ArrayList < String > ( taskTimeStatistics.keySet () );

		taskDelayStatistics = getStatisticsService ().taskDelayStatistics ( queryResults );
		taskDelayKeyList = new ArrayList < String > ( taskDelayStatistics.keySet () );

		workOrderTimeStatistics = getStatisticsService ().workOrderTimeStatistics ( queryResults );
		workOrderTimeKeyList = new ArrayList < String > ( workOrderTimeStatistics.keySet () );

		workOrderDelayStatistics = getStatisticsService ().workOrderDelayStatistics ( queryResults );
		workOrderDelayKeyList = new ArrayList < String > ( workOrderDelayStatistics.keySet () );

		taskByStatusStatistics = getStatisticsService ().taskByStatusStatistics ();
		taskByStatusKeyList = new ArrayList < String > ( taskByStatusStatistics.keySet () );

		taskByWorkflowStatistics = getStatisticsService ().taskByWorkflowStatistics ( queryResults );
		taskByWorkflowKeyList = new ArrayList < String > ( taskByWorkflowStatistics.keySet () );

		workOrderByStatusStatistics = getStatisticsService ().workOrderByStatusStatistics ();
		workOrderByStatusKeyList = new ArrayList < String > ( workOrderByStatusStatistics.keySet () );

		workOrderByWorkflowStatistics = getStatisticsService ().workOrderByWorkflowStatistics ( queryResults );
		workOrderByWorkflowKeyList = new ArrayList < String > ( workOrderByWorkflowStatistics.keySet () );

	}

	public void woStatisticsResults()
	{
		workOrderTimeStatistics = getStatisticsService ().workOrderTimeStatistics ( queryResults );
		workOrderTimeKeyList = new ArrayList < String > ( workOrderTimeStatistics.keySet () );
		workOrderDelayStatistics = getStatisticsService ().workOrderDelayStatistics ( queryResults );
		workOrderDelayKeyList = new ArrayList < String > ( workOrderDelayStatistics.keySet () );
	}

	public void taskStatisticsResults()
	{
		// getStatisticsService ().taskTimeStatistics ( queryResults );
		// getStatisticsService ().byStatusStatistics ();
		// getStatisticsService ().taskDelayStatistics(queryResults);
		// getStatisticsService ().taskByWorkflowStatistics ( queryResults );
		// getStatisticsService ().workOrderTimeStatistics ( queryResults );
		// getStatisticsService ().workOrderByStatusStatistics ();
		// getStatisticsService ().workOrderByWorkflowStatistics ( queryResults
		// );
		// getStatisticsService ().workOrderDelayStatistics ( queryResults );
		loadTaskData ();
	}

	public HashMap < String , List < Object >> getQueryLists()
	{
		return queryLists;
	}

	public void setQueryLists( HashMap < String , List < Object >> queryLists )
	{
		this.queryLists = queryLists;
	}

	public HashMap < String , Object > getQueryResults()
	{
		return queryResults;
	}

	public void setQueryResults( HashMap < String , Object > queryResults )
	{
		this.queryResults = queryResults;
	}

	public HashMap < String , Object > getTaskTimeStatistics()
	{
		return taskTimeStatistics;
	}

	public void setTaskTimeStatistics( HashMap < String , Object > taskTimeStatistics )
	{
		this.taskTimeStatistics = taskTimeStatistics;
	}

	public HashMap < String , Integer > getTaskByStatusStatistics()
	{
		return taskByStatusStatistics;
	}

	public void setTaskByStatusStatistics( HashMap < String , Integer > taskByStatusStatistics )
	{
		this.taskByStatusStatistics = taskByStatusStatistics;
	}

	public HashMap < String , Object > getTaskDelayStatistics()
	{
		return taskDelayStatistics;
	}

	public void setTaskDelayStatistics( HashMap < String , Object > taskDelayStatistics )
	{
		this.taskDelayStatistics = taskDelayStatistics;
	}

	public HashMap < String , Object > getWorkOrderTimeStatistics()
	{
		return workOrderTimeStatistics;
	}

	public void setWorkOrderTimeStatistics( HashMap < String , Object > workOrderTimeStatistics )
	{
		this.workOrderTimeStatistics = workOrderTimeStatistics;
	}

	public HashMap < String , Integer > getWorkOrderByStatusStatistics()
	{
		return workOrderByStatusStatistics;
	}

	public void setWorkOrderByStatusStatistics( HashMap < String , Integer > workOrderByStatusStatistics )
	{
		this.workOrderByStatusStatistics = workOrderByStatusStatistics;
	}


	public HashMap < String , Object > getWorkOrderDelayStatistics()
	{
		return workOrderDelayStatistics;
	}

	public void setWorkOrderDelayStatistics( HashMap < String , Object > workOrderDelayStatistics )
	{
		this.workOrderDelayStatistics = workOrderDelayStatistics;
	}

	// here begins keylists

	public List < String > getTaskTimeKeyList()
	{
		return taskTimeKeyList;
	}

	public void setTaskTimeKeyList( List < String > taskTimeKeyList )
	{
		this.taskTimeKeyList = taskTimeKeyList;
	}

	public List < String > getTaskByStatusKeyList()
	{
		return taskByStatusKeyList;
	}

	public void setTaskByStatusKeyList( List < String > taskByStatusKeyList )
	{
		this.taskByStatusKeyList = taskByStatusKeyList;
	}

	public List < String > getTaskDelayKeyList()
	{
		return taskDelayKeyList;
	}

	public void setTaskDelayKeyList( List < String > taskDelayKeyList )
	{
		this.taskDelayKeyList = taskDelayKeyList;
	}

	public List < String > getWorkOrderTimeKeyList()
	{
		return workOrderTimeKeyList;
	}

	public void setWorkOrderTimeKeyList( List < String > workOrderTimeKeyList )
	{
		this.workOrderTimeKeyList = workOrderTimeKeyList;
	}

	public List < String > getWorkOrderByStatusKeyList()
	{
		return workOrderByStatusKeyList;
	}

	public void setWorkOrderByStatusKeyList( List < String > workOrderByStatusKeyList )
	{
		this.workOrderByStatusKeyList = workOrderByStatusKeyList;
	}

	public List < String > getWorkOrderDelayKeyList()
	{
		return workOrderDelayKeyList;
	}

	public void setWorkOrderDelayKeyList( List < String > workOrderDelayKeyList )
	{
		this.workOrderDelayKeyList = workOrderDelayKeyList;
	}

	
	public HashMap < String , List < Integer >> getTaskByWorkflowStatistics()
	{
		return taskByWorkflowStatistics;
	}

	public void setTaskByWorkflowStatistics( HashMap < String , List < Integer >> taskByWorkflowStatistics )
	{
		this.taskByWorkflowStatistics = taskByWorkflowStatistics;
	}
	
	public List < String > getTaskByWorkflowKeyList()
	{
		return taskByWorkflowKeyList;
	}

	public void setTaskByWorkflowKeyList( List < String > taskByWorkflowKeyList )
	{
		this.taskByWorkflowKeyList = taskByWorkflowKeyList;
	}
	public List < String > getWorkOrderByWorkflowKeyList()
	{
		return workOrderByWorkflowKeyList;
	}

	public void setWorkOrderByWorkflowKeyList( List < String > workOrderByWorkflowKeyList )
	{
		this.workOrderByWorkflowKeyList = workOrderByWorkflowKeyList;
	}

	public HashMap < String , List < Integer >> getWorkOrderByWorkflowStatistics()
	{
		return workOrderByWorkflowStatistics;
	}

	public void setWorkOrderByWorkflowStatistics( HashMap < String , List < Integer >> workOrderByWorkflowStatistics )
	{
		this.workOrderByWorkflowStatistics = workOrderByWorkflowStatistics;
	}

}
