package com.karimovceyhun.workflow.interfaces;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import com.karimovceyhun.workflow.data.Project;

public interface IProjectService  extends IService,Serializable
{
	public Project findProject(Long id);
	public Project findProjectByName(String name);
	public List<Project> list(int firstRow,int rowCount, String sortField, boolean sortAscending, DetachedCriteria criteriaaa);
	public List<Project> getMyProjects(Long id);
}
