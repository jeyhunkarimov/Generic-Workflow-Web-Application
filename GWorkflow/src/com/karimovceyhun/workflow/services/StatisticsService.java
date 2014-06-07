package com.karimovceyhun.workflow.services;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.Type;
import org.springframework.transaction.annotation.Transactional;

import com.karimovceyhun.workflow.data.Project;
import com.karimovceyhun.workflow.data.Status;
import com.karimovceyhun.workflow.data.Task;
import com.karimovceyhun.workflow.data.User;
import com.karimovceyhun.workflow.data.WorkOrder;
import com.karimovceyhun.workflow.data.Workflow;
import com.karimovceyhun.workflow.interfaces.IStatisticsService;
import com.karimovceyhun.workflow.interfaces.IWorkflowService;

public class StatisticsService extends Service implements IStatisticsService , Serializable
{

	public StatisticsService( SessionFactory sessionFactory )
	{
		super ( sessionFactory );
	}

	private static final long	serialVersionUID	= 8965280920749375415L;

	@Transactional
	public HashMap < String , Object > taskTimeStatistics( HashMap < String , Object > queryObject ) // time
																										// statistics
																										// for
																										// completed
																										// tasks
	{
		String whereQueryStr = constructTaskWhereClause ( queryObject );

		String averageTimeSelectQuery = "select  " + "avg(distinct unix_timestamp(t.endTime) - unix_timestamp( t.startTime))" + "  from Task t, WorkOrder wo,Project p,WorkFlow wf  where t.endTime != 'NULL' and " + whereQueryStr;

		String totalTimeSelectQuery = "select  " + "sum(distinct unix_timestamp(t.endTime) - unix_timestamp( t.startTime))  from Task t, WorkOrder wo,Project p,WorkFlow wf  where t.endTime != 'NULL' and " + whereQueryStr;

		String longestTimeSelectQuery1 = "select " + "(unix_timestamp(t.endTime) - unix_timestamp( t.startTime)) as mx, t.id from Task t, WorkOrder wo,Project p,WorkFlow wf where t.endTime != 'NULL' and " + whereQueryStr;
		String longestTimeSelectQuery2 = "select " + "(unix_timestamp(now()) - unix_timestamp( t.startTime))as mx , t.id from Task t, WorkOrder wo,Project p,WorkFlow wf where t.endTime is NULL and " + whereQueryStr;

		if ( whereQueryStr == "" ) // remove 'and' from the end of select
									// statement
		{
			averageTimeSelectQuery = averageTimeSelectQuery.substring ( 0 , averageTimeSelectQuery.length () - 4 );
			totalTimeSelectQuery = totalTimeSelectQuery.substring ( 0 , totalTimeSelectQuery.length () - 4 );
			longestTimeSelectQuery1 = longestTimeSelectQuery1.substring ( 0 , longestTimeSelectQuery1.length () - 4 );
			longestTimeSelectQuery2 = longestTimeSelectQuery2.substring ( 0 , longestTimeSelectQuery2.length () - 4 );
		}

		longestTimeSelectQuery1 = longestTimeSelectQuery1 + " ORDER BY mx DESC LIMIT 1"; // finding
																							// the
																							// max
																							// value
		longestTimeSelectQuery2 = longestTimeSelectQuery2 + " ORDER BY mx DESC LIMIT 1";

		// compute query for average time and total time
		List average = calculateResults ( averageTimeSelectQuery );
		List total = calculateResults ( totalTimeSelectQuery );
		List longest_1 = calculateResults ( longestTimeSelectQuery1 );
		List longest_2 = calculateResults ( longestTimeSelectQuery2 );
		List longest = getLongestIssue ( longest_1 , longest_2 );

		HashMap < String , Object > result = new HashMap < String , Object > ();
		
		result.put ( "Longest Open Task ID" , longest == null || longest.size () == 0 || longest.get ( 0 ) == null? "" : ( ( Object [ ] ) longest.get ( 0 ) ) [ 1 ] );
		result.put ( "Longest Open Task Time" , longest == null || longest.size () == 0 || longest.get ( 0 ) == null ? "" : ((BigInteger) ( ( Object [ ] ) longest.get ( 0 ) ) [ 0 ]).longValue ()/3600);
		result.put ( "Average Task Time" , average == null || average.size () == 0 || average.get ( 0 ) == null ? "" : ((BigDecimal) average.get ( 0 )).longValue ()/3600 );
		result.put ( "Total Task Time" , total == null || total.size () == 0 || total.get ( 0 ) == null ? "" : ((BigDecimal) total.get ( 0 )).longValue ()/3600 );

		return result;
	}

	public List calculateResults( String queryStr )
	{
		Query query = getHibernateTemplate ().getSessionFactory ().getCurrentSession ().createSQLQuery ( queryStr );
		return query.list ();
	}

	public List getLongestIssue( List list1, List list2 )
	{
		if ( ( list1 == null || list1.size () == 0 ) && ( list2 == null || list2.size () == 0 ) )
		{
			return null;
		}
		if ( list1 == null || list1.size () == 0 )
		{
			return list2;
		}
		else if ( list2 == null || list2.size () == 0 )
		{
			return list1;
		}
		else
		{
			Integer l1 = (( BigInteger ) ( ( Object [ ] ) list1.get ( 0 ) ) [ 0 ]).intValue ();
			Integer l2 = (( BigInteger ) ( ( Object [ ] ) list2.get ( 0 ) ) [ 0 ]).intValue ();
			return l1 > l2 ? list1 : list2;
		}
	}

	public String constructTaskWhereClause( HashMap < String , Object > queryObject )
	{
		String whereQueryStr = "";
		Set < String > keys = queryObject.keySet ();
		for ( String key : keys )
		{
			if ( queryObject.get ( key ) != null )
			{
				if ( queryObject.get ( key ) instanceof User )
				{
					whereQueryStr = whereQueryStr + " ( t.responsible_id = " + ( ( User ) queryObject.get ( key ) ).getId () + ") and ";
					// dc.createCriteria ( "responsible" ).add ( Restrictions.eq
					// ( "id" , ((User)queryObject.get ( key )).getId () ) );
				}
				else if ( queryObject.get ( key ) instanceof Project )
				{
					whereQueryStr = whereQueryStr + "( ( wo.project_id = " + ( ( Project ) queryObject.get ( key ) ).getId () + ") and ( t.workorder_id = wo.id )) and ";
					// DetachedCriteria tempDc = dc.createCriteria ( "workorder"
					// );
					// tempDc.createCriteria ( "project" ).add ( Restrictions.eq
					// ( "id" , ((Project)queryObject.get ( key )).getId () ) );
				}
				else if ( queryObject.get ( key ) instanceof Workflow )
				{
					whereQueryStr = whereQueryStr + "( ( wf.id = " + ( ( Workflow ) queryObject.get ( key ) ).getId () + ") and ( wo.workflow_id = wf.id ) and (t.workorder_id = wo.id) ) and ";
					// DetachedCriteria tempDc = dc.createCriteria ( "workorder"
					// );
					// tempDc.createCriteria ( "workflow" ).add (
					// Restrictions.eq ( "id" , ((Workflow)queryObject.get ( key
					// )).getId () ) );
				}
			}
		}
		if ( whereQueryStr != "" ) // remove the last 'and'
		{
			whereQueryStr = whereQueryStr.substring ( 0 , whereQueryStr.length () - 4 );
		}
		return whereQueryStr;
	}

	@Transactional
	public HashMap < String , Integer > taskByStatusStatistics()
	{
		DetachedCriteria dc = DetachedCriteria.forClass ( Task.class );
		dc.createCriteria ( "status" ).add ( Restrictions.eq ( "statusType" , Status.CLOSED ) );
		Integer closedTasks = this.countList ( Task.class , dc );

		DetachedCriteria dc2 = DetachedCriteria.forClass ( Task.class );
		dc2.createCriteria ( "status" ).add ( Restrictions.ne ( "statusType" , Status.CLOSED ) );

		Integer onGoingTasks = this.countList ( Task.class , dc2 );
		Integer openTastks = closedTasks + onGoingTasks;

		HashMap < String , Integer > result = new HashMap < String , Integer > ();
		result.put ( "Opened Tasks" , openTastks );
		result.put ( "OnGoing Tasks" , onGoingTasks );
		result.put ( "Closed Tasks" , closedTasks );
		return result;

	}

	@Transactional
	public HashMap < String , Object > taskDelayStatistics( HashMap < String , Object > queryObject ) // time
																										// statistics
																										// for
																										// task
																										// delay
	{
		String taskWhereStr = constructTaskWhereClause ( queryObject );
		Date currentDate = Calendar.getInstance ().getTime ();
		String maxDelaySelectQuery = "select  " + "(unix_timestamp(now()) - unix_timestamp( t.startTime)) as mx ,t.id " + "  from Task t, WorkOrder wo,Project p,WorkFlow wf, TaskNode tn  where t.node_id = tn.id and  t.endTime is NULL and ( (unix_timestamp(now()) - unix_timestamp(t.startTime)) > tn.estimatedTime * 3600 )  and " + taskWhereStr;
		String avgDelaySelectQuery = "select  " + "avg(unix_timestamp(now()) - unix_timestamp( t.startTime)) " + "  from Task t, WorkOrder wo,Project p,WorkFlow wf, TaskNode tn  where t.node_id = tn.id and  t.endTime is NULL and ( (unix_timestamp(now()) - unix_timestamp(t.startTime)) > tn.estimatedTime * 3600 )  and " + taskWhereStr;

		String totalDelaySelectQuery = "select  " + "sum(unix_timestamp(now()) - unix_timestamp( t.startTime)) " + "  from Task t, WorkOrder wo,Project p,WorkFlow wf, TaskNode tn  where t.node_id = tn.id and  t.endTime is NULL and ( (unix_timestamp(now()) - unix_timestamp(t.startTime)) > tn.estimatedTime * 3600 )  and " + taskWhereStr;

		String delayCountSelectQuery = "select count(distinct t.id) " + "  from Task t, WorkOrder wo,Project p,WorkFlow wf, TaskNode tn  where t.node_id = tn.id and  t.endTime is NULL and ( (unix_timestamp(now()) - unix_timestamp(t.startTime)) > tn.estimatedTime * 3600 )  and " + taskWhereStr;

		if ( taskWhereStr == "" )
		{
			maxDelaySelectQuery = maxDelaySelectQuery.substring ( 0 , maxDelaySelectQuery.length () - 4 );
			avgDelaySelectQuery = avgDelaySelectQuery.substring ( 0 , avgDelaySelectQuery.length () - 4 );
			totalDelaySelectQuery = totalDelaySelectQuery.substring ( 0 , totalDelaySelectQuery.length () - 4 );
			delayCountSelectQuery = delayCountSelectQuery.substring ( 0 , delayCountSelectQuery.length () - 4 );
		}
		maxDelaySelectQuery = maxDelaySelectQuery + " ORDER BY mx DESC LIMIT 1";
		
		List maxDelay = calculateResults ( maxDelaySelectQuery );
		List avgDelay = calculateResults ( avgDelaySelectQuery );
		List totalDelay = calculateResults ( totalDelaySelectQuery );
		List delayCount = calculateResults ( delayCountSelectQuery );

		HashMap < String , Object > result = new HashMap < String , Object > ();
		result.put ( "Number Of Delayed Tasks" , delayCount == null || delayCount.size () == 0 || delayCount.get ( 0 ) == null ? "" : delayCount.get ( 0 ) );
		result.put ( "Maximum Delayed Task ID" , maxDelay == null || maxDelay.size () == 0 || maxDelay.get ( 0 ) == null  ? "" : ( ( Object [ ] ) maxDelay.get ( 0 ) ) [ 1 ] );
		result.put ( "Maximum Delayed Task Time" , maxDelay == null || maxDelay.size () == 0 || maxDelay.get ( 0 ) == null  ? "" : ((BigInteger)( ( Object [ ] ) maxDelay.get ( 0 ) ) [ 0 ]).longValue ()/3600 );
		result.put ( "Average Task Delay" , avgDelay == null || avgDelay.size () == 0 || avgDelay.get ( 0 ) == null  ? "" : ((BigDecimal)avgDelay.get ( 0 )).longValue () / 3600 );
		result.put ( "Total Task Delay" , totalDelay == null || totalDelay.size () == 0 || totalDelay.get ( 0 ) == null  ? "" : ((BigDecimal)totalDelay.get ( 0 )).longValue ()/3600 );

		return result;
	}

	@Transactional
	public HashMap < String , List <Integer> > taskByWorkflowStatistics( HashMap < String , Object > queryObject )
	{
		HashMap < String , List <Integer> > result = new HashMap < String , List<Integer> > ();
		List < Workflow > wfs = ( ( IWorkflowService ) ServiceFinder.findBean ( "WorkflowService" ) ).list ( Workflow.class );
		if ( wfs != null && wfs.size () > 0 )
		{
			for ( Workflow wf : wfs )
			{
				DetachedCriteria completedTasksDc = DetachedCriteria.forClass ( Task.class );
				completedTasksDc.createCriteria ( "workorder" ).createCriteria ( "workflow" ).add ( Restrictions.eq ( "id" , wf.getId () ) );
				completedTasksDc.createCriteria ( "status" ).add ( Restrictions.eq ( "statusType" , Status.CLOSED ) );

				DetachedCriteria openedTasksDc = DetachedCriteria.forClass ( Task.class );
				openedTasksDc.createCriteria ( "workorder" ).createCriteria ( "workflow" ).add ( Restrictions.eq ( "id" , wf.getId () ) );

				Integer openedNumber = this.countList ( Task.class , openedTasksDc );
				Integer completedNumber = this.countList ( Task.class , completedTasksDc );

				List<Integer> values = new ArrayList < Integer > ();
				values.add ( openedNumber );
				values.add ( completedNumber );
				
				result.put ( wf.getName () , values );
			}
			return result;
		}
		return null;
	}

	@Transactional
	public String constructWorkOrderWhereClause( HashMap < String , Object > queryObject )
	{
		String whereQueryStr = "";
		Set < String > keys = queryObject.keySet ();
		for ( String key : keys )
		{
			if ( queryObject.get ( key ) != null )
			{
				if ( queryObject.get ( key ) instanceof Project )
				{
					whereQueryStr = whereQueryStr + "( wo.project_id = " + ( ( Project ) queryObject.get ( key ) ).getId () + ") and ";
					// DetachedCriteria tempDc = dc.createCriteria ( "workorder"
					// );
					// tempDc.createCriteria ( "project" ).add ( Restrictions.eq
					// ( "id" , ((Project)queryObject.get ( key )).getId () ) );
				}
				else if ( queryObject.get ( key ) instanceof Workflow )
				{
					whereQueryStr = whereQueryStr + "( ( wf.id = " + ( ( Workflow ) queryObject.get ( key ) ).getId () + ") and ( wo.workflow_id = wf.id )) and ";
					// DetachedCriteria tempDc = dc.createCriteria ( "workorder"
					// );
					// tempDc.createCriteria ( "workflow" ).add (
					// Restrictions.eq ( "id" , ((Workflow)queryObject.get ( key
					// )).getId () ) );
				}
			}
		}
		if ( whereQueryStr != "" ) // remove the last 'and'
		{
			whereQueryStr = whereQueryStr.substring ( 0 , whereQueryStr.length () - 4 );
		}
		return whereQueryStr;
	}

	
	
	
	@Transactional
	public HashMap < String , Object > workOrderTimeStatistics( HashMap < String , Object > queryObject ) // time statistics for workorders
	{
		String whereQueryStr = constructWorkOrderWhereClause ( queryObject );

		String averageTimeSelectQuery = "select  " + "avg(distinct unix_timestamp(wo.endDate) - unix_timestamp( wo.startDate))" + "  from WorkOrder wo,Project p,WorkFlow wf  where wo.endDate != 'NULL' and " + whereQueryStr;

		String totalTimeSelectQuery = "select  " + "sum(distinct unix_timestamp(wo.endDate) - unix_timestamp( wo.startDate))  from WorkOrder wo,Project p,WorkFlow wf  where wo.endDate != 'NULL' and " + whereQueryStr;

		String longestTimeSelectQuery1 = "select " + "(unix_timestamp(wo.endDate) - unix_timestamp( wo.startDate)) as mx, wo.id from WorkOrder wo,Project p,WorkFlow wf where wo.endDate != 'NULL' and " + whereQueryStr;
		String longestTimeSelectQuery2 = "select " + "(unix_timestamp(now()) - unix_timestamp( wo.startDate))as mx , wo.id from WorkOrder wo,Project p,WorkFlow wf where wo.endDate is NULL and " + whereQueryStr;

		if ( whereQueryStr == "" ) // remove 'and' from the end of select
									// statement
		{
			averageTimeSelectQuery = averageTimeSelectQuery.substring ( 0 , averageTimeSelectQuery.length () - 4 );
			totalTimeSelectQuery = totalTimeSelectQuery.substring ( 0 , totalTimeSelectQuery.length () - 4 );
			longestTimeSelectQuery1 = longestTimeSelectQuery1.substring ( 0 , longestTimeSelectQuery1.length () - 4 );
			longestTimeSelectQuery2 = longestTimeSelectQuery2.substring ( 0 , longestTimeSelectQuery2.length () - 4 );
		}

		longestTimeSelectQuery1 = longestTimeSelectQuery1 + " ORDER BY mx DESC LIMIT 1"; // finding
																							// the
																							// max
																							// value
		longestTimeSelectQuery2 = longestTimeSelectQuery2 + " ORDER BY mx DESC LIMIT 1";

		// compute query for average time and total time
		List average = calculateResults ( averageTimeSelectQuery );
		List total = calculateResults ( totalTimeSelectQuery );
		List longest_1 = calculateResults ( longestTimeSelectQuery1 );
		List longest_2 = calculateResults ( longestTimeSelectQuery2 );
		List longest = getLongestIssue ( longest_1 , longest_2 );

		HashMap < String , Object > result = new HashMap < String , Object > ();
		result.put ( "Longest Open WorkOrder ID" , longest == null || longest.size () == 0 || longest.get ( 0 ) == null? "" : ( ( Object [ ] ) longest.get ( 0 ) ) [ 1 ] );
		result.put ( "Longest Open WorkOrder Time" , longest == null || longest.size () == 0 || longest.get ( 0 ) == null ? "" : ((BigInteger) ( ( Object [ ] ) longest.get ( 0 ) ) [ 0 ]).longValue ()/3600 );
		result.put ( "Average WorkOrder Time" ,average == null || average.size () == 0 || average.get ( 0 ) == null ? "" : ((BigDecimal) average.get ( 0 )).longValue ()/3600 );
		result.put ( "Total WorkOrder Time" , total == null || total.size () == 0 || total.get ( 0 ) == null ? "" : ((BigDecimal) total.get ( 0 )).longValue ()/3600);

		
		return result;
	}
	
	
	@Transactional
	public HashMap < String , Integer > workOrderByStatusStatistics()
	{
		DetachedCriteria dc = DetachedCriteria.forClass ( WorkOrder.class );
		dc.add ( Restrictions.isNotNull ( "startDate") );
		dc.add ( Restrictions.isNotNull ( "endDate") );
		Integer closedWorkOrders = this.countList ( WorkOrder.class , dc );

		DetachedCriteria dc2 = DetachedCriteria.forClass ( WorkOrder.class );
		dc2.add ( Restrictions.isNotNull ( "startDate" ));
		dc2.add ( Restrictions.isNull ( "endDate" ) );

		Integer onGoingWorkOrders = this.countList ( WorkOrder.class , dc2 );
		Integer openWorkOrders = closedWorkOrders + onGoingWorkOrders;

		HashMap < String , Integer > result = new HashMap < String , Integer > ();
		result.put ( "Opened WorkOrders" , openWorkOrders );
		result.put ( "OnGoing WorkOrders" , onGoingWorkOrders );
		result.put ( "Closed WorkOrders" , closedWorkOrders );
		return result;

	}
	
	
	
	
	
	
	
	@Transactional
	public HashMap < String , List <Integer > > workOrderByWorkflowStatistics( HashMap < String , Object > queryObject )
	{
		HashMap < String , List <Integer > > result = new HashMap < String , List<Integer> > ();
		List < Workflow > wfs = ( ( IWorkflowService ) ServiceFinder.findBean ( "WorkflowService" ) ).list ( Workflow.class );
		if ( wfs != null && wfs.size () > 0 )
		{
			for ( Workflow wf : wfs )
			{
				DetachedCriteria completedWorkOrdersDc = DetachedCriteria.forClass ( WorkOrder.class );
				completedWorkOrdersDc.createCriteria ( "workflow" ).add ( Restrictions.eq ( "id" , wf.getId () ) );
				completedWorkOrdersDc.add ( Restrictions.isNotNull( "endDate") );

				DetachedCriteria openedWorkOrdersDc = DetachedCriteria.forClass ( WorkOrder.class );
				openedWorkOrdersDc.createCriteria ( "workflow" ).add ( Restrictions.eq ( "id" , wf.getId () ) );

				Integer openedNumber = this.countList ( Task.class , openedWorkOrdersDc );
				Integer completedNumber = this.countList ( Task.class , completedWorkOrdersDc );

				List < Integer > values = new ArrayList < Integer > ();
				values.add (  openedNumber );
				values.add( completedNumber );

				result.put ( wf.getName () , values );
			}
			return result;
		}
		return null;
	}
	
	
	@Transactional
	public HashMap < String , Object > workOrderDelayStatistics( HashMap < String , Object > queryObject ) // time
																										// statistics
																										// for
																										// workOrder
																										// delay
	{
		String taskWhereStr = constructWorkOrderWhereClause ( queryObject );
		String maxDelaySelectQuery = "select  " + "(unix_timestamp(now()) - unix_timestamp( wo.dueDate)) as mx ,wo.id " + "  from WorkOrder wo,Project p,WorkFlow wf where wo.endDate is NULL and ( (unix_timestamp(now()) ) > (unix_timestamp(wo.dueDate) ) )  and " + taskWhereStr;
		String avgDelaySelectQuery = "select  " + "avg(distinct unix_timestamp(now()) - unix_timestamp( wo.dueDate)) " + "  from WorkOrder wo,Project p,WorkFlow wf where wo.endDate is NULL and ( (unix_timestamp(now()) ) > (unix_timestamp(wo.dueDate) ) )  and " + taskWhereStr;

		String totalDelaySelectQuery = "select  " + "sum(distinct unix_timestamp(now()) - unix_timestamp( wo.dueDate)) " + "  from WorkOrder wo,Project p,WorkFlow wf where wo.endDate is NULL and ( (unix_timestamp(now()) ) > (unix_timestamp(wo.dueDate) ) )  and " + taskWhereStr;

		String delayCountSelectQuery = "select count(distinct wo.id) " + "  from WorkOrder wo,Project p,WorkFlow wf where wo.endDate is NULL and ( (unix_timestamp(now()) ) > (unix_timestamp(wo.dueDate) ) )  and " + taskWhereStr;

		if ( taskWhereStr == "" )
		{
			maxDelaySelectQuery = maxDelaySelectQuery.substring ( 0 , maxDelaySelectQuery.length () - 4 );
			avgDelaySelectQuery = avgDelaySelectQuery.substring ( 0 , avgDelaySelectQuery.length () - 4 );
			totalDelaySelectQuery = totalDelaySelectQuery.substring ( 0 , totalDelaySelectQuery.length () - 4 );
			delayCountSelectQuery = delayCountSelectQuery.substring ( 0 , delayCountSelectQuery.length () - 4 );
		}
		maxDelaySelectQuery = maxDelaySelectQuery + " ORDER BY mx DESC LIMIT 1";

		List maxDelay = calculateResults ( maxDelaySelectQuery );
		List avgDelay = calculateResults ( avgDelaySelectQuery );
		List totalDelay = calculateResults ( totalDelaySelectQuery );
		List delayCount = calculateResults ( delayCountSelectQuery );

		HashMap < String , Object > result = new HashMap < String , Object > ();
		result.put ( "Number Of Delayed WorkOrders" , delayCount == null || delayCount.size () == 0 || delayCount.get ( 0 ) == null ? "" : delayCount.get ( 0 ) );
		result.put ( "Maximum Delayed WorkOrder ID" , maxDelay == null || maxDelay.size () == 0 || maxDelay.get ( 0 ) == null  ? "" : ( ( Object [ ] ) maxDelay.get ( 0 ) ) [ 1 ] );
		result.put ( "Maximum Delayed WorkOrder Time" , maxDelay == null || maxDelay.size () == 0 || maxDelay.get ( 0 ) == null  ? "" : ((BigInteger)( ( Object [ ] ) maxDelay.get ( 0 ) ) [ 0 ]).longValue ()/3600);
		result.put ( "Average WorkOrder Delay" , avgDelay == null || avgDelay.size () == 0 || avgDelay.get ( 0 ) == null  ? "" : ((BigDecimal)avgDelay.get ( 0 )).longValue () / 3600 );
		result.put ( "Total WorkOrder Delay" , totalDelay == null || totalDelay.size () == 0 || totalDelay.get ( 0 ) == null  ? "" : ((BigDecimal)totalDelay.get ( 0 )).longValue ()/3600 );


		return result;
	}


}
