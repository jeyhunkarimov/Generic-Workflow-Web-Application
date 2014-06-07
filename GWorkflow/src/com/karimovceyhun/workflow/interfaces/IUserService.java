package com.karimovceyhun.workflow.interfaces;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import com.karimovceyhun.workflow.data.User;

public interface IUserService extends IService,Serializable
{
	public User findUser(Long id);
	
	public User checkForTheUser(String email, String password);

	public List<User> list(int firstRow,int rowCount, String sortField, boolean sortAscending, DetachedCriteria criteriaaa);
	
	public void forgetPassword(String email);
}
