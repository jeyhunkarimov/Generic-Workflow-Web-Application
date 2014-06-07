package com.karimovceyhun.workflow.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import com.karimovceyhun.workflow.data.Project;
import com.karimovceyhun.workflow.data.User;
import com.karimovceyhun.workflow.interfaces.IProjectService;
import com.karimovceyhun.workflow.interfaces.IUserService;

public class ProjectService extends Service implements IProjectService,Serializable  
{

	private static final long serialVersionUID = -5345138621349915116L;

	public ProjectService(SessionFactory sessionFactory) 
	{
		super(sessionFactory);
	}

	@Transactional
	public Project findProjectByName(String name) 
	{
		Session sess = getHibernateTemplate().getSessionFactory().getCurrentSession();
		
		Criteria criteria = sess.createCriteria(Project.class);
		
		criteria.add(Restrictions.eq("name", name));
		
		Project project = (Project)criteria.list().get(0);

		for(User e : project.getUsers())
		{
			e.getFullName();
		}
		
		return project;
	}

	@Transactional
	public Project findProject(Long id) 
	{
		Project project = find(Project.class, id);
		for(User e : project.getUsers())
		{
			e.getFullName();
		}
		if( project.getManager() != null )
		{
			project.getManager().getFullName();
		}
		return project;
	}

	@Transactional
	public List<Project> list(int firstRow, int rowCount, String sortField,boolean sortAscending, DetachedCriteria criteriaaa) 
	{
		List<Project> projects = super.list(Project.class, firstRow,rowCount, sortField, sortAscending, criteriaaa);
		
		return projects;
	}
	
	
	@Transactional
	public List<Project> getMyProjects(Long id)
	{
		List<Project> p1 = new ArrayList<Project>();
		DetachedCriteria dc = DetachedCriteria.forClass(Project.class);
		List<Long> ids = new ArrayList<Long>();
		ids.add(id);
		dc.createCriteria("users","u").add(Restrictions.in("u.id", ids));
		p1 =  this.list(Project.class, dc);
		
		List<Project> p2 = new ArrayList<Project>();
		DetachedCriteria dc2 = DetachedCriteria.forClass(Project.class);
		dc.createCriteria("manager","m").add(Restrictions.eq("m.id", id));
		p2 =  this.list(Project.class, dc2);
		
		for(Project p:p2)
		{
			if(! p1.contains(p))
			{
				p1.add(p);
			}
		}
		return p1;
	}

}
