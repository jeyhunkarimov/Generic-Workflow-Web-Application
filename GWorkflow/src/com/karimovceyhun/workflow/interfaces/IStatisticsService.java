package com.karimovceyhun.workflow.interfaces;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import com.karimovceyhun.workflow.data.Workflow;

public interface IStatisticsService extends IService,Serializable
{
	public HashMap < String , Object > taskTimeStatistics(HashMap < String , Object >	queryObject);

	public String constructTaskWhereClause( HashMap < String , Object > queryObject );

	public List calculateResults( String queryStr );

	public HashMap < String , Integer > taskByStatusStatistics();
	
	public HashMap < String , Object > taskDelayStatistics(HashMap < String , Object >	queryObject);
 
	public HashMap < String , List <Integer> > taskByWorkflowStatistics( HashMap < String , Object > queryObject );

	public String constructWorkOrderWhereClause(HashMap < String , Object > queryObject );

	public HashMap < String , Object > workOrderTimeStatistics( HashMap < String , Object > queryObject );

	public HashMap < String , Integer > workOrderByStatusStatistics();

	public HashMap < String , List <Integer > > workOrderByWorkflowStatistics( HashMap < String , Object > queryObject );

	public HashMap < String , Object > workOrderDelayStatistics( HashMap < String , Object > queryObject );

}
