package com.karimovceyhun.workflow.services;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.karimovceyhun.workflow.data.Bean;
import com.karimovceyhun.workflow.interfaces.IService;
public class Service implements IService 
{

	/**
	 * Hibernate template for execution of database operations
	 */
	private HibernateTemplate hibernateTemplate;

	public Service(SessionFactory sessionFactory) {
		this.hibernateTemplate = new HibernateTemplate(sessionFactory);
	}

	/**
	 * Setter
	 * 
	 * @param hibernateTemplate
	 */
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	/**
	 * Getter of hibernate template
	 * 
	 * @return
	 */
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	@Transactional
	public <T extends Bean> void save(T bean) {
		getHibernateTemplate().saveOrUpdate(bean);
	}

	@Transactional
	public <T extends Bean> void delete(T bean) {
		getHibernateTemplate().delete(bean);
	}

	public <T extends Bean> T find(Class<T> clazz, Long id) {
		return (T) getHibernateTemplate().get(clazz, id);
	}

	@Transactional
	public <T extends Bean> List<T> list(Class<T> clazz, int firstRow,
			int rowCount, String sortField, boolean sortAscending) {
		Session sess = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		Criteria criteria = sess.createCriteria(clazz);
		criteria.setFirstResult(firstRow);
		criteria.setMaxResults(rowCount);
		if( sortField != null && !sortField.equals("") )
		{
			if(sortAscending)
				criteria.addOrder( Order.asc(sortField) );
			else
				criteria.addOrder( Order.desc(sortField) );
			
		}
		return criteria.list();
	}

	@Transactional
	public <T extends Bean> int countList(Class<T> clazz) {
		Session sess = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		Criteria criteria = sess.createCriteria(clazz);
		criteria.setProjection(Projections.rowCount());
		return ((Integer) criteria.list().get(0)).intValue();
	}

	@Transactional
	public <T extends Bean> List<T> list(Class<T> clazz, int firstRow,
			int rowCount, String sortField, boolean sortAscending,
			List<Criterion> criterions) {

		Session sess = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		Criteria criteria = sess.createCriteria(clazz);
		if (criterions != null) {
			for (Criterion criterion : criterions) {
				criteria.add(criterion);
			}
		}
		
		Criteria sortCriteria = criteria;
		if( sortField != null && !sortField.equals("") )
		{
			String sortOn = sortField;
			while( sortOn.contains(".") )
			{
				String temp = sortOn.substring(0,sortOn.indexOf("."));
				sortCriteria = sortCriteria.createCriteria( temp );
				sortOn = sortOn.substring(sortOn.indexOf(".")+1);
			}
			
			if(sortAscending)
				sortCriteria.addOrder( Order.asc(sortOn) );
			else
				sortCriteria.addOrder( Order.desc(sortOn) );
			
		}
		
		criteria.setFirstResult(firstRow);
		criteria.setMaxResults(rowCount);
		return criteria.list();
	}

	@Transactional
	public <T extends Bean> List<T> list(Class<T> clazz) {
		// TODO Auto-generated method stub
		return getHibernateTemplate().loadAll(clazz);
	}

	@Transactional
	public <T extends Bean> int countList(Class<T> clazz,
			List<Criterion> criterions) {
		Session sess = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		Criteria criteria = sess.createCriteria(clazz);
		criteria.setProjection(Projections.rowCount());
		if (criterions != null) {
			for (Criterion criterion : criterions) {
				criteria.add(criterion);
			}
		}

		return ((Integer) criteria.list().get(0)).intValue();
	}

	@Transactional
	public <T extends Bean> List<T> list(Class<T> clazz, int firstRow,
			int rowCount, String sortField, boolean sortAscending,
			DetachedCriteria detachedCriteria) {
		Session sess = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		Criteria criteria = detachedCriteria.getExecutableCriteria(sess);
		criteria.setFirstResult(firstRow);
		criteria.setMaxResults(rowCount);
		Criteria sortCriteria = criteria;
		if( sortField != null && !sortField.equals("") )
		{
			String sortOn = sortField;
			while( sortOn.contains(".") )
			{
				String temp = sortOn.substring(0,sortOn.indexOf("."));
				sortCriteria = sortCriteria.createCriteria( temp );
				sortOn = sortOn.substring(sortOn.indexOf(".")+1);
			}
			
			if(sortAscending)
				sortCriteria.addOrder( Order.asc(sortOn) );
			else
				sortCriteria.addOrder( Order.desc(sortOn) );
			
		}
		return criteria.list();
	}

	@Transactional
	public <T extends Bean> int countList(Class<T> clazz,
			DetachedCriteria detachedCriteria) {
		Session sess = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		Criteria criteria = detachedCriteria.getExecutableCriteria(sess);
		criteria.setProjection(Projections.rowCount());
		return ((Long) criteria.list().get(0)).intValue();
	}
	
	@Transactional
	public <T extends Bean> List<T> list(Class<T> clazz, DetachedCriteria detachedCriteria)
	{
		Session sess = getHibernateTemplate().getSessionFactory()
		.getCurrentSession();
		Criteria criteria = detachedCriteria.getExecutableCriteria(sess);
		
		return criteria.list();
	}
	
	@Transactional
	public <T extends Bean> List<T> list(Class<T> clazz, List<Criterion> criterions) {

		Session sess = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		Criteria criteria = sess.createCriteria(clazz);
		if (criterions != null) {
			for (Criterion criterion : criterions) {
				criteria.add(criterion);
			}
		}

		return criteria.list();
	}
}
