package com.karimovceyhun.workflow.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import com.karimovceyhun.workflow.data.Collector;
import com.karimovceyhun.workflow.data.Separator;
import com.karimovceyhun.workflow.data.Status;
import com.karimovceyhun.workflow.data.Task;
import com.karimovceyhun.workflow.data.User;
import com.karimovceyhun.workflow.data.WorkOrder;
import com.karimovceyhun.workflow.interfaces.ITaskService;
import com.karimovceyhun.workflow.interfaces.IUserService;

public class TaskService extends Service implements ITaskService,Serializable 
{

	private static final long serialVersionUID = 9156128013484859347L;

	public TaskService(SessionFactory sessionFactory) 
	{
		super(sessionFactory);
	}

	@Transactional
	public List<Task> getAssignedToMeTasks(Long responsibleId, int first, int pageSize, String sortField, Boolean sortAssending) 
	{
		List<Task> tasks = new ArrayList<Task>();
		DetachedCriteria dc = DetachedCriteria.forClass(Task.class);

		dc.createCriteria("responsible").add(Restrictions.eq("id", responsibleId));
		dc.createCriteria("status").add(Restrictions.and(Restrictions.and(Restrictions.ne("statusType", Status.CLOSED),Restrictions.ne("statusType", Status.ACCEPTED)),Restrictions.ne("statusType", Status.REJECTED)));
		// when i call without extra arguments , put just null to other orguments than responsibleid
		if(sortAssending != null){
			tasks = this.list(Task.class, first, pageSize, sortField, sortAssending, dc); 
		}
		else{
			tasks = this.list(Task.class,  dc); 
		}
		return tasks;
	}
	
	@Transactional
	public List<Task> getManagedByMeTasks(Long currentUserId, int first,
			int pageSize, String sortField, boolean equals) {
		List<Task> tasks = new ArrayList<Task>();
		
		DetachedCriteria dc = DetachedCriteria.forClass(Task.class);
		
		dc.createCriteria("status").add(Restrictions.and(Restrictions.and(Restrictions.ne("statusType", Status.CLOSED),Restrictions.ne("statusType", Status.ACCEPTED)),Restrictions.ne("statusType", Status.REJECTED)));
		
		Disjunction disjunction = Restrictions.disjunction();
		
		DetachedCriteria workorder = dc.createCriteria("workorder");
		
		workorder.createCriteria("workflow").createCriteria("manager","m",Criteria.LEFT_JOIN);
		workorder.createCriteria("project","p",Criteria.LEFT_JOIN).createCriteria("manager","m2",Criteria.LEFT_JOIN);
		
		disjunction.add(Restrictions.eq("m.id",currentUserId));
		disjunction.add(Restrictions.eq("m2.id",currentUserId));
		
		dc.add(disjunction);
		
		tasks = list(Task.class,dc);

		return tasks;
	}
	
	@Transactional
	public int getManagedByMeTasksCount(Long currentUserId) {

		DetachedCriteria dc = DetachedCriteria.forClass(Task.class);
		
		dc.createCriteria("status").add(Restrictions.and(Restrictions.and(Restrictions.ne("statusType", Status.CLOSED),Restrictions.ne("statusType", Status.ACCEPTED)),Restrictions.ne("statusType", Status.REJECTED)));
		
		Disjunction disjunction = Restrictions.disjunction();
		
		DetachedCriteria workorder = dc.createCriteria("workorder");
		
		workorder.createCriteria("workflow").createCriteria("manager","m",Criteria.LEFT_JOIN);
		workorder.createCriteria("project","p",Criteria.LEFT_JOIN).createCriteria("manager","m2",Criteria.LEFT_JOIN);
		
		disjunction.add(Restrictions.eq("m.id",currentUserId));
		disjunction.add(Restrictions.eq("m2.id",currentUserId));
		
		dc.add(disjunction);
					
		return countList(Task.class, dc);
	}
	
	@Transactional
	public List<Task> getClosedByMeTasks(Long responsibleId, int first, int pageSize, String sortField, Boolean sortAssending) 
	{
		List<Task> tasks = new ArrayList<Task>();
		DetachedCriteria dc = DetachedCriteria.forClass(Task.class);
		dc.createCriteria("responsible").add(Restrictions.eq("id", responsibleId));
		dc.createCriteria("status").add(Restrictions.or(Restrictions.or(Restrictions.eq("statusType", Status.CLOSED),Restrictions.eq("statusType", Status.ACCEPTED)),Restrictions.eq("statusType", Status.REJECTED)));
		tasks = this.list(Task.class, first, pageSize, sortField, sortAssending, dc); 
		return tasks;
	}

	
	@Transactional
	public List<Task> getMonitoringByMeTasks(Long userId, int first, int pageSize, String sortField, Boolean sortAssending) 
	{
		List<Task> tasks = new ArrayList<Task>();
		DetachedCriteria dc = DetachedCriteria.forClass(Task.class);
		User user = ((IUserService)ServiceFinder.findBean("UserService")).findUser(userId);
		List<Long> userIds = new ArrayList<Long>();
		userIds.add(user.getId());
		dc.createCriteria("workorder").createCriteria("monitoringUser", "m").add(Restrictions.in("m.id", userIds));
		tasks = this.list(Task.class, first, pageSize, sortField, sortAssending, dc); 
		return tasks;
	}
	
	
	
	
	@Transactional
	public List<Task> getReportedByMeTasks(Long userId, int first, int pageSize, String sortField, Boolean sortAssending) 
	{
		List<Task> tasks = new ArrayList<Task>();
		DetachedCriteria dc = DetachedCriteria.forClass(Task.class);
		dc.createCriteria("workorder").createCriteria("requesterUser","r").add(Restrictions.eq("r.id", userId));
		tasks = this.list(Task.class, first, pageSize, sortField, sortAssending, dc); 
		return tasks;
	}
	
	
	
	@Transactional
	public List<Task> getAssignableToMeTasks(Long userId, int first, int pageSize, String sortField, Boolean sortAssending) 
	{
		List<Task> tasks = new ArrayList<Task>();
		DetachedCriteria node;
		DetachedCriteria 
		dc = DetachedCriteria.forClass(Task.class);
		User user = ((IUserService)ServiceFinder.findBean("UserService")).findUser(userId);
		
		List<Long> userIds = new ArrayList<Long>();
		userIds.add(user.getId());
		
		node = dc.createCriteria("node","n");
		node.createCriteria("assignableUser", "m").add(Restrictions.in("m.id", userIds));
		dc.createCriteria("status").add(Restrictions.eq("statusType", Status.NEW));
		
		Disjunction disjunction = Restrictions.disjunction();
		
		dc.createCriteria("workorder","workorder").createCriteria("project","project",Criteria.LEFT_JOIN).createAlias("users","u",Criteria.LEFT_JOIN);
		
		disjunction.add(Restrictions.and( Restrictions.eq("n.projectDependant", Boolean.TRUE),Restrictions.in("u.id", userIds)) );
		disjunction.add(Restrictions.ne("n.projectDependant", Boolean.TRUE ));
		
		// node.add(Restrictions.eq("n.projectDependant", Boolean.FALSE ));
		
		dc.add(disjunction);
		
		tasks = this.list(Task.class, first, pageSize, sortField, sortAssending, dc);
		
		return tasks;

	}
	
	
	@Transactional
	public Task getTask(Long id) 
	{
		Task task = this.find ( Task.class , id );
		
		task.getWorkorder().getProject();
		if( task.getWorkorder().getProject() != null )
		{
			task.getWorkorder().getProject().getUsers().isEmpty();
		}
		task.getWorkorder().getFieldValues().isEmpty();
		task.getWorkorder().getNotes().isEmpty();
		task.getWorkorder().getHistory().isEmpty();
		task.getWorkorder ().getAttachments ().isEmpty ();
		try
		{
			((Separator)task.getNode()).getSucceedingProcesses().isEmpty();
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		return task;
	}
	
	
	@Transactional
	 public Integer getAssignedToMeTasksCount(Long responsibleId)
	 {
		DetachedCriteria dc = DetachedCriteria.forClass(Task.class);
		dc.createCriteria("responsible").add(Restrictions.eq("id", responsibleId));
		dc.createCriteria("status").add(Restrictions.and(Restrictions.and(Restrictions.ne("statusType", Status.CLOSED),Restrictions.ne("statusType", Status.ACCEPTED)),Restrictions.ne("statusType", Status.REJECTED)));
		return this.countList(Task.class, dc);
	 }
	
	@Transactional
    public Integer getClosedByMeTasksCount(Long responsibleId)
	{
		DetachedCriteria dc = DetachedCriteria.forClass(Task.class);
		dc.createCriteria("responsible").add(Restrictions.eq("id", responsibleId));
		dc.createCriteria("status").add(Restrictions.or(Restrictions.or(Restrictions.eq("statusType", Status.CLOSED),Restrictions.eq("statusType", Status.ACCEPTED)),Restrictions.eq("statusType", Status.REJECTED)));
		return this.countList(Task.class, dc);
	}
	
	@Transactional
	public Integer getMonitoringByMeTasksCount(Long userId)
	{
		DetachedCriteria dc = DetachedCriteria.forClass(Task.class);
		User user = ((IUserService)ServiceFinder.findBean("UserService")).findUser(userId);

		List<Long> userIds = new ArrayList<Long>();
		userIds.add(user.getId());
		
		dc.createCriteria("workorder").createCriteria("monitoringUser", "m").add(Restrictions.in("m.id", userIds));
		
		return this.countList(Task.class, dc);
	}
	
	@Transactional
	public Integer getReportedByMeTasksCount(Long userId)
	{
		DetachedCriteria dc = DetachedCriteria.forClass(Task.class);
		dc.createCriteria("workorder").createCriteria("requesterUser","r").add(Restrictions.eq("r.id", userId));
		return this.countList(Task.class, dc);	
	}
	
	@Transactional
	public Integer getAssignableToMeTasksCount(Long userId)
	{
		DetachedCriteria node;
		DetachedCriteria 
		dc = DetachedCriteria.forClass(Task.class);
		User user = ((IUserService)ServiceFinder.findBean("UserService")).findUser(userId);
		
		List<Long> userIds = new ArrayList<Long>();
		userIds.add(user.getId());
		
		node = dc.createCriteria("node","n");
		node.createCriteria("assignableUser", "m").add(Restrictions.in("m.id", userIds));
		dc.createCriteria("status").add(Restrictions.eq("statusType", Status.NEW));
		
		Disjunction disjunction = Restrictions.disjunction();
		
		dc.createCriteria("workorder","workorder").createCriteria("project","project",Criteria.LEFT_JOIN).createAlias("users","u",Criteria.LEFT_JOIN);
		
		disjunction.add(Restrictions.and( Restrictions.eq("n.projectDependant", Boolean.TRUE),Restrictions.in("u.id", userIds)) );
		disjunction.add(Restrictions.ne("n.projectDependant", Boolean.TRUE ));
		
		dc.add(disjunction);
		return this.countList(Task.class, dc);		
	}
	
	@Transactional
	public boolean isCollectorFinalize(Collector collector,WorkOrder workOrder) {
		
		Session sess = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Criteria criteria = sess.createCriteria(Task.class);
		
		criteria.add(Restrictions.eq("workorder.id", workOrder.getId()));
		criteria.createCriteria("status").add(Restrictions.eq("statusType", Status.CLOSED));
		criteria.createCriteria("node").add(Restrictions.eq("succeedingProcess.id", collector.getId()));
		
		List<Task> tasksProcess = criteria.list();
		
		criteria = sess.createCriteria(Task.class);
		
		criteria.add(Restrictions.eq("workorder.id", workOrder.getId()));
		criteria.createCriteria("status").add(Restrictions.eq("statusType", Status.ACCEPTED));
		criteria.createCriteria("node").add(Restrictions.eq("acceptedsucceedingProcess.id", collector.getId()));
		
		List<Task> tasksAcceptDecision = criteria.list();
		
		criteria = sess.createCriteria(Task.class);
		
		criteria.add(Restrictions.eq("workorder.id", workOrder.getId()));
		criteria.createCriteria("status").add(Restrictions.eq("statusType", Status.REJECTED));
		criteria.createCriteria("node").add(Restrictions.eq("rejectedsucceedingProcess.id", collector.getId()));
		
		List<Task> tasksRejectDecision = criteria.list();
		
		int size = tasksProcess.size() + tasksAcceptDecision.size() + tasksRejectDecision.size();
		
		getHibernateTemplate().clear();
		getHibernateTemplate().update(collector);
		
		return collector.getPredecessorProcesses().size() == size;
		
	}
	
}
