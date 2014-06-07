package com.karimovceyhun.workflow.interfaces;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import com.karimovceyhun.workflow.data.EmailNotification;
import com.karimovceyhun.workflow.data.Project;

public interface IEmailNotificationService extends IService,Serializable
{
	public List<EmailNotification> list(int firstRow,int rowCount, String sortField, boolean sortAscending, DetachedCriteria criteriaaa);
	
	public List<EmailNotification> listForNode(int node);
}
