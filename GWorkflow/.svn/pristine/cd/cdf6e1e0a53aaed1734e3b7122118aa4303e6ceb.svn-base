package com.karimovceyhun.workflow.services;

import java.io.Serializable;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import com.karimovceyhun.workflow.data.Bean;
import com.karimovceyhun.workflow.data.EmailNotification;
import com.karimovceyhun.workflow.data.Project;
import com.karimovceyhun.workflow.interfaces.IEmailNotificationService;

public class EmailNotificationService extends Service implements IEmailNotificationService,Serializable
{

	
	private static final long serialVersionUID = -8858011755783306711L;
	public EmailNotificationService(SessionFactory sessionFactory) {
		super(sessionFactory);

	}
	

	@Transactional
	public List<EmailNotification> list(int firstRow, int rowCount, String sortField,	boolean sortAscending, DetachedCriteria criteriaaa) 
	{
		List<EmailNotification> enl = super.list(EmailNotification.class, firstRow,rowCount, sortField, sortAscending, criteriaaa);
		return enl;

	}


	@Transactional
	public List<EmailNotification> listForNode(int node) {
		List<EmailNotification> processNots;
		DetachedCriteria proDet = DetachedCriteria.forClass(EmailNotification.class);
		proDet.add(Restrictions.eq("forNode",node));
		processNots = this.list(EmailNotification.class, proDet);
		return processNots;
	}

	
}
