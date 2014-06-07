package com.karimovceyhun.workflow.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.FetchType;

import org.hibernate.FetchMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import com.karimovceyhun.workflow.data.Bean;
import com.karimovceyhun.workflow.data.Collector;
import com.karimovceyhun.workflow.data.Decision;
import com.karimovceyhun.workflow.data.Field;
import com.karimovceyhun.workflow.data.Node;
import com.karimovceyhun.workflow.data.Process;
import com.karimovceyhun.workflow.data.Project;
import com.karimovceyhun.workflow.data.Separator;
import com.karimovceyhun.workflow.data.TaskNode;
import com.karimovceyhun.workflow.data.Terminate;
import com.karimovceyhun.workflow.data.Workflow;
import com.karimovceyhun.workflow.interfaces.IWorkflowService;

public class WorkflowService extends Service implements IWorkflowService , Serializable
{

	private static final long	serialVersionUID	= - 5345138621349915116L;

	public WorkflowService( SessionFactory sessionFactory )
	{
		super ( sessionFactory );
	}

	@Transactional
	public Workflow findWorkflow( Long id )
	{
		DetachedCriteria det = DetachedCriteria.forClass ( Workflow.class );
		// det.setFetchMode("projects", FetchMode.EAGER);
		det.add ( Restrictions.eq ( "id" , id ) );
		det.setFetchMode ( "projects" , FetchMode.EAGER );
		det.setFetchMode ( "fields" , FetchMode.EAGER );
		List < Workflow > workflows = list ( Workflow.class , det );
		/*
		 * for(Workflow wf: workflows) { List<Field> lf = wf.getFields();
		 * List<Project> lp = wf.getProjects(); wf.setFields(lf);
		 * wf.setProjects(lp); }
		 */
		if ( workflows == null || workflows.size () == 0 )
		{
			return null;
		}
		else
		{
			// workflows.get(0).getProjects().isEmpty();
			// workflows.get(0).getFields().isEmpty();
			workflows.get ( 0 ).getUserAbleToMonitor ().isEmpty ();
			workflows.get ( 0 ).getUserAbleToOpen ().isEmpty ();
			workflows.get ( 0 ).getNodes ().isEmpty ();
			for ( Node node : workflows.get ( 0 ).getNodes () )
			{
				node.getEmailNotifications ().isEmpty ();
				if ( node instanceof Separator )
				{
					( ( Separator ) node ).getSucceedingProcesses ().isEmpty ();
				}
			}
			return workflows.get ( 0 );
		}
	}

	@Transactional
	public Workflow findWorkflowbyName( String name )

	{
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass ( Workflow.class );
		detachedCriteria.add ( Restrictions.eq ( "name" , name ) );
		detachedCriteria.setFetchMode ( "projects" , FetchMode.EAGER );
		List < Workflow > workFlowList = list ( Workflow.class , detachedCriteria );

		Workflow workflow = workFlowList.get ( 0 );

		for ( Field field : workflow.getFields () )
		{
			field.getFieldname ();
		}

		return workflow;
	}

	@Transactional
	public List < Workflow > list( int firstRow, int rowCount, String sortField, boolean sortAscending, DetachedCriteria criteriaaa )
	{
		List < Workflow > workflows = super.list ( Workflow.class , firstRow , rowCount , sortField , sortAscending , criteriaaa );

		return workflows;
	}

	@Transactional
	public void save( Workflow workflow )
	{

		for ( Node node : workflow.getNodes () )
		{
			if ( node instanceof Process )
			{
				Node succedingNode = ( ( Process ) node ).getSucceedingProcess ();

				getHibernateTemplate ().merge ( node );
				( ( Process ) node ).setSucceedingProcess ( succedingNode );
			}
			else if ( node instanceof Decision )
			{
				Node succedingAcceptNode = ( ( Decision ) node ).getAcceptedsucceedingProcess ();
				Node succedingRejectNode = ( ( Decision ) node ).getRejectedsucceedingProcess ();

				getHibernateTemplate ().merge ( node );

				( ( Decision ) node ).setAcceptedsucceedingProcess ( succedingAcceptNode );
				( ( Decision ) node ).setRejectedsucceedingProcess ( succedingRejectNode );
			}
			else if ( node instanceof Collector )
			{
				getHibernateTemplate ().merge ( node );
			}
			else if ( node instanceof Separator )
			{
				getHibernateTemplate ().merge ( node );
			}
			else if ( node instanceof Terminate )
			{
				getHibernateTemplate ().merge ( node );
			}
		}

		getHibernateTemplate ().merge ( workflow );
	}
	
	@Transactional
	public List < Workflow > findWorkflowsDueToUserAbleToOpen(Long userId)
	{
		DetachedCriteria dc = DetachedCriteria.forClass ( Workflow.class );
		List<Long> userIds = new ArrayList < Long > ();
		userIds.add ( userId );
		dc.createCriteria ( "userAbleToOpen" , "uao" ).add ( Restrictions.in ( "uao.id" , userIds ) );
		return this.list ( Workflow.class , dc );
	}
}
